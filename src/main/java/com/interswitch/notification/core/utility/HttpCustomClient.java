package com.interswitch.notification.core.utility;

import com.interswitch.notification.core.exceptions.AppBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class HttpCustomClient {

    private  final Logger logger = LoggerFactory.getLogger(getClass());

    private String url;
    private String jsonBody ;
    private String method;
    private HashMap<String,String> headers ;


    public HttpCustomClient(String url,String jsonBody, String method, HashMap<String,String> headers) {
        this.url = url;
        this.jsonBody = jsonBody;
        this.method = method;
        this.headers = headers;
    }

    public String makeHttpRequest() {

        String finalResponse;

        URL obj;
        try {

            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(this.method);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            if(headers !=null && headers.size() > 0 ){
                headers.forEach((s, s2) -> {
                    con.setRequestProperty(s,s2);
                });
            }

            if(method=="POST" && jsonBody!=null){
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonBody.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            int responseCode = con.getResponseCode();

            logger.info(" Sending {} request to URL {} ",method,url);
            logger.info(" Service Response Code is :  {} ",responseCode);

            if(responseCode == 429){
                throw new AppBaseException("Burst Allowance Exceeded");
            }


            BufferedReader in = null;
            if(con.getErrorStream() !=null){
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }else if(con.getInputStream()!=null){
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            finalResponse = response.toString();

            logger.info(" Service Response is :  {} ",finalResponse);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new AppBaseException(e.getMessage());
        }  catch (IOException e) {
            e.printStackTrace();
            throw new AppBaseException(e.getMessage());
        }

        return finalResponse;


    }


}
