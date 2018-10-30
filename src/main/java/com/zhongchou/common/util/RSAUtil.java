package com.zhongchou.common.util;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

import org.bouncycastle.util.encoders.Base64;

public class RSAUtil {
/**使用私钥加密数据
  * 用一个已打包成byte[]形式的私钥加密数据，即数字签名
  *
  * @param keyInByte
  *            打包成byte[]的私钥
  * @param source
  *            要签名的数据，一般应是数字摘要
  * @return 签名 byte[]
  */
public static byte[] sign(byte[] keyInByte, byte[] source) {
  try {
   PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(keyInByte);
   KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
   PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
   Signature sig = Signature.getInstance("SHA1withRSA");
   sig.initSign(privKey);
   sig.update(source);
   return sig.sign();
  } catch (Exception e) {
   return null;
  }
}
/**
  * 验证数字签名
  *
  * @param keyInByte
  *            打包成byte[]形式的公钥
  * @param source
  *            原文的数字摘要
  * @param sign
  *            签名（对原文的数字摘要的签名）
  * @return 是否证实 boolean
  */
public static boolean verify(byte[] keyInByte, byte[] source, byte[] sign) {
  try {
   KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
   Signature sig = Signature.getInstance("SHA1withRSA");
   X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(keyInByte);
   PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
   sig.initVerify(pubKey);
   sig.update(source);
   return sig.verify(sign);
  } catch (Exception e) {
   return false;
  }
}
/**
  * 建立新的密钥对，返回打包的byte[]形式私钥和公钥
  *
  * @return 包含打包成byte[]形式的私钥和公钥的object[],其中，object[0]为私钥byte[],object[1]为公钥byte[]
  */
public static Object[] giveRSAKeyPairInByte() {
  KeyPair newKeyPair = creatmyKey();
  if (newKeyPair == null)
   return null;
  Object[] re = new Object[2];
  if (newKeyPair != null) {
   PrivateKey priv = newKeyPair.getPrivate();
   byte[] b_priv = priv.getEncoded();
   PublicKey pub = newKeyPair.getPublic();
   byte[] b_pub = pub.getEncoded();
   re[0] = b_priv;
   re[1] = b_pub;
   return re;
  }
  return null;
}
/**
  * 新建密钥对
  *
  * @return KeyPair对象
  */
public static KeyPair creatmyKey() {
  KeyPair myPair;
  long mySeed;
  mySeed = System.currentTimeMillis();
  try {
   KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
   SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
   random.setSeed(mySeed);
   keyGen.initialize(1024, random);
   myPair = keyGen.generateKeyPair();
  } catch (Exception e1) {
   return null;
  }
  return myPair;
}
/**
  * 使用RSA公钥加密数据
  *
  * @param pubKeyInByte
  *            打包的byte[]形式公钥
  * @param data
  *            要加密的数据
  * @return 加密数据
  */
public static byte[] encryptByRSA(byte[] pubKeyInByte, byte[] data) {
  try {
   KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
   X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
   PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
   Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
   cipher.init(Cipher.ENCRYPT_MODE, pubKey);
   return cipher.doFinal(data);
  } catch (Exception e) {
   return null;
  }
}
/**
  * 用RSA私钥解密
  *
  * @param privKeyInByte
  *            私钥打包成byte[]形式
  * @param data
  *            要解密的数据
  * @return 解密数据
  */
public static byte[] decryptByRSA(byte[] privKeyInByte, byte[] data) {
  try {
   PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(
     privKeyInByte);
   KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
   PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
   Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
   cipher.init(Cipher.DECRYPT_MODE, privKey);
   return cipher.doFinal(data);
  } catch (Exception e) {
   return null;
  }
}

/**
 * 使用RSA私钥加密数据
 *
 * @param pubKeyInByte
 *            打包的byte[]形式私钥
 * @param data
 *            要加密的数据
 * @return 加密数据
 */
public static byte[] encryptByRSA1(byte[] privKeyInByte, byte[] data) {
  try {
    PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(
      privKeyInByte);
    KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
    PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
    Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
    cipher.init(Cipher.ENCRYPT_MODE, privKey);
    return cipher.doFinal(data);
   } catch (Exception e) {
    return null;
   }
}
/**
 * 用RSA公钥解密
 *
 * @param privKeyInByte
 *            公钥打包成byte[]形式
 * @param data
 *            要解密的数据
 * @return 解密数据
 */
public static byte[] decryptByRSA1(byte[] pubKeyInByte, byte[] data) {
 try {
   KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
   X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
   PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
   Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
   cipher.init(Cipher.DECRYPT_MODE, pubKey);
   return cipher.doFinal(data);
  } catch (Exception e) {
   return null;
  }
}

/**
 * 计算字符串的SHA数字摘要，以byte[]形式返回
 */
public static byte[] MdigestSHA(String source) {
 //byte[] nullreturn = { 0 };
 try {
  MessageDigest thisMD = MessageDigest.getInstance("SHA");
  byte[] digest = thisMD.digest(source.getBytes("UTF-8"));
  return digest;
 } catch (Exception e) {
  return null;
 }
}

/**
 * 私钥解密
 * @throws UnsupportedEncodingException
 */
public static String decryptByPrivate(String privateKey,String content) throws UnsupportedEncodingException{
	//公钥加密私钥解密
	content = content.replace(" ", "+");
    //获得摘要
    byte[] sourcepub_pri = Base64.decode(content);
    byte[] bytKey = Base64.decode(privateKey);
    //使用私钥对密文进行解密 返回解密后的数据
    byte[] newSourcepub_pri=decryptByRSA(bytKey,sourcepub_pri);
    if(newSourcepub_pri ==null){
    	throw new UnsupportedEncodingException("解密失败");
    }
    String returnContent =  new String(newSourcepub_pri);
   // String returnContent = Base64.toBase64String(newSourcepub_pri);
    return returnContent;
}


/**
 * 公钥加密  decryptByPrivate   encryptionByPublic
 * @throws UnsupportedEncodingException
 */
public static String encryptionByPublic(String publicKey,String content) throws UnsupportedEncodingException{
	//公钥加密私钥解密
    //获得摘要
    //byte[] sourcepub_pri = Base64.decode(content);
	byte[] sourcepub_pri = content.getBytes();
    byte[] bytKey = Base64.decode(publicKey);
    //使用私钥对密文进行解密 返回解密后的数据
    byte[] newSourcepub_pri=encryptByRSA(bytKey,sourcepub_pri);
    String returnContent =  Base64.toBase64String(newSourcepub_pri);
    return returnContent;
}



/**
 * 私钥解密
 * @throws UnsupportedEncodingException
 */
public static String decryptByPrivateBase64(String privateKey,String content) throws UnsupportedEncodingException{
	//公钥加密私钥解密
    //获得摘要
    byte[] sourcepub_pri = Base64.decode(content);
    byte[] bytKey = Base64.decode(privateKey);
    //使用私钥对密文进行解密 返回解密后的数据
    byte[] newSourcepub_pri=decryptByRSA(bytKey,sourcepub_pri);
    String returnContent = Base64.toBase64String(newSourcepub_pri);
    return returnContent;
}


/**
 * 公钥加密  decryptByPrivate   encryptionByPublic
 * @throws UnsupportedEncodingException
 */
public static String encryptionByPublicBase64(String publicKey,String content) throws UnsupportedEncodingException{
	//公钥加密私钥解密
    //获得摘要
    byte[] sourcepub_pri = Base64.decode(content);
    byte[] bytKey = Base64.decode(publicKey);
    //使用私钥对密文进行解密 返回解密后的数据
    byte[] newSourcepub_pri=encryptByRSA(bytKey,sourcepub_pri);
    String returnContent =  Base64.toBase64String(newSourcepub_pri);
    return returnContent;
}

 /**
   *测试
   *
   */
 public static void main(String[] args) {
	/* String resort = null;
	 String str = "M24/DgqOq6AVYiomBve7Z4JFNVIueH9dJw2PSKoJGUF2QbRueg8ypXZVB3w0vtGq7ELCktTl3yJtaRR6tIs5fjBscZ7GoCnAftPQSr1/ygpoohonFW8UlvPeMZYJWau3DBX6iU2UuEAVlmg2FId/DXunIuKPEUfyURqBQ6IEQZw=";
	 String privateKey="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANBkyqgjZHafj7ztzQeFExZwyjIi9JUypAhYf+wuXJ7UmutK0Ru73Rj7YtDHbiBhev7g7zZlA8uSu1OilvCDBT40gQqHwgZaLPsUizzmeCLB62eIPr+tmddSC/xTOtOuD5k3wTid+pr/mykW/7vMxeJaGuwDIBikG3vF/WApEX9TAgMBAAECgYEAsp/icdK8d0132HqBBtQTM2YEeosp2IUqwxiQtfJbY61bCRn9OWeDMbmwzsJS7CCCW9yUTqyxXxprxVmVRZ0YrNfylzgnX0qj2R8RrMKPSoly6D5tuFtd8QEUvoCh51WU0vAxG7OAiyQlCT2RY31SFeF0IKex4T8Tvqi+S9isJqECQQDwJ0eYJw2WI6ielzTw13x951Z2mxcNQdPCOKSNlAEz5otqLPZOc2uOn/HgeA6ZJq48dW6PaXOLcvC22qygvQOxAkEA3iUFN4PcNwXRl84SDEt0/EZNX2TpaTIn2cDesxafsbm12WjNW1Wp+yCIYNmHJ68aSas49VJtld38jbwC8jwIQwJBAIy6N9YNbITCuJjWeot3etg8rAosr8FxBaoWjzb3uAy12JSLbHpkYG7fg1nKgKN2azu6hOM9JBxfand8hRRcLsECQHMHmHv2u8GCMdX5zz6ulBVOEiKjjnn6jsgjJy51OJw/HftGWZBB+5MedYAxI9tYwLQFBamC1VwVZwG9qYjlrSECQQCNBJT0rGnGhh3T7s1IM42rsAUvR704PFNo4/os8kAuL3ZSVLno2OH1sX4Z1DOwppBV49kPkiGFj+FTYgVHmJ7r";
		try {
			resort = RSAUtil.decryptByPrivate(privateKey,str);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("resort:"+resort);*/

   try {
/*
私钥：MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIVt2RZ6q1VRJnDbcXgM1Bs7kdcovaZDsN99I4eO2NRBIY/z1w82dqLetWzT2GwnS7fyGxM7/KQOUP6cnKp1wDVK60dsQE71OMWsgJHAPN0otICk3KZRB4m+hmwrLslbBZi0XWUYSakHc/NkZLeJRGqpyztaCa4XRM0ngYrVFc1HAgMBAAECgYBTdpI8KIGyPa/hGrPQ1516Z3nj7/0p2t/x/NJTwnxJ0XAQHwSg5H2zhcJRD/cqOC56IvzivDxd1wK9MfQiAMQ44GaK2Zr3Hj5tGcSnFbQcjemhCIlmXe/cp53kH3XtJuHJmSViDT2KMcAsX+vFLOrdJDgYjFFxLOnpIj0RatYnCQJBAPi2DceJWKgkeJzs3BZQv55bmgyV/EM/mhoERNQoP7d4KRLksAMEbg4Y/JRbzcQmWklp7ZPOr7foN4q8jcy4HcMCQQCJVuUTxvZxg3XLJ98WsM7mtgC3GNQ8A9swN4TiAA7gUD3ZRIguGHQSzM9O7OyJz8Y1RY3RKJ5m/GD7PmY5ggYtAkBDF2FKNWu8F/KNanvIbUnznwREZuVhArRAVmx2ytC9XmW+GgC9H6HMncLtBoLQagQrD68uEzBlrPfmrgyWcAhfAkB0bfUgEhFE5ESNKM0IiHWuwubSUjtCcaZ1NuvxSElUQtJcCaxFH7fvZC/vYg2ZO0p2SPPUOFIpTpXNCPZSlHc1AkByvOLDc3Y0w9cIwX9wD2E5rV/ARMU3Oq5GdIaAkg+SK/yVnhWwWUFqf1YB4snsWhfXsnj1n6/gNrmHVxvNdSx9
公钥：MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFbdkWeqtVUSZw23F4DNQbO5HXKL2mQ7DffSOHjtjUQSGP89cPNnai3rVs09hsJ0u38hsTO/ykDlD+nJyqdcA1SutHbEBO9TjFrICRwDzdKLSApNymUQeJvoZsKy7JWwWYtF1lGEmpB3PzZGS3iURqqcs7WgmuF0TNJ4GK1RXNRwIDAQAB

*/
    //私钥加密 公钥解密
    //生成私钥-公钥对
    Object[] v = giveRSAKeyPairInByte();
    //获得摘要
    byte[] source =MdigestSHA("假设这是要加密的客户数据");
    //使用私钥对摘要进行加密 获得密文 即数字签名
    System.out.println("私钥："+new String(Base64.toBase64String((byte[]) v[0])));
    String prKey = Base64.toBase64String((byte[]) v[0]);
    String puKey =  Base64.toBase64String((byte[]) v[1]);
    System.out.println("私钥："+prKey);
    System.out.println("公钥："+puKey);
    byte[] sign = sign((byte[]) v[0], source);
    //使用公钥对密文进行解密,解密后与摘要进行匹配
    boolean yes = verify((byte[]) v[1], source, sign);
    if (yes)
     System.out.println("匹配成功 合法的签名!");






   //公钥加密私钥解密
    //获得摘要
    byte[] sourcepub_pri = Base64.decode("7f61ff638daf5bffd12f4ce2");
   // System.out.println(new String(sourcepub_pri,"GBK"));
    System.out.println(Base64.toBase64String(sourcepub_pri));
    //使用公钥对摘要进行加密 获得密文
    byte[] signpub_pri =encryptByRSA((byte[]) v[1] ,sourcepub_pri);
    System.out.println("公钥加密密文："+Base64.toBase64String(signpub_pri));
    //使用私钥对密文进行解密 返回解密后的数据
    byte[] newSourcepub_pri=decryptByRSA((byte[]) v[0],signpub_pri);

    System.out.println("私钥解密："+Base64.toBase64String(newSourcepub_pri));
    //对比源数据与解密后的数据
    if(Arrays.equals(sourcepub_pri, newSourcepub_pri))
     System.out.println("匹配成功 合法的私钥!");



    //私钥加密公钥解密
    //获得摘要
    //byte[] sourcepri_pub = MdigestSHA("假设这是要加密的客户数据");
    byte[] sourcepri_pub = ("13265986584||316494646546486498||01||private").getBytes("UTF-8");

    //使用私钥对摘要进行加密 获得密文
    byte[] signpri_pub =encryptByRSA1((byte[]) v[0] ,sourcepri_pub);

 //   System.out.println("私钥加密密文："+new String(Base64.encodeBase64(sign11)));
    //使用公钥对密文进行解密 返回解密后的数据
    byte[] newSourcepri_pub=decryptByRSA1((byte[]) v[1],signpri_pub);



    System.out.println("公钥解密："+new String(newSourcepri_pub,"UTF-8"));













    String PUBLICKEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEGENnf3rdiO20isoLQqezw12FoWXII9FBw8nR1MWQ3X0CVzOsqY1hOmxD/YI9OB7WVIaVax5tj1l+wk6A0v85Z4OpGWqz4B5L3fCUlBwf/M6DXHlSN1OZttvQF3OeWvc6gvJHihR7pp18zc4KfCJx0Ry6IrGH/2SNOVE1AIgvRQIDAQAB";
    String PRIVATEKEY="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIQYQ2d/et2I7bSKygtCp7PDXYWhZcgj0UHDydHUxZDdfQJXM6ypjWE6bEP9gj04HtZUhpVrHm2PWX7CToDS/zlng6kZarPgHkvd8JSUHB/8zoNceVI3U5m229AXc55a9zqC8keKFHumnXzNzgp8InHRHLoisYf/ZI05UTUAiC9FAgMBAAECgYAGNcHNds/G5G4QY8n1149cwx19b8YCL7Thu5ucUr1q/w6mcoUKY/oyjPWUCLH7wMyqVNTy51NJ4UhazjW0lrbK4ZbPDHFij9CiZ7QFASiQ/TQWaL+KSIWnE6/rK9IdouwFKxk+cvvLteZoAXP6mFcrsa7LzfkENiIMu7mjpTNHAQJBANXv9U5JWOAVhWHDQcEWKn7YKpAVRleXdeUeJrXcdkqBDI+P6suA9j+ahDREfu+x65wUsrJotPHUXgJG0TarJIUCQQCeEPLrv6Qvi5+nbn2Eifn/fjsmIdI0U2WZKDHWJEnLsRUuGDNYxVE/SPDNDedA2OHeFB6j0Kk/ECdsWnUq6zvBAkAgUGViFMwa1MVX1fFZo+p5TFdpef0s/9Cr8djxAULQ0BtAmAFkCa+oPcOYTXxK4jnvUmUHc69ZE7W7bEzvj/wtAkB50X4mClAzBFxK4XCC0QOG0HYtcStbgFpwqvWdn+Hvxc4Y9DW+WHPBXimXHvv2ki+gw8jJX2rQW1bGvwBFz30BAkASPkORJxVWv91StjI2f/HXDO5eG5/su/XIb3eajaLUSEdaQlcs3ywLrrJ0o3VAR0J9aq59cmp6em017AMnmbF7";

    byte[] signPrivate=Base64.decode(PRIVATEKEY.getBytes());
    byte[] signPublic=Base64.decode(PUBLICKEY.getBytes());

    String publicpwd ="N/b4nYbbLFVq0yTAIOpNNydtNQUCQxQy0B7bD6kzxLMW2guYxXtWOC/9Z5dpWecx/y7d5CezUJ6cf/8++msiNie4DcKBaFDFPh5rPbjeEB+DRfhjcdR2BsVGXWLsq3dLYLgZObQXG6Tb9rXakuH34Y+6KIIwCjiODH2QAU+PSiM=";
    String privatepwd ="MTMyNjU5ODY1ODR8fDMxNjQ5NDY0NjU0NjQ4NjQ5OHx8MDF8fHByaXZhdGU=";
    //使用私钥对密文进行解密 返回解密后的数据
    byte[] newSource111=decryptByRSA(signPrivate,Base64.decode(publicpwd.getBytes()));
    System.out.println("私钥解密1："+new String(newSource111,"UTF-8"));


   } catch (Exception e) {
    e.printStackTrace();
   }


 }

}
