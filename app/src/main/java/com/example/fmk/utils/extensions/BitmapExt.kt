package com.example.fmk.utils.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import androidx.annotation.ColorInt
import kotlin.math.min

fun Bitmap.size(size: Int): Bitmap {
    val scaleWidth = size / this.width.toFloat()
    val scaleHeight = size / this.height.toFloat()
    val scale =
        if (scaleWidth < 1 || scaleHeight < 1) maxOf(scaleWidth, scaleHeight)
        else minOf(scaleWidth, scaleHeight)


    val matrix = Matrix()
    matrix.postScale(scale, scale)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}

fun Bitmap.roundedCorner(corner: Float = 5f, borderWidth: Int = 5, @ColorInt borderColor: Int = Color.BLACK): Bitmap {
    // Bitmap myCoolBitmap = ... ; // <-- Your bitmap you
    // want rounded
    val w = this.width
    val h = this.height

    // We have to make sure our rounded corners have an
    // alpha channel in most cases
    val rounder = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(rounder)

    // We're going to apply this paint eventually using a
    // porter-duff xfer mode.
    // This will allow us to only overwrite certain pixels.
    // RED is arbitrary. This
    // could be any color that was fully opaque (alpha =
    // 255)
    val xferPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    xferPaint.color = Color.RED

    // We're just reusing xferPaint to paint a normal
    // looking rounded box, the 20.f
    // is the amount we're rounding by.
    canvas.drawRoundRect(RectF(0f, 0f, w.toFloat(), h.toFloat()), corner, corner, xferPaint)

    // Now we apply the 'magic sauce' to the paint
    xferPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    val result = Bitmap.createBitmap(this.width + borderWidth * 2, this.height + borderWidth * 2, Bitmap.Config.ARGB_8888)
    val resultCanvas = Canvas(result)
    resultCanvas.drawBitmap(this, borderWidth.toFloat(), borderWidth.toFloat(), null)
    resultCanvas.drawBitmap(rounder, borderWidth.toFloat(), borderWidth.toFloat(), xferPaint)

    if (borderWidth != 0) {
        xferPaint.run {
            xfermode = null
            style = Paint.Style.STROKE
            color = borderColor
            strokeWidth = borderWidth.toFloat()
        }
        resultCanvas.drawRoundRect(
            RectF(
                borderWidth.toFloat(),
                borderWidth.toFloat(),
                w.toFloat() + borderWidth,
                h.toFloat() + borderWidth
            ), corner, corner, xferPaint
        )
    }

    return result
}