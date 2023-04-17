package com.cradle.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterCountryListBinding
import com.cradle.intarfaces.ItemClickListner
import com.cradle.model.allcountry.CountryListItem

class CountryAllAdapter(private val context: Context, private var wishList: List<CountryListItem?>?,
                        val clickListener: ItemClickListner, var listener:TextBookNow, private var screen: String) :
    RecyclerView.Adapter<CountryAllAdapter.MyViewHolder>() {


    @NonNull
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
        fun bind(wishList: CountryListItem?, position: Int){
          //  adapterUserAttractionMenuBinding.adapterCountryColorCodeListItem=wishList
            adapterUserAttractionMenuBinding.executePendingBindings()
           /* Glide.with(context).load(MyConstants.file_Base_URL_flag+wishList!!.flag).into(adapterUserAttractionMenuBinding.ivCountryImage)
*/
            adapterUserAttractionMenuBinding.ivCountryImage.visibility=android.view.View.GONE
            adapterUserAttractionMenuBinding.tvCountryName.text=wishList!!.countryName
            adapterUserAttractionMenuBinding.tvCountryName.setTextColor(context.getColor(R.color.black))

            adapterUserAttractionMenuBinding.releCountry.setOnClickListener {
                listener.bookSession(position,wishList)


            }
           /* if(screen.equals("country")){
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

            }*/

        }
    }
    interface TextBookNow{
        fun bookSession(position: Int, data: CountryListItem?)
    }
    override fun getItemCount(): Int {

        return if (wishList == null) 0 else wishList!!.size
    }
    fun filterList(filterdNames: ArrayList<CountryListItem>): ArrayList<CountryListItem> {
        this.wishList = filterdNames
        notifyDataSetChanged()
        return wishList as ArrayList<CountryListItem>
    }

}