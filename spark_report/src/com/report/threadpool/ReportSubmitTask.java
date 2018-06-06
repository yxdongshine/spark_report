package com.report.threadpool;

import java.io.IOException;

import com.base.log.Logging;
import com.report.ssh.SshConnectionInstance;
import com.report.util.TaskUtil;
import com.stif.common.util.IOUtils;

public class ReportSubmitTask implements Runnable {

	public static Logging log = Logging.getLogging(ReportSubmitTask.class.getName());
	private String sql;
	
	public ReportSubmitTask(String sql) {
		// TODO Auto-generated constructor stub
		this.sql = sql;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		executeShell();
	}

	/**
	 * 执行Linux shell 命令
	 * @param sql
	 * @author YXD
	 */
	public void executeShell(){
		 // shell所在的地址路径						
        String shellPath = "/app/module/shell/script_for_submit.sh";
        // shell脚本需要传递的参数
        String sQLParam = sql;
        String regulateParam = " --driver-memory 4g --executor-memory 12g --executor-cores 2 --num-executors 7 ";
        // 最终执行的命令
        String cmd = "sh " + shellPath + " " + sQLParam+ " " + TaskUtil.QUOTATION_MARK_SIGN+regulateParam+TaskUtil.QUOTATION_MARK_SIGN;
        String[] cmds = new String[]{"sh",shellPath,sQLParam,regulateParam};
        //方案一：本地调用远程shell脚本
        //TaskUtil.log.info("执行命令："+cmd.length);
        /*try {
            // 执行命令，获取执行命令的进程对象
            Process process = Runtime.getRuntime().exec(cmds);
            WirteLogTask wlTask = new WirteLogTask(process);
			ThreadPoolInstance.getThreadPoolInstancle().getThreadPoolExecutor().execute(wlTask);
            // 等等进程执行完成
            int waitValue = process.waitFor();
            // 获取进程执行的结果值
            int exitValue = process.exitValue();
        	TaskUtil.log.info("执行等待值："+waitValue+"执行结果退出值："+exitValue);
            if (exitValue == 0) {
                // 脚本执行正常
                // TODO: 更新任务的一些状态信息
            	TaskUtil.log.info("执行成功的sql："+sql);
            } else {
                // 脚步执行失败
                // TODO: 通知开发人员，执行失败
            	TaskUtil.log.info("sql执行失败："+sql);
            }
        } catch (IOException e) {
            // 脚步执行失败
            // TODO: 通知开发人员，执行失败
        	TaskUtil.log.error("异常："+IOUtils.getStackTrace(e));
        } catch (InterruptedException e) {
            // 脚步执行失败
            // TODO: 通知开发人员，执行失败
        	TaskUtil.log.error("异常："+IOUtils.getStackTrace(e));
        }*/
        //方案二 ：远程调用shell脚本
        SshConnectionInstance scInstance = new SshConnectionInstance();
        scInstance.getSshConnectionInstance();
        scInstance.exec(cmd);
	}
}
