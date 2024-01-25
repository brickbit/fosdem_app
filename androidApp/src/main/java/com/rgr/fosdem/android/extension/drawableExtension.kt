package com.rgr.fosdem.android.extension

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap

fun Drawable?.splitImage(): Pair<Bitmap, Bitmap>? {
    val bitmap = this?.toBitmap()
    bitmap?.let {
        val leftBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width/2, bitmap.height)
        val rightBitmap = Bitmap.createBitmap(bitmap, bitmap.width/2, 0, bitmap.width/2, bitmap.height)
        return  Pair(leftBitmap, rightBitmap)
    } ?: return null
}