package com.util.excel.decryptor;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class XlsDecryptor {
	public static HSSFWorkbook decrypt(InputStream input, String password) throws IOException {
		try {
			Biff8EncryptionKey.setCurrentUserPassword(password);

			return new HSSFWorkbook(input);
		} catch (Exception ex) {
			throw new RuntimeException("Unable to process encrypted document", ex);
		} finally {
			Biff8EncryptionKey.setCurrentUserPassword(null);
		}
	}
}