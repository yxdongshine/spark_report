package com.report.dao;

import com.base.dao.SQLMap;
import com.base.utils.ParaMap;
import com.stif.common.dao.STIFDataSetDao;


public class TemplateDao extends STIFDataSetDao {
	
	/**
	 * 执行SQL
	 * @param sql
	 * @return
	 * @throws Exception
	 * @author OL
	 */
	public String executeSql(String sql) throws Exception{
		SQLMap sqlMap = new SQLMap();
		sqlMap.setSQL(sql);
		
		ParaMap outMap = query(sqlMap);
		return outMap.getRecordString(0, 0);
	}
	
}
