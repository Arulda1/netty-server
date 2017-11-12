package com.auzmor.netty.netty_httpserver.serializer;

import org.apache.kafka.common.serialization.Serializer;

import com.auzmor.netty.netty_httpserver.message.MessageProto.Message;

public class MessageSerializer extends Adapter implements Serializer<Message>{
	
	 @Override
	    public byte[] serialize(final String topic, final Message data) {
	        return data.toByteArray();
	    }

}
