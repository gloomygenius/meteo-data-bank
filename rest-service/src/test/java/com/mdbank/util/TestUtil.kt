package com.mdbank.util

import org.apache.commons.io.IOUtils
import java.io.InputStream
import java.nio.charset.StandardCharsets

object ResourceLoader {
    fun getResourceAsStream(path: String): InputStream {
        return this.javaClass.classLoader
                .getResourceAsStream(path)
    }

    fun getResourceAsString(path: String): String {
        return this.javaClass.classLoader
                .getResourceAsStream(path).use { IOUtils.toString(it, StandardCharsets.UTF_8) }
    }
}
