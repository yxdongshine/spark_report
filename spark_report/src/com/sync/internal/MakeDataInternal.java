package com.sync.internal;

import java.util.Random;

import com.base.ds.DataSourceManager;
import com.base.log.Logging;
import com.base.utils.IDGenerator;
import com.base.utils.ParaMap;
import com.stif.common.util.DataSetUtil;
import com.sync.dao.OrderDao;
import com.sync.dao.PayRoadDao;

public class MakeDataInternal {

	private Logging log = Logging.getLogging(this.getClass().getName());
	private static final int[] numArrs = new int[]{0,1,2,3,4,5,6,7,8,9};
	private OrderDao orderDao = new OrderDao();
	private PayRoadDao payRoadDao = new PayRoadDao();
	/**
	 * @param inMap
	 * @return
	 * @throws Exception
	 * @author yxd
	 */
	public ParaMap addData(ParaMap inMap)throws Exception{
		ParaMap outMap = new ParaMap();
		Random random = new Random();
		int number = inMap.getInt("number");
		for (int i = 0; i < number; i++) {
			//新增订单
			ParaMap orderInMap = new ParaMap();
			String id = IDGenerator.newGUID();
			String orderId = id.substring(id.length()-16, id.length());
			orderInMap.put("id", orderId);//订单主键
			String ordercode = IDGenerator.newGUID();
			orderInMap.put("ordercode", ordercode);//订单编号
			orderInMap.put("systype", "3");//对应产品（3-路边）
			int state = random.nextInt(4);
			orderInMap.put("state", state);//'1-计费中 2-已结算 3-离场待补缴'
			String comid = orderId.substring(0, 8);
			orderInMap.put("comid", comid);//业主ID
			String ploid = comid+state;
			orderInMap.put("ploid", ploid);//停车场ID
			String areaid = ploid+state;
			orderInMap.put("areaid", areaid);//泊位相关的什么ID
			orderInMap.put("cantonid", areaid);//泊位相关的ID
			orderInMap.put("sectionid", areaid);//路段ID
			orderInMap.put("berthcode", areaid);//泊位号
			String mebid = IDGenerator.newGUID();
			orderInMap.put("mebid", mebid);//客户ID
			StringBuilder tel =  new StringBuilder();
			for (int j = 0; j < 11; j++) {
				tel.append(numArrs[random.nextInt(10)]);
			}
			orderInMap.put("mebtel", tel.toString());//客户电话
			StringBuilder carplate =  new StringBuilder();
			for (int j = 0; j < 6; j++) {
				carplate.append(numArrs[random.nextInt(6)]);
			}
			orderInMap.put("carplate", carplate.toString());//车牌号码
			orderInMap.put("cartype", 0);//车辆类型
			Long addtime = System.currentTimeMillis();
			orderInMap.put("addtime", addtime);//订单创建时间
			Long applytime = System.currentTimeMillis();
			orderInMap.put("applytime", applytime);//支付时间）
			orderInMap.put("billstart", System.currentTimeMillis());//计费开始时间 （路边停车用）
			orderInMap.put("intime", System.currentTimeMillis());//车辆驶入时间
			orderInMap.put("outtime", System.currentTimeMillis());//车辆驶离时间
			orderInMap.put("applyway", 0);//申请方式
			orderInMap.put("applyduration", 0);//申请总时长
			int paymentway = random.nextInt(7);
			orderInMap.put("paymentway", paymentway);//支付渠道(1-APP 4-公众号 5-支付宝 6-POS)
			int paytype = random.nextInt(4);
			orderInMap.put("paytype",paytype);//支付方式
			orderInMap.put("paymethod", random.nextInt(4));//支付方式
			orderInMap.put("amount", random.nextInt(100));//订单金额
			int shouldpay = random.nextInt(100);
			orderInMap.put("shouldpay", shouldpay);//应付金额
			int actualpay = random.nextInt(100);
			orderInMap.put("actualpay", actualpay);//应付金额
			orderInMap.put("dctrate", 1);//折扣率
			int discount = random.nextInt(10);
			orderInMap.put("discount", discount);//优惠金额
			String creator = IDGenerator.newGUID().substring(9,15);
			orderInMap.put("creator", creator);//创建者
			Long createtime = System.currentTimeMillis();
			orderInMap.put("createtime", createtime);//创建时间
			Long updatetime = System.currentTimeMillis();
			orderInMap.put("updatetime",updatetime);//版本时间戳
			orderInMap.put("remark", "");//'系统备注
			int isvalid =  random.nextInt(10) > 8?0:1;
			orderInMap.put("isvalid",isvalid);//'是否有效 0-无效 1-有效'
			orderInMap.put("carproperty", random.nextInt(5));//'车辆属性(1.普通车 2.包月车 3.白名单 4.特殊车辆)'
			orderInMap.put("actualouttime", System.currentTimeMillis());//实际驶离时间
			orderInMap.put("monthcartype", random.nextInt(7) > 8?0:1);//月卡车辆类型
			StringBuilder monthcarno =  new StringBuilder();
			for (int j = 0; j < 7; j++) {
				monthcarno.append(numArrs[random.nextInt(7)]);
			}
			orderInMap.put("monthcarno", monthcarno.toString());//月卡编号
			orderInMap.put("ischange",  random.nextInt(10) > 8?0:1);//是否改单 1:是
			orderInMap.put("printcode", System.currentTimeMillis());//打印编号
			orderInMap.put("discountrate", 1);//折扣率(活动)
			orderInMap.put("discountedprice", random.nextInt(10));//折扣金额(活动)
			//开始新增订单数据
			ParaMap orderOutMap = new ParaMap();
			orderOutMap = orderDao.addOrder(orderInMap);
			if(DataSetUtil.addSuccess(orderOutMap)){
				//开始新增交易订单数据
				ParaMap traInMap = new ParaMap();
				id = IDGenerator.newGUID();
				String traId = id.substring(id.length()-16, id.length());
				traInMap.put("id", traId);//交易主键
				traInMap.put("orderid", orderId);//订单主键
				traInMap.put("mebid", mebid);//交易人编号
				traInMap.put("mebtel", tel.toString());//客户电话
				traInMap.put("addtime", addtime);//申请时间 applytime
				traInMap.put("paytime", applytime);//付款时间 
				traInMap.put("shouldpay", shouldpay);//应付
				traInMap.put("actualpay", actualpay);//实付
				traInMap.put("paymentway", paymentway);//付款渠道(1-APP 4-公众号 5-支付宝 6-POS)
				traInMap.put("paytype", paytype);//付款方式,1:余额付款2:现金3:微信支付4支付宝支付5:洪城一卡通支付6:银联卡
				traInMap.put("dctrate", 1);//折扣率
				traInMap.put("discount", 1);//折扣
				traInMap.put("cardcode", monthcarno.toString());//月卡
				traInMap.put("exptime", random.nextInt(10000000));//时长
				traInMap.put("poscode", random.nextInt(10000000));//
				traInMap.put("posuserid", random.nextInt(10000000));//
				traInMap.put("psamcode", random.nextInt(10000000));//
				traInMap.put("type", random.nextInt(5));//1-付费  2-欠款  3-退款  4-预付  11-无订单计费
				traInMap.put("state", random.nextInt(4));//1-未付 2-已付 3已取消
				traInMap.put("creator", creator);//
				traInMap.put("createtime", createtime);//
				traInMap.put("updatetime", updatetime);//
				traInMap.put("remark", "");//
				traInMap.put("isvalid", isvalid);//
				traInMap.put("paymebid", IDGenerator.newGUID());//代补缴人id
				traInMap.put("balancecode", IDGenerator.newGUID());//扎帐流水
				traInMap.put("balancetime", System.currentTimeMillis());//扎帐流水
				traInMap.put("discountrate", 1);//折扣率(活动)
				traInMap.put("discountedprice", random.nextInt(10));//折扣金额(活动)
				ParaMap traOutMap = payRoadDao.addPayRoad(traInMap);
				if(DataSetUtil.addSuccess(traOutMap)){
					outMap.put("state", 1);
				}else{
					DataSourceManager.rollback();
				}
			}
		}
		return outMap;
	}
	
	
	public static void main(String[] args) {
		/*for (int i = 0; i < 10; i++) {
			System.out.println(new Random().nextInt(4));
		}*/
		
	}
}
