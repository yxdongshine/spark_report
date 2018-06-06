package com.report.dao;

import com.base.dao.SQLMap;
import com.base.utils.ParaMap;
import com.stif.common.dao.STIFDataSetDao;


public class ReportDao extends STIFDataSetDao {
	
	/**
	 * 执行查询语句
	 * @param sql
	 * @return
	 * @throws Exception
	 * @author YXD
	 */
	public String executeSql(String sql) throws Exception{
		SQLMap sqlMap = new SQLMap();
		sqlMap.setSQL(sql);
		ParaMap outMap = query(sqlMap);
		return outMap.getRecordString(0, 0);
	}
	
}
