package com.cradle.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.common_screen.CountryActivity
import com.cradle.model.ImageModel
import com.cradle.utils.SharaGoPref
import kotlinx.android.synthetic.main.pager_item.view.*

class MutimediaAdapter (
    private var context: Context,
    private var bannerList: List<String?>?,
    private var imageVideo: String,
    private var size: Int,
    private var string: String
) : RecyclerView.Adapter<MutimediaAdapter.HomeOffersViewHolder>(){

    private var list: List<ImageModel> = listOf()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        return HomeOffersViewHolder(parent)
    }


    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        val bannerList1 = bannerList!![position]

        holder.bind(bannerList1,context,position,size,imageVideo,string)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItem(list: List<ImageModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = bannerList!!.size

    class HomeOffersViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_multimedia,
                parent, false
            )
        )

        fun bind(
            bannerList1: String?,
            context: Context,
            position: Int,
            size: Int,
            imageVideo: String,
            string: String
        ) {

            if (bannerList1!!.isNotEmpty()) {
                if (imageVideo.equals("multimedia")) {
                    if (position == 0) {
                        Glide.with(context).load(R.drawable.multimedia_two)
                            .error(R.drawable.loading).into(itemView.offerImage)
                    }
                    if (position == 1) {
                        Glide.with(context).load(R.drawable.multimedia_one)
                            .error(R.drawable.loading).into(itemView.offerImage)
                    }
                } else {
                    if (position == 0) {
                        Glide.with(context).load(R.drawable.marketplace_one)
                            .error(R.drawable.loading).into(itemView.offerImage)
                    }
                    if (position == 1) {
                        Glide.with(context).load(R.drawable.marketplace_two)
                            .error(R.drawable.loading).into(itemView.offerImage)
                    }
                    if (position == 2) {
                        Glide.with(context).load(R.drawable.marketplace_three)
                            .error(R.drawable.loading).into(itemView.offerImage)
                    }
                }

            }
            itemView.offerImage.setOnClickListener {
                SharaGoPref.getInstance(context).setShowList(context.getString(R.string.multimedia))

                context.startActivity(
                    Intent(
                        context,
                        CountryActivity::class.java
                    ).putExtra("screen", "multimedia")
                )
            }
        }


    }
}


