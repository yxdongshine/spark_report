package com.report.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class CellUtil {
	
	public static String formatCellValue(Cell cell, FormulaEvaluator evaluator) {
		if (cell == null) {
			return "";
		}

		int cellType = cell.getCellType();
		if (cellType == 2) {
			if (evaluator == null) {
				return cell.getCellFormula();
			}
			cellType = evaluator.evaluateFormulaCell(cell);
		}
		switch (cellType) {
		case 1:
			return cell.getStringCellValue().toString();
		case 4:
			return String.valueOf(cell.getBooleanCellValue());
		case 3:
			return "";
		case 5:
			return FormulaError.forInt(cell.getErrorCellValue()).getString();
		case 2:
		}

		throw new RuntimeException(new StringBuilder()
				.append("Unexpected celltype (").append(cellType).append(")")
				.toString());
	}
}
	
