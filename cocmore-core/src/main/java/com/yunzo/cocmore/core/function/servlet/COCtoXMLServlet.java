package com.yunzo.cocmore.core.function.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.yunzo.cocmore.core.function.service.UserService;

@Controller("/coc_servlet")
public class COCtoXMLServlet extends HttpServlet {
	private static final long serialVersionUID = 8975959920935824256L;

	private static final String CONTENT_TYPE = "application/xml; charset=UTF-8";

	private static final Logger logger = Logger.getLogger(COCServlet.class);

	@Resource(name = "userInfo")
	private UserService userService;

	@PostConstruct
	// init-method="init"
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		logger.info("COCtoXMLServlet init start");

		logger.info("COCtoXMLServlet init end");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		logger.info("Demo2Servlet service start");

		response.setContentType(CONTENT_TYPE);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String result = userService.getById("11").getFaccount();
		// Integer.valueOf(result); //测试异常显示页面
		PrintWriter out = response.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<root>\n");
		out.println("<result>" + result + "</result>\n");
		out.println("</root>\n");

		out.flush();
		out.close();

		logger.info("COCtoXMLServlet service end");
	}

	@PreDestroy
	// destroy-method="destroy"
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		logger.info("COCtoXMLServlet destroy start");

		logger.info("COCtoXMLServlet destroy end");
	}

}
