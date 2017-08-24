package com.carko.carko.context

import android.app.Application
import android.content.Context

/**
 * Created by fabrice on 2017-08-10.
 */
class ApyaApp: Application() {

    companion object {
        private lateinit var mContext: Context

        fun getAppContext(): Context {
            return mContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
    }
}