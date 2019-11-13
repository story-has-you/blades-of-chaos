package com.fxipp.kratos.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author fangxi
 */
public class Md5Utils {
	private static final Logger log = LoggerFactory.getLogger(Md5Utils.class);

	/**
	 *
	 * @Title: MD5Utils.java
	 * @Package com.imooc.utils
	 * @Description: 对字符串进行md5加密
	 */
	public static String md5(String strValue) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			log.error("加密失败", e);
		}
		return Base64.encodeBase64String(md5.digest(strValue.getBytes()));
	}
}
