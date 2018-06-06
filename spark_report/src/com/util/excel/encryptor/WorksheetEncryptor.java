package com.util.excel.encryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.util.excel.ExcelType;

public abstract class WorksheetEncryptor {
	public static void encrypt(InputStream input, OutputStream output, ExcelType worksheetType, String password) throws IOException {
		if (ExcelType.LEGACY.equals(worksheetType))
			XlsEncryptor.encrypt(input, output, password);
		else
			XlsxEncryptor.encrypt(input, output, password);
	}

	public static void encrypt(String fileName, OutputStream output, String password) throws IOException {
		encrypt(new FileInputStream(fileName), output, ExcelType.forFileName(fileName), password);
	}

	public static void encrypt(File file, OutputStream output, String password) throws IOException {
		encrypt(new FileInputStream(file), output, ExcelType.forFile(file), password);
	}
}