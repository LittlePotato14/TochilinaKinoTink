package com.example.tinkoff_kinopoisk.core

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream

object ImageConverter {
    fun bitmapToByteArray(b: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }
}