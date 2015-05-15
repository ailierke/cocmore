/**
 * DecryptByABC.js
 */
/**
 * 利用ascii转码 解密方法
 * 
 * @param str
 * @return
 */
function decryptByABC(tel){
	var dealStr = hexToString(tel);
	dealStr = dealStr.substring(1, dealStr.length - 1);
	//alert(dealStr);
	var charArray = toCharArray(dealStr);
	//alert(charArray);
	var paraStr = arrayToBackward(charArray);
	//alert(paraStr);
	return changeAsciiToNumber(paraStr);
}

/**
 * 转化十六进制编码为字符串
 * @param 传入要转化的字符串
 * @return 返回转化后的字符串
 */
function hexToString(sinfo) {
	var returnStr ="";
	try {
		for (var i = 0; i < sinfo.length / 2; i++) {
			returnStr =returnStr+String.fromCharCode((0xff & parseInt(sinfo.substring(i * 2, i * 2 + 2), 16)));
		}
		//alert(returnStr);
		//alert("A".charCodeAt(0));  //将A-Z转化为数字
		//alert(String.fromCharCode(97));//将数字转化为A-Z
	} catch ( e1) {
		alert(e1.message);

	}
	return returnStr;
}
/**
 *字符串转化成字符数组
 */
function toCharArray(deal){
	if(typeof deal !="string"){
		return [];
	}
	var arr=[];
	for(var i=0;i<deal.length;i++){
		arr.push(deal.charAt(i))
	} 
	return arr;
}
/**
 *将上面的字符数组倒序，然后在转化成string在返回
 */
function arrayToBackward(arr){
	var value="";
	if(arr.length>0)
	{
		var t;
		//侄序
		for (var i = 0; i < arr.length / 2; i++) {
			t = arr[i];
			arr[i] = arr[arr.length - 1 - i];
			arr[arr.length - 1 - i] = t;
		}

		for (var j = 0; j < arr.length; j++) {
			value =value+arr[j];
		}
	}
	return value;

}

/**
 *调用上面返回的字符串参数，转化成电话号码
 */
function changeAsciiToNumber( str) {
	var changeStr = "";
	for (var i = 0; i < str.length; i++) {
		var ci = str.charAt(i); // 获取字符串每个字符ascii

		var change = ci.charCodeAt(0) - 17;
		var changeChar = String.fromCharCode(change); // 转换
		changeStr += changeChar;
	}
	return changeStr;
}