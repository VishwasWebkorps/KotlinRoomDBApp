package com.example.webtest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webtest.R
import com.example.webtest.adapter.RvUserDataAdaptor
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_userlist.*

class UserListFragment : Fragment() {

     var rootView:View?=null
    lateinit var userDataAdaptor : RvUserDataAdaptor
    var rvUserList:RecyclerView?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_userlist, container, false)
        rvUserList = rootView!!.findViewById(R.id.rvUserList)
        setUserData()
        return rootView
    }
    private fun setUserData(){

        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        userDataAdaptor = RvUserDataAdaptor(activity!!, activity!!)
        rvUserList!!.setLayoutManager(layoutManager)
        rvUserList!!.setAdapter(userDataAdaptor)
        rvUserList!!.setHasFixedSize(true)
    }
}