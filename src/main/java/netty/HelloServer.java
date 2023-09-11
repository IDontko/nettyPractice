package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {

    public static void main(String[] args) {
        //1.启动器，负责组装netty组件，启动服务类
        new ServerBootstrap().
                //2. EventLoopGroup(selector, thread)
                group(new NioEventLoopGroup())
                //3. 选择服务器ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                //4. boss 负责处理连接， work负责读写，决定worker能执行哪些操作（handler)
                .childHandler(
                        //5. channel 代表 和客户端进行数据读写的通道 Initializer  是初始化
                        new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //6.添加具体Handler
                        ch.pipeline().addLast(new StringDecoder());
                        //7.自定义Handler
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            //读事件
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });

                    }
                    //绑定监听端口
                }).bind(8080);

    }
}
