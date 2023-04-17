package com.cradle.user.adapters
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterUserAddressListBinding

import com.cradle.model.address.AddressListItem
import com.cradle.user.userActivity.UserAddAddressActivity
import com.cradle.utils.MyConstants
import kotlinx.android.synthetic.main.adapter_user_address_list.view.*

class UserAddressListAdapter (private val context: Context, private  var list:ArrayList<AddressListItem>?,private var textBook: TextBook) :
    RecyclerView.Adapter<UserAddressListAdapter.MyViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAddressListAdapter.MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserAddressListBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_address_list, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val addressList = list!![position]
        holder.bind(position,addressList)
    }
    inner class MyViewHolder(mBinding: AdapterUserAddressListBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int,data: AddressListItem?){

            if (data!!.isDefault!!){
                itemView.cv_container.setBackgroundResource(R.drawable.rac_rounded_green_new)
                itemView.ll_default.visibility=View.VISIBLE
            }
            itemView.tv_land_mark.text=data.metaData!!.addressType.toString()
            itemView.tv_land_title.text=data.metaData!!.landmark.toString()
            itemView.tv_name.text=data!!.metaData!!.firstName.toString()+" "+data.metaData!!.lastName.toString()
            itemView.tv_address.text=data.metaData.streetAddress.toString()+", "+ data.metaData.city+", "+ data.metaData.country
            itemView.tv_phone.text="Mobile : "+ data.metaData.phone.toString()

            itemView.iv_delete_add.setOnClickListener {
                textBook.deleteAddress(MyConstants.MARKDEFAULT_ADDRESS_REQ_CODE,data)
            }
            itemView.more_button.setOnClickListener {
                textBook.deleteAddress(MyConstants.MARKDEFAULT_ADDRESS_REQ_CODE,data)
            }
            itemView.tv_name.setOnClickListener {
                textBook.deleteAddress(MyConstants.MARKDEFAULT_ADDRESS_REQ_CODE,data)
            }

            itemView.iv_edit_address.setOnClickListener {
                Log.e("id",data.id.toString())
                context.startActivity(
                    Intent(context, UserAddAddressActivity::class.java).apply {
                        putExtra("data",data)
                            .putExtra("id",data.id.toString())
                    }
                )

            }

        }

    }
    override fun getItemCount(): Int {
        return list!!.size
    }
    interface TextBook{
        fun deleteAddress(position: Int,data: Any)
    }
}