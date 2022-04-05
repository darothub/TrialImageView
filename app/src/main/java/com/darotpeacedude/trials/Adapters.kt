package com.darotpeacedude.trials

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.core.view.doOnDetach
import androidx.databinding.BindingAdapter


@BindingAdapter("widthWeight")
fun setWidthWeight(view: SampleShimmerView, weight: Float) {
    val result = validateWeight(weight)
    view.widthWeight = result
}

@BindingAdapter("heightWeight")
fun setHeightWeight(view: SampleShimmerView, weight: Float) {
    val result = validateWeight(weight)
    view.heightWeight = result
}

@BindingAdapter("startLoading")
fun enableShimmer(view: SampleShimmerView, startLoading: Boolean) {
    if (startLoading) {
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1000f, view.context.resources.displayMetrics)
        val d = ScrollingGradient(px)
        view.background = d
        d.start()
//        val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
//        rectPaint.color = Color.GREEN
//        val colors = intArrayOf(Color.GRAY, Color.WHITE)
//        val gd = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
//        val valueAnimator: ValueAnimator = ValueAnimator.ofFloat(0.5f, 1f)
//        valueAnimator.repeatCount = ObjectAnimator.INFINITE
//        valueAnimator.duration = ANIMATION_CYCLE_DURATION.toLong()
//        valueAnimator.repeatMode = ValueAnimator.REVERSE
//        valueAnimator.interpolator = LinearInterpolator()
//        val objectAnimator = ObjectAnimator.ofFloat(view, "x")
//        valueAnimator.addUpdateListener {
//            val progress = it.animatedValue as Float
//            rectPaint.alpha = (progress * 255).toInt()
//            if (!TextUtils.isEmpty(view.text)){
//                view.background = null
//            }
//            else{
//
//                view.setBackgroundDrawable(gd)
//            }
//
//
//
//        }
//        valueAnimator.start()


    }
}

fun SampleShimmerView.initiate() {
    val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    setRectColor(rectPaint, this)
    AnimatorHandler.setValueAnimator(ObjectAnimator.INFINITE)
    onDraw(
        Canvas(), compoundPaddingLeft.toFloat(),
        compoundPaddingTop.toFloat(),
        compoundPaddingRight.toFloat(),
        compoundPaddingBottom.toFloat(),
        rectPaint
    )

}

class AnimatorHandler {

    companion object : ValueAnimator.AnimatorUpdateListener {
        var progress = 0f
        val valueAnimator: ValueAnimator = ValueAnimator.ofFloat(0.5f, 1f)
        fun setValueAnimator(infinite: Int) {
            valueAnimator.repeatCount = infinite
            valueAnimator.duration = ANIMATION_CYCLE_DURATION.toLong()
            valueAnimator.repeatMode = ValueAnimator.REVERSE
            valueAnimator.interpolator = LinearInterpolator()
        }

        fun removeValueAnimator() {
            valueAnimator.removeUpdateListener(this)
            valueAnimator.cancel()
            progress = 0f
        }

        override fun onAnimationUpdate(animation: ValueAnimator?) {
            progress = valueAnimator.animatedValue as Float
        }
    }


}

fun setRectColor(rectPaint: Paint, view: SampleShimmerView) {
    val typeface = view.typeface
    if (typeface != null && typeface.style == Typeface.BOLD) {
        rectPaint.color = ContextCompat.getColor(
            view.context, R.color.purple_700
        )
    } else {
        rectPaint.color = ContextCompat.getColor(
            view.context, R.color.purple_200
        )
    }
}

@BindingAdapter("removeAnimation")
fun removeAnimation(view: SampleShimmerView, removeAnimation: Boolean) {
    if (removeAnimation) {
        view.doOnDetach {
            AnimatorHandler.removeValueAnimator()
        }
    }
}


fun SampleShimmerView.onDraw(
    canvas: Canvas,
    left_pad: Float,
    top_pad: Float,
    right_pad: Float,
    bottom_pad: Float,
    paint: Paint
) {
    val margin_height = canvas.height * (1 - heightWeight) / 2
    paint?.let {
        canvas.drawRoundRect(
            RectF(
                0 + left_pad,
                margin_height + top_pad,
                canvas.width * widthWeight - right_pad,
                canvas.height - margin_height - bottom_pad
            ),
            corners.toFloat(), corners.toFloat(),
            it
        )
    }
}

private fun SampleShimmerView.prepareGradient(fl: Float, linearGradient: LinearGradient? = null) {
    if (linearGradient == null) {
        rectPaint?.color?.let {
            LinearGradient(
                0f, 0f, fl, 0f, it,
                Constants.COLOR_DEFAULT_GRADIENT, Shader.TileMode.MIRROR
            )
        }
    }
    rectPaint?.shader = linearGradient
}

@BindingAdapter("draw")
fun setDraw(view: SampleShimmerView, canvas: Canvas) {
    view.draw(canvas)
}

fun validateWeight(weight: Float): Float {
    if (weight > LoaderConstant.MAX_WEIGHT) return LoaderConstant.MAX_WEIGHT
    return if (weight < LoaderConstant.MIN_WEIGHT) LoaderConstant.MIN_WEIGHT else weight
}