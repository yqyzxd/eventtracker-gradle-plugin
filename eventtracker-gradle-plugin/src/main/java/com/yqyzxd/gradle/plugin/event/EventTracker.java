package com.yqyzxd.gradle.plugin.event;

import com.android.tools.r8.w.F;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public EventTracker(TrackerConfig config){
        this.mConfig=config;
    }

    public void track(Map<File, File> dirMap, Map<File, File> jarMap) {
        trackSrc(dirMap);
        trackJars(jarMap);
    }

    private void trackJar(Map<File, File> jarMap) {

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
        }else {
            classFiles.add(input);
        }
        for (File classFile:classFiles) {
            InputStream is=null;
            FileOutputStream os=null;
            try {
                final String changedFileInputFullPath=classFile.getAbsolutePath();
                final File changedFileOutput=new File(changedFileInputFullPath.replace(input.getAbsolutePath(),output.getAbsolutePath()))
                if (!changedFileOutput.exists()) {
                    changedFileOutput.getParentFile().mkdirs();
                }
                changedFileOutput.createNewFile();

                if (!mConfig.exclude(classFile.getName())){



                }else {
                    FileUtils.copyFile(classFile,changedFileOutput);
                }


            }catch (Exception e){
                e.printStackTrace();
            }finally {
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
