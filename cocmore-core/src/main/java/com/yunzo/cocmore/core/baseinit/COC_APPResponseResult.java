package com.yunzo.cocmore.core.baseinit;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * ailierke1111111
 * JSON 转换格式
 * @author david
 *
 */
public class COC_APPResponseResult {

	public static void responseToGJson(Object map,HttpServletResponse response)
	{
		final Gson gson = new Gson();
		String value = "";
		value = gson.toJson(map);
		try {
			response.setContentType("application/json;charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
