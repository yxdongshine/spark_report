package com.util.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.util.excel.decryptor.WorksheetDecryptor;
import com.util.excel.encryptor.WorksheetEncryptor;

public class ExcelUtil {

	/**
	 * 对Excel进行加密并替换原文件
	 * 
	 * @author OL
	 */
	public static void encryptExcel(String filePath, String password) {
		try {
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(new File(filePath));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			bos.reset();
			ExcelType type = ExcelType.forFileName(filePath);
			WorksheetEncryptor.encrypt(bis, bos, type, password);
			bis.close();
			bos.flush();
			bos.close();
			saveToLocal(filePath, bos.toByteArray());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static synchronized Workbook readExcel(String filePath) {
		try {
			FileInputStream is = new FileInputStream(new File(filePath));
			ExcelType type = ExcelType.forFileName(filePath);
			if (ExcelType.LEGACY.equals(type)) {
				return new HSSFWorkbook(is);
			}
			return new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 读取加密的Excel文件
	 * 
	 * @author OL
	 */
	public static synchronized Workbook readExcel(String filePath, String password) {
		try {
			ExcelType type = ExcelType.forFileName(filePath);
			FileInputStream is = new FileInputStream(new File(filePath));
			Workbook workbook = (XSSFWorkbook) WorksheetDecryptor.decrypt(is, type, password);
			return workbook;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存到磁盘
	 * @param path
	 * @param bytes
	 * @author OL
	 */
	public static synchronized void saveToLocal(String path, byte[] bytes) {
		File file = new File(path);
		FileOutputStream out = null;
		ByteArrayInputStream bIn = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			out = new FileOutputStream(file);
			bIn = new ByteArrayInputStream(bytes);
			int i = 0;
			while ((i = bIn.read()) != -1) {
				out.write(i);
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (bIn != null) {
				try {
					bIn.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.flush();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				try {
					out.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 将文件保存到本地
	 * @param workbook
	 * @param filePath
	 * @author YXD
	 * @throws Exception 
	 */
	public static synchronized void fileSaveToLocal(Workbook workbook,String filePath) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		bos.flush();
		ExcelUtil.saveToLocal(filePath, bos.toByteArray());
		bos.close();
	}

	
	/**
	 * 将流加密并保存到指定位置
	 * @param workbook
	 * @param filePath
	 * @param password
	 * @throws Exception
	 * @author YXD
	 */
	public static synchronized void encryptStreamAndSave(Workbook workbook,String filePath, String password)throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		bos.reset();
		ExcelType type = ExcelType.forFileName(filePath);
		WorksheetEncryptor.encrypt(bis, bos, type, password);
		bis.close();
		bos.flush();
		bos.close();
		ExcelUtil.saveToLocal(filePath, bos.toByteArray());
	}

	/**
	 * 解密文件并且保存到指定位置
	 * @param filePath
	 * @param password
	 * @author YXD
	 */
	public static synchronized void dencryptExcel(String filePath, String password) {
		try {
			Workbook workbook = readExcel(filePath, password);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			saveToLocal(filePath, bos.toByteArray());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		String path = "E:\\yxdCode\\stif_report\\template\\report1.xlsx";
		String pass = "12345678";
		dencryptExcel(path, pass);
		//encryptExcel(path, pass);
	}
}
