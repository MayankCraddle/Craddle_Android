package com.cradle.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterStateListBinding
import com.cradle.intarfaces.ItemClickListner
import com.cradle.intarfaces.clickItem
import com.cradle.model.allcountry.CountryListItem
import com.cradle.utils.MyConstants

class CityAdapter(private val context: Context, private var cityList: ArrayList<String?>?,
                  private  val clickListener: clickItem
) :
    RecyclerView.Adapter<CityAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterStateListBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_state_list, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = cityList!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterStateListBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        @SuppressLint("NewApi")
        fun bind(cityListitem: String?, position: Int){
            mBinding.executePendingBindings()

            mBinding.ivCountryImage.visibility=android.view.View.GONE
            //  mBinding.tvCountryName.text=wishList!!.countryName
            mBinding.tvCountryName.setTextColor(context.getColor(R.color.black))
            mBinding.tvCountryName.text=cityListitem.toString()

            mBinding.releState.setOnClickListener {
                clickListener.onClickItem(position, MyConstants.CITY_REQ_CODE,cityListitem.toString())
            }
        }
    }
    interface TextBookNow{
        fun bookSession(position: Int, data: CountryListItem?)
    }
    override fun getItemCount(): Int {
        return cityList!!.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterCity(filterdNames: ArrayList<String?>?): ArrayList<String?>? {
        this.cityList = filterdNames
        notifyDataSetChanged()
        return cityList
    }


}