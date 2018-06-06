package com.report.task;

import java.io.File;

import com.base.log.Logging;
import com.base.task.MyTimerTask;
import com.base.utils.StrUtils;
import com.base.web.AppConfig;
import com.stif.common.model.MailAccount;
import com.stif.common.model.MailBody;
import com.stif.common.util.IOUtils;
import com.stif.common.util.MailUtils;

public class CycleSendMailTask extends MyTimerTask {
	
	private static Logging log = Logging.getLogging(CycleSendMailTask.class.getName());
	private static String SMTP_HOST = "smtp.exmail.qq.com";
	private static String SMTP_PORT = "25";
	private static String MAIL_REPORT_SUBJECT = "报表统计";
	private static String SEPARATE_SIGN = "#";
	
	public CycleSendMailTask(String id) {
		super(id);
	}

	@Override
	public void execute() throws Exception {
		//String filePath = "E:\\yxdCode\\stif_report\\template\\report.xlsx";
		String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"report.xlsx";
		log.info("filePath:"+filePath);
		String receiveMailAccount = AppConfig.getStringPro("receive_mail_account");
		if(StrUtils.isNotNull(receiveMailAccount)){
			String[] receiveMailAccounts = receiveMailAccount.split(SEPARATE_SIGN);
			MailAccount account = new MailAccount();
			account.setAccount(AppConfig.getStringPro("send_mail_account"));
			account.setPassword(AppConfig.getStringPro("send_mail_password"));
			account.setSmtpHost(SMTP_HOST);
			account.setSmtpPort(SMTP_PORT);
			MailBody mailBody = new MailBody();
			mailBody.setSubject(MAIL_REPORT_SUBJECT);
			
			//附件
			mailBody.addAttachment(new File(filePath));
			for (int i = 0; i < receiveMailAccounts.length; i++) {
				try {
					MailUtils.newInstance(account).send(receiveMailAccounts[i], mailBody);
				} catch (Exception e) {
					// TODO: handle exception
					log.error("异常："+IOUtils.getStackTrace(e));
				}
				log.info("邮件发送信息：给"+receiveMailAccounts[i]+"报表邮件已发送！！！");
			}
		}
	}

	public static void main(String[] args) throws Exception {
		CycleSendMailTask task = new CycleSendMailTask("CycleSendMailTask");
		task.execute();
	}
}
