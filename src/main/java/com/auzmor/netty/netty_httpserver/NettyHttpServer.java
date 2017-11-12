package com.auzmor.netty.netty_httpserver;

import static io.netty.buffer.Unpooled.copiedBuffer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

public class NettyHttpServer
{
    private ChannelFuture channel;
    private final EventLoopGroup masterGroup;
    private final EventLoopGroup slaveGroup;
    
    public NettyHttpServer()
    {
        masterGroup = new NioEventLoopGroup();
        slaveGroup = new NioEventLoopGroup();        
    }

    public void start()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() { shutdown(); }
        });
        
        try
        {
            final ServerBootstrap bootstrap =
                new ServerBootstrap()
                    .group(masterGroup, slaveGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>(){
                        @Override
                        protected void initChannel(final NioSocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast("codec", new HttpServerCodec());
                            ch.pipeline().addLast("aggregator", new HttpObjectAggregator(512*1024));
                            ch.pipeline().addLast("request", new ChannelInboundHandlerAdapter()
                            {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg)
                                        throws Exception
                                {
                                    if (msg instanceof FullHttpRequest)
                                    {
                                        final FullHttpRequest request = (FullHttpRequest) msg;
                                        
                                        System.out.println("request entered");
                                        FullHttpResponse response = RequestHandler.response(ctx, msg);
                                        System.out.println("response enterededed");
                                        if (HttpUtil.isKeepAlive(request))
                                        {
                                            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                                        }
                                        
                                        ctx.writeAndFlush(response);
                                    }
                                    else
                                    {
                                        super.channelRead(ctx, msg);
                                    }
                                }
    
                                @Override
                                public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
                                {
                                    ctx.flush();
                                }
    
                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                                        throws Exception
                                {
                                    ctx.writeAndFlush(new DefaultFullHttpResponse(
                                        HttpVersion.HTTP_1_1,
                                        HttpResponseStatus.INTERNAL_SERVER_ERROR,
                                        copiedBuffer(cause.getMessage().getBytes())
                                    ));
                                }                                    
                            });
                        }

                    })
                    .option(ChannelOption.SO_BACKLOG, 128);
            channel = bootstrap.bind(8088).sync();
        }
        catch (final InterruptedException e) {
        	
        }
    }
    
    public void shutdown()
    {
        slaveGroup.shutdownGracefully();
        masterGroup.shutdownGracefully();

        try
        {
            channel.channel().closeFuture().sync();
        }
        catch (InterruptedException e) { }
    }

    public static void main(String[] args)
    {
    	
        new NettyHttpServer().start();
    }
}
