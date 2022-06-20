package com.yqyzxd.gradle.plugin.event;

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: TrackerConfig
 * Author: wind
 * Date: 2022/6/17 14:21
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 * <author> <time> <version> <desc>
 */
public class TrackerConfig {

    public static final String[] EXCLUDE_CLASS = {"R.class", "R$", "Manifest", "BuildConfig"};
    public static final String[] EXCLUDE_PACKAGE = {"android", "androidx"};


    public boolean exclude(String fileName) {
        boolean exclude = false;
        if (fileName.endsWith(".class")) {
            for (String excludeClass : EXCLUDE_CLASS) {
                if (fileName.contains(excludeClass)) {
                    exclude = true;
                    break;
                }
            }
        } else {
            exclude = true;
        }
        return exclude;

    }

}
