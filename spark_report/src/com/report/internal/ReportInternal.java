package com.report.internal;

import com.report.dao.ReportDao;
import com.report.dao.TemplateDao;


public class ReportInternal {

	private ReportDao rDao = new ReportDao();
	
	public String queryData(String sql) throws Exception{
		String value = rDao.executeSql(sql);
		return value;
	}
	
	
	
	
	
	
	public static void main(String[] args) throws Exception {
		ReportInternal templateInternal = new ReportInternal();
		
		String sql = "SELECT * FROM invoice";
		String value = templateInternal.queryData(sql);
		System.out.println(value);
	}
}
