package com.wattpad.android.util

import java.io.File

object JsonConverter {

    fun getJsonFile(path: String): String {
        val uri = javaClass.classLoader?.getResource(path)
        val file = File(uri?.path!!)
        return String(file.readBytes())
    }
}