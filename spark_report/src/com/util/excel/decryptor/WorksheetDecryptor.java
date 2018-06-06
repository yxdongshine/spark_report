package com.util.excel.decryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;

import com.util.excel.ExcelType;


public abstract class WorksheetDecryptor {
	
	public static Workbook decrypt(InputStream input, ExcelType worksheetType, String password) throws IOException {
		if (ExcelType.LEGACY.equals(worksheetType)) {
			return XlsDecryptor.decrypt(input, password);
		}
		return XlsxDecryptor.decrypt(input, password);
	}

	public static Workbook decrypt(String fileName, String password) throws IOException {
		return decrypt(new FileInputStream(fileName), ExcelType.forFileName(fileName), password);
	}

	public static Workbook decrypt(File file, String password) throws IOException {
		return decrypt(new FileInputStream(file), ExcelType.forFile(file), password);
	}
}
