package com.report.task;


import com.base.task.MyTimerTask;
import com.base.utils.IDGenerator;
import com.report.ssh.SshConnectionInstance;
import com.report.util.DateUtils;

public class SyscDataTask extends MyTimerTask {
	
	
	public SyscDataTask(String id) {
		super(id);
	}

	
	
	@Override
	public void execute() throws Exception {
		String format = DateUtils.TO_DAY_LINE;
		//昨天日期
		String yesterday = DateUtils.format(DateUtils.prevDay(1), format); // 昨天凌晨
		//今天开始时间
		long startTime = DateUtils.getTime(yesterday+" 00:00:00", DateUtils.TO_SECOND_LINE);
		long endTime = DateUtils.getTime(yesterday+" 23:59:59", DateUtils.TO_SECOND_LINE);
		/*long startTime = 0L;
		long endTime = Long.MAX_VALUE;*/
		// shell所在的地址路径					  
        String shellPath = "/app/module/shell/script_for_syscdata.sh";
        // 最终执行的命令
        String cmd = "sh " + shellPath + " " + yesterday+ " " + startTime+ " "+endTime;
       //String cmd = "sh " + shellPath + " " + "2018-06-03"+ " " + "1527955200000" + " "+ "1528041599000";
        /*try {
            // 执行命令，获取执行命令的进程对象
            Process process = Runtime.getRuntime().exec(cmd);
            // 等等进程执行完成
            process.waitFor();
            // 获取进程执行的结果值
            int exitValue = process.exitValue();
            if (exitValue == 0) {
                // 脚本执行正常
                // TODO: 更新任务的一些状态信息
           	 	TaskUtil.log.info("同步数据执行成功！！！");

            } else {
                // 脚步执行失败
                // TODO: 通知开发人员，执行失败
           	 	TaskUtil.log.info("同步数据执行失败：");
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
	
        //方案二：远程调用脚本
        SshConnectionInstance scInstance = new SshConnectionInstance();
        scInstance.getSshConnectionInstance();
        scInstance.exec(cmd);
	}
	
	public static void main(String[] args) throws Exception {
		/*SyscDataTask task = new SyscDataTask("SyscDataTask");
		task.execute();*/
		System.out.println(Long.MAX_VALUE);
	}
}
