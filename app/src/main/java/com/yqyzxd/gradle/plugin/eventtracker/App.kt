package com.yqyzxd.gradle.plugin.eventtracker

import android.app.Application
import com.yqyzxd.event.MEvent

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: App
 * Author: wind
 * Date: 2022/6/24 10:26
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MEvent.install(this)
    }
}