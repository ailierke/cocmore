package com.yunzo.cocmore.utils.base.email;

import javax.mail.*;   
/**
 * @author Terry 2014-5-9
 * @see 云筑科技有限公司深圳研发中心
 *  密码验证器  
 */     
public class CustomAuthenticator extends Authenticator{   
    String userName=null;   
    String password=null;   
        
    public CustomAuthenticator(){   
    }   
    public CustomAuthenticator(String username, String password) {    
        this.userName = username;    
        this.password = password;    
    }    
    protected PasswordAuthentication getPasswordAuthentication(){   
        return new PasswordAuthentication(userName, password);   
    }   
}   