package com.test;

import junit.framework.TestCase;

import com.base.ds.DataSourceManager;
import com.base.utils.IDGenerator;
import com.base.utils.ParaMap;
import com.sync.service.SyncService;

public class SyncServiceTest extends TestCase{
	
	private SyncService syncService = new SyncService();
	
	public void testAddPartnerInfo()throws Exception{
		for (int i = 0; i < 10; i++) {
			ParaMap inMap = new ParaMap();
			inMap.put("number", 10000);
			System.out.println(syncService.addData(inMap));
			DataSourceManager.commit(); 
		}
	}
	
	public static void main(String[] args) {
		String id = IDGenerator.newGUID();
		String orderId = id.substring(id.length()-20, id.length());
		System.out.println("9223372036854775807".length());
	}
}
