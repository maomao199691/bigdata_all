package elasticsearch.server;

import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: MaoMao
 * @date: 2023/5/5 22:09
 */
public class ElasticServerUtil {

    public static Logger log = LogManager.getLogger(ElasticServerUtil.class);

    private static ElasticServerUtil instance;
    private RestHighLevelClient restHighLevelClient;

    public ElasticServerUtil(String hosts, String username, String password) {

        restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                getHostsArray(hosts)
        )
                .setHttpClientConfigCallback( httpRequest -> httpRequest
                        .setDefaultCredentialsProvider(getProvider(username, password))));

    }

    private static HttpHost[] getHostsArray(String hosts){
        if (StringUtils.isBlank(hosts)){
            log.error("Elasticsearch地址为空");
            System.exit(-1);
        }

        if (!hosts.contains(":") || !hosts.contains(",")){
            log.error("Elasticsearch地址格式错误");
            System.exit(-1);
        }

        List<HttpHost> httpHosts = new ArrayList<>();

        String[] requestHosts = hosts.split(",");
        for (String requestHost : requestHosts) {
            String[] parts = requestHost.split(":");
            httpHosts.add(new HttpHost(parts[0], Integer.parseInt(parts[1])));
        }

        return httpHosts.toArray(new HttpHost[0]);
    }

    private static CredentialsProvider getProvider(String username, String password){
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            log.error("账号或者密码为空");
            System.exit(-1);
        }

        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        return provider;
    }

    public static RestHighLevelClient getClient(String hosts, String username, String password){
        if (instance == null){
            synchronized (ElasticServerUtil.class){
                instance = new ElasticServerUtil(hosts, username, password);
            }
        }
        return ElasticServerUtil.instance.restHighLevelClient;
    }
}