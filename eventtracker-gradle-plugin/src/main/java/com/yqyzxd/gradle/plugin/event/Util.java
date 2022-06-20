package com.yqyzxd.gradle.plugin.event;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: Util
 * Author: wind
 * Date: 2022/6/17 10:49
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 * <author> <time> <version> <desc>
 */
public class Util {

    public static boolean isRealZipOrJar(File input) {
        ZipFile zf = null;

        try {
            zf = new ZipFile(input);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(zf);
        }
        return false;

    }

    public static void closeQuietly(AutoCloseable target) {
        if (target == null) {
            return;
        }
        try {
            target.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
