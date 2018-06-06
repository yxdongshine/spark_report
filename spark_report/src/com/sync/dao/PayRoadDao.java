package com.sync.dao;

import com.base.utils.ParaMap;
import com.stif.common.dao.STIFDataSetDao;

public class PayRoadDao extends STIFDataSetDao{
	private static final String PAY_ROAD = "tra_pay_road";
	private static final String PAY_ROAD_SQL = "sql/tra_pay_road";
	
	/**
	 * 新增支付订单
	 * @param inMap
	 * @return
	 * @throws Exception
	 * @author yxd
	 */
	public ParaMap addPayRoad(ParaMap data)throws Exception{
		return insert(PAY_ROAD, data);
	}
	
	/**
	 * 修改支付订单
	 * @param inMap
	 * @return
	 * @throws Exception
	 * @author yxd
	 */
	public ParaMap updatePayRoad(ParaMap updateData, ParaMap conditions)throws Exception{
		return update(PAY_ROAD, conditions, updateData);
	}
	
	/**
	 * 查询支付订单指定的列
	 * @param conditions
	 * @return
	 * @throws Exception
	 * @author yxd
	 */
	public ParaMap getPayRoadColunm(ParaMap conditions, String colunms)throws Exception{
		return querySimpleFormat(PAY_ROAD, conditions, null, colunms);
	}
	
	
	
}
