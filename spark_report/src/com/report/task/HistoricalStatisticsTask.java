package com.report.task;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.base.task.MyTimerTask;
import com.base.utils.StrUtils;
import com.base.web.AppConfig;
import com.report.internal.ReportInternal;
import com.report.util.TaskUtil;
import com.util.excel.ExcelUtil;



public class HistoricalStatisticsTask extends MyTimerTask {
	
	private static final String SQL_SIGN = "SQL";
	private static final String TIME_TITLE_SIGN = "时间";

	private ReportInternal rInternal = new ReportInternal();
	
	public HistoricalStatisticsTask(String id) {
		super(id);
	}

	@Override
	public void execute() throws Exception {
		//String filePath = "E:\\yxdCode\\stif_report\\template\\report.xlsx";
		String filePath = this.getClass().getResource("/").getPath()+"report.xlsx";
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
			}else if (StrUtils.isNotNull(firstCellValue)
					&&StrUtils.isNull(formatter.formatCellValue(row.getCell(1)))
					&& !TIME_TITLE_SIGN.equals(firstCellValue)
					&& null != sqlRow){
				//这里开始处理每一行数据
				Row newRow = sheet.getRow(rowIndex);
				//循环sql列
				TaskUtil.fillRow(sqlRow, newRow, rInternal, firstCellValue.replace("/", "-"));
			}
			++rowIndex;
		}
		//数据写入文件
		if(StrUtils.isNotNull(encryptOr)
				&& StrUtils.isNotNull(password)
				&& StrUtils.equals(encryptOr, TaskUtil.EXECL_ENCRYPT)){
			ExcelUtil.encryptStreamAndSave(workbook, filePath, password);
		}else{
			ExcelUtil.fileSaveToLocal(workbook, filePath);
		}
	}

	public static void main(String[] args) throws Exception {
		HistoricalStatisticsTask task = new HistoricalStatisticsTask("ReportTask");
		task.execute();
	}
}
