package com.auzmor.netty.netty_httpserver;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.auzmor.netty.netty_httpserver.message.MessageProto;
import com.auzmor.netty.netty_httpserver.message.MessageProto.Message;
import com.auzmor.netty.netty_httpserver.message.MessageProto.Message.Builder;
import com.auzmor.netty.netty_httpserver.utils.PropUtils;

public class KafkaQueueHandler {

	public static void pushMsgToKafka(Map<String, String> queryParams){
		
		 Properties props = new Properties();
		    props.put("bootstrap.servers", PropUtils.getProperty("bootstrap.servers"));
		    props.put("acks", PropUtils.getProperty("acks"));
		    props.put("retries", PropUtils.getProperty("retries"));
		    props.put("batch.size", PropUtils.getProperty("batch.size"));
		    props.put("linger.ms", PropUtils.getProperty("linger.ms"));
		    props.put("buffer.memory", PropUtils.getProperty("buffer.memory"));
		    props.put("key.serializer", PropUtils.getProperty("key.serializer"));
		    props.put("value.serializer", PropUtils.getProperty("value.serializer"));
		    
		    try( Producer<String, Message> producer=new KafkaProducer<>(props)) {
		    	
		    	
		    	Builder newBuilder = MessageProto.Message.newBuilder();
		    	newBuilder.putAllQuery(queryParams);
		    	Message msg = newBuilder.build();
		    	producer.send(new ProducerRecord<String, Message>("querySave", msg));
		    	
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		
	}
	
	
	
}
