package com.auzmor.netty.netty_httpserver;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
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
	
	public static void getMsgFrmKafka(Map<String, String> queryParams){
		 Properties props = new Properties();
		    props.put("bootstrap.servers", PropUtils.getProperty("bootstrap.servers"));
		    props.put("acks", PropUtils.getProperty("acks"));
		    props.put("retries", PropUtils.getProperty("retries"));
		    props.put("batch.size", PropUtils.getProperty("batch.size"));
		    props.put("linger.ms", PropUtils.getProperty("linger.ms"));
		    props.put("buffer.memory", PropUtils.getProperty("buffer.memory"));
		    props.put("key.deserializer", PropUtils.getProperty("key.deserializer"));
		    props.put("value.deserializer", PropUtils.getProperty("value.deserializer"));
		    props.put("group.id", PropUtils.getProperty("group.id"));
		    
		    try( Consumer<String, Message> consumer=new KafkaConsumer<>(props)) {
		    	
		    	consumer.subscribe(Collections.singletonList("querySave"));
		        ConsumerRecords<String, Message> records = consumer.poll(1000);
		        for (ConsumerRecord<String, Message> record : records) {
		            System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
		        }
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		
	}
	
	
	
}
