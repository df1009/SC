package com.zhongchou.common.login.controller.personalCenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.DealRecordDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IPCMessageService;
import com.zhongchou.common.util.JsonUtil;

@Controller
public class PCMessageController extends BaseController {

	Logger logger=Logger.getLogger(PCMessageController.class);

	@Autowired
	private IPCMessageService messageService;

	/**
	 * 查询消息列表
	 * @return
	 */
	@RequestMapping("/SCLogin/PCselMessageList.do")
	public ModelAndView PCselMessageList(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("PCMessageController.PCselMessageList  start");
		Map out = new HashMap();
		UserDto userDto = getUser();
		String msgType = request.getParameter("msgType");//消息类型 1系统消息  2项目消息  空所有消息
	    String page = request.getParameter("curPage");
		int curPage = 0;
		if(StringUtils.isEmpty(page)){
			curPage = 1;
		}else{
			try {
				curPage = Integer.valueOf(page);
			} catch (Exception e) {
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "系统异常");
				JsonUtil.writeJson(response,out);
				return null;
			}
		}
		
	    int pageSize =10;//显示条数
	    int PageCount =(curPage-1)*pageSize;//起始位置
	    Map reqMap = new HashMap();
	    reqMap.put("oidUserId",userDto.getOidUserId());
	    reqMap.put("msgType",msgType);
	    reqMap.put("curPage",PageCount);
	    reqMap.put("pageSize",pageSize);
	    int MsgCnt = messageService.getMessageCnt(reqMap);
	    List messageList = messageService.getMessageList(reqMap);
		out.put("curPage", curPage);
		if(MsgCnt == 0){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "暂无记录");
			JsonUtil.writeJson(response,out);
			return null;
		}
		int totalPage = (MsgCnt-1)/pageSize +1;//总页数
		out.put("totalPage", totalPage);
		out.put("MsgCnt", MsgCnt);
		out.put("messageList", messageList);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
        return null;
	}

}
