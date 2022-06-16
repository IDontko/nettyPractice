package server;

import channel.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author gaoyang
 * create on 2022/4/30
 */
@Slf4j(topic = "c")
public class Server {
    private static void split(ByteBuffer buffer) {
        //切换读模式
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i++) {
            if (buffer.get(i) == '\n'){
                int length = i - buffer.position() + 1;
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(buffer.get());
                }
                ByteBufferUtil.debugAll(target);
            }
        }
        //切换写模式
        buffer.compact();
    }
    public static void main(String[] args) throws IOException {
//        getServer1();
//        getServer3();
        getServer4();

    }

    private static void server2() throws IOException {
        Selector selector = Selector.open();
        //创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        SelectionKey sscKey = ssc.register(selector, 0, null);
        log.debug("register key: {}", sscKey);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        //绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            //3.select没有事件发生，线程阻塞，有事件，线程才会恢复运行
            selector.select();
            //4.处理事件，selectKeys包含了所有发生事件。
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            if (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                log.debug("key:{}", key);
                //5.区分事件类型 连接事件
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    //将byteBuffer关联到SelectionKey上
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("sc: {}", sc);
                } else if (key.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        //
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer); //如果是正常断开，read的方法的返回值是-1
                        log.debug("read {}", read);
                        if (read == -1) {
                            key.cancel();
                        } else {
                            split(buffer);
//                            buffer.flip();
//                            ByteBufferUtil.debugRead(buffer);
                            if (buffer.position() == buffer.limit()){
                                ByteBuffer newBuffer= ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel(); //客户端断开，因此需要将key取消,从Selector集合中删除
                    }
                }

//                key.cancel();
            }
        }
    }

    //阻塞模式
    public static void getServer1() throws IOException{
        //建立缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //2.绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        //3.建立链接
        List<SocketChannel> channels = new ArrayList<>();
        while (true){
            //4.accept 建立与客户端的连接，SocketChannel 用来与客户端通信
            log.debug("connecting");
            SocketChannel sc = ssc.accept();//阻塞方法，线程停止运行
            log.debug("connected.. {}", sc);
            channels.add(sc);
            for (SocketChannel channel : channels){
                log.debug("before read");
                channel.read(buffer);
                buffer.flip();
                ByteBufferUtil.debugRead(buffer);
                log.debug("after read");
                buffer.clear();
            }
        }

    }

    //非阻塞模式
    public static void getServer3() throws IOException{
        //建立缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//非阻塞模式
        //2.绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        //3.建立链接
        List<SocketChannel> channels = new ArrayList<>();
        while (true){
            //4.accept 建立与客户端的连接，SocketChannel 用来与客户端通信
            SocketChannel sc = ssc.accept();//非阻塞，线程还会继续运行，如果没有连接，但sc是null
            if (sc != null){
                log.debug("connected.. {}", sc);
                sc.configureBlocking(false); //非阻塞模式
                channels.add(sc);
            }
            for (SocketChannel channel : channels){
                int read = channel.read(buffer); //非阻塞，线程仍然会运行，如果没有读到数据，read返回0
                if (read > 0){
                    buffer.flip();
                    ByteBufferUtil.debugRead(buffer);
                    log.debug("after read");
                    buffer.clear();
                }
            }
        }
    }

    //非阻塞模式, Selector使用
    public static void getServer4() throws IOException{
        //创建Selector,管理多个Channel
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//非阻塞模式
        //channel注册到selector上
        //selectionKey 就是事件发生后，通过它可以知道事件和哪个channel的事件(accept, connect, read, write)
        SelectionKey sscKey = ssc.register(selector, 0, null);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("register key: {}", sscKey);
        ssc.bind(new InetSocketAddress(8080));

        while (true){
            //3. select方法, 没有事件发生，线程阻塞，有事件，线程才会恢复运行
            //select未处理事件时，它不会阻塞，事件发生后，要么处理要么取消，不能置之不理
            selector.select();
            //4.处理事件,包含所有发生的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                //处理key时候，要从selectedKeys集合中删除，否则下次处理会有问题。
                iterator.remove();
                log.debug("key: {}", key);
                //5.区分数据类型
                if (key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    //接收到客户端请求
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, 0, null);
                    //关注读事件
                    scKey.interestOps(SelectionKey.OP_READ);

                    log.debug("sc: {}", sc);
                }else if (key.isReadable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    channel.read(buffer);
                    buffer.flip();
                    ByteBufferUtil.debugRead(buffer);

                }

                //事件处理取消
//                key.cancel();
            }
        }
    }
}
