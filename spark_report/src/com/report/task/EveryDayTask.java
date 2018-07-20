package com.report.task;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.base.task.MyTimerTask;
import com.base.utils.StrUtils;
import com.base.web.AppConfig;
import com.report.internal.ReportInternal;
import com.report.threadpool.ReportSubmitTask;
import com.report.threadpool.ThreadPoolInstance;
import com.report.util.CellUtil;
import com.report.util.DateUtils;
import com.report.util.TaskUtil;
import com.stif.common.util.StringUtils;
import com.util.excel.ExcelUtil;



public class EveryDayTask extends MyTimerTask {
	
	private static final String SQL_SIGN = "SQL";
	
	private ReportInternal rInternal = new ReportInternal();
	
	public EveryDayTask(String id) {
		super(id);
	}

	public void executeMysql() throws Exception {
		//String filePath = "E:\\yxdCode\\stif_report\\template\\report1.xlsx";
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
		DataFormatter formatter = new DataFormatter();
		//得到sql行
		//寻找最近空白一列 跳出循环
		Row row = null;
		Row sqlRow = null;
		int rowIndex = 0;//行号
		Iterator<Row> rowIt = sheet.rowIterator();
		while (rowIt.hasNext()) {
			row = rowIt.next();
			String firstCellValue = formatter.formatCellValue(row.getCell(0));
			if (SQL_SIGN.equals(firstCellValue.trim())) {
				sqlRow = row;
			}else if (StrUtils.isNull(firstCellValue)
					&& null != sqlRow){
				++rowIndex;
				break;
			}
			++rowIndex;
		}
		//创建一行 同时增加第一列的时间格式
		Row newRow = sheet.createRow(rowIndex);
		String title = DateUtils.format(DateUtils.prevDay(1), DateUtils.TO_DAY_LINE);
		newRow.createCell(0).setCellValue(title.replace("-", "/"));
		//循环sql列
		TaskUtil.fillRow(sqlRow, newRow, rInternal, title);
		//数据写入文件
		if(StrUtils.isNotNull(encryptOr)
				&& StrUtils.isNotNull(password)
				&& StrUtils.equals(encryptOr, TaskUtil.EXECL_ENCRYPT)){
			ExcelUtil.encryptStreamAndSave(workbook, filePath, password);
		}else{
			ExcelUtil.fileSaveToLocal(workbook, filePath);
		}
	}

	
	@Override
	public void execute() throws Exception {
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
		DataFormatter formatter = new DataFormatter();
		//得到sql行
		//寻找最近空白一列 跳出循环
		Row row = null;
		Row sqlRow = null;
		int rowIndex = 0;//行号
		Iterator<Row> rowIt = sheet.rowIterator();
		while (rowIt.hasNext()) {
			row = rowIt.next();
			String firstCellValue = formatter.formatCellValue(row.getCell(0));
			if (SQL_SIGN.equals(firstCellValue.trim())) {
				sqlRow = row;
			}else if (StrUtils.isNull(firstCellValue)
					&& null != sqlRow){
				++rowIndex;
				break;
			}
			++rowIndex;
		}
		String title = DateUtils.format(DateUtils.prevDay(1), DateUtils.TO_DAY_LINE);
		//String title = "2018-06-03";
		//循环sql行的所有列
		Iterator<Cell> cellIt = sqlRow.cellIterator();
		while (cellIt.hasNext()) {
			Cell cell = cellIt.next();
			String sql = CellUtil.formatCellValue(cell, null);
			if (SQL_SIGN.equals(sql.trim())) {
				continue;
			}
			if(StringUtils.isNotNull(sql)){
				//这里替换特殊符号
				sql = TaskUtil.replaceSign(sql, title);
				//列号
				int cellIndex = cell.getColumnIndex();
				sql = TaskUtil.QUOTATION_MARK_SIGN+TaskUtil.SPACE_MARK_SIGN+rowIndex+TaskUtil.ROW_COL_DELIMITER_SIGN+cellIndex+TaskUtil.SQL_DELIMITER_SIGN+sql+TaskUtil.SPACE_MARK_SIGN+TaskUtil.QUOTATION_MARK_SIGN;
				//提交sql任务
				ReportSubmitTask rsTask = new ReportSubmitTask(sql);
				//ThreadPoolInstance.getThreadPoolInstancle().getThreadPoolExecutor().execute(rsTask);
				TaskUtil.log.info("提交的sql："+sql);
				rsTask.executeShell();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		EveryDayTask task = new EveryDayTask("EveryDayTask");
		task.execute();
	}
}
