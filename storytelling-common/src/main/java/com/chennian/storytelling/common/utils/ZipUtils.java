package com.chennian.storytelling.common.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 打包工具类
 *
 * @author by chennian
 * @date 2023/5/16 19:17
 */
public class ZipUtils {
    /**
     * 把一个目录打包到zip文件中的某目录
     *
     * @param dirpath 目录绝对地址
     */

    public static void packToolFiles(String dirpath, String zipFilePath) throws Exception {

        OutputStream out = null;
        BufferedOutputStream bos = null;
        ZipArchiveOutputStream zaos = null;
        File zipFile = new File(zipFilePath);
        if (!zipFile.getParentFile().exists()) {
            zipFile.getParentFile().mkdirs();
        }
        zipFile.delete();
        try {
            out = new FileOutputStream(zipFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bos = new BufferedOutputStream(out);
        zaos = new ZipArchiveOutputStream(bos);
        zaos.setEncoding("GBK");
        packToolFiles(zaos, dirpath, "");
        zaos.flush();
        zaos.close();
        bos.flush();
        bos.close();
        out.flush();
        out.close();
    }

    public static void packToolFilesWithPassword(String dirPath, String zipFilePath, String password) throws Exception {
        ZipFile zipFile = new ZipFile(zipFilePath);
        ZipParameters parameters = new ZipParameters();
        // 压缩方式
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);
        // 压缩级别
        parameters.setCompressionLevel(CompressionLevel.NORMAL);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
        zipFile.setPassword(password.toCharArray());
        //zipFile.addFolder(dirPath, parameters);
        // 要打包的文件夹
        File currentFile = new File(dirPath);
        File[] fs = currentFile.listFiles();
        // 遍历test文件夹下所有的文件、文件夹
        for (File f : fs) {
            if (f.isDirectory()) {
                zipFile.addFolder(f, parameters);
            } else {
                zipFile.addFile(f, parameters);
            }
        }
    }

    private static void packToolFiles(ZipArchiveOutputStream zaos, String dirpath, String pathName)
            throws FileNotFoundException, IOException {
        ByteArrayOutputStream tempbaos = new ByteArrayOutputStream();
        BufferedOutputStream tempbos = new BufferedOutputStream(tempbaos);
        File dir = new File(dirpath);
        // 返回此绝对路径下的文件
        File[] files = dir.listFiles();
        if (files == null || files.length < 1) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            // 判断此文件是否是一个文件夹
            if (files[i].isDirectory()) {
                packToolFiles(zaos, files[i].getAbsolutePath(), pathName + files[i].getName() + File.separator);
            } else {
                zaos.putArchiveEntry(new ZipArchiveEntry(pathName + files[i].getName()));
                IOUtils.copy(new FileInputStream(files[i].getAbsolutePath()), zaos);
                zaos.closeArchiveEntry();
            }
        }
        tempbaos.flush();
        tempbaos.close();
        tempbos.flush();
        tempbos.close();
    }

    public static void writeZip(StringBuffer sb, String zipname) throws IOException {

        String[] files = sb.toString().split(",");
        System.out.println(files);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(zipname + ".zip"));
        ZipOutputStream zos = new ZipOutputStream(os);
        byte[] buf = new byte[8192];
        int len;
        for (int i = 0; i < files.length; i++) {
            File file = new File(files[i]);
            if (!file.isFile())
                continue;
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            while ((len = bis.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            bis.close();
        }
        // zos.setEncoding("GBK");
        zos.closeEntry();
        zos.close();

        for (int i = 0; i < files.length; i++) {
            System.out.println("------------" + files);
            File file = new File(files[i]);
            file.delete();
        }
    }
}
