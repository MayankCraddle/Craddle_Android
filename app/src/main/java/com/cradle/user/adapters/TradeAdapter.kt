package com.cradle.user.adapters
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.user.userActivity.UserVendorListActivity
import kotlinx.android.synthetic.main.pager_item.view.*

class TradeAdapter(
    private var context: Context,
    private var bannerList: ArrayList<Any?>?,

    ) : RecyclerView.Adapter<TradeAdapter.HomeOffersViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        return HomeOffersViewHolder(parent)
    }


    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        val bannerList1 = bannerList!![position]

        holder.bind(bannerList1?.toString(),context,position)
    }


    override fun getItemCount(): Int = bannerList!!.size

    class HomeOffersViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_trade,
                parent, false
            )
        )

        fun bind(
            bannerList1: String?,
            context: Context,
            position: Int
        ) {

            if (bannerList1!!.isNotEmpty()) {

                if (position == 0) {
                    Glide.with(context).load(R.drawable.slide_one_two)
                        .error(R.drawable.loading).into(itemView.offerImage)
                }
                if (position == 1) {
                    Glide.with(context).load(R.drawable.slide_two_two)
                        .error(R.drawable.loading).into(itemView.offerImage) }
                if (position == 2) {
                    Glide.with(context).load(R.drawable.slider_three_two)
                        .error(R.drawable.loading).into(itemView.offerImage)
                }
                if (position == 3) {
                    Glide.with(context).load(R.drawable.slider4)
                        .error(R.drawable.loading).into(itemView.offerImage)
                }
                if (position == 4) {
                    Glide.with(context).load(R.drawable.slider5)
                        .error(R.drawable.loading).into(itemView.offerImage)
                }



                itemView.offerImage.setOnClickListener {
                    context.startActivity(Intent(context, UserVendorListActivity::class.java).putExtra("WichVendor",context.getString(R.string.manufacturer)).putExtra("POSITION",position.toString()))

                }
            }


        }}
}


