package com.zhongchou.common.zhongzheng.cipher;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.bouncycastle.util.encoders.Base64;

public class Des3 {
	private static final Logger logger = Logger.getLogger(RSA.class);
	private static final String DES3_ALGORITHM = "DESede";
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final int KEY_LENG = 24;
	private static final String RANDOM_KEYCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 3des加密
	 * 
	 * @author : liliang
	 * @version : 2017-03-24 13:52:17
	 * @param secretKey
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(SecretKey secretKey, String source) throws Exception {
		try {
			// 实例化Cipher
			Cipher cipher = Cipher.getInstance(DES3_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			/** 执行加密操作 */
			byte[] bytes = cipher.doFinal(source.getBytes(DEFAULT_CHARSET));
			return Base64.toBase64String(bytes);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			logger.error("desKey is invalid", e);
			throw e;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error(DES3_ALGORITHM + " algorithm not support", e);
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("encrypt exception", e);
			throw e;
		}
	}

	/**
	 * 3des解密
	 * 
	 * @author : liliang
	 * @version : 2017-03-24 13:52:27
	 * @param secretKey
	 * @param encryptData
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(SecretKey secretKey, String cipherStr) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(DES3_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			/** 执行解密操作 */
			byte[] bytes = cipher.doFinal(Base64.decode(cipherStr));
			return new String(bytes, DEFAULT_CHARSET);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			logger.error("desKey is invalid", e);
			throw e;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error(DES3_ALGORITHM + " algorithm not support", e);
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("decrypt exception", e);
			;
			throw e;
		}
	}

	/**
	 * 根据3des密钥字符串获取密钥
	 * 
	 * @author : liliang
	 * @version : 2017-03-24 13:52:36
	 * @param desKey
	 * @return
	 */
	public static SecretKey getSecretKey(String desKey) {
		return new SecretKeySpec(desKey.getBytes(), DES3_ALGORITHM);
	}

	/**
	 * 随机生成3des密钥字符串
	 * 
	 * @author : liliang
	 * @version : 2017-03-24 13:55:42
	 * @return
	 */
	public static String randomDesKey() {
		String desKey = "";
		Random random = new Random();
		for (int i = 1; i <= KEY_LENG; i++) {
			desKey += RANDOM_KEYCHAR.charAt(random.nextInt(RANDOM_KEYCHAR.length()));
		}
		return desKey;
	}

	public static void main(String[] args) throws Exception {
		String desKey = randomDesKey();
		System.out.println(desKey);
		String cipherStr = encrypt(getSecretKey(desKey), "1qaz2wsx");
		System.out.println(cipherStr);
		System.out.println(decrypt(getSecretKey(desKey), cipherStr));
	}
}
