package com.darotpeacedude.trials

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class ShimmerImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?=null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val shimmerDrawable = ShimmerDrawable()
    private var shimmerStarted = false
    var toShime = false
    var srcDrawable: Drawable?=null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShimmerImageView, 0, 0)
        toShime = typedArray.getBoolean(
            R.styleable.ShimmerImageView_shime,
            toShime
        )
        shimmerDrawable.callback = this
        this.setWillNotDraw(false)

        val builder = Shimmer.AlphaHighlightBuilder()
            .setClipToChildren(false)
            .setDropoff(1f)
            .setDuration(4000)
            .setRepeatDelay(0)
            .setBaseAlpha(0.1f)
            .setHighlightAlpha(0.2f)
        shimmerDrawable.setShimmer(builder.build())
        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shimmerDrawable.setBounds(0, 0, width, height)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (shimmerStarted && toShime && canvas != null){
            shimmerDrawable.draw(canvas)
//            invalidate()
        }
        else{
            stopShimmer()
        }
    }

    override fun verifyDrawable(dr: Drawable): Boolean {
        return super.verifyDrawable(dr) || dr == shimmerDrawable
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isShown && toShime) startShimmer()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE && toShime) startShimmer() else stopShimmer()
        invalidate()
    }

    private fun stopShimmer() {
        if (shimmerStarted && srcDrawable != null){
            background = srcDrawable
            shimmerStarted = false
            setLayerType(View.LAYER_TYPE_NONE, null)
            shimmerDrawable.stopShimmer()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startShimmer() {
        if(ValueAnimator.areAnimatorsEnabled() && !shimmerStarted){
            shimmerStarted = true
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            shimmerDrawable.startShimmer()
        }
    }
}