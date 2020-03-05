package com.fxipp.kratos.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @author fangxi
 */
public class Md5Utils {
	private static final Logger log = LoggerFactory.getLogger(Md5Utils.class);

	/**
	 *
	 */
	public static String md5(String str) {
		//确定计算方法
		try {
			MessageDigest md5 = MessageDigest.getInstance("md5");
			//加密后的字符串
			byte[] src = md5.digest(str.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(src);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
