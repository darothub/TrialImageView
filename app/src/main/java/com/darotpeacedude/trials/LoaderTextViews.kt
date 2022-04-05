package com.darotpeacedude.trials

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

class LoaderTextViews @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?, defStyleAttr: Int
) : AppCompatTextView(context, attrs, defStyleAttr), LoaderView {
    private var loaderController: LoaderController? = null
    private var linearGradient: LinearGradient? = null
    private var valueAnimator: ValueAnimator? = null
    private var progress = 0f
    var widthWeight = 0f
    var heightWeight = 0f
    var useGradient = Constants.USE_GRADIENT_DEFAULT
    var corners = 0
    var rectPaint: Paint? = null
    var darkerColorResource = 0
    var defaultColorResource = 0

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        loaderController = LoaderController(this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.loader_view, 0, 0)
        loaderController?.setWidthWeight(
            typedArray.getFloat(
                R.styleable.loader_view_width_weight,
                LoaderConstant.MAX_WEIGHT
            )
        )
        loaderController?.setHeightWeight(
            typedArray.getFloat(
                R.styleable.loader_view_height_weight,
                LoaderConstant.MAX_WEIGHT
            )
        )
        loaderController?.setUseGradient(
            typedArray.getBoolean(
                R.styleable.loader_view_use_gradient,
                LoaderConstant.USE_GRADIENT_DEFAULT
            )
        )
        loaderController?.setCorners(typedArray.getInt(R.styleable.loader_view_corners, Constants.CORNER_DEFAULT))
        defaultColorResource = typedArray.getColor(
            R.styleable.loader_view_custom_color, ContextCompat.getColor(
                context, R.color.purple_200
            )
        )
        darkerColorResource = typedArray.getColor(
            R.styleable.loader_view_custom_color, ContextCompat.getColor(
                context, R.color.purple_700
            )
        )
        typedArray.recycle()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        loaderController!!.onSizeChanged()
    }

    fun resetLoader() {
        if (!TextUtils.isEmpty(text)) {
            super.setText(null)
            loaderController!!.startLoading()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        loaderController!!.onDraw(
            canvas, compoundPaddingLeft.toFloat(),
            compoundPaddingTop.toFloat(),
            compoundPaddingRight.toFloat(),
            compoundPaddingBottom.toFloat()
        )
    }

    override fun setText(text: CharSequence, type: BufferType) {
        super.setText(text, type)
        if (loaderController != null) {
            loaderController!!.stopLoading()
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

    override fun valueSet(): Boolean {
        return !TextUtils.isEmpty(text)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loaderController!!.removeAnimatorUpdateListener()
    }
}
