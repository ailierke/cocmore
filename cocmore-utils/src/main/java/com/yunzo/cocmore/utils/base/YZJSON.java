package com.yunzo.cocmore.utils.base;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * JSON
 * 
 * @author star
 *
 */
public class YZJSON {

	/**
	 * map转JSON文本
	 * 
	 * @param map
	 * @return
	 */
	public static String map2json(Map<Object, Object> map) {
		return JSONObject.toJSON(map).toString();
	}

}
