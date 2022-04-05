package com.darotpeacedude.trials

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.darotpeacedude.trials.Constants.COLOR_DEFAULT_GRADIENT
import com.darotpeacedude.trials.Constants.CORNER_DEFAULT
import com.darotpeacedude.trials.LoaderConstant.MAX_WEIGHT

private const val MAX_COLOR_CONSTANT_VALUE = 255

class SampleShimmerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?=null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), AnimatorUpdateListener, LoaderView {
    private var linearGradient: LinearGradient? = null
    private var valueAnimator: ValueAnimator? = null
    private var progress = 0f
    var widthWeight = MAX_WEIGHT
    var heightWeight = MAX_WEIGHT
    var useGradient = Constants.USE_GRADIENT_DEFAULT
    var corners = CORNER_DEFAULT
    var rectPaint: Paint? = null
    var darkerColorResource = 0
    var defaultColorResource = 0
    var enableShimmer = false

    init {
        initialize(attrs)
    }
    private fun initialize(attrs: AttributeSet?){
//        loaderController = LoaderController(this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SampleShimmerView, 0, 0)
        widthWeight = validateWeight(typedArray.getFloat(
            R.styleable.SampleShimmerView_width_wt,
            LoaderConstant.MAX_WEIGHT
        ))

        heightWeight = validateWeight(
            typedArray.getFloat(
                R.styleable.SampleShimmerView_height_wt,
                LoaderConstant.MAX_WEIGHT
            )
        )
        useGradient = typedArray.getBoolean(
            R.styleable.SampleShimmerView_use_gradt,
            LoaderConstant.USE_GRADIENT_DEFAULT
        )
        corners = typedArray.getInt(R.styleable.SampleShimmerView_corner, Constants.CORNER_DEFAULT)
        defaultColorResource = typedArray.getColor(
            R.styleable.SampleShimmerView_shimmer_color, ContextCompat.getColor(
                context, R.color.purple_200
            )
        )
        darkerColorResource = typedArray.getColor(
            R.styleable.SampleShimmerView_shimmer_color, ContextCompat.getColor(
                context, R.color.purple_700
            )
        )
        enableShimmer = typedArray.getBoolean(
            R.styleable.SampleShimmerView_enable_shimmer, false
        )
        initiate()
        typedArray.recycle()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        linearGradient = null
//        startLoading()
    }

    private fun startLoading() {
        if (valueAnimator != null && !valueSet()) {
            valueAnimator?.cancel()
            initiate()
            valueAnimator?.start()
        }
    }


    override fun valueSet(): Boolean {
        return !TextUtils.isEmpty(text)
    }

    private fun initiate() {
        if (enableShimmer){
            rectPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
            rectPaint?.let { setRectColor(it) }
            setValueAnimator(0.5f, 1f, ObjectAnimator.INFINITE)
        }

    }

    override fun setRectColor(rectPaint: Paint) {
        val typeface = typeface
        if (typeface != null && typeface.style == Typeface.BOLD) {
            rectPaint.color = darkerColorResource
        } else {
            rectPaint.color = defaultColorResource
        }
    }

    private fun setValueAnimator(fl: Float, fl1: Float, infinite: Int) {
        valueAnimator = ValueAnimator.ofFloat(fl, fl1)
        valueAnimator?.repeatCount = infinite
        valueAnimator?.duration = ANIMATION_CYCLE_DURATION.toLong()
        valueAnimator?.repeatMode = ValueAnimator.REVERSE
        valueAnimator?.interpolator = LinearInterpolator()
        valueAnimator?.addUpdateListener(this)
    }

    fun resetLoader() {
        if (!TextUtils.isEmpty(text)) {
            super.setText(null)
            startLoading()
        }
    }

    @SuppressLint("WrongCall")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            onDraw(
                canvas, compoundPaddingLeft.toFloat(),
                compoundPaddingTop.toFloat(),
                compoundPaddingRight.toFloat(),
                compoundPaddingBottom.toFloat()
            )
        }
    }

    private fun onDraw(
        canvas: Canvas,
        left_pad: Float,
        top_pad: Float,
        right_pad: Float,
        bottom_pad: Float
    ) {
        val margin_height = canvas.height * (1 - heightWeight) / 2
        rectPaint?.alpha = (progress * 255).toInt()
        if (useGradient) {
            prepareGradient(canvas.width * widthWeight)
        }
        rectPaint?.let {
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


    private fun validateWeight(weight: Float): Float {
        if (weight > MAX_WEIGHT) return MAX_WEIGHT
        return if (weight < LoaderConstant.MIN_WEIGHT) LoaderConstant.MIN_WEIGHT else weight
    }

    private fun prepareGradient(fl: Float) {
        if (linearGradient == null) {
            linearGradient = rectPaint?.color?.let {
                LinearGradient(
                    0f, 0f, fl, 0f, it,
                    COLOR_DEFAULT_GRADIENT, Shader.TileMode.MIRROR
                )
            }
        }
        rectPaint?.shader = linearGradient
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        stopLoading()
    }

    private fun stopLoading() {
        if (valueAnimator != null) {
            valueAnimator?.cancel()
            setValueAnimator(progress, 0f, 0)
            valueAnimator?.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeAnimatorUpdateListener()
    }

    private fun removeAnimatorUpdateListener() {
        if (valueAnimator != null) {
            valueAnimator?.removeUpdateListener(this)
            valueAnimator?.cancel()
        }
        progress = 0f
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        progress = valueAnimator?.animatedValue as Float
        invalidate()
    }
}