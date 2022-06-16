package channel;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author gaoyang
 * create on 2022/4/29
 */
@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
        try(FileChannel channel = new FileInputStream("data.txt").getChannel()){
            //准备缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(10);
            //从channel读
            while (true){
                int len = channel.read(buffer);
                log.debug("读取到的字节数 {}", len);
                if (len == -1){
                    break;
                }
                //打印buffer内容
                buffer.flip();//切换读模式
                while(buffer.hasRemaining()){
                    byte b = buffer.get();
                    log.debug("读取到的实际的字节 {}", (char)b);
                }
                buffer.clear();
            }

        }catch (IOException e){

        }
    }
}
