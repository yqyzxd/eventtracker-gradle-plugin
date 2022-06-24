package com.yqyzxd.gradle.plugin.event;


import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: EventTracker
 * Author: wind
 * Date: 2022/6/17 13:54
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 * <author> <time> <version> <desc>
 */
public class EventTracker {
    private TrackerConfig mConfig;

    public EventTracker(TrackerConfig config) {
        this.mConfig = config;
    }

    public void track(Map<File, File> dirMap, Map<File, File> jarMap) {
        trackSrc(dirMap);
        trackJar(jarMap);
    }

    private void trackJar(Map<File, File> jarMap) {
        if (jarMap != null) {
            for (Map.Entry<File, File> entry : jarMap.entrySet()) {
                trackJar(entry.getKey(), entry.getValue());
            }
        }
    }

    private void trackJar(File inputJar, File outputJar) {
        ZipOutputStream zipOutputStream = null;
        ZipFile zipFile = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(outputJar));
            zipFile = new ZipFile(inputJar);
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                String zipEntryName = zipEntry.getName();
                if (!mConfig.exclude(zipEntryName)) {
                    InputStream inputStream = zipFile.getInputStream(zipEntry);

                    byte[] data = visit(inputStream);

                    InputStream byteArrayInputStream = new ByteArrayInputStream(data);
                    ZipEntry newZipEntry = new ZipEntry(zipEntryName);
                    Util.addZipEntry(zipOutputStream, newZipEntry, byteArrayInputStream);

                } else {
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    ZipEntry newZipEntry = new ZipEntry(zipEntryName);
                    Util.addZipEntry(zipOutputStream, newZipEntry, inputStream);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(zipOutputStream);
            Util.closeQuietly(zipFile);
        }
    }

    private byte[] visit(InputStream inputStream) throws IOException {
        //使用asm修改class
        ClassReader classReader = new ClassReader(inputStream);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new EventClassAdapter(Opcodes.ASM6, classWriter);
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

        return classWriter.toByteArray();
    }

    private void trackSrc(Map<File, File> dirMap) {
        if (dirMap != null) {
            for (Map.Entry<File, File> entry : dirMap.entrySet()) {
                trackSrc(entry.getKey(), entry.getValue());
            }
        }
    }

    private void trackSrc(File input, File output) {
        List<File> classFiles = new ArrayList<>();
        if (input.isDirectory()) {
            traverseClassFile(classFiles, input);
        } else {
            classFiles.add(input);
        }
        for (File classFile : classFiles) {
            InputStream is = null;
            FileOutputStream os = null;
            try {
                final String changedFileInputFullPath = classFile.getAbsolutePath();
                final File changedFileOutput = new File(changedFileInputFullPath.replace(input.getAbsolutePath(), output.getAbsolutePath()));
                if (!changedFileOutput.exists()) {
                    changedFileOutput.getParentFile().mkdirs();
                }
                changedFileOutput.createNewFile();

                if (!mConfig.exclude(classFile.getName())) {
                    is = new FileInputStream(classFile);

                    byte[] data = visit(is);
                    if (output.isDirectory()){
                        os = new FileOutputStream(changedFileOutput);
                    }else {
                        os = new FileOutputStream(output);
                    }
                    os.write(data);

                } else {
                    FileUtils.copyFile(classFile, changedFileOutput);
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Util.closeQuietly(is);
                Util.closeQuietly(os);
            }
        }


    }

    private void traverseClassFile(List<File> classFiles, File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file == null) {
                continue;
            }
            if (file.isDirectory()) {
                traverseClassFile(classFiles, file);
            } else {
                if (file != null && file.isFile()) {
                    classFiles.add(file);
                }
            }
        }
    }

}
