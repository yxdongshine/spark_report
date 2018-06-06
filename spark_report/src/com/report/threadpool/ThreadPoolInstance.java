package com.report.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolInstance {
	
	private static int MIN = 2;
	private static int MAX = 4;
	private static int TIME_UNIT = 3000;
	private static int QUEUE_SIZE = 100;
	private ThreadPoolInstance(){
		
	}
	
	private static ThreadPoolInstance tpInstancle = null;
	private  ThreadPoolExecutor  tpExecutor = null;

	
	/**
	 * 获取单利线程池
	 * @return
	 * @author YXD
	 */
	public static ThreadPoolInstance getThreadPoolInstancle(){
		if(null == tpInstancle){
			tpInstancle = new ThreadPoolInstance();
			if( null == tpInstancle.tpExecutor){
				tpInstancle.tpExecutor = new ThreadPoolExecutor(MIN, MAX, TIME_UNIT,TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(QUEUE_SIZE));
			}
		}
		return tpInstancle;
	}

	/**
	 * 获取单利线程池
	 * @return
	 * @author YXD
	 */
	public ThreadPoolExecutor getThreadPoolExecutor(){
		return tpInstancle.tpExecutor;
	}
	
}
