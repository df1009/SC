package com.zhongchou.common.util;


import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.yanshang.config.Config;
import com.yanshang.util.MD5;

/*字符串 DESede(3DES) 加密
 * ECB模式/使用PKCS7方式填充不足位,目前给的密钥是192位
 * 3DES（即Triple DES）是DES向AES过渡的加密算法（1999年，NIST将3-DES指定为过渡的
 * 加密标准），是DES的一个更安全的变形。它以DES为基本模块，通过组合分组方法设计出分组加
 * 密算法，其具体实现如下：设Ek()和Dk()代表DES算法的加密和解密过程，K代表DES算法使用的
 * 密钥，P代表明文，C代表密表，这样，
 * 3DES加密过程为：C=Ek3(Dk2(Ek1(P)))
 * 3DES解密过程为：P=Dk1((EK2(Dk3(C)))
 * */
public class ThreeDesUtil {

    /**
     * @param args在java中调用sun公司提供的3DES加密解密算法时，需要使
     * 用到$JAVA_HOME/jre/lib/目录下如下的4个jar包：
     *jce.jar
     *security/US_export_policy.jar
     *security/local_policy.jar
     *ext/sunjce_provider.jar
     */

    private static final String Algorithm = "DESede"; //定义加密算法,可用 DES,DESede,Blowfish
    //keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] keybyte,byte[] src){
         try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);//在单一方面的加密或解密
        } catch (java.security.NoSuchAlgorithmException e1) {
            // TODO: handle exception
             e1.printStackTrace();
        }catch(javax.crypto.NoSuchPaddingException e2){
            e2.printStackTrace();
        }catch(java.lang.Exception e3){
            e3.printStackTrace();
        }
        return null;
    }

    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decryptMode(byte[] keybyte,byte[] src){
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            // TODO: handle exception
            e1.printStackTrace();
        }catch(javax.crypto.NoSuchPaddingException e2){
            e2.printStackTrace();
        }catch(java.lang.Exception e3){
            e3.printStackTrace();
        }
        return null;
    }

    //转换成十六进制字符串
    public static String byte2Hex(byte[] b){
        String hs="";
        String stmp="";
        for(int n=0; n<b.length; n++){
            stmp = (java.lang.Integer.toHexString(b[n]& 0XFF));
            if(stmp.length()==1){
                hs = hs + "0" + stmp;
            }else{
                hs = hs + stmp;
            }
            if(n<b.length-1)hs=hs+":";
        }
        return hs.toUpperCase();
    }
    public static byte[] hex(String username){
        String key = String.valueOf(new Date().getTime());//关键字
        String f = DigestUtils.md5Hex(username+key);
       // System.out.println(f);
        byte[] bkeys = new String(f).getBytes();
        byte[] enk = new byte[24];
        for (int i=0;i<24;i++){
            enk[i] = bkeys[i];
           // System.out.println(i+":"+enk[i]);
        }
        return enk;
    }

    public static String getKey(String username){
    	byte[] enk = hex(username);//用户名
    	String key = new String(enk);
    	return key;
    }
    public static void main(String[] args) {
        String key = getKey("yilicai");
        key = "7f61ff638daf5bffd12f4ce2";
        System.out.println("key:"+key);
       // Security.addProvider(new com.sun.crypto.provider.SunJCE());
        String password = "fsdfdsfafdsfsfdsfdsfdsfsfds";//密码


        password = "{"+
    			"'head':{"+
    			"'req_ssn':'201508201000000',"+
    			"'req_date':'20150820',"+
    			"'version':'1.0',"+
    			"'ins_cd':'08J00102',"+
    			"'channel_no':'08J001020010001',"+
    			"'function':'G0000001',"+
    			"'rsp_code':'00000000',"+
    			"'rsp_desc':'成功'"+
    		"},"+
    		"'body':{"+
    			"'login_id':'18616896064',"+
    			"'total_results':2,"+
    			"'records':["+
    				"{"+
    					"'income_dt':'20150820',"+
    					"'income_amt':1000"+
    				"},"+
    				"{"+
    					"'income_dt':'20150819',"+
    					"'income_amt':1000"+
    				"}"+
    			"]"+
    		"}"+
    	"}";



        System.out.println("加密前的字符串:" + password);
        byte[] encoded = encryptMode(key.getBytes(),password.getBytes());
        String pword = Base64.encodeBase64String(encoded);
        System.out.println("加密后的字符串:" + pword);

        byte[] reqPassword = Base64.decodeBase64(pword);
        byte[] srcBytes = decryptMode(key.getBytes(),reqPassword);
        System.out.println("解密后的字符串:" + (new String(srcBytes)));
       // System.out.println(DigestUtils.md5Hex("yilicai"));
        //System.out.println(MD5.encryptMD5("yilicai"));
        System.out.println("1:"+ZhongzhengUtil.geTimeStamp());
        System.out.println("2:"+ZhongzhengUtil.get3desKey());
        System.out.println("3:"+ZhongzhengUtil.key);
    }
}
