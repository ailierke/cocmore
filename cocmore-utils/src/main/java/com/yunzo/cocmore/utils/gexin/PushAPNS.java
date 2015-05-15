package com.yunzo.cocmore.utils.gexin;

 
 
 
 
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;
public class PushAPNS {
static String appId = "Dx3831cvM86E9uMidPFrF1";
    static String appKey = "kHG4hCQ6nr5keVek1woOB2";
    static String masterSecret = "omUyi4tIl47ACVB1MdVnx2";
    static String devicetoken = "9b34918589c6ebac437a12067453f6d9284971ba99df249d2fced89cd6693e95";
    static String url ="http://sdk.open.api.igexin.com/serviceex";
       public static void apnpush() throws Exception {
              IGtPush push = new IGtPush(url, appKey, masterSecret); 
              APNTemplate t = new APNTemplate();
              t.setPushInfo("ok", 3, "wangbadan", "default", "234567890-8675423134567980", "test2131232", "", ""); 
              SingleMessage sm = new SingleMessage();
              sm.setData(t);
              IPushResult ret0 = push.pushAPNMessageToSingle(appId, devicetoken, sm);
              System.out.println(ret0.getResponse());
              
       }
       
       public static void main(String[] args) throws Exception {
              apnpush();
//    	 System.out.println(new String("b34918589c6ebac437a12067453f6d9284971ba99df249d2fced89cd6693e95").length());  
       }
 
 
}