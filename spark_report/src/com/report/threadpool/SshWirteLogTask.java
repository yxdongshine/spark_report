package com.report.threadpool;

import java.io.IOException;
import java.io.InputStream;
import com.base.web.AppConfig;

public class SshWirteLogTask implements Runnable {
	
	private InputStream in;
	
	public SshWirteLogTask(InputStream in) {
		this.in = in;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
        byte[] buf = new byte[1024];  
        StringBuffer sb = new StringBuffer();  
        try {  
            while (in.read(buf) != -1) {  
                sb.append(new String(buf, AppConfig.getStringPro("shh_charset")));  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    	ReportSubmitTask.log.info(sb.toString());
	}
	
}
