package org.suhui.common.util;

import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

	public static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++){
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n += 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname)) {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			} else {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
			}
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };




	/**
	 * 方法描述:将字符串MD5加码 生成32位md5码
	 *
	 * @author
	 * @param inStr
	 * @return
	 */
	public static String md5(String inStr) {
		try {
			return DigestUtils.md5Hex(inStr.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误");
		}
	}

	/**
	 * 方法描述:签名字符串
	 *
	 * @author
	 * @param params 需要签名的参数
	 * @param appSecret 签名密钥
	 * @return
	 */
	public static String sign(HashMap<String, String> params, String appSecret) {
		StringBuilder valueSb = new StringBuilder();
		params.put("appSecret", appSecret);
		// 将参数以参数名的字典升序排序
		Map<String, String> sortParams = new TreeMap<String, String>(params);
		Set<Entry<String, String>> entrys = sortParams.entrySet();
		// 遍历排序的字典,并拼接value1+value2......格式
		for (Entry<String, String> entry : entrys) {
			valueSb.append(entry.getValue());
		}
		params.remove("appSecret");
		return md5(valueSb.toString());
	}

	/**
	 * 方法描述:验证签名
	 *
	 * @author
	 * @param appSecret 加密秘钥
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String appSecret, HttpServletRequest request) throws Exception {
		String sign = request.getParameter("sign");
		if (sign == null) {
			throw new Exception("请求中没有带签名");
		}
		if (request.getParameter("timestamp") == null) {
			throw new Exception("请求中没有带时间戳");
		}

		HashMap<String, String> params = new HashMap<String, String>();

		// 获取url参数
		@SuppressWarnings("unchecked")
		Enumeration<String> enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paramName = enu.nextElement().trim();
			if (!paramName.equals("sign")) {
				// 拼接参数值字符串并进行utf-8解码，防止中文乱码产生
				params.put(paramName, URLDecoder.decode(request.getParameter(paramName), "UTF-8"));
			}
		}

		params.put("appSecret", appSecret);

		// 将参数以参数名的字典升序排序
		Map<String, String> sortParams = new TreeMap<String, String>(params);
		Set<Entry<String, String>> entrys = sortParams.entrySet();

		// 遍历排序的字典,并拼接value1+value2......格式
		StringBuilder values = new StringBuilder();
		for (Entry<String, String> entry : entrys) {
			values.append(entry.getValue());
		}

		String mysign = md5(values.toString());
		if (mysign.equals(sign)) {
			return true;
		} else {
			return false;
		}

	}

}
