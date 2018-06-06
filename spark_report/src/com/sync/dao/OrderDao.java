package com.sync.dao;

import com.base.utils.ParaMap;
import com.stif.common.dao.STIFDataSetDao;


public class OrderDao extends STIFDataSetDao{
	private static final String ORDER_INFO = "tra_order";
	private static final String ORDER_INFO_SQL = "sql/tra_order";
	
	/**
	 * 新增订单
	 * @param inMap
	 * @return
	 * @throws Exception
	 * @author yxd
	 */
	public ParaMap addOrder(ParaMap data)throws Exception{
		return insert(ORDER_INFO, data);
	}
	
	/**
	 * 修改订单
	 * @param inMap
	 * @return
	 * @throws Exception
	 * @author yxd
	 */
	public ParaMap updateOrder(ParaMap updateData, ParaMap conditions)throws Exception{
		return update(ORDER_INFO, conditions, updateData);
	}
	
	/**
	 * 查询订单指定的列
	 * @param conditions
	 * @return
	 * @throws Exception
	 * @author yxd
	 */
	public ParaMap getPartnerColunm(ParaMap conditions, String colunms)throws Exception{
		return querySimpleFormat(ORDER_INFO, conditions, null, colunms);
	}
	
	
	
}
