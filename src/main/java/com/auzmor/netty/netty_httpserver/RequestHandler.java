package com.auzmor.netty.netty_httpserver;

import static io.netty.buffer.Unpooled.copiedBuffer;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class RequestHandler {
	
	public static FullHttpResponse response(ChannelHandlerContext ctx, Object msg){
		if (msg instanceof FullHttpRequest)
        {
            final FullHttpRequest request = (FullHttpRequest) msg;
            String uri=request.uri();
            String url=uri.substring(0, uri.indexOf("?"));
            HttpResponseStatus htt=HttpResponseStatus.BAD_REQUEST;
            
            String responseMessage = "Data saved!"; 
            
            // for specific url
            if(url.equals("/data/save")){
            	
            	Map<String, String> reqParmGetrFrmUri = reqParmGetrFrmUri(uri);
            	
            	if(reqParmGetrFrmUri!=null && reqParmGetrFrmUri.size()>0){
            		KafkaQueueHandler.pushMsgToKafka(reqParmGetrFrmUri);
            	}else{
            		htt=HttpResponseStatus.BAD_REQUEST;
            		responseMessage="No data to store!";
            	}
            	
            	
            }else{
            	htt=HttpResponseStatus.NOT_FOUND;
            	responseMessage="URL not found";
            }
            
            return new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    htt,
                    copiedBuffer(responseMessage.getBytes())
                );                                           
            
        }
		
		return null;
	}
	
	public static Map<String , String> reqParmGetrFrmUri(String uri){
		
        if(uri.contains("?")){
        	String queryString=uri.substring(uri.indexOf("?")+1);
        	
        	Map<String,String> queryParams=Arrays.asList(queryString.split("&")).stream().
        	filter(ele->ele.contains("="))
        	.map(map->{
        		
        		map.substring(0, map.indexOf("="));
        		return map;
        	}).collect(Collectors.toMap(ele->ele.substring(0, ele.indexOf("=")),
        			ele->ele.substring(ele.indexOf("=")+1)));
        	
        	return queryParams;
        }
		
		return null;
	}
	

}
