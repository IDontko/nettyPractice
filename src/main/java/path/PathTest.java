package path;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * jdk7引入Path类
 */
public class PathTest {

    public static void main(String[] args) {
        Path source = Paths.get("d:\\data\\projects\\a\\b");
        System.out.println(source);
        System.out.println(source.normalize());
        
        //查看文件是否存在
        Files.exists(source, LinkOption.NOFOLLOW_LINKS);

        //创建目录，但是不能创建多级目录
        try {
            Path directory = Files.createDirectory(source);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //拷贝文件
        Path path1 = Paths.get("data.txt");
        Path path2 = Paths.get("to.txt");
        try {
            Files.copy(path1, path2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //删除目录,如果目录还有内容，会抛出异常
        try {
            Files.delete(path1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
