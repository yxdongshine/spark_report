package com.report.util;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import com.base.log.Logging;
import com.report.internal.ReportInternal;
import com.stif.common.util.StringUtils;

public class TaskUtil {

	public  static Logging log = Logging.getLogging(TaskUtil.class.getName());
	private static final String DATE_START_SIGN = "$date_start";
	private static final String DATE_END_SIGN = "$date_end";
	public static final String SPACK = " ";
	private static final String DAY_START = " 00:00:00";
	private static final String DAY_END = " 23:59:59";
	private static final String TIME_START_SIGN = "$time_start";
	private static final String TIME_END_SIGN = "$time_end";
	private static final String DATE_SIGN = "$date";
	private static final String SQL_SIGN = "SQL";
	public static final String EXECL_ENCRYPT = "1";//加密
	public static final String EXECL_ENCRYPT_NO = "0";//默认不加密
	public static final String SQL_DELIMITER_SIGN = "#";//sql字符分隔符号
	public static final String ROW_COL_DELIMITER_SIGN = "@";//行列字符分隔符号
	public static final String QUOTATION_MARK_SIGN = "\"";//行列字符分隔符号
	public static final String SINGLE_QUOTATION_MARK_SIGN = "'";//单引号
	public static final String SPACE_MARK_SIGN = " ";//空格


	/**
	 * 替换sql里面特殊标志
	 * @param sql
	 * @param dateTitle
	 * @return
	 * @author YXD
	 */
	public static String replaceSign(String sql,String dateTitle){
		if(null == sql) return sql;
		if (sql.contains(DATE_START_SIGN)) {//如果包含日期开始符号
			sql = sql.replace(DATE_START_SIGN, dateTitle + DAY_START);
		}
		if(sql.contains(DATE_END_SIGN)) {//如果包含日期结束符号
			sql = sql.replace(DATE_END_SIGN, dateTitle + DAY_END);
		} 
		if(sql.contains(TIME_START_SIGN)){//如果包含时间搓开始符号
			Long timeStart = DateUtils.getTime(dateTitle + DAY_START, DateUtils.TO_SECOND_LINE);
			sql = sql.replace(TIME_START_SIGN, timeStart+"");
		} 
		if(sql.contains(TIME_END_SIGN)){//如果包含时间搓结束符号
			Long timeEnd = DateUtils.getTime(dateTitle + DAY_END, DateUtils.TO_SECOND_LINE);
			sql = sql.replace(TIME_END_SIGN, timeEnd+"");
		}
		if(sql.contains(DATE_SIGN)){//如果包含日期符号
			sql = sql.replace(DATE_SIGN,SINGLE_QUOTATION_MARK_SIGN+dateTitle+SINGLE_QUOTATION_MARK_SIGN);
		}
		return sql.trim();
	}
	
	/**
	 * 根据sql列查询结果填充指定列数据
	 * @param sqlRow
	 * @param fillRow
	 * @author YXD
	 * @throws Exception 
	 */
	public static void fillRow(Row sqlRow,Row fillRow,ReportInternal rInternal,String title) throws Exception{
		DataFormatter formatter = new DataFormatter();
		Iterator<Cell> cellIt = sqlRow.cellIterator();
		while (cellIt.hasNext()) {
			Cell cell = cellIt.next();
			String sql = formatter.formatCellValue(cell);
			if (SQL_SIGN.equals(sql.trim())) {
				continue;
			}
			if(StringUtils.isNotNull(sql)){
				//这里替换特殊符号
				sql = TaskUtil.replaceSign(sql, title);
				Long startTime = System.currentTimeMillis();
				String result = rInternal.queryData(sql);
				Long endTime = System.currentTimeMillis();
				//列号
				int cellIndex = cell.getColumnIndex();
				log.info("**第"+(fillRow.getRowNum()+1)+"行，第"+cellIndex+"列执行时间："+(endTime-startTime)+"毫秒,执行sql："+sql);
				//System.out.println("**第"+(fillRow.getRowNum()+1)+"行，第"+cellIndex+"列执行时间："+(endTime-startTime)+"毫秒,执行sql："+sql);
				//向指定行写入数据
				fillRow.createCell(cellIndex).setCellValue(result);
			}
		}
	}

}
