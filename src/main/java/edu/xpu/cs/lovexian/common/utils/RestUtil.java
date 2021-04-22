package edu.xpu.cs.lovexian.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class RestUtil {

    public static String load(String url) throws Exception {
        URL restURL = new URL(url);

        HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();


        conn.setRequestMethod("POST");

        //设置是否从httpUrlConnection读入，默认情况下是true; httpUrlConnection.setDoInput(true);

        conn.setDoOutput(true);

        //allowUserInteraction 如果为 true，则在允许用户交互（例如弹出一个验证对话框）的上下文中对此 URL 进行检查。

        conn.setAllowUserInteraction(false);
        BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        System.out.println("44");
        String line, resultStr = "";


        while (null != (line = bReader.readLine())) {

            resultStr += line;

        }

        System.out.println("3412412---" + resultStr);

        bReader.close();


        return resultStr;


    }


    public static void main(String[] args) {
        try {


            RestUtil restUtil = new RestUtil();


            String resultString = restUtil.load(

                    "http://39.105.171.192:8886/command?cmd=AA5504A2000102D4BE55AA");


        } catch (Exception e) {


            // TODO: handle exception

            System.out.print(e.getMessage());


        }


    }

}