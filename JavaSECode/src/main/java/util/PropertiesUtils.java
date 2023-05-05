package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: MaoMao
 * @date: 2023/5/5 22:42
 */
public class PropertiesUtils {

    private static final Logger log = LogManager.getLogger(PropertiesUtils.class);

    public static Properties getPath(String path){

        Properties pro = new Properties();
        ClassLoader classLoader = PropertiesUtils.class.getClassLoader();

        try {
            InputStream in = classLoader.getResourceAsStream(path);
            pro.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pro;
    }
}
