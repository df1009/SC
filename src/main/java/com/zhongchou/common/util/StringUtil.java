package com.zhongchou.common.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yanshang.util.StringUtils;

public class StringUtil {
	public static boolean match(String regex, String str)
	  {
		if(StringUtils.isEmpty(str))
			return false;
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(str);
	    return matcher.matches();
	  }
	//是否为纯数字，带小数点和负号
	public static boolean isNumber(String value){
		if(StringUtils.isEmpty(value)){
			return false;
		}
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return value.matches(regex);
    }

	//是否为纯数字
	public static boolean isAllNumber(String value){
		if(StringUtils.isEmpty(value)){
			return false;
		}
        String regex = "[0-9]+";
        return value.matches(regex);
    }
	//手机号格式
	public static boolean isMobileNum(String str)
	  {
		if(StringUtils.isEmpty(str)){
			return false;
		}
	    String regex = "^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$";
	    return match(regex, str);
	  }
	//身份证格式
	public static boolean isIdCard(String str)
	{
		if(StringUtils.isEmpty(str)){
			return false;
		}
		String regex = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
		return match(regex, str);
	}
	//银行卡掩码
	public static String hideCardNum(String bankCode)
	{
		if(bankCode.length()>7){
			//返回银行卡号并只显示前4位后3位
			String str1 = bankCode.substring(0, 4);
			String str2 = bankCode.substring(bankCode.length()-3, bankCode.length());
			StringBuilder sb = new StringBuilder();
			sb.append(str1);
			for(int i=0; i<bankCode.length()-7;i++){
				sb.append("*");
			}
			return sb.append(str2).toString();
		}else{
			return bankCode;
		}
	}
	//邮箱掩码
	public static String encryptEmail(String email)
	 {
		 if(StringUtils.isEmpty(email)){
				return "";
		 	}
	    String ret = email.substring(0, 3);
	    ret = ret + "******";
	    ret = ret + email.substring(email.length() - 3);
	    return ret;
	 }

	//金额过大转成数字1.0E7
	public static String doubleDecimalFormat(Double amount)
	 {
		if(StringUtils.isEmpty(amount+"")){
			return "";
		}
		return new DecimalFormat("#").format(amount);

	 }
	//销售额-1转换0 分转元
	public static String changeSalesQuota(String amount)
	 {
		if(StringUtils.isEmpty(amount)){
			return "0";
		}else{
			if("-1".equals(amount)){
				return "0";
			}else{
				return StringUtil.doubleDecimalFormat(Double.parseDouble(amount)/100);
			}
		}
	 }
	//转换成万元
	public static String changebSalesQuota(String amount)
	 {
		if(StringUtils.isEmpty(amount)){
			return "0";
		}else{
			if("-1".equals(amount)){
				return "0";
			}else{
				return StringUtil.doubleDecimalFormat(Double.parseDouble(amount)/100/10000);
			}
		}
	 }
	/**
	 * 两个时间相差多少个小时
	 * @param str1
	 * @param str2
	 * @return
	 * @throws Exception
	 */
	public static long getDistanceDays(String str1) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long days=0;
        Date now = new Date();
        Date two = null;
		try {
			two = (Date) df.parse(str1);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
        long time1 = now.getTime();
        long time2 = two.getTime();
        long diff=0 ;
        if(time1>time2) {
            diff = time1 - time2;
        }
        days = diff / (1000 * 60 * 60);
        return days;
    }

	//真实姓名加*
	public static String userNameMask(String userName)
	 {
		String retName = "";
		if(StringUtils.isEmpty(userName)){
			return retName;
		}
		retName = userName.substring(0,1);
		for (int i = 1; i < userName.length(); i++) {
			retName+="*";
		}
		return retName;
	 }

	//中证返回的desc取错误消息
	public static String splitDesc(String msg)
	 {
		String rsp_desc =msg;
		if(rsp_desc.indexOf("|")>0){
			String[] desc = rsp_desc.split("\\|");
			rsp_desc = desc[desc.length-1];
		}
		return rsp_desc;
	 }

	//身份证后4位掩码
	public static String maskIdCard(String idCard)
	 {
		 if (StringUtils.isEmpty(idCard)) {
		      return "";
		    }
		 String ret = null;
		 if(idCard.length() == 18){
			 ret = idCard.substring(0,14);
			 ret = ret + "****";
		 }else if(idCard.length() == 15){
			 ret = idCard.substring(0,12);
			 ret = ret + "***";
		 }
		  return ret;
	 }
	//版本号比价，返回最大位数版本的差值
	public static int compareVersion(String version1, String version2) throws Exception {
	    if (version1 == null || version2 == null) {
	        throw new Exception("compareVersion error:illegal params.");
	    }
	    String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
	    String[] versionArray2 = version2.split("\\.");
	    int idx = 0;
	    int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
	    int diff = 0;
	    while (idx < minLength
	            && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
	            && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
	        ++idx;
	    }
	    //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
	    diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
	    return diff;
	}

	//富文本编辑器去格式
	public static String editorFormat(String txtcontent)
	 {
		txtcontent = txtcontent.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符
		return txtcontent;
	 }
	//风险等级转换
	public static Map levelNumberTOString(String riskLevel)
	 {
		Map out = new HashMap();
		switch (riskLevel) {
		case "1":
			out.put("riskHand","低");//风险承受能力
			out.put("profitExpectation","谨慎型");//投资行为
			break;
		case "2":
			out.put("riskHand","中低");//风险承受能力
			out.put("profitExpectation","稳健型");//投资行为
			break;
		case "3":
			out.put("riskHand","中");//风险承受能力
			out.put("profitExpectation","平衡型");//投资行为
			break;
		case "4":
			out.put("riskHand","中高");//风险承受能力
			out.put("profitExpectation","进取型");//投资行为
			break;
		case "5":
			out.put("riskHand","高");//风险承受能力
			out.put("profitExpectation","激进型");//投资行为
			break;
		default:
			out.put("riskHand","");//风险承受能力
			out.put("profitExpectation","");//投资行为
			break;
		}
		return out;
	 }

	//根据银行cd判断该行是否需要网银签约 0：否  1：是
	public static String isBankSign(String bankCd)
	 {
		if("0103,0307,0309,0401".indexOf(bankCd)>0){
			return "0";
		}
		return "1";
	 }
	//根据银行cd转换银行名
	public static String supportBank(String bankCd)
	 {
		switch (bankCd) {
	        case "0100" :
	            return "邮储银行";
	        case "0102" :
	            return "工商银行";
	        case "0103" :
	            return "农业银行";
	        case "0104" :
	            return "中国银行";
	        case "0105" :
	            return "建设银行";
	        case "0301" :
	            return "交通银行";
	        case "0302" :
	            return "中信银行";
	        case "0303" :
	            return "光大银行";
	        case "0304" :
	            return "华夏银行";
	        case "0305" :
	            return "民生银行";
	        case "0306" :
	            return "广发银行";
	        case "0307" :
	            return "平安银行";
	        case "0308" :
	            return "招商银行";
	        case "0309" :
	            return "兴业银行";
	        case "0310" :
	            return "浦发银行";
	        case "0316" :
	            return "浙商银行";
	        case "0401" :
	            return "上海银行";
	        default :
	        	return "";
		 }
	 }
	public static String changeIdCard(String idCard) {
		if (idCard != null && !"".equals(idCard)) {
			String ret = idCard.substring(0, 3);
			String hideCard = idCard.substring(3, idCard.length() - 4);
			for(int i=0;i<hideCard.length();i++){
				ret += "*";
			}
			ret = ret+idCard.substring(idCard.length() - 4,idCard.length());
			return ret;
		} else {
			return "";
		}
	}
	public static String convertProgress(String number,String floorNum) {
		String progress = String.format("%." + floorNum + "f", number);
		return progress;
	}
}
