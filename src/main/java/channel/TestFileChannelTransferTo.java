package channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author gaoyang
 * create on 2022/4/30
 */
public class TestFileChannelTransferTo {

    public static void main(String[] args) {

        try {
            FileChannel from = new FileInputStream("data.txt").getChannel();
            FileChannel to = new FileOutputStream("to.txt").getChannel();
            long size = from.size();
            //left变量代表还剩余多少字节
            for (long left = size; left > 0; ) {
                left -= from.transferTo((size - left), left, to);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
