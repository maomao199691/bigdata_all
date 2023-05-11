package util;

import constant.ConstantUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * @Author: MaoMao
 * @date: 2023/5/11 22:35
 */
public class HiveClient {

    private static HiveClient instance;
    private Connection conn;

    private Properties pro;

    public HiveClient(Properties pro) {
       getInit(pro);
    }

    public static Connection getConn(Properties pro){
        if (HiveClient.class == null){
            synchronized (HiveClient.class){
                if (HiveClient.instance == null){
                    HiveClient.instance = new HiveClient(pro);
                }
            }
        }

        return HiveClient.instance.getCon();
    }

    private void getInit(Properties pro){

        String url = pro.getProperty(ConstantUtil.HIVE_URL);
        String username = pro.getProperty(ConstantUtil.HIVE_USER_NAME);
        String password = pro.getProperty(ConstantUtil.HIVE_USER_PASSWORD);

        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getCon(){ return this.conn; }
}
