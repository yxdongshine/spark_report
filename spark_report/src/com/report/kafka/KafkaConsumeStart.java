package com.report.kafka;

import java.util.ArrayList;
import java.util.List;

import com.base.web.AppConfig;
import com.report.util.TaskUtil;
import com.stif.common.util.IOUtils;

public class KafkaConsumeStart implements Runnable{

	/**
	 *启动消费者 
	 * @author YXD
	 */
	public void startConsume(){
		
		JavaKafkaSimpleConsumer consumer = new JavaKafkaSimpleConsumer();
        KafkaTopicPartitionInfo topicPartitionInfo = new KafkaTopicPartitionInfo(AppConfig.getStringPro("kafka.topic"), Integer.parseInt(AppConfig.getStringPro("kafka.partitionID")));
        List<KafkaBrokerInfo> seeds = new ArrayList<KafkaBrokerInfo>();
        seeds.add(new KafkaBrokerInfo(AppConfig.getStringPro("kafka.host"), Integer.parseInt(AppConfig.getStringPro("kafka.port"))));
        try {
        	consumer.run(topicPartitionInfo, seeds);
        } catch (Exception e) {
        	TaskUtil.log.error("异常："+IOUtils.getStackTrace(e));  
        }
	}
	
	public static void main(String[] args) {
		new KafkaConsumeStart().startConsume();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		startConsume();
	}
}
