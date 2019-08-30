package net.onebean.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 文件压缩工具类
 * @author 0neBean
 */
public class TarFileUtil {

    private final static Logger logger = LoggerFactory.getLogger(TarFileUtil.class);

    public static void compress(List<String> paths, String target) {
        File outputFile = new File(target);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
             CompressorOutputStream gzippedOut = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, fileOutputStream);
             TarArchiveOutputStream taos = new TarArchiveOutputStream(gzippedOut)) {
            for (String path : paths) {
                putFile(path, taos, "");
            }
            taos.close();
        } catch (IOException | CompressorException e) {
            logger.error("compress error", e);
        }
    }

    private static void putFile(String path, TarArchiveOutputStream outputStream, String prefix) throws IOException {
        File inputFile = new File(path);
        TarArchiveEntry tae = new TarArchiveEntry(inputFile, prefix + inputFile.getName());
        outputStream.putArchiveEntry(tae);
        if (inputFile.isDirectory()) {
            logger.info("compressing directory: " + path);
            File[] files = inputFile.listFiles();
            if (null == files || files.length == 0) {
                outputStream.closeArchiveEntry();
                return;
            }
            for (File file : files) {
                putFile(file.getAbsolutePath(), outputStream, prefix + inputFile.getName() + "/");
            }
        } else {
            logger.info("compressing file: " + path);
            outputStream.write(FileUtils.readFileToByteArray(inputFile));
            outputStream.closeArchiveEntry();
        }
    }


}
