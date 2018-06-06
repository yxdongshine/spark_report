package com.report.internal;

import com.report.dao.TemplateDao;


public class TemplateInternal {

	private TemplateDao templateDao = new TemplateDao();
	
	public String queryData(String sql) throws Exception{
		String value = templateDao.executeSql(sql);
		return value;
	}
	
	
	
	
	
	
	public static void main(String[] args) throws Exception {
		TemplateInternal templateInternal = new TemplateInternal();
		
		String sql = "SELECT * FROM invoice";
		String value = templateInternal.queryData(sql);
		System.out.println(value);
	}
}
