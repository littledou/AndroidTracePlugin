package cn.idu.iwan.ext

import android.util.Log

class Sample {
    fun log() {
        val time_start = System.currentTimeMillis()
        val sampleCode = "samplecode"
        val time_end = System.currentTimeMillis()
        if (time_end > 10) {
            Log.d("tag", "msg: ${time_start - time_end}ms");
        }
    }
}