package app1;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: MaoMao
 * @date: 2022/10/26 16:25
 * Desc: valueState状态的底层逻辑
 */
public class ProcessTime {
    /**
     * KeyedProcessFunction的子类，作用是将每个单词最新出现时间记录到backend，并创建定时器
     * 定时器触发的时候，检查这个单词距离上次出现是否已经达到10秒，如果是，就发送给下游算子
     */
    static class CountWithTimeoutFunction extends KeyedProcessFunction<Tuple, Tuple2<String, Integer>,Tuple2<String,Long>>{

        //自定义状态
        private ValueState<CountWithTimestamp> state;

        @Override
        public void open(Configuration parameters) throws Exception {
            //初始化状态，name是myState
            state = getRuntimeContext().getState(new ValueStateDescriptor<CountWithTimestamp>("myState",CountWithTimestamp.class));
        }

        @Override
        public void processElement(Tuple2<String, Integer> value, Context ctx, Collector<Tuple2<String, Long>> out) throws Exception {
            //取得当前是哪个单词
            Tuple currentKey = ctx.getCurrentKey();

            //从backend取得当前单词的myState状态
            CountWithTimestamp current = state.value();

            //如果myState还从未没有赋值过，就在此初始化
            if (current == null){
                current = new CountWithTimestamp();
                current.key = value.f0;
            }

            //单词数量增加一
            current.count++;

            //取当前元素的时间戳，作为该的单词最后一次出现的时间
            current.lastModified = ctx.timestamp();

            //重新保存到backend，包括该单词出现的次数，以及最后一次出现的时间
            state.update(current);

            //为当前单词创建定时器，十秒后触发
            long timer = current.lastModified + 10000;

            ctx.timerService().registerProcessingTimeTimer(timer);

            //打印所有信息，用于核对数据正确性
            System.out.println(String.format("process, %s, %d, lastModified : %d (%s), timer : %d (%s)\n\n",
                    currentKey.getField(0),
                    current.count,
                    current.lastModified,
                    time(current.lastModified),
                    timer,
                    time(timer)));
        }

        /**
         * 定时器触发后执行的方法
         * @param timestamp 这个时间戳代表的是该定时器的触发时间
         */
        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<Tuple2<String, Long>> out) throws Exception {

            //取得当前单词
            Tuple currentKey = ctx.getCurrentKey();

            //取得该单词的myState状态
            CountWithTimestamp result = state.value();

            //当前元素是否已经连续10秒未出现的标志
            boolean isTimeout = false;

            //timestamp是定时器触发时间，如果等于最后一次更新时间+10秒，就表示这十秒内已经收到该单词了，
            //这种连续十秒没有出现的元素，被发送到下游算子
            if (timestamp == result.lastModified + 10000){
                //发送
                out.collect(new Tuple2<String, Long>(result.key, result.count));

                isTimeout = true;
            }

            //打印数据，用于核对是否符合预期
            System.out.println(String.format("onTimer, %s, %d, lastModified: %d (%s), stamp :%d (%s),isTimeout : %s\n\n",
                    currentKey.getField(0),
                    result.count,
                    result.lastModified,
                    time(result.lastModified),
                    timestamp,
                    time(timestamp),
                    String.valueOf(isTimeout)));
        }
    }

    public static void main(String[] args) throws Exception{
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        //监听本地999端口，读取字符串
        DataStreamSource<String> socketDataStream = env.socketTextStream("localhost", 9999);

        //所有输入的单词，如果超过10秒没有再出现，都可以通过CountWithTimeoutFuntion得到
        socketDataStream
                .flatMap(new Splitter())
                //设置时间戳分配器，用当前时间作为时间戳
                .assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple2<String, Integer>>() {
                    @Nullable
                    @Override
                    public Watermark getCurrentWatermark() {
                        //使用当前系统时间作为时间戳
                        return null;
                    }

                    @Override
                    public long extractTimestamp(Tuple2<String, Integer> element, long recordTimestamp) {
                        //本例不需要watermark。返回null
                        return System.currentTimeMillis();
                    }
                })
                .keyBy(0)
                .process(new CountWithTimeoutFunction());
    }

    public static String time(long timeStamp){
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(timeStamp));
    }
}
