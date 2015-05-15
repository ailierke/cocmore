package com.yunzo.cocmore.core.function.servlet;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


/**
 * Description: <测试SERVLET>. <br>
 * <p>
 * <SERVLET>
 * </p>
 * Makedate:2014年11月18日 上午4:26:59
 * 
 * @author xiaobo
 * @version V1.0
 */
public class COCServlet extends HttpServlet {
	private static final long serialVersionUID = -9187642226361597464L;
	private static final Logger logger = Logger
			.getLogger(COCServlet.class);
	
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		logger.info("COCServlet init start");
		
		
		logger.info("COCServlet init end");
	}



	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		logger.info("COCServlet service start");
		
		
		logger.info("COCServlet service end");
	}

}
