package com.gumaoqi.test.kotlinbaseproject.kt

import com.gumaoqi.test.kotlinbaseproject.tool.T

fun String.show() {
    if (this == null) {
        return
    }
    T.s(this)
}