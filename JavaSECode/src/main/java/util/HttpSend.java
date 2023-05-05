package util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;

import java.util.HashMap;

/**
 * @Author: MaoMao
 * @date: 2023/4/18 22:45
 */
public class HttpSend {

    public static void sendData(JSONObject obj){

        HashMap<String, String> headers = new HashMap<>();
        headers.put("content-type", "image/png");


//        HttpUtil.createPost("").addHeaders(headers)
    }
}
