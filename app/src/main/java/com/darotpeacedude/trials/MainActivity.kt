package com.darotpeacedude.trials

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darotpeacedude.trials.databinding.ActivityMainBinding
import com.darotpeacedude.trials.databinding.AnotherTestLayoutBinding
import com.darotpeacedude.trials.databinding.FacebooAdaptedLayoutBinding
import com.darotpeacedude.trials.databinding.NewTestLayoutBinding
import kotlin.concurrent.thread

const val ANIMATION_CYCLE_DURATION = 750

class MainActivity : AppCompatActivity() {
    @SuppressLint("Recycle")
    lateinit var bindingUtil: ActivityMainBinding
    val rcvAdapter = RecyclerViewAdapter()
    var drawable: Drawable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingUtil = DataBindingUtil.setContentView(this, R.layout.activity_main)

        drawable = ContextCompat.getDrawable(this, R.drawable.ic_baseline_airport_shuttle_24)
//
//        bindingUtil.rcv.let {
//            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//            it.adapter = adapter
//        }


//        bindingUtil.apply {
//            txtNamea.text = "This is really fun"
//            txtNamea1.text = "This has to be fun"
//            txtNamea2.text = "This is so fun"
//        }

//        thread {
//            Thread.sleep(4000)
//            runOnUiThread {
//                bindingUtil.image.toShime = false
//                bindingUtil.image.srcDrawable = ContextCompat.getDrawable(this, R.drawable.ic_baseline_airport_shuttle_24)
//            }
//        }

        loadData()

    }

    private fun loadData() {
        bindingUtil.rcv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false )
            adapter = rcvAdapter
        }
        rcvAdapter.setData(
            listOf(
                TestClass(
                    drawable,
                    "This is what I wanna show"
                ),
                TestClass(
                    drawable,
                    "This is what I wanna show"
                ),
                TestClass(
                    drawable,
                    "This is what I wanna show"
                ),
                TestClass(
                    drawable,
                    "This is what I wanna show"
                ),
                TestClass(
                    drawable,
                    "This is what I wanna show"
                ),
                TestClass(
                    drawable,
                    "This is what I wanna show"
                )
            )
        )


    }

//    private fun shineAnimation() {
//        // attach the animation layout Using AnimationUtils.loadAnimation
//        val anim = AnimationUtils.loadAnimation(this, R.anim.left_right)
//        bindingUtil.shine.startAnimation(anim)
//        // override three function There will error
//        // line below the object
//        // click on it and override three functions
//        anim.setAnimationListener(object : Animation.AnimationListener {
//            // This function starts the
//            // animation again after it ends
//            override fun onAnimationEnd(p0: Animation?) {
//                bindingUtil.shine.startAnimation(anim)
//            }
//
//            override fun onAnimationStart(p0: Animation?) {}
//
//            override fun onAnimationRepeat(p0: Animation?) {}
//
//        })
//    }

    override fun onResume() {
        super.onResume()

    }

}


