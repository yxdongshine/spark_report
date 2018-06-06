package com.util.excel;

import java.io.File;

public enum ExcelType {
	XML, LEGACY;

	public static ExcelType forFile(File file) {
		return forFileName(file.getName());
	}

	public static ExcelType forFileName(String fileName) {
		if (fileName.toLowerCase().endsWith(".xlsx")) {
			return XML;
		} else if(fileName.toLowerCase().endsWith(".xls")){
			return LEGACY;
		}
		throw new RuntimeException("the document type was illegal");
	}
}
