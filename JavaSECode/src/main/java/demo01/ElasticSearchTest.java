package demo01;

import com.alibaba.fastjson.JSON;
import constant.ConstantUtil;
import elasticsearch.server.ElasticServerUtil;
import entry.IdxInformation;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import util.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author: MaoMao
 * @date: 2023/5/5 22:41
 */
public class ElasticSearchTest {
    public static void main(String[] args) {

        IdxInformation idxInformation = new IdxInformation();

        Properties pro = PropertiesUtils.getPath("elasticsearch.properties");
        String hosts = pro.getProperty(ConstantUtil.ES_HOSTS);
        String username = pro.getProperty(ConstantUtil.ES_USERNAME);
        String password = pro.getProperty(ConstantUtil.ES_PASSWORD);

        RestHighLevelClient client = ElasticServerUtil.getClient(hosts, username, password);

        IndexRequest indexRequest = new IndexRequest("index");

        indexRequest.id(idxInformation.getUuid());

        String json = JSON.toJSONString(idxInformation);

        indexRequest.source(json);

        try {
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
