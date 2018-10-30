package com.zhongchou.common.dao.impl;

import com.yanshang.dao.BaseDao;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.PcProductDao;

import java.util.List;

/**
 * @author gaoming
 * @create 2017-09-05-17:43
 **/
public class PcProductDaoImpl extends BaseDao implements PcProductDao {

    /**
     * 展示其它项目信息列表
     * @param productId
     * @return
     */
    @Override
    public List<Row> getOtherProjectList(String productId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pd.PROJECT_MAIN_TITLE projectMainTitle,");
        sql.append("IFNULL(pf.SALES_QUOTA,0) salesQuota,");
        sql.append("IFNULL(pd.MIN_SUBS_AMT_DES,0) firstMinBuy,");
        sql.append("IFNULL(pf.SURPLUS_SALES_QUOTA,0) surplusQuota,");
        sql.append("pd.ISS_BGN_DT_DES issBgnDt,");
        sql.append("pd.ISS_END_DT_DES issEndDt,");
        sql.append("pf.OID_PLATFORM_PROJECTS_ID projectId,");
        sql.append("pf.PLATFORM_PROJECTS_ST projectState,");
        sql.append("pd.PROJECT_COMPANY_NAME projectcompanyName,");//项目方名字
        sql.append("pd.PROJECT_ADDS projectAdds,");//项目方地址
        sql.append("pd.PROJECT_CHARACTER projectCharacter,");//项目特点(比如:环保;健康;盈利情况等;逗号分隔)
        sql.append("pd.APP_LIST_IMG smallImgPath,");//图片
        sql.append("(SELECT count(1) FROM user_tender u where u.OID_PLATFORM_PROJECTS_ID=pf.OID_PLATFORM_PROJECTS_ID and u.TENDER_STATUS='3') payPersonNum ");

        sql.append(" FROM projects_info pf , projects_info_des_online pd ");

        sql.append(" WHERE pf.OID_PLATFORM_PROJECTS_ID = pd.OID_PLATFORM_PROJECTS_ID ");
        sql.append(" and pd.OID_PLATFORM_PROJECTS_ID != ?");
        sql.append(" and pd.SHOW_PROJECT_FLAG = (select i.SHOW_PROJECT_FLAG from projects_info i where i.OID_PLATFORM_PROJECTS_ID=?)");
        return queryForList(sql.toString(),new Object[]{ productId,productId});
    }

    //查询单个项目详细信息
    @Override
    public Row selProductDetaile(String productId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i.OID_PLATFORM_PROJECTS_ID,");
        sql.append(" i.PLATFORM_PROJECTS_ST,");
        sql.append(" i.PLATFORM_PROJECTS_FULL_NM,");
        sql.append(" i.PLATFORM_PROJECTS_SHORT_NM,");
        sql.append(" i.SALES_QUOTA,");
        sql.append(" i.INVESTMENTCONFIRMATION,");
        sql.append(" i.SURPLUS_SALES_QUOTA,");
        sql.append(" i.FIRST_MIN_BUY,");
        sql.append(" d.*,ud.USER_NAME,ud.USER_ICON_FILE_ID from projects_info_des_online d ");
        sql.append(" INNER JOIN PROJECTS_INFO i");
        sql.append(" ON d.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID");
        sql.append(" INNER JOIN USER_DETAIL ud");
        sql.append(" ON d.OID_USER_ID=ud.OID_USER_ID");
        sql.append(" WHERE d.OID_PLATFORM_PROJECTS_ID = ?");

        return singleQuery(sql.toString(),new Object[]{ productId});
    }

    //查询单个项目的项目介绍信息
    @Override
    public List<Row> selProductIntroduce(String productId,String type) {
        StringBuilder sql = new StringBuilder();
        if("1".equals(type)){//项目信息
            sql.append("SELECT TITLE,SUMMARYTEXT,INS_DATE,IMG_PATH FROM projects_info_detail_online");
            sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ?");
            sql.append(" AND STATUS = 3");
            sql.append(" AND TYPE = ? ");
            sql.append(" ORDER BY CAST(PARTY_NUM AS SIGNED),INS_DATE");
        }else if("2".equals(type)){//项目进展
            sql.append("SELECT TITLE,SUMMARYTEXT,INS_DATE,IMG_PATH,PROGRESS_DATE FROM projects_info_detail_online");
            sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ?");
            sql.append(" AND STATUS = 3");
            sql.append(" AND TYPE = ? ");
            sql.append(" ORDER BY PROGRESS_DATE DESC,INS_DATE DESC");
        }

        return queryForList(sql.toString(),  new Object[]{ productId,type});
    }

    //查询单个项目投资数量
    @Override
    public int countProductInvestor(String productId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(1) from USER_TENDER ut");
        sql.append(" INNER JOIN USER u");
        sql.append(" on ut.OID_USER_ID = u.OID_USER_ID");
        sql.append(" INNER JOIN PROJECTS_INFO p");
        sql.append(" on p.OID_PLATFORM_PROJECTS_ID=ut.OID_PLATFORM_PROJECTS_ID");
        sql.append(" INNER JOIN USER_DETAIL ud");
        sql.append(" on ud.OID_USER_ID = u.OID_USER_ID");
        sql.append(" WHERE p.OID_PLATFORM_PROJECTS_ID=?");
        sql.append(" AND ut.TENDER_STATUS =3");
        sql.append(" AND not exists(select 1 from ORDER_CANCELLATION_RECORD oc where ut.TENDER_SSN = oc.REQ_SSN)");
        sql.append(" ORDER BY ut.INS_DATE DESC");
        return getCount(sql.toString(),new Object[]{ productId});
    }

    //查询单个项目投资人
    @Override
    public List<Row> selProductInvestor(String productId,int sumNum,int pageSize) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.OID_USER_ID oid,UT.TENDER_AMOUNT amount,UT.INS_DATE insDate,ud.NICKNAME nickName,ud.USER_ICON_FILE_ID iconfile,ud.USER_NAME userName from USER_TENDER ut");
        sql.append(" INNER JOIN USER u");
        sql.append(" on ut.OID_USER_ID = u.OID_USER_ID");
        sql.append(" INNER JOIN PROJECTS_INFO p");
        sql.append(" on p.OID_PLATFORM_PROJECTS_ID=ut.OID_PLATFORM_PROJECTS_ID");
        sql.append(" INNER JOIN USER_DETAIL ud");
        sql.append(" on ud.OID_USER_ID = u.OID_USER_ID");
        sql.append(" WHERE p.OID_PLATFORM_PROJECTS_ID=?");
        sql.append(" AND ut.TENDER_STATUS =3");
        sql.append(" AND not exists(select 1 from ORDER_CANCELLATION_RECORD oc where ut.TENDER_SSN = oc.REQ_SSN)");
        sql.append(" ORDER BY ut.INS_DATE DESC");
        return queryForList(sql.toString(),sumNum,pageSize,new Object[]{ productId});
    }

    @Override
    public int selUserTenderPro(String productId, String loginId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT count(1) from user_tender");
        sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ?");
        sql.append(" AND OID_USER_ID = ?");
        sql.append(" AND TENDER_STATUS IN (1,3)");
        return getCount(sql.toString(), new Object[]{ productId,loginId});
    }

    //查询产品购买状态
    @Override
    public Row selProjectToBuy(String proId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT d.RISK_LVL_DES,i.FIRST_MIN_BUY,d.MIN_SUBS_AMT_DES,i.MIN_BIDS_AMT,i.PLATFORM_PROJECTS_ST,d.MAX_SUBS_AMT_DES,i.SURPLUS_SALES_QUOTA,d.SUBS_ADD_AMT_DES,i.BIDS_ADD_AMT,d.PROJECT_MAIN_TITLE,d.SUMMARYTEXT,d.APP_LIST_IMG from projects_info_des_online d ");
        sql.append(" INNER JOIN PROJECTS_INFO i ");
        sql.append(" ON d.OID_PLATFORM_PROJECTS_ID = i.OID_PLATFORM_PROJECTS_ID ");
        sql.append(" AND d.LAST_STATE = 1 ");
        sql.append(" AND d.STATUS <> 4 ");
        sql.append(" AND i.OID_PLATFORM_PROJECTS_ID = ? ");
        return singleQuery(sql.toString(),new Object[]{proId});
    }
    //根据项目code查询项目名
    @Override
    public Row selProductName(String productId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" PLATFORM_PROJECTS_SHORT_NM ");
        sql.append(" PLATFORM_PROJECTS_FULL_NM ");
        sql.append(" from PROJECTS_INFO");
        sql.append(" WHERE OID_PLATFORM_PROJECTS_ID = ?");

        return singleQuery(sql.toString(),new Object[]{ productId});
    }

}

