package com.zhongchou.common.service;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;
import com.zhongchou.common.dto.InvestorDto;
import com.zhongchou.common.dto.OrderInfoDto;
import com.zhongchou.common.dto.ProgressDto;
import com.zhongchou.common.dto.ProjectInfoDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.dto.UserForum;
import com.zhongchou.common.dto.UserTenderDto;

/**
 * 用户中心-订单管理
 */
public interface IUserTenderService {

	/**
	 * 根据oid和用户的搜索内容获取已经投资项目的数量()
	 * 用户的IOD  oidUserId
	 * 产品的名称      projectName
	 * 认购时间         开始beginDate 结束 endDate
	 * 产品的状态      projectState
	 * @return   list的总数   listCnt
	 */
	public int getUserTenderCnt(Map tenderMap);

	/**
	 * 根据oid和用户的搜索内容获取已经投资项目的list数组数据
	 * 用户的IOD  oidUserId
	 * 产品的名称      projectName
	 * 认购时间         开始beginDate 结束 endDate
	 * 产品的状态      projectState
	 * 每页面的条数  pageSize
	 * 当前页数          curPage
	 * @return
	 */
	public List<UserTenderDto> getUserTenders(Map tenderMap);

	/**
	 * 我的中心    跟投人详情   数据获取
	 * @param projectsId
	 * @return
	 */
	public List<InvestorDto> getInvestorList(String projectsId);

	/**
	 * 我的中心   订单详情   数据获取
	 * @param orderNum
	 * @return
	 */


	public OrderInfoDto getOrderInfo(String orderNum);

	/**
	 * 撤单操作
	 * @param orderNum
	 * @return
	 * @throws Exception
	 */
	public Map returnOrder(Map reqMap) throws Exception;


	Map<String,Object> getProductinfo(String productId,String loginId);

	Map selProductDetailList(String productId,String type);

	/**
	 *查询单个项目投资人
	 */
	List<Row> selProductInvestor(String productId);

	/**
	 *查询app个人中心信息
	 */
	Map selPresonMsgToApp(String userId);
	//模糊查询用户投资信息
	List likeTender(String userId, String proName);
	//查询用户投资该产品的总额
	public int selTenderProAmount(String userId,String proName);
	/**
	 *查询用户银行卡剩余可用额度
	 */
	Map selRemainingQuota(String userId,String proId);
}
