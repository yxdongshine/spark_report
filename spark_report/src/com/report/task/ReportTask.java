package com.report.task;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.base.log.Logging;
import com.base.task.MyTimerTask;
import com.report.internal.TemplateInternal;
import com.stif.common.util.StringUtils;
import com.util.excel.ExcelUtil;

/**
 * 每天23点缓存第二天的排期数据<br>
 * 并把前一天的排期数据缓存删除
 * @author OL
 *
 */
public class ReportTask extends MyTimerTask {
	
	private Logging log = Logging.getLogging("log");
	
	private TemplateInternal templateInternal = new TemplateInternal();
	
	public ReportTask(String id) {
		super(id);
	}

	@Override
	public void execute() throws Exception {
		log.info("任务开始执行...");
		
		Workbook workbook = ExcelUtil.readExcel("E:\\yxdCode\\stif_report\\template","12345678");
		Sheet sheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		Row row = null;
		Iterator<Row> rowIt = sheet.rowIterator();
		while (rowIt.hasNext()) {
			row = rowIt.next();
			String firstCellValue = formatter.formatCellValue(row.getCell(0));
			if (firstCellValue.equals("SQL")) {
				break;
			}
		}
		
		Iterator<Cell> cellIt = row.cellIterator();
		while (cellIt.hasNext()) {
			Cell cell = cellIt.next();
			String sql = formatter.formatCellValue(cell);
			if (sql.equals("SQL")) {
				continue;
			}
			if(StringUtils.isNotNull(sql)){
				String result = templateInternal.queryData(sql);
				System.out.println(cell.getColumnIndex() + "：" + sql);
				System.out.println(cell.getColumnIndex() + "：" + result);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		ReportTask task = new ReportTask("ReportTask");
		task.execute();
	}
}
