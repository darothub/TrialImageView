package com.darotpeacedude.trials

import android.animation.TimeAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import androidx.core.graphics.alpha

class ScrollingGradient(private val pixelsPerSecond: Float) : Drawable(), Animatable, TimeAnimator.TimeListener {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private var x: Float = 0.toFloat()
    private val animator = TimeAnimator()
    private var progress = 0f
    init {

    }

    override fun onBoundsChange(bounds: Rect) {


        val lg =  LinearGradient(
            -bounds.width().toFloat(),
            0f,
            0f,
            0f,
            intArrayOf(
                Color.GREEN,
                Color.WHITE,
                Color.GREEN
            ),
            floatArrayOf(
                0f,
                1f,
                1f
            ),
            Shader.TileMode.MIRROR
        )
        val mat = Matrix()
        mat.setTranslate(-50f, 0f)
        lg.setLocalMatrix(mat)
        paint.shader = lg
    }

    override fun draw(canvas: Canvas) {
        canvas.clipRect(bounds)
        canvas.translate(x, 0f)
        canvas.drawPaint(paint)
    }

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    override fun getOpacity(): Int = PixelFormat.TRANSPARENT

    override fun start() {
        animator?.repeatMode = ValueAnimator.REVERSE
        animator?.interpolator = LinearInterpolator()
        animator.setTimeListener(this)
        animator.start()
    }

    override fun stop() {
        animator.cancel()
    }

    override fun isRunning(): Boolean = animator.isRunning

    override fun onTimeUpdate(animation: TimeAnimator, totalTime: Long, deltaTime: Long) {
        x = pixelsPerSecond * totalTime / 2500
        invalidateSelf()
    }
}