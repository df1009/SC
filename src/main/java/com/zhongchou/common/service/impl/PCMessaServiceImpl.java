package com.zhongchou.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanshang.util.Row;
import com.zhongchou.common.dao.IPCMessageDao;
import com.zhongchou.common.loginOut.controller.login.LoginController;
import com.zhongchou.common.service.IPCMessageService;



/**
 * 个人中心逻辑的实现类。
 */
@Service
public class PCMessaServiceImpl implements IPCMessageService {
	Logger logger=Logger.getLogger(LoginController.class);
	@Autowired
	private IPCMessageDao messageDao;

	@Override
	public int getMessageCnt(Map reqMap) {
		return messageDao.getMessageCnt(reqMap);
	}

	@Override
	public List getMessageList(Map reqMap) {
		List<Row> msgList = messageDao.getMessageList(reqMap);
		List retList = new ArrayList<>();
		for (Row row : msgList) {
			if(!row.isEmpty()){
				Map retMap = new HashMap();
				retMap.put("title", row.get("MSG_TITLE"));//标题
				retMap.put("styleTitle", row.get("MSG_STYLE_TITLE"));//样式标题
				retMap.put("content", row.get("MSG_CONTENT"));//内容
				retMap.put("type", row.get("MSG_TYPE"));//消息类型 1系统消息  2项目消息
				retMap.put("stuType", row.get("MSG_STU_TP"));//消息状态类型 1:项目状态消息 2:风险测评消息 3:绑卡消息 4:认证合格投资人消息
				retMap.put("creatDate", row.get("INS_DATE"));//时间
				retMap.put("creatDate1", row.get("INS_DATE1"));//时间
				retList.add(retMap);
			}
		}
		return retList;
	}

	//添加消息
	@Override
	public boolean insMessageList(Map reqMap) {
		return messageDao.insMessage(reqMap)>0;
	}

}
