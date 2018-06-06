package com.common.init;


import com.base.log.Logging;
import com.base.mq.MQReceUtils;
import com.base.task.TaskManager;
import com.base.utils.StrUtils;
import com.base.web.AppConfig;
import com.base.web.AppInit;
import com.report.kafka.KafkaConsumeStart;
import com.report.task.CycleSendMailTask;
import com.report.task.EveryDayTask;
import com.report.task.SyscDataTask;
import com.report.threadpool.ThreadPoolInstance;
import com.report.util.DateUtils;
import com.report.util.TaskUtil;
import com.stif.common.util.IOUtils;
import com.util.excel.ExcelUtil;

public class ApplicationMain extends AppInit {
	
	private static Logging log = Logging.getLogging(ApplicationMain.class.getName());

	/**
	 * 强制需要t 且同步
	 */
	public void accessInit() {
		// 配置防止重复访问的url
	}

	public void mqInit() {

	}

	public void redirectInit() {
		// 配置允许第三方回调的接口
	}

	public void whetherEncrypt(){
		String encryptOr = AppConfig.getStringPro("excel_encrypt");
		String password = AppConfig.getStringPro("excel_password");
		if(StrUtils.isNotNull(encryptOr)
				&& StrUtils.isNotNull(password)
				&& StrUtils.equals(encryptOr, TaskUtil.EXECL_ENCRYPT)){
			//要求加密 则需要加密后替换源文件
			String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"report.xlsx";
			ExcelUtil.encryptExcel(filePath, password);
		}
	}
	
	public void taskInit(){
		try {
			long nowTime = DateUtils.nowTime(); // 当前时间
			String format = DateUtils.TO_DAY_LINE;
			String today = DateUtils.format(nowTime, format); // 今天凌晨
			/*
			//初始化历史统计定时器 只执行一次
			long firstTime = 0;
			String taskFristTime = AppConfig.getStringPro("task_frist_time");
			if(StrUtils.isNotNull(taskFristTime)){
				Long todayTime = DateUtils.getTime(today+TaskUtil.SPACK+taskFristTime, DateUtils.TO_MINUTE_LINE);// 今天某点某分钟
				if(nowTime < todayTime){//执行当前配置的时刻
					firstTime = todayTime;
				}else{
					firstTime = todayTime + DateUtils.ONE_DAY_TIME;//第二天的这个时刻执行
				}
			}
			long hisDelay = firstTime - nowTime;
			TaskManager.add(new HistoricalStatisticsTask("HistoricalStatisticsTask"), hisDelay);
			log.info("历史统计任务初始化成功");*/

			//初始化每天周期任务定时器
			long period = DateUtils.ONE_DAY_TIME; // 执行周期：默认24小时
			String taskEverydayPeriod = AppConfig.getStringPro("task_everyday_period");
			if(StrUtils.isNotNull(taskEverydayPeriod)){
				period =  Long.parseLong(taskEverydayPeriod)*1000;
			}
			long atTime = 0;
			String taskEverydayTime = AppConfig.getStringPro("task_everyday_time");
			if(StrUtils.isNotNull(taskEverydayTime)){
				Long todayTime = DateUtils.getTime(today+TaskUtil.SPACK+taskEverydayTime, DateUtils.TO_MINUTE_LINE);// 今天某点某分钟
				if(nowTime < todayTime){//执行当前配置的时刻
					atTime = todayTime;
				}else{
					atTime = todayTime + DateUtils.ONE_DAY_TIME;//第二天的这个时刻执行
				}
			}
			long delay = atTime - nowTime;
			TaskManager.add(new EveryDayTask("EveryDayTask"), delay, period);
			log.info("每天任务初始化成功");
			
			
			//周期性发邮件
			long mailPeriod = DateUtils.ONE_DAY_TIME; // 执行周期：默认24小时
			String sendMailPeriod = AppConfig.getStringPro("send_mail_period");
			if(StrUtils.isNotNull(sendMailPeriod)){
				mailPeriod =  Long.parseLong(sendMailPeriod)*DateUtils.ONE_DAY_TIME;
			}
			long mailTime = 0;
			String sendMailTime = AppConfig.getStringPro("send_mail_time");
			if(StrUtils.isNotNull(sendMailTime)){
				Long appointTime = DateUtils.getTime(sendMailTime, DateUtils.TO_MINUTE_LINE);// 今天某点某分钟
				if(nowTime < appointTime){//执行当前配置的时刻
					mailTime = appointTime;
				}else{
					mailTime = nowTime + 1000*60;//延迟一分钟
				}
			}
			long mailDelay = mailTime - nowTime;
			TaskManager.add(new CycleSendMailTask("CycleSendMailTask"), mailDelay, mailPeriod);
			log.info("周期性发送邮件任务初始化成功");
			syscData();
			log.info("周期性同步数据任务初始化成功");
		} catch (Exception e) {
			log.error("定时任务初始化出错：" + IOUtils.getStackTrace(e, true));
		}
	}
	
	private void syscData(){
		long nowTime = DateUtils.nowTime(); // 当前时间
		String format = DateUtils.TO_DAY_LINE;
		String today = DateUtils.format(nowTime, format); // 今天凌晨
		long atTime = 0;
		String taskEverydayTime = AppConfig.getStringPro("sysc_data_time");
		if(StrUtils.isNotNull(taskEverydayTime)){
			Long todayTime = DateUtils.getTime(today+TaskUtil.SPACK+taskEverydayTime, DateUtils.TO_MINUTE_LINE);// 今天某点某分钟
			if(nowTime < todayTime){//执行当前配置的时刻
				atTime = todayTime;
			}else{
				atTime = todayTime + DateUtils.ONE_DAY_TIME;//第二天的这个时刻执行
			}
		}
		long delay = atTime - nowTime;
		try {
			TaskManager.add(new SyscDataTask("SyscDataTask"), delay, DateUtils.ONE_DAY_TIME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(IOUtils.getStackTrace(e));
		}
	}

	public void init() {
		accessInit();
		redirectInit();
		if (AppConfig.getBooleanPro("mqReceEnable")) {
			mqInit();
			MQReceUtils.startRece();
		}
		whetherEncrypt();
		taskInit();
		ThreadPoolInstance.getThreadPoolInstancle().getThreadPoolExecutor().execute(new KafkaConsumeStart());
		log.info("********** 服务初始化完成. **********");
	}

	public void saveMQException(long id, String mqName, String mqContent, String errorMessage) throws Exception {
		// 保存mq异常
	}
}