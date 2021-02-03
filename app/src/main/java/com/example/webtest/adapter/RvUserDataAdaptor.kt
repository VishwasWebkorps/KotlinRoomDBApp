package com.example.webtest.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.webtest.R


import java.util.*

class RvUserDataAdaptor(
    private val mContext: Context,
    private val mActivity: Activity

) : RecyclerView.Adapter<RvUserDataAdaptor.MyHolder>() {

    private var holder: MyHolder? = null
    var mClickListener:View.OnClickListener?=null


    override fun onCreateViewHolder(
        viewGroup: ViewGroup, i: Int
    ): MyHolder {
        val inflater = LayoutInflater.from(mContext)

//        View v = inflater.inflate(R.layout.fruitcard,viewGroup,
//                false);
        val v: View = inflater.inflate(
            R.layout.userlist_item, viewGroup,
            false
        )
        holder = MyHolder(v)
        return holder as MyHolder
    }

    override fun onBindViewHolder(
        myHolder: MyHolder, position: Int
    ) {
     //   holder!!.userCheck.setTag(position)
       // holder!!.userCheck.setOnClickListener(mClickListener)
    }

    override fun getItemCount(): Int {
        return 10
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    inner class MyHolder(v: View) : RecyclerView.ViewHolder(v) {
       // var tvUserName: TextView

      //  var userImage : ImageView

        init {
       //     userImage = v.findViewById(R.id.ivItem)

           // tvUserName = v.findViewById(R.id.tvUserName)
        }
    }

    init {
       // this.mUserData = userData

    }
}