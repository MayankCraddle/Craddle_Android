package com.cradle.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterCountryListBinding
import com.cradle.intarfaces.ItemClickListner
import com.cradle.model.CountryColorCodeListItem
import com.cradle.utils.MyConstants
import com.cradle.utils.showToast

class AdapterCountryList (private val context: Context, private var wishList: List<CountryColorCodeListItem?>?,val clickListener: ItemClickListner, var listener:TextBookNow,private var screen: String) :
    RecyclerView.Adapter<AdapterCountryList.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterCountryListBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_country_list, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
           val list = wishList!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val adapterUserAttractionMenuBinding: AdapterCountryListBinding) :
        RecyclerView.ViewHolder(adapterUserAttractionMenuBinding.root) {
        @SuppressLint("NewApi")
        fun bind(wishList: CountryColorCodeListItem?, position: Int){
              adapterUserAttractionMenuBinding.adapterCountryColorCodeListItem=wishList
              adapterUserAttractionMenuBinding.executePendingBindings()
            Log.e("flag",wishList!!.flag.toString())
            Glide.with(context).load(MyConstants.file_Base_URL_flag+wishList!!.flag).into(adapterUserAttractionMenuBinding.ivCountryImage)

            if(screen.equals("country")){
                if (wishList.active!!){
                    adapterUserAttractionMenuBinding.tvCountryName.setTextColor(context.getColor(R.color.black))

                }else{
                    adapterUserAttractionMenuBinding.tvCountryName.setTextColor(context.getColor(R.color.light_grey))

                }
                adapterUserAttractionMenuBinding.releCountry.setOnClickListener {
                    if (wishList.active){

                        listener.bookSession(position,wishList)
                    }else{
                        context.showToast(context.getString(R.string.We_will_be_soon))
                    }

                }
            }else{
                adapterUserAttractionMenuBinding.tvCountryName.setTextColor(context.getColor(R.color.black))
                adapterUserAttractionMenuBinding.releCountry.setOnClickListener {
                        listener.bookSession(position,wishList)


                }
            }

        }
    }
    interface TextBookNow{
        fun bookSession(position: Int,data: CountryColorCodeListItem)
    }
    override fun getItemCount(): Int {
        return wishList!!.size
    }
    fun filterList(filterdNames: ArrayList<CountryColorCodeListItem>): ArrayList<CountryColorCodeListItem> {
        this.wishList = filterdNames
        notifyDataSetChanged()
        return wishList as ArrayList<CountryColorCodeListItem>
    }
}