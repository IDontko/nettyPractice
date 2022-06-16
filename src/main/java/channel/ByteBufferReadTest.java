package channel;


import io.netty.buffer.ByteBufUtil;

import java.nio.ByteBuffer;

/**
 * @author gaoyang
 * create on 2022/4/29
 */
public class ByteBufferReadTest {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();


     /*   buffer.get(new byte[4]);
        int position = buffer.position();
        System.out.println(position);

        System.out.println((char)buffer.get());*/
        //设置position为0,从头读
   /*     buffer.rewind();
        System.out.println((char)buffer.get());*/
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());
        buffer.mark(); //加标记，索引2的位置
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());
        buffer.reset(); //将position重置到索引2
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());
        ByteBufferUtil.debugAll(buffer);



    }
}
