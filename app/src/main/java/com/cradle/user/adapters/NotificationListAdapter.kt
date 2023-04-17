package com.cradle.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterNotificationBinding
import com.cradle.databinding.AdapterStateListBinding
import com.cradle.intarfaces.ItemClickListner
import com.cradle.model.NotificationListItem
import com.cradle.model.allcountry.CountryListItem
import com.cradle.utils.MyConstants

class NotificationListAdapter(private val context: Context, private var notificationList: List<NotificationListItem?>

) :
    RecyclerView.Adapter<NotificationListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterNotificationBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_notification, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = notificationList[position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterNotificationBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        @SuppressLint("NewApi")
        fun bind(stateList: NotificationListItem?, position: Int){
            mBinding.executePendingBindings()

            mBinding.tvNoticationTitle.text=stateList!!.title.toString()
            //  mBinding.tvCountryName.text=wishList!!.countryName
            mBinding.tvNotificationDis.text=stateList.body
            mBinding.tvNotificationTime.text=stateList.date




        }
    }
    interface TextBookNow{
        fun bookSession(position: Int, data: CountryListItem?)
    }
    override fun getItemCount(): Int {
        return notificationList.size
    }


}