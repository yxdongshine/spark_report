package com.report.threadpool;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.base.utils.StrUtils;
import com.base.web.AppConfig;
import com.report.util.DateUtils;
import com.report.util.TaskUtil;
import com.stif.common.util.IOUtils;
import com.util.excel.ExcelUtil;

import org.apache.poi.ss.usermodel.Row;

public class ReportResultTask implements Runnable {

	private String key;
	private String message;
	public ReportResultTask(String key,String message) {
		// TODO Auto-generated constructor stub
		this.key = key;
		this.message = message;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		handleResult();
	}

	public void handleResult(){
		//String filePath = "D:\\yxd\\tool\\sparkPro\\spark_report\\template\\report.xlsx";
		String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"report.xlsx";
		String password = AppConfig.getStringPro("excel_password");
		String encryptOr = AppConfig.getStringPro("excel_encrypt");
		Workbook workbook = null;
		if(StrUtils.isNotNull(encryptOr)
				&& StrUtils.isNotNull(password)
				&& StrUtils.equals(encryptOr, TaskUtil.EXECL_ENCRYPT)){
			 workbook = ExcelUtil.readExcel(filePath,password);
		}else{
			 workbook = ExcelUtil.readExcel(filePath);
		}
		Sheet sheet = workbook.getSheetAt(0);
		//获取消息
		if(StrUtils.isNotNull(message)){
			String[] strs = message.split(TaskUtil.SQL_DELIMITER_SIGN);
			if(2 == strs.length){
				String result = strs[1];
				String index = strs[0];
				String[] indexs = index.split(TaskUtil.ROW_COL_DELIMITER_SIGN);
				if(2 == indexs.length
						&& StrUtils.isNumber(indexs[0])
						&& StrUtils.isNumber(indexs[1])){
					int rowIndex = Integer.parseInt(indexs[0]);
					int colIndex = Integer.parseInt(indexs[1]);
					//获取指定的行
					Row row = sheet.getRow(rowIndex);
					if(null == row){//创建这行
						row = sheet.createRow(rowIndex);
						String title = DateUtils.format(DateUtils.prevDay(1), DateUtils.TO_DAY_LINE);
						row.createCell(0).setCellValue(title.replace("-", "/"));
					}
					//向指定列写
					row.createCell(colIndex).setCellValue(result);
					//保存到磁盘
					try {
						if(StrUtils.isNotNull(encryptOr)
								&& StrUtils.isNotNull(password)
								&& StrUtils.equals(encryptOr, TaskUtil.EXECL_ENCRYPT)){
								ExcelUtil.encryptStreamAndSave(workbook, filePath, password);
						}else{
							ExcelUtil.fileSaveToLocal(workbook, filePath);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
			        	TaskUtil.log.error("写入到磁盘异常："+IOUtils.getStackTrace(e));
					}
					TaskUtil.log.info("写入位置及结果："+message);
				}
			}
		}else{
			TaskUtil.log.info("获取消息为null");
		}
	}
	
	
}
