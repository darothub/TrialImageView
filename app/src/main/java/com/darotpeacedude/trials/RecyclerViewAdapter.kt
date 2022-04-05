package com.darotpeacedude.trials

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.darotpeacedude.trials.databinding.TrialLayoutBinding
import kotlin.concurrent.thread

class RecyclerViewAdapter() : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewVH>() {
    var list = ArrayList<TestClass>()
    inner class RecyclerViewVH(val view: TrialLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bindTo(value:TestClass) {


            Handler(Looper.getMainLooper()).postDelayed({
                value.let{str->
                    view.apply {
                        image.toShime = false
                        image.background =value.drawable
                    }
                }

            }, 5000)

            view.btnReset.setOnClickListener {
//                view.txtEmail.resetLoader()
//                view.txtName.resetLoader()
//                view.txtPhone.resetLoader()
//                view.txtTitle.resetLoader()
//                notifyDataSetChanged()
            }

        }
    }
    fun setData(newList: List<TestClass>?) {

        if (newList != null) {
            list.addAll(newList)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewVH {
        val binding = TrialLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewVH(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewVH, position: Int) {
        val item = list[position]
        holder.bindTo(item)
    }

    override fun getItemCount(): Int = list.size
}

interface ItemClickListener {
    fun clickSomething()
}