package com.sync.service;

import com.base.service.BaseService;
import com.base.utils.ParaMap;
import com.sync.internal.MakeDataInternal;

public class SyncService extends BaseService{
	
	private MakeDataInternal mdInternal = new MakeDataInternal();
	/**
	 * @param inMap
	 * @return
	 * @throws Exception
	 * @author yxd
	 */
	public ParaMap addData(ParaMap inMap)throws Exception{
		ParaMap outMap = new ParaMap();
		outMap = mdInternal.addData(inMap);
		return outMap;
	}
}
