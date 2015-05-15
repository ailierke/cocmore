package com.yunzo.cocmore.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class TestJx {
    public static void main(String[] args) throws ClientProtocolException, IOException {
        
        HttpClient httpclient =new DefaultHttpClient();//114.215.201.200:8080
        HttpPost httpPost = new HttpPost("http://10.0.0.2:8080/cocmore-web/mobileapi/business/getBusinessActivityPage");
        //添加所需要的post内容
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("infoMap", "{'mid':'8a21693447b3b1b20147cd6c22fb3dc6','localCacheVersion':'2','deviceOsversion':'7.1','appChannelNo':'1','sessionToken':'0274629d047bd7eae2214cb0364a3843','appVersion':'1.0.1209','client':'DGGS','deviceOs':'iOS','businessId':'8a21693449e519480149ebf5a99827a2','userName':'18627087136','deviceId':'a7e4a81690eebbf0f79951d94408d02b0c1261b0','deviceType':'iPhone Simulator','md5Info':'7a5aa9321c3ea39500375dc651fdaea0','sign':'A27963CFCDACD6D767DA881FD714EE9E'}"));
        
        
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        
        HttpResponse response = httpclient.execute(httpPost);
        
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
        System.out.println(content);
        // EntityUtils.consume(entity);
    }
}
