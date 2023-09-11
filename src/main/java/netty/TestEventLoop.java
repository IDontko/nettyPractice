package netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup(2); // io事件，普通任务，定时任务
        //2. 获取下一个事件循环对象
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        group.next().submit(() -> {
        try {
            Thread.sleep(1000);
        }   catch (InterruptedException e){
            e.printStackTrace();
        }
            log.debug("ok");
        } );

        log.debug ("main");
    }
}
