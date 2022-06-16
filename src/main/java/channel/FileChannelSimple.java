package channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * @author gaoayang
 * create by gaoyang on 2020/9/7
 */
public class FileChannelSimple {

    public static void main(String[] args) throws IOException {
        //写文件
        FileChannel fc = new FileOutputStream("D:/data.txt").getChannel();
        fc.write(ByteBuffer.wrap("some text".getBytes()));
        fc.close();
        //写入更多的内容
        fc = new RandomAccessFile("D:/data.txt", "rw").getChannel();
        fc.position(fc.size());
        fc.write(ByteBuffer.wrap("some more".getBytes()));
        fc.close();

        //读取文件内容
        fc = new FileInputStream("D:/data.txt").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        fc.read(buffer);
        buffer.flip();
        while (buffer.hasRemaining()){
            System.out.println((char)buffer.get());
        }


    }
}
