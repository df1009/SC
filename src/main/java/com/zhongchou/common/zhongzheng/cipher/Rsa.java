package com.zhongchou.common.zhongzheng.cipher;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.log4j.Logger;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

public class Rsa {
	private static final Logger logger = Logger.getLogger(Rsa.class);
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final int KEY_SIZE = 1024;// 密钥长度
	private static final String SIGN_ALGORITHM = "SHA1withRSA";// 签名验签算法
	static {
		// 引入BouncyCastle
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	/**
	 * 随机生成密钥对
	 *
	 * @author : liliang
	 * @version : 2017-03-24 10:21:24
	 * @return
	 * @throws Exception
	 */
	public static KeyPair generateKeyPair() throws Exception {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
			keyPairGen.initialize(KEY_SIZE, new SecureRandom());
			return keyPairGen.genKeyPair();
		} catch (Exception e) {
			logger.error("init KeyPairGenerator failure", e);
			throw e;
		}
	}

	/**
	 * 根据公钥字符串获取公钥对象
	 *
	 * @author : liliang
	 * @version : 2017-03-24 10:32:56
	 * @param publicKeyStr
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String publicKeyStr) throws Exception {
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKeyStr));
			return kf.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error("rsa algorithm not support", e);
			throw e;
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			logger.error(BouncyCastleProvider.PROVIDER_NAME + " provider not support", e);
			throw e;
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			logger.error("publicKeyStr is invalid", e);
			throw e;
		}
	}

	/**
	 * 根据私钥字符串获取私钥对象
	 *
	 * @author : liliang
	 * @version : 2017-03-24 10:32:56
	 * @param privateKeyStr
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String privateKeyStr) throws Exception {
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKeyStr));
			return kf.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error("rsa algorithm not support", e);
			throw e;
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			logger.error(BouncyCastleProvider.PROVIDER_NAME + " provider not support", e);
			throw e;
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			logger.error("privateKeyStr is invalid", e);
			throw e;
		}
	}

	/**
	 * 私钥签名
	 *
	 * @author : liliang
	 * @version : 2017-03-24 11:04:29
	 * @param privateKey
	 * @param sourceStr
	 * @return
	 * @throws Exception
	 */
	public static String sign(String ownPrivateKeyStr, String sourceStr) throws Exception {
		try {
			PrivateKey privateKey = getPrivateKey(ownPrivateKeyStr);
			Signature signature = Signature.getInstance(SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
			signature.initSign(privateKey);
			signature.update(sourceStr.getBytes(DEFAULT_CHARSET));
			return Base64.toBase64String(signature.sign());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			logger.error("privateKey is invalid", e);
			throw e;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error(SIGN_ALGORITHM + " algorithm not support", e);
			throw e;
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			logger.error(BouncyCastleProvider.PROVIDER_NAME + " provider not support", e);
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("signature exception", e);
			throw e;
		}
	}

	/**
	 * 验证签名
	 *
	 * @author : liliang
	 * @version : 2017-03-24 11:07:46
	 * @param publicKey
	 * @param sourceStr
	 * @param signValue
	 * @return
	 * @throws Exception
	 */
	public static boolean verifySign(PublicKey publicKey, String sourceStr, String signValue) throws Exception {
		try {
			Signature signature = Signature.getInstance(SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
			signature.initVerify(publicKey);
			signature.update(sourceStr.getBytes(DEFAULT_CHARSET));
			return signature.verify(Base64.decode(signValue));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			logger.error("publicKey is invalid", e);
			throw e;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error(SIGN_ALGORITHM + " algorithm not support", e);
			throw e;
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			logger.error(BouncyCastleProvider.PROVIDER_NAME + " provider not support", e);
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("signature exception", e);
			throw e;
		}
	}

	/**
	 * 用公钥加密
	 *
	 * @author : liliang
	 * @version : 2017-03-24 11:10:31
	 * @param publicKey
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String publicKeyStr, String sourceStr) throws Exception {
		try {
			PublicKey publicKey = getPublicKey(publicKeyStr);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			/** 执行加密操作 */
			byte[] bytes = cipher.doFinal(sourceStr.getBytes(DEFAULT_CHARSET));
			return Base64.toBase64String(bytes);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			logger.error("publicKey is invalid", e);
			throw e;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error("rsa algorithm not support", e);
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("encrypt exception", e);
			throw e;
		}
	}

	/**
	 * 用私钥解密
	 *
	 * @author : liliang
	 * @version : 2017-03-24 11:18:25
	 * @param privateKey
	 * @param cipherStr
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String ownPrivateKeyStr, String cipherStr) throws Exception {
		try {
			PrivateKey privateKey = getPrivateKey(ownPrivateKeyStr);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			/** 执行解密操作 */
			byte[] bytes = cipher.doFinal(Base64.decode(cipherStr));
			return new String(bytes, DEFAULT_CHARSET);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			logger.error("privateKey is invalid", e);
			throw e;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.error("rsa algorithm not support", e);
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("decrypt exception", e);
			throw e;
		}
	}

	public static void main(String[] args) throws Exception {
		KeyPair kp = generateKeyPair();
		PrivateKey privateKey = kp.getPrivate();
		PublicKey publicKey = kp.getPublic();
		System.out.println(Base64.toBase64String(privateKey.getEncoded()));
		System.out.println(Base64.toBase64String(publicKey.getEncoded()));
	}
}
