package com.zhongchou.common.dto;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;
import com.zhongchou.common.model.SearchConditionBase;

/**
 * iLoan系统：管理员账户的数据传输类。
 *
 */
public class AdminUserDto extends SearchConditionBase {

	private static final long serialVersionUID = 1L;

	/** 管理员UUID */
	private String oidAdminUserId;

	/** 管理员登录账户  */
	private String adminUserId;

	/** 管理员名称  */
	private String adminUserName;

	/** 管理员密码  */
	private String adminUserPwd;

	/** 管理标示  */
	private String sysFlg;

	/** 组UUID */
	private String oidGroupId;

	/** 登录错误次数  */
	private String loginErrorTimes;

	/** 组名称  */
	private String groupName;

	/** 角色  */
	private String[] roleId;

	/** 删除标识  */
	private String delFlg;

	/** 拥有访问权限的URL */
	private Map<String, String> url;

	/** 菜单导航 */
	private Map<String, String[]> menuNav;

	/** 访问权限  */
	private List<Row> acl;

	/** 拥有访问权限的菜单  */
	private List<Row> menu;

	/** 错误代码  */
	private int errorCode;

	/**
	 * 获取管理员UUID的方法。
	 *
	 * @return 管理员UUID
	 */
	public String getOidAdminUserId() {
		return oidAdminUserId;
	}

	/**
	 * 设置管理员UUID的方法。
	 *
	 * @param oidAdminUserId 管理员UUID
	 */
	public void setOidAdminUserId(String oidAdminUserId) {
		this.oidAdminUserId = oidAdminUserId;
	}

	/**
	 * 获取管理员登录账户的方法。
	 *
	 * @return 管理员登录账户
	 */
	public String getAdminUserId() {
		return adminUserId;
	}

	/**
	 * 设置管理员登录账户的方法。
	 *
	 * @param adminUserId 管理员登录账户
	 */
	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	/**
	 * 获取管理员名称的方法。
	 *
	 * @return 管理员名称
	 */
	public String getAdminUserName() {
		return adminUserName;
	}

	/**
	 * 设置管理员名称的方法。
	 *
	 * @param adminUserName 管理员名称
	 */
	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}

	/**
	 * 获取管理员密码的方法。
	 *
	 * @return 管理员密码
	 */
	public String getAdminUserPwd() {
		return adminUserPwd;
	}

	/**
	 * 设置管理员密码的方法。
	 *
	 * @param adminUserPwd 管理员密码
	 */
	public void setAdminUserPwd(String adminUserPwd) {
		this.adminUserPwd = adminUserPwd;
	}

	/**
	 * 获取管理标示的方法。
	 *
	 * @return 管理标示
	 */
	public String getSysFlg() {
		return sysFlg;
	}

	/**
	 * 设置管理标示的方法。
	 *
	 * @param sysFlg 管理标示
	 */
	public void setSysFlg(String sysFlg) {
		this.sysFlg = sysFlg;
	}

	/**
	 * 获取组UUID的方法。
	 *
	 * @return 组UUID
	 */
	public String getOidGroupId() {
		return oidGroupId;
	}

	/**
	 * 设置组UUID的方法。
	 *
	 * @param oidGroupId 组UUID
	 */
	public void setOidGroupId(String oidGroupId) {
		this.oidGroupId = oidGroupId;
	}

	/**
	 * 获取登录错误次数的方法。
	 *
	 * @return 登录错误次数
	 */
	public String getLoginErrorTimes() {
		return loginErrorTimes;
	}

	/**
	 * 设置登录错误次数的方法。
	 *
	 * @param loginErrorTimes 登录错误次数
	 */
	public void setLoginErrorTimes(String loginErrorTimes) {
		this.loginErrorTimes = loginErrorTimes;
	}

	/**
	 * 获取组名称的方法。
	 *
	 * @return 组名称
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * 设置组名称的方法。
	 *
	 * @param groupName 组名称
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * 获取角色的方法。
	 *
	 * @return 角色
	 */
	public String[] getRoleId() {
		return roleId;
	}

	/**
	 * 设置角色的方法。
	 *
	 * @param roleId 角色
	 */
	public void setRoleId(String[] roleId) {
		this.roleId = roleId;
	}

	/**
	 * 获取删除标识的方法。
	 *
	 * @return 删除标识
	 */
	public String getDelFlg() {
		return delFlg;
	}

	/**
	 * 设置删除标识的方法。
	 *
	 * @param delFlg 删除标识
	 */
	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	/**
	 * 获取拥有访问权限的URL的方法。
	 *
	 * @return 拥有访问权限的URL
	 */
	public Map<String, String> getUrl() {
		return url;
	}

	/**
	 * 设置拥有访问权限的URL的方法。
	 *
	 * @param url 拥有访问权限的URL
	 */
	public void setUrl(Map<String, String> url) {
		this.url = url;
	}

	/**
	 * 获取访问权限的方法。
	 *
	 * @return 访问权限
	 */
	public List<Row> getAcl() {
		return acl;
	}

	/**
	 * 设置访问权限的方法。
	 *
	 * @param acl 访问权限
	 */
	public void setAcl(List<Row> acl) {
		this.acl = acl;
	}

	/**
	 * 获取拥有访问权限的菜单的方法。
	 *
	 * @return 拥有访问权限的菜单
	 */
	public List<Row> getMenu() {
		return menu;
	}

	/**
	 * 设置拥有访问权限的菜单的方法。
	 *
	 * @param menu 拥有访问权限的菜单
	 */
	public void setMenu(List<Row> menu) {
		this.menu = menu;
	}

	/**
	 * 获取错误代码的方法。
	 *
	 * @return 错误代码
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * 设置错误代码的方法。
	 *
	 * @param errorCode 错误代码
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * 取得菜单导航的方法。
	 *
	 * @return 菜单导航
	 */
	public Map<String, String[]> getMenuNav() {
		return menuNav;
	}

	/**
	 * 设置菜单导航的方法。
	 *
	 * @param menuNav 菜单导航
	 */
	public void setMenuNav(Map<String, String[]> menuNav) {
		this.menuNav = menuNav;
	}
}
