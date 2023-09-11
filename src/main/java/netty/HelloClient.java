package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        //1.启动类
        new Bootstrap()
                //添加Eventloop
                .group(new NioEventLoopGroup())
                //3，选择客户端channel实现
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    //7。在建立连接后被 调用
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringEncoder());
                        }
                })
                //4.建立连接
                .connect(new InetSocketAddress("localhost", 8080))
                .sync() //5. 阻塞方法，知道连接建立
                .channel()
                .writeAndFlush("hello world"); //6.发送数据
    }
}
