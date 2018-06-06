package com.report.threadpool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class WirteLogTask implements Runnable {
	
	private Process process;
	
	public WirteLogTask(Process process) {
		this.process = process;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String s = null;
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        try {
			while ((s = stdInput.readLine()) != null) {
			  ReportSubmitTask.log.info(s);
			}
	        while ((s = stdError.readLine()) != null) {
	      	  ReportSubmitTask.log.info(s);
	        }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
