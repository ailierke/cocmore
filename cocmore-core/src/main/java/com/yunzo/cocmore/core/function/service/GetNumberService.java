package com.yunzo.cocmore.core.function.service; 

import java.util.Map;



/**
 * 
 * @author ailierke
 *
 */
public interface GetNumberService {
	/**
	 * 判断是福存在
	 * @param Fid
	 * @return
	 */
	public Map<String, Object> checkExist(String Fid);
	/**
	 * 增加流水号 FserialNumber
	 * @param Fid
	 */
	void addSerialNumber(String fid);
}
