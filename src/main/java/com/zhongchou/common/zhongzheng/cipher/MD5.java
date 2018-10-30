package com.zhongchou.common.zhongzheng.cipher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;

public class MD5 {
	private static final Logger logger = Logger.getLogger(RSA.class);
	private static final String MD5_ALGORITHM = "MD5";
	private static final String DEFAULT_CHARSET = "UTF-8";

	public static String encode(String origin) throws NoSuchAlgorithmException, Exception {
		try {
			MessageDigest md = MessageDigest.getInstance(MD5_ALGORITHM);
			return Hex.encodeHexString(md.digest(origin.getBytes(DEFAULT_CHARSET)));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error(MD5_ALGORITHM + " algorithm not support", e);
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("encode exception", e);
			throw e;
		}
	}
}