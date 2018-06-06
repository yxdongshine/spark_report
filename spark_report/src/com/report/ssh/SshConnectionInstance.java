package com.report.ssh;

import java.io.IOException;
import java.io.InputStream;

import com.base.log.Logging;
import com.base.web.AppConfig;
import com.report.threadpool.SshWirteLogTask;
import com.report.threadpool.ThreadPoolInstance;
import com.stif.common.util.IOUtils;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class SshConnectionInstance {
	
	public static Logging log = Logging.getLogging(SshConnectionInstance.class.getName());
	private static final int TIME_OUT = 1000 * 60 * 60;
	
    private  Connection conn = null;  
    private  Session session = null;
	
	/**
	 * 获取单例连接session
	 * @return
	 * @author YXD
	 */
	public void getSshConnectionInstance(){
		if(null == conn){
			conn = new Connection(AppConfig.getStringPro("ssh_ip"));  
		    try {
				conn.connect();
			    conn.authenticateWithPassword(AppConfig.getStringPro("ssh_user"), AppConfig.getStringPro("ssh_password")); // 认证  
			    getSession();
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("ssh连接异常："+IOUtils.getStackTrace(e));
			} 
		}
	}

	/**
	 * 获取连接
	 * @return
	 * @author YXD
	 */
	private  Session getSession(){

		try {
			if(null == session) session = conn.openSession();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("打开会话异常："+IOUtils.getStackTrace(e));
		} // 打开一个会话  
		return session;
	}
	
	/**
	 * 关闭连接session
	 * @return
	 * @author YXD
	 */
	private void closeSession(){
		session.close();
	}
	
	/**
	 * 关闭连接conn
	 * @return
	 * @author YXD
	 */
	private void closeConn(){
		conn.close();
	}
	
	
	/**
	 * 执行命令
	 * @param cmd
	 * @author YXD
	 */
	public void exec(String cmd) {  
        // 执行命令，获取执行命令的进程对象
		try {
			session.execCommand(cmd);
			
			InputStream errIn = session.getStderr();  
	        SshWirteLogTask errSshwlTask = new SshWirteLogTask(errIn);
	        ThreadPoolInstance.getThreadPoolInstancle().getThreadPoolExecutor().execute(errSshwlTask);
	        //errSshwlTask.run();
	        
			InputStream out = session.getStdout();
	        SshWirteLogTask outSshwlTask = new SshWirteLogTask(out);
	        ThreadPoolInstance.getThreadPoolInstancle().getThreadPoolExecutor().execute(outSshwlTask);
	        //outSshwlTask.run();
			
            session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
            // 获取进程执行的结果值
 			while(null == session.getExitStatus()){
 			}
 			// 等等进程执行完成
 			String exitSignal = session.getExitSignal();
            int exitValue = session.getExitStatus();
        	log.info("退出信号"+exitSignal+"; 执行结果退出值："+exitValue);
            if (exitValue == 0) {
                // 脚本执行正常
                // TODO: 更新任务的一些状态信息
            	log.info("执行成功的sql："+cmd);
            } else {
                // 脚步执行失败
                // TODO: 通知开发人员，执行失败
            	log.info("sql执行失败："+cmd);
            }
			//log.info("执行成功的sql："+cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("执行异常："+IOUtils.getStackTrace(e));
		}
		closeSession();
		closeConn();
		// 采用暂停提交最大任务时间
       /* try {
			Thread.sleep(1000*60*3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error("休眠异常："+IOUtils.getStackTrace(e));
		}*/
	}  
}
