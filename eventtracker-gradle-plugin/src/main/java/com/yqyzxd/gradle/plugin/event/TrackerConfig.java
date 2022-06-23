package com.yqyzxd.gradle.plugin.event;

import java.util.List;

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

    private List<String> mExcludePackages;

    public TrackerConfig(Builder builder) {

        mExcludePackages = builder.excludePackages;
    }

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

    public static Builder newBuilder(){
        return new Builder();
    }

    public static class Builder {
        private List<String> excludePackages;

        public Builder excludePackages(List<String> excludePackages) {
            this.excludePackages = excludePackages;
            return this;
        }

        public TrackerConfig build() {
            return new TrackerConfig(this);
        }
    }

}
