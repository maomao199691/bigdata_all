package uploaddata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import util.HiveClient;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author: MaoMao
 * @date: 2023/5/11 22:30
 */
public class UploadDataToOSS {
    public static void main(String[] args) throws SQLException {

        if (args.length != 2){
            System.out.println("请输入上传的列名和表名 => image info");
        }

        String column = args[0];
        String tableName = args[1];

        Properties pro = getPro("");

        Connection conn = HiveClient.getConn(pro);

        String sql = "select "+ column + " from " + tableName + " where length(" + column + ") > 1;";

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);


        List<String> dataList = new ArrayList<>();
        while (resultSet.next()){
            String data = resultSet.getString(1);
            String jsonData = getJsonData(data);
            if (jsonData != null){
                dataList.add(jsonData);
            }

        }

        // TODO 遍历需要上传的 oriUrl 访问三台机器的路径
        for (String data : dataList) {

        }

    }

    private static String getJsonData(String data){

        String result = null;

        if (StringUtils.isNotBlank(data)){
            if (data.startsWith("{")){
                JSONObject dataObj = JSON.parseObject(data);
                String oriUrl = dataObj.getString("ori_file");
                if (StringUtils.isNotBlank(oriUrl)){
                    result = oriUrl;
                }
            }
        }
        return result;
    }


    private static Properties getPro(String path){

        Properties pro = new Properties();

        ClassLoader classLoader = UploadDataToOSS.class.getClassLoader();
        InputStream in = classLoader.getResourceAsStream(path);

        try {
            pro.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pro;
    }
}
