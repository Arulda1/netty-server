package com.auzmor.netty.netty_httpserver.serializer;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auzmor.netty.netty_httpserver.message.MessageProto.Message;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessageDeserializer extends Adapter implements Deserializer<Message>{

	private static final Logger LOG = LoggerFactory.getLogger(MessageDeserializer.class);

    @Override
    public Message deserialize(final String topic, byte[] data) {
        try {
            return Message.parseFrom(data);
        } catch (final InvalidProtocolBufferException e) {
            LOG.error("Received unparseable message", e);
            throw new RuntimeException("Received unparseable message " + e.getMessage(), e);
        }
    }
	
}
