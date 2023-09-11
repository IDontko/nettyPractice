package channel;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gaoyang
 * create on 2022/4/30
 */
public class TestFilesWalkFileTree {
    public static void main(String[] args) throws IOException {
//        m1();
        m2();
    }


    private static void  m2() throws IOException{
        AtomicInteger jarCount  = new AtomicInteger();
        Files.walkFileTree(Paths.get("D:\\w_work\\s_softerware\\j_jdk1.8"), new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".jar")){
                    System.out.println(file);
                    jarCount.incrementAndGet();
                }
                return super.visitFile(file, attrs);
            }

        });
        System.out.println("jar count: " +jarCount);

    }
    //遍历目录
    private static void m1() throws IOException {
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(Paths.get("D:\\w_work\\s_softerware\\j_jdk1.8"), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException
            {
                System.out.println(dir);
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            /**
             * Invoked for a file in a directory.
             *
             * <p> Unless overridden, this method returns {@link FileVisitResult#CONTINUE
             * CONTINUE}.
             */
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException
            {
                System.out.println(file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        System.out.println(dirCount);
        System.out.println(fileCount);
    }
}
