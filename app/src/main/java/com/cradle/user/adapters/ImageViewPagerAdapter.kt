package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.utils.MyConstants
import kotlinx.android.synthetic.main.adapter_zoominout.view.*


class ImageViewPagerAdapter(
    private var context: Context,
    private var bannerList: ArrayList<String>?,
    private var imageVideo: String
) : RecyclerView.Adapter<ImageViewPagerAdapter.HomeOffersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        return HomeOffersViewHolder(parent)
    }
    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        //val bannerList1 = bannerList!![position]

        holder.bind(bannerList!!)
    }

    override fun getItemCount(): Int {
        if ((imageVideo.equals("video"))){
            return bannerList?.size!!
        }else{
            return bannerList?.size!!
        }
    }

    class HomeOffersViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_zoominout,
                parent, false
            )
        )

        fun bind(
             bannerList: List<String?>
        ) {
            Glide.with(itemView.context).load(MyConstants.file_Base_URL + bannerList.get(position).toString()).placeholder(R.drawable.loading)
                .into(itemView.image_zoom)
        }

        private fun shareUrl(context: Context,shareURl: String?) {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
            i.putExtra(Intent.EXTRA_TEXT, shareURl)
            context.startActivity(Intent.createChooser(i, "Share URL"))
        }

    }


}


