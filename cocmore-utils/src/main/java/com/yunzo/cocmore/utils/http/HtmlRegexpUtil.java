package com.yunzo.cocmore.utils.http;
import java.util.regex.Matcher;   
import java.util.regex.Pattern;  
/**  
 * <p>  
 * Title: HTML相关的正则表达式工具类  
 * </p>  
 * <p>  
 * Description: 包括过滤HTML标记，转换HTML标记，替换特定HTML标记  
 * </p>  
 * <p>  
 * Copyright: Copyright (c) 2015  
 * </p>  
 *   
 * @author david 
 * @version 1.0  
 * @createtime 2015-01-16  
 */
public class HtmlRegexpUtil {
	 private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签   
	  
	    private final static String regxpForImgTag = "<\\s*img\\s+([^>]*)\\s*>"; // 找出IMG标签   
	  
	    private final static String regxpForImaTagSrcAttrib = "src=\"([^\"]+)\""; // 找出IMG标签的SRC属性   
	  
	    /**  
	     *   
	     */  
	    public HtmlRegexpUtil() {   
	        // TODO Auto-generated constructor stub   
	    }   
	    /**
	     * 精减过滤方法
	     * @param inputString
	     * @return
	     */
	    public static String Html2Text(String inputString) {
	        String htmlStr = inputString; // 含html标签的字符串
	        String textStr = "";
	        Pattern p_script;
	        Matcher m_script;
	        Pattern p_style;
	        Matcher m_style;
	        Pattern p_html;
	        Matcher m_html;
	 
	        try {
	            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
	                                                                                                        // }
	            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
	                                                                                                    // }
	            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	 
	            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
	            m_script = p_script.matcher(htmlStr);
	            htmlStr = m_script.replaceAll(""); // 过滤script标签
	 
	            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
	            m_style = p_style.matcher(htmlStr);
	            htmlStr = m_style.replaceAll(""); // 过滤style标签
	 
	            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
	            m_html = p_html.matcher(htmlStr);
	            htmlStr = m_html.replaceAll(""); // 过滤html标签
	 
	            textStr = htmlStr;
	 
	        } catch (Exception e) {
	            System.err.println("Html2Text: " + e.getMessage());
	        }
	 
	        return textStr;// 返回文本字符串
	    }
	 
	    /**  
	     *   
	     * 基本功能：替换标记以正常显示  
	     * <p>  
	     *   
	     * @param input  
	     * @return String  
	     */  
	    public String replaceTag(String input) {   
	        if (!hasSpecialChars(input)) {   
	            return input;   
	        }   
	        StringBuffer filtered = new StringBuffer(input.length());   
	        char c;   
	        for (int i = 0; i <= input.length() - 1; i++) {   
	            c = input.charAt(i);   
	            switch (c) {   
	            case '<':   
	                filtered.append("&lt;");   
	                break;   
	            case '>':   
	                filtered.append("&gt;");   
	                break;   
	            case '"':   
	                filtered.append("&quot;");   
	                break;   
	            case '&':   
	                filtered.append("&amp;");   
	                break;   
	            default:   
	                filtered.append(c);   
	            }   
	  
	        }   
	        return (filtered.toString());   
	    }   
	  
	    /**  
	     *   
	     * 基本功能：判断标记是否存在  
	     * <p>  
	     *   
	     * @param input  
	     * @return boolean  
	     */  
	    public boolean hasSpecialChars(String input) {   
	        boolean flag = false;   
	        if ((input != null) && (input.length() > 0)) {   
	            char c;   
	            for (int i = 0; i <= input.length() - 1; i++) {   
	                c = input.charAt(i);   
	                switch (c) {   
	                case '>':   
	                    flag = true;   
	                    break;   
	                case '<':   
	                    flag = true;   
	                    break;   
	                case '"':   
	                    flag = true;   
	                    break;   
	                case '&':   
	                    flag = true;   
	                    break;   
	                }   
	            }   
	        }   
	        return flag;   
	    }   
	  
	    /**  
	     *   
	     * 基本功能：过滤所有以"<"开头以">"结尾的标签  
	     * <p>  
	     *   
	     * @param str  
	     * @return String  
	     */  
	    public static String filterHtml(String str) {   
	        Pattern pattern = Pattern.compile(regxpForHtml);   
	        Matcher matcher = pattern.matcher(str);   
	        StringBuffer sb = new StringBuffer();   
	        boolean result1 = matcher.find();   
	        while (result1) {   
	            matcher.appendReplacement(sb, "");   
	            result1 = matcher.find();   
	        }   
	        matcher.appendTail(sb);   
	        return sb.toString();   
	    }   
	  
	    /**  
	     *   
	     * 基本功能：过滤指定标签  
	     * <p>  
	     *   
	     * @param str  
	     * @param tag  
	     *            指定标签  
	     * @return String  
	     */  
	    public static String fiterHtmlTag(String str, String tag) {   
	        String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";   
	        Pattern pattern = Pattern.compile(regxp);   
	        Matcher matcher = pattern.matcher(str);   
	        StringBuffer sb = new StringBuffer();   
	        boolean result1 = matcher.find();   
	        while (result1) {   
	            matcher.appendReplacement(sb, "");   
	            result1 = matcher.find();   
	        }   
	        matcher.appendTail(sb);   
	        return sb.toString();   
	    }   
	  
	    /**  
	     *   
	     * 基本功能：替换指定的标签  
	     * <p>  
	     *   
	     * @param str  
	     * @param beforeTag  
	     *            要替换的标签  
	     * @param tagAttrib  
	     *            要替换的标签属性值  
	     * @param startTag  
	     *            新标签开始标记  
	     * @param endTag  
	     *            新标签结束标记  
	     * @return String  
	     * @如：替换img标签的src属性值为[img]属性值[/img]  
	     */  
	    public static String replaceHtmlTag(String str, String beforeTag,   
	            String tagAttrib, String startTag, String endTag) {   
	        String regxpForTag = "<\\s*" + beforeTag + "\\s+([^>]*)\\s*>";   
	        String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";   
	        Pattern patternForTag = Pattern.compile(regxpForTag);   
	        Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);   
	        Matcher matcherForTag = patternForTag.matcher(str);   
	        StringBuffer sb = new StringBuffer();   
	        boolean result = matcherForTag.find();   
	        while (result) {   
	            StringBuffer sbreplace = new StringBuffer();   
	            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag   
	                    .group(1));   
	            if (matcherForAttrib.find()) {   
	                matcherForAttrib.appendReplacement(sbreplace, startTag   
	                        + matcherForAttrib.group(1) + endTag);   
	            }   
	            matcherForTag.appendReplacement(sb, sbreplace.toString());   
	            result = matcherForTag.find();   
	        }   
	        matcherForTag.appendTail(sb);   
	        return sb.toString();   
	    }   
}
