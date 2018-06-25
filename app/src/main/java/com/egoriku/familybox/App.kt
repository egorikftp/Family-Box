package com.egoriku.familybox

import android.app.Application
import com.egoriku.familybox.entity.MyObjectBox
import io.objectbox.BoxStore

class App : Application() {

    lateinit var boxStore: BoxStore
        private set

    override fun onCreate() {
        super.onCreate()

        boxStore = MyObjectBox.builder().androidContext(this).build()

    }
}