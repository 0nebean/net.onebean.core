package net.onebean.util;

import com.google.common.io.Files;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * IO操作工具类
 * @author 0neBean
 */
public class IOUtils {

    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(IOUtils.class);

    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    public IOUtils() {
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
            ;
        }

    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        return count > 2147483647L ? EOF : (int)count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0L;

        int n;
        for(boolean var5 = false; EOF != (n = input.read(buffer)); count += (long)n) {
            output.write(buffer, 0, n);
        }

        return count;
    }

    public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        } else {
            int remaining;
            int count;
            for(remaining = length; remaining > 0; remaining -= count) {
                int location = length - remaining;
                count = input.read(buffer, offset + location, remaining);
                if (EOF == count) {
                    break;
                }
            }

            return length - remaining;
        }
    }

    public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        int actual = read(input, buffer, offset, length);
        if (actual != length) {
            throw new EOFException("Length to read: " + length + " actual: " + actual);
        }
    }

    public static void readFully(InputStream input, byte[] buffer) throws IOException {
        readFully(input, buffer, 0, buffer.length);
    }

    public static final String getPathOfFile(String filepath) {
        String fileSeparator = "/";
        String result;
        if (StringUtils.isEmpty(filepath)) filepath = StringUtils.EMPTY;
        filepath = filepath.replace("\\\\", "/").replace("\\", "/");
        if (filepath.endsWith(fileSeparator)) filepath = filepath.substring(0, filepath.length() - 1);
        if (filepath.contains(fileSeparator)) {
            result = filepath.substring(0, filepath.lastIndexOf(fileSeparator));
        } else {
            result = StringUtils.EMPTY;
        }
        if (!result.startsWith(fileSeparator)) {
            result = fileSeparator + result;
        }
        return result;
    }


    public static boolean mkDir(String dir) {
        File dirFile = new File(dir);
        if (dirFile.exists()) {
            return true;
        } else {
            try {
                Files.createParentDirs(new File(dir + "/temp"));
                return true;
            } catch (IOException var3) {
                Logger.error("创建[" + dir + "]目录失败", var3);
                return false;
            }
        }
    }

    public static boolean touch(String filePath) {
        File file = new File(filePath);
        return touch(file);
    }

    public static boolean touch(File file) {
        if (file.exists()) {
            return true;
        } else {
            try {
                Files.createParentDirs(file);
                Files.touch(file);
                return true;
            } catch (IOException var2) {
                Logger.error("创建[" + file + "]文件失败", var2);
                return false;
            }
        }
    }

    public static boolean deleteFileOrDir(String path) {
        if(StringUtils.isEmpty(path)){
            throw  new RuntimeException("传入目录路径不可以为空");
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        } else {
            boolean flag = true;
            if (file.isFile()) {
                flag = deleteFile(path);
            } else if (file.isDirectory()) {
                flag = deleteDir(path);
            }

            return flag;
        }
    }

    public static boolean deleteFile(String filePath) {
        if(StringUtils.isEmpty(filePath)){
            throw  new RuntimeException("传入目录路径不可以为空");
        }
        File file = new File(filePath);
        boolean flag = true;
        if (file.exists() && file.isFile()) {
            flag = file.delete();
        } else if (file.exists() && file.isDirectory()) {
            flag = false;
        }

        return flag;
    }

    public static boolean deleteDir(String dir) {
        if(StringUtils.isEmpty(dir)){
            throw  new RuntimeException("传入目录路径不可以为空");
        }
        if (!dir.endsWith("/") && !dir.endsWith("\\") && !dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }

        File file = new File(dir);
        boolean isSuccess = true;
        if (!file.exists()) {
            return true;
        } else {
            File[] var3 = file.listFiles();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File childFile = var3[var5];
                if (childFile.isFile()) {
                    isSuccess = deleteFile(childFile.getAbsolutePath());
                    if (!isSuccess) {
                        return isSuccess;
                    }
                } else if (childFile.isDirectory()) {
                    isSuccess = deleteDir(childFile.getAbsolutePath());
                    if (!isSuccess) {
                        return isSuccess;
                    }
                }
            }

            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static String compFileSeparator(String path) {
        if (!path.endsWith("/") && !path.endsWith("\\") && !path.endsWith(File.separator)) {
            path = path + File.separator;
        }

        return path;
    }

    public static void write(byte[] content, String toFilePath) throws IOException {
        write(content, new File(toFilePath));
    }

    public static void write(byte[] content, File toFile) throws IOException {
        touch(toFile);
        Files.write(content, toFile);
    }

    public static void write(String content, File toFile) throws IOException {
        write(content, toFile, "UTF-8");
    }

    public static void write(String content, File toFile, String charset) throws IOException {
        touch(toFile);
        Files.write(content, toFile, Charset.forName(charset));
    }

    public static void write(String content, String toFilePath) throws IOException {
        write(content, toFilePath, Charset.forName("UTF-8"));
    }

    public static void write(String content, String toFilePath, Charset charset) throws IOException {
        try {
            touch(toFilePath);
            Files.write(content, new File(toFilePath), charset);
        } catch (IOException var4) {
            throw var4;
        }
    }


}
