package netty.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class ChannelFutureTest {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
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
//        该方法为异步非阻塞方法，主线程调用后不会被阻塞，真正去执行连接操作的是NIO线程
                // NIO线程：NioEventLoop 中的线程
                .connect(new InetSocketAddress("localhost", 8080));
        //这是因为建立连接(connect)的过程是异步非阻塞的，若不通过sync()方法阻塞主线程，
        // 等待连接真正建立，这时通过 channelFuture.channel() 拿到的 Channel 对象，
        // 并不是真正与服务器建立好连接的 Channel，也就没法将信息正确的传输给服务器端
        //所以需要通过channelFuture.sync()方法，阻塞主线程，同步处理结果，等待连接真正建立好以后，
        // 再去获得 Channel 传递数据。使用该方法，获取 Channel 和发送数据的线程都是主线程
        //1.使用sync方法同步处理
   /*     channelFuture.sync();
        Channel channel = channelFuture.channel();
        System.out.println(channel);
        channel.writeAndFlush("hello world");
*/
        //2.异步处理
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = channelFuture.channel();
                channel.writeAndFlush("hello world");
            }
        });
    }
}
