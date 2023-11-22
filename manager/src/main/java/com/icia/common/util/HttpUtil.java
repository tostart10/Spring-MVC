/**
 * <pre>
 * 프로젝트명 : BasicBoard
 * 패키지명   : com.icia.web.util
 * 파일명     : HttpUtil.java
 * 작성일     : 2020. 12. 29.
 * 작성자     : daekk
 * </pre>
 */
package com.icia.common.util;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * <pre>
 * 패키지명   : com.icia.web.util
 * 파일명     : HttpUtil.java
 * 작성일     : 2020. 12. 29.
 * 작성자     : daekk
 * 설명       : HTTP 통신 관련 유틸리티
 * </pre>
 */
public final class HttpUtil
{
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	private HttpUtil() {}

	/**
	 * <pre>
	 * 메소드명   : getRealPath
	 * 작성일     : 2020. 12. 29.
	 * 작성자     : daekk
	 * 설명       : Servlet 웹 루트 경로를 얻는다.(절대경로)
	 * </pre>
	 * @param request HttpServletRequest
	 * @return String
	 */
	public static String getRealPath(HttpServletRequest request)
	{
		return getRealPath(request, "/");
	}
	
	/**
	 * <pre>
	 * 메소드명   : getRealPath
	 * 작성일     : 2020. 12. 29.
	 * 작성자     : daekk
	 * 설명       : Servlet 웹 루트 경로를 얻는다.(절대경로)
	 * </pre>
	 * @param request HttpServletRequest
	 * @param path 경로
	 * @return String
	 */
	public static String getRealPath(HttpServletRequest request, String path)
	{
		if(request != null)
		{
			if(path != null)
			{	
				return request.getSession().getServletContext().getRealPath(path);
			}
			else
			{
				return request.getSession().getServletContext().getRealPath("/");
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * <pre>
	 * 메소드명   : isAjax
	 * 작성일     : 2020. 12. 29.
	 * 작성자     : daekk
	 * 설명       : AJAX 호출 여부 체크
	 * </pre>
	 * @param request HttpServletRequest
	 * @return boolean
	 */
	public static boolean isAjax(HttpServletRequest request)
	{
		if(request != null)
		{
			return StringUtil.equalsIgnoreCase("XMLHttpRequest", request.getHeader("X-Requested-With"));
		}
		
		return false;
	}
	
	/**
	 * <pre>
	 * 메소드명   : isAjax
	 * 작성일     : 2020. 12. 29.
	 * 작성자     : daekk
	 * 설명       : AJAX 호출 여부 체크 (헤더 지정 체크 포함)       
	 * </pre>
	 * @param request HttpServletRequest
	 * @param headerName 헤더명
	 * @return boolean
	 */
	public static boolean isAjax(HttpServletRequest request, String headerName)
	{
		if(request != null)
		{
			if(!StringUtil.isEmpty(headerName))
			{
				if(request.getHeader(headerName) != null || isAjax(request) == true)
				{
					return true;
				}
			}
			else
			{
				return isAjax(request);
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : get
	 * 메소드 설명    : HttpServletRequest 객체에서 name과 일치하는 값을 가져온다.
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * @param request javax.servlet.http.HttpServletRequest
	 * @param name 이름
	 * @return String
	 * </pre>
	 */
	public static String get(HttpServletRequest request, String name)
	{
		if(name == null)
		{
			return "";
		}
		
		return StringUtil.nvl(request.getParameter(name)).trim();
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : get
	 * 메소드 설명    : HttpServletRequest 객체에서 name과 일치하는 값을 가져온다.
	 *                  값이 없을시 defaultValue로 대체
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * 수정이력       :
	 * @param request javax.servlet.http.HttpServletRequest
	 * @param name 이름
	 * @param defValue 기본값
	 * @return String
	 * </pre>
	 */
	public static String get(HttpServletRequest request, String name, String defValue)
	{
		String str = get(request, name);
		
		if(StringUtil.isEmpty(str))
		{
			return defValue;
		}
		else
		{
			return str;
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : get
	 * 메소드 설명    : HttpServletRequest 객체에서 name과 일치하는 값을 가져온다.
	 *                  값이 없을시 defaultValue로 대체
	 *
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * 수정이력       :
	 * @param request javax.servlet.http.HttpServletRequest
	 * @param name 이름
	 * @param defValue 기본값
	 * @return short
	 * </pre>
	 */
	public static short get(HttpServletRequest request, String name, short defValue)
	{
		String str = get(request, name);
		
		if(StringUtil.isEmpty(str))
		{
			return defValue;
		}
		else
		{
			try 
			{
				short result = Short.parseShort(str);
				
				return result;
			}
			catch(NumberFormatException e)
			{
				return defValue;
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : get
	 * 메소드 설명    : HttpServletRequest 객체에서 name과 일치하는 값을 가져온다.
	 *                  값이 없을시 defaultValue로 대체
	 *
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * 수정이력       :
	 * @param request javax.servlet.http.HttpServletRequest
	 * @param name 이름
	 * @param defValue 기본값
	 * @return int
	 * </pre>
	 */
	public static int get(HttpServletRequest request, String name, int defValue)
	{
		String str = get(request, name);
		
		if(StringUtil.isEmpty(str))
		{
			return defValue;
		}
		else
		{
			try 
			{
				int result = Integer.parseInt(str);
				
				return result;
			}
			catch(NumberFormatException e)
			{
				return defValue;
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : get
	 * 메소드 설명    : HttpServletRequest 객체에서 name과 일치하는 값을 가져온다.
	 *                  값이 없을시 defaultValue로 대체
	 *
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * 수정이력       :
	 * @param request javax.servlet.http.HttpServletRequest
	 * @param name 이름
	 * @param defValue 기본값
	 * @return long
	 * </pre>
	 */
	public static long get(HttpServletRequest request, String name, long defValue)
	{
		String str = get(request, name);
		
		if(StringUtil.isEmpty(str))
		{
			return defValue;
		}
		else
		{
			try 
			{
				long result = Long.parseLong(str);
				
				return result;
			}
			catch(NumberFormatException e)
			{
				return defValue;
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : get
	 * 메소드 설명    : HttpServletRequest 객체에서 name과 일치하는 값을 가져온다.
	 *                  값이 없을시 defaultValue로 대체
	 *
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * 수정이력       :
	 * @param request javax.servlet.http.HttpServletRequest
	 * @param name 이름
	 * @param defValue 기본값
	 * @return float
	 * </pre>
	 */
	public static float get(HttpServletRequest request, String name, float defValue)
	{
		String str = get(request, name);
		
		if(StringUtil.isEmpty(str))
		{
			return defValue;
		}
		else
		{
			try 
			{
				float result = Float.parseFloat(str);
				
				return result;
			}
			catch(NumberFormatException e)
			{
				return defValue;
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : get
	 * 메소드 설명    : HttpServletRequest 객체에서 name과 일치하는 값을 가져온다.
	 *                  값이 없을시 defaultValue로 대체
	 *
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * 수정이력       :
	 * @param request javax.servlet.http.HttpServletRequest
	 * @param name 이름
	 * @param defValue 기본값
	 * @return double
	 * </pre>
	 */
	public static double get(HttpServletRequest request, String name, double defValue)
	{
		String str = get(request, name);
		
		if(StringUtil.isEmpty(str))
		{
			return defValue;
		}
		else
		{
			try 
			{
				double result = Double.parseDouble(str);
				
				return result;
			}
			catch(NumberFormatException e)
			{
				return defValue;
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : gets
	 * 메소드 설명    : HttpServletRequest 객체에서 Paramter name과 일치하는 값을 가져온다.
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * 수정이력       :
	 * @param request javax.servlet.http.HttpServletRequest
	 * @param name 이름
	 * @return String[]
	 * </pre>
	 */
	public static String[] gets(HttpServletRequest request, String name)
	{
		return request.getParameterValues(name);
	}
	
	/**
	 * <pre>
	 * 메소드명   : getMap
	 * 작성일     : 2020. 12. 29.
	 * 작성자     : daekk
	 * 설명       : HttpServletRequest 객체에서 Paramter를 Map<String, String[]> 형태로 가져온다.
	 * </pre>
	 * @param request javax.servlet.http.HttpServletRequest
	 * @return java.util.Map<String, String[]>
	 */
	public static Map<String, String[]> getMap(HttpServletRequest request)
	{
		return request.getParameterMap();
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : getHeader
	 * 메소드 설명    : HttpServletRequest 객체에서 name과 일치하는 헤더 정보를 가져온다.
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * @param request javax.servlet.http.HttpServletRequest
	 * @param name 이름
	 * @return String
	 * </pre>
	 */
	public static String getHeader(HttpServletRequest request, String name)
	{
		if(name == null)
		{
			return "";
		}
		
		return StringUtil.nvl(request.getHeader(name));
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : getHeaders
	 * 메소드 설명    : HttpServletRequest 객체에서 전체 헤더 정보를 가져온다.
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * @param request javax.servlet.http.HttpServletRequest
	 * @return java.util.Map
	 * </pre>
	 */
	public static Map<String, String> getHeaders(HttpServletRequest request)
	{
		Map<String, String> map = new HashMap<String, String>();
		
		Enumeration<String> headers = request.getHeaderNames();
        while(headers.hasMoreElements())
        {
        	String key = (String) headers.nextElement();
        	String value = request.getHeader(key);
        	map.put(key, value);
        }
        		
		return map;
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : getIP
	 * 메소드 설명    : 호출자의 IP를 가져온다.
	 *
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * 수정이력       :
	 * @param request HttpServletRequest
	 * @return String
	 * </pre>
	 */
	public static String getIP(HttpServletRequest request)
	{
		String[] headers = {"X-FORWARDED-FOR", 
				            "X-Forwarded-For", 
				            "Proxy-Client-IP", 
				            "WL-Proxy-Client-IP", 
				            "HTTP_CLIENT_IP", 
				            "HTTP_X_FORWARDED_FOR", 
				            "X-Real-IP", 
				            "X-RealIP"};
		
		String strIP = getIP(request, headers);

		return strIP;
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : getIP
	 * 메소드 설명    : 호출자의 IP를 가져온다.
	 *                  (WAS 는 보통 2차 방화벽 안에 있고 Web Server 를 통해 client 에서 호출되거나 cluster로 구성되어 
	 *                   load balancer 에서 호출되는데 이럴 경우에서 getRemoteAddr() 을 호출하면 웹서버나 load balancer의 IP 가 나옴)
	 *
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * 수정이력       :
	 * @param request HttpServletRequest
	 * @param headers 헤더 배열
	 * @return String
	 * </pre>
	 */
	public static String getIP(HttpServletRequest request, String[] headers)
	{
		if(headers == null)
		{
			return request.getRemoteAddr();
		}
		
		String strIP = null;
		
		for(int i=0; i<headers.length; i++)
		{
			strIP = getHeaderIP(request, headers[i]);
			
			if(strIP != null && strIP.length() != 0 && !"unknown".equalsIgnoreCase(strIP))
			{
				if(strIP.indexOf(",") > -1)
				{
					String[] ipArray = StringUtil.tokenizeToStringArray(strIP, ",");
					
					if(ipArray != null && ipArray.length > 0)
					{
						strIP = StringUtil.trim(ipArray[0]);
					}
				}
				
				break;
			}
		}
		
		if(strIP == null || strIP.length() == 0 || "unknown".equalsIgnoreCase(strIP))
		{
			strIP = request.getRemoteAddr();
		}
		
		return strIP;
	}

	/**
	 * 
	 * <pre>
	 * 메소드명       : getHeaderIP
	 * 메소드 설명    : 호출자의 IP를 가져온다.
	 *                  (WAS 는 보통 2차 방화벽 안에 있고 Web Server 를 통해 client 에서 호출되거나 cluster로 구성되어 
	 *                   load balancer 에서 호출되는데 이럴 경우에서 getRemoteAddr() 을 호출하면 웹서버나 load balancer의 IP 가 나옴)
	 *
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : daekk
	 * 수정이력       :
	 * @param request HttpServletRequest
	 * @param header 헤더
	 * @return String
	 * </pre>
	 */
	public static String getHeaderIP(HttpServletRequest request, String header)
	{
		if(header == null)
		{
			return null;
		}
				
		return request.getHeader(header);
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명      : getUrlDecode
	 * 메소드 설명   : 값을 UTF-8로 Decode 한다.
	 * 작성일        : 2020. 12. 29.
	 * 작성자        : daekk 
	 * @param str 값
	 * @return String
	 * </pre>
	 */
	public static String getUrlDecode(String str)
	{
		return getUrlDecode(str, "UTF-8");
	}
	
	/**
	 * <pre>
	 * 메소드명   : getUrlDecode
	 * 작성일     : 2020. 12. 29.
	 * 작성자     : daekk
	 * 설명       :
	 * </pre>
	 * @param str
	 * @param charset
	 * @return String
	 */
	public static String getUrlDecode(String str, String charset)
	{
		if(!StringUtil.isEmpty(str))
		{
			String strDecode = "";
			
			if(StringUtil.isEmpty(charset))
			{
				charset = "UTF-8";				
			}	
			
			try 
			{
				Charset _charset = Charset.forName(charset);
				charset = _charset.name();
	        } 
			catch (Exception e) 
			{
				Charset _charset = Charset.forName("UTF-8");
				charset = _charset.name();
	        } 
			
			try
			{
				strDecode = URLDecoder.decode(str, charset);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
				return str;
			}
			
			return strDecode;
		}
		else
		{
			return str;
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명      : getUrlEncode
	 * 메소드 설명   : 값을 UTF-8로 Encode 한다.
	 * 작성일        : 2020. 12. 29.
	 * 작성자        : 
	 * 수정이력      :
	 * @param str 값
	 * @return String
	 * </pre>
	 */
	public static String getUrlEncode(String str)
	{
		return getUrlEncode(str, "UTF-8");
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : getUrlEncode
	 * 메소드 설명   : 값을 charset으로 Encode 한다.
	 *
	 * 작성일          : 2020. 12. 29.
	 * 작성자          : 
	 * 수정이력       :
	 * @param str 값
	 * @param charset 캐릭터셋
	 * @return String
	 * </pre>
	 */
	public static String getUrlEncode(String str, String charset)
	{
		if(!StringUtil.isEmpty(str))
		{
			String strEncode = "";
			
			if(StringUtil.isEmpty(charset))
			{
				charset = "UTF-8";				
			}
			
			try 
			{
				Charset _charset = Charset.forName(charset);
				charset = _charset.name();
	        } 
			catch (Exception e) 
			{
				Charset _charset = Charset.forName("UTF-8");
				charset = _charset.name();
	        } 
			
			try
			{
				strEncode = URLEncoder.encode(str, charset);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
				return str;
			}
			
			return strEncode;
		}
		else
		{
			return str;
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 메소드명       : isHttps
	 * 메소드 설명    : 보안프로트콜 확인 (true : HTTPS, false : HTTP)
	 *
	 * 작성일         : 2020. 12. 29.
	 * 작성자         : 
	 * 수정이력       :
	 * @param request HttpServletRequest
	 * @return boolean
	 * </pre>
	 */
	public static boolean isHttps(HttpServletRequest request)
	{
		if(request.isSecure())
		{
			return true;
		}
		else
		{
			return false;
		}
	}	
	
	/**
	 * <pre>
	 * 메소드명   : filter
	 * 작성일     : 2020. 12. 30.
	 * 작성자     : daekk
	 * 설명       : 특수문자 필터링
	 * </pre>
	 * @param message 문자열
	 * @return String
	 */
	public static String filter(String message)
	{

		if (message == null)
		{
			return null;
		}

		char content[] = new char[message.length()];
		message.getChars(0, message.length(), content, 0);
		StringBuilder result = new StringBuilder(content.length + 50);
		
		for (int i = 0; i < content.length; i++)
		{
			switch (content[i])
			{
				case '<' :
					result.append("&lt;");
					break;
				case '>' :
					result.append("&gt;");
					break;
				case '&' :
					result.append("&amp;");
					break;
				case '"' :
					result.append("&quot;");
					break;
				default :
					result.append(content[i]);
			}
		}
		
		return result.toString();
	}
	
	/**
	 * <pre>
	 * 메소드명   : requestLog
	 * 작성일     : 2021. 1. 19.
	 * 작성자     : daekk
	 * 설명       : HTTP 로깅
	 * </pre>
	 * @param request HttpServletRequest
	 */
	public static void requestLog(HttpServletRequest request)
	{
		if(logger.isDebugEnabled())
		{
			String lineSeparator = FileUtil.getLineSeparator();
			StringBuilder sb = new StringBuilder();
			
			sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
			sb.append("[Remote IP] : [" + HttpUtil.getIP(request) + "]"+lineSeparator);
			sb.append("[Locale]    : [" + request.getLocale() + "]"+lineSeparator);
			sb.append("[URL]       : [" + request.getRequestURL() + "]"+lineSeparator);
	        sb.append("[URI]       : [" + request.getRequestURI() + "]"+lineSeparator);
	        sb.append("[Method]    : [" + request.getMethod() + "]"+lineSeparator);
	        sb.append("[Protocol]  : [" + request.getProtocol() + "]"+lineSeparator);
	        sb.append("[Referer]   : [" + StringUtil.nvl(request.getHeader("Referer")) + "]"+lineSeparator);
	        sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	        sb.append("// Headers                                                                      "+lineSeparator);
	        sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	        
	        Enumeration<String> headers = request.getHeaderNames();
	        while(headers.hasMoreElements())
	        {
	            String name = headers.nextElement();
	        	Enumeration<String> values = request.getHeaders(name);
	            
	            if(values != null)
	            {	
	                while(values.hasMoreElements())
	                {
	                	sb.append("["+name+"] : [" + StringUtil.nvl(values.nextElement()) + "]"+lineSeparator);
	                }
	            }
	        }
	        sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	        
	        sb.append(lineSeparator);
	        if(StringUtil.equalsIgnoreCase(request.getMethod(), "get"))
	        {
	        	sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	            sb.append("// Get Parameters                                                               "+lineSeparator);
	            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	            sb.append(StringUtil.nvl(request.getQueryString())+lineSeparator);
	            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	        }
	        else
	        {
		    	if(!(request instanceof MultipartHttpServletRequest))
		    	{
		            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
		            sb.append("// Post Parameters                                                              "+lineSeparator);
		            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
		            
		            Enumeration<String> params = request.getParameterNames();
		            if(params.hasMoreElements())
		            {	
			            while(params.hasMoreElements())
			            {
			                String name = params.nextElement();
			                String[] values = request.getParameterValues(name);
			
			                if(values != null)
			                {
			                    for(int i=0; i<values.length; i++)
			                    {
			                        sb.append("["+name+"] : " + (values[i] != null ? "["+values[i]+"]" : "[null]")+lineSeparator);
			                    }
			                }
			
			            }
		            }
		            
		            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
		    	}
		    	else
		    	{
		    		MultipartHttpServletRequest _request = (MultipartHttpServletRequest)request;
		    		
		    		sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
		            sb.append("// Multipart Parameters                                                         "+lineSeparator);
		            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
		            
		            Enumeration<String> params = _request.getParameterNames();
		            while(params.hasMoreElements())
		            {
		                String name = params.nextElement();
		                String[] values = _request.getParameterValues(name);
		
		                if(values != null)
		                {
		                    for(int i=0; i<values.length; i++)
		                    {
		                        sb.append("["+name+"] : [" + values[i] + "]"+lineSeparator);
		                    }
		                }
		
		            }
		            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
		            
		            sb.append(lineSeparator);
		            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
		            sb.append("// Multipart Files                                                              "+lineSeparator);
		            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
		            
		            long total = 0;
		            Iterator<String> files = _request.getFileNames();
		            while(files.hasNext())
		            {
		                String name = files.next();
		                List<MultipartFile> multipartFiles = _request.getFiles(name);
		
		                if(multipartFiles != null)
		                {
		                    for(int i=0; i<multipartFiles.size(); i++)
		                    {
		                    	MultipartFile multipartFile = multipartFiles.get(i);
		                    	
		                    	if(multipartFile != null)
		                    	{
		                    		total += multipartFile.getSize();
		                    		sb.append("["+name+"] : [" + multipartFile.getOriginalFilename() + "] {contentType: "+multipartFile.getContentType()+", size: " + FileUtil.byteToDisplay(multipartFile.getSize()) + "}"+lineSeparator);
		                    	}
		                    }
		                    sb.append("[File Total Size] : " + FileUtil.byteToDisplay(total)+lineSeparator);
		                }
		
		            }
		            sb.append("////////////////////////////////////////////////////////////////////////////////");
		    	}
	        }
	        
	        logger.debug(sb.toString());
		}
	}
	
	/**
	 * <pre>
	 * 메소드명   : requestLogString
	 * 작성일     : 2021. 1. 19.
	 * 작성자     : daekk
	 * 설명       : HTTP 로그 문자열을 얻는다.
	 * </pre>
	 * @param request HttpServletRequest
	 * @return String
	 */
	public static String requestLogString(HttpServletRequest request)
	{
		String lineSeparator = FileUtil.getLineSeparator();
		StringBuilder sb = new StringBuilder();
		
		sb.append(lineSeparator);
		sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
		sb.append("[Remote IP] : [" + HttpUtil.getIP(request) + "]"+lineSeparator);
		sb.append("[Locale]    : [" + request.getLocale() + "]"+lineSeparator);
		sb.append("[URL]       : [" + request.getRequestURL() + "]"+lineSeparator);
        sb.append("[URI]       : [" + request.getRequestURI() + "]"+lineSeparator);
        sb.append("[Method]    : [" + request.getMethod() + "]"+lineSeparator);
        sb.append("[Protocol]  : [" + request.getProtocol() + "]"+lineSeparator);
        sb.append("[Referer]   : [" + StringUtil.nvl(request.getHeader("Referer")) + "]"+lineSeparator);
        sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
        sb.append("// Headers                                                                      "+lineSeparator);
        sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
        
        Enumeration<String> headers = request.getHeaderNames();
        while(headers.hasMoreElements())
        {
            String name = headers.nextElement();
        	Enumeration<String> values = request.getHeaders(name);
            
            if(values != null)
            {	
                while(values.hasMoreElements())
                {
                	sb.append("["+name+"] : [" + StringUtil.nvl(values.nextElement()) + "]"+lineSeparator);
                }
            }
        }
        sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
        
        sb.append(lineSeparator);
        if(StringUtil.equalsIgnoreCase(request.getMethod(), "get"))
        {
        	sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
            sb.append("// Get Parameters                                                               "+lineSeparator);
            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
            sb.append(StringUtil.nvl(request.getQueryString())+lineSeparator);
            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
        }
        else
        {
	    	if(!(request instanceof MultipartHttpServletRequest))
	    	{
	            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	            sb.append("// Post Parameters                                                              "+lineSeparator);
	            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	            
	            Enumeration<String> params = request.getParameterNames();
	            if(params.hasMoreElements())
	            {	
		            while(params.hasMoreElements())
		            {
		                String name = params.nextElement();
		                String[] values = request.getParameterValues(name);
		
		                if(values != null)
		                {
		                    for(int i=0; i<values.length; i++)
		                    {
		                        sb.append("["+name+"] : " + (values[i] != null ? "["+values[i]+"]" : "[null]")+lineSeparator);
		                    }
		                }
		
		            }
	            }
	            
	            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	    	}
	    	else
	    	{
	    		MultipartHttpServletRequest _request = (MultipartHttpServletRequest)request;
	    		
	    		sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	            sb.append("// Multipart Parameters                                                         "+lineSeparator);
	            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	            
	            Enumeration<String> params = _request.getParameterNames();
	            while(params.hasMoreElements())
	            {
	                String name = params.nextElement();
	                String[] values = _request.getParameterValues(name);
	
	                if(values != null)
	                {
	                    for(int i=0; i<values.length; i++)
	                    {
	                        sb.append("["+name+"] : [" + values[i] + "]"+lineSeparator);
	                    }
	                }
	
	            }
	            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	            
	            sb.append(lineSeparator);
	            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	            sb.append("// Multipart Files                                                              "+lineSeparator);
	            sb.append("////////////////////////////////////////////////////////////////////////////////"+lineSeparator);
	            
	            long total = 0;
	            Iterator<String> files = _request.getFileNames();
	            while(files.hasNext())
	            {
	                String name = files.next();
	                List<MultipartFile> multipartFiles = _request.getFiles(name);
	
	                if(multipartFiles != null)
	                {
	                    for(int i=0; i<multipartFiles.size(); i++)
	                    {
	                    	MultipartFile multipartFile = multipartFiles.get(i);
	                    	
	                    	if(multipartFile != null)
	                    	{
	                    		total += multipartFile.getSize();
	                    		sb.append("["+name+"] : [" + multipartFile.getOriginalFilename() + "] {contentType: "+multipartFile.getContentType()+", size: " + FileUtil.byteToDisplay(multipartFile.getSize()) + "}"+lineSeparator);
	                    	}
	                    }
	                    sb.append("[File Total Size] : " + FileUtil.byteToDisplay(total)+lineSeparator);
	                }
	
	            }
	            sb.append("////////////////////////////////////////////////////////////////////////////////");
	    	}
        }
	    
        return sb.toString();
	}
}
