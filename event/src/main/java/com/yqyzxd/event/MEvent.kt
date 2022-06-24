package com.yqyzxd.event

import android.content.Context

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: MEvent
 * Author: wind
 * Date: 2022/6/24 10:19
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */
class MEvent {

    companion object{
        @JvmStatic
        private var mAppContext:Context?=null

        @JvmStatic
        fun install(appContext: Context){
            mAppContext=appContext;
        }
        @JvmStatic
        fun onEvent(eventId:String){
            println("MEvent eventId:${eventId}")
        }

    }

}