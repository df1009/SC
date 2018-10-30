package com.zhongchou.common.zhongzhengSao;

import java.util.Date;
import java.util.Map;

import com.yanshang.config.Config;
import com.yanshang.util.DateUtils;
import com.zhongchou.common.dao.ICommonDao;
import com.zhongchou.common.dao.impl.CommonDaoImpl;


abstract class BaseSao {

	//用户没有上传头像时的默认头像
	String ZZinsCd = Config.getString("ZZinsCd");
	String ZZChannelNo = Config.getString("ZZChannelNo");
	String req_date = DateUtils.convertDate2String(new Date(),"yyyyMMdd");
	String req_ssn = "209912310000000001";
	String version ="1.0";
	String ins_cd = ZZinsCd;
	String channel_no = ZZChannelNo;

	abstract Map setEncryptData(Map setData);

	abstract Map receivEncryptData(String content);
}
