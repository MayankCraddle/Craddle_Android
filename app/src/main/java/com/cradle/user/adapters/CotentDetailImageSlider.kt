package com.cradle.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.cradle.R
import com.cradle.common_screen.PlayVideoOnYoutubePlayer
import com.cradle.model.ImageModel
import com.cradle.user.userActivity.ImageZoomInOutActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import kotlinx.android.synthetic.main.adapter_content_detail_image_slider.view.*
import kotlinx.android.synthetic.main.pager_item.view.offerImage
import kotlinx.android.synthetic.main.pager_item.view.play
import kotlinx.android.synthetic.main.pager_item.view.tv_title_banner
import kotlinx.android.synthetic.main.pager_item.view.ytPlayer
import java.io.Serializable
import java.util.ArrayList


class CotentDetailImageSlider(
    private var context: Context,
    private var bannerList: java.util.ArrayList<String>,
    private var imageVideo: String,
    private var size: Int,
    private var string: String,
    private var readCount: Int?,
    private var shareURl: String?
) : RecyclerView.Adapter<CotentDetailImageSlider.HomeOffersViewHolder>(), YouTubePlayer.OnInitializedListener {

    private var list: List<ImageModel> = listOf()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        return HomeOffersViewHolder(parent)
    }


    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        val bannerList1 = bannerList!![position]

        holder.bind(bannerList1,context,position,size,imageVideo,string,readCount,shareURl,
            bannerList!!
        )
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
                R.layout.adapter_content_detail_image_slider,
                parent, false
            )
        )

        fun bind(
            bannerList1: String?,
            context: Context,
            position: Int,
            size: Int,
            imageVideo: String,
            string: String,
            readCount: Int?, shareURl: String?, bannerList: ArrayList<String>
        ) {

            Log.e("bannerList",bannerList1.toString())
            if (!string.equals("Home")){
                itemView.tv_title_banner.visibility= View.GONE
            }else{
                itemView.tv_title_banner.visibility= View.GONE
            }
            /*if(bannerList1!!.isNotEmpty()){
                Log.e("rahul",bannerList1.toString())
                Glide.with(context).load(MyConstants.file_Base_URL+bannerList1).error(R.drawable.loading).into(itemView.offerImage)
            }*/
            itemView.tv_view.text=readCount.toString()

            itemView.ll_share_url.setOnClickListener {  shareUrl(context,shareURl)}
            if(imageVideo.equals("video")){

                if (position.equals(0)){
                    itemView.ytPlayer.visibility= View.VISIBLE
                    itemView.play.visibility= View.VISIBLE
                    itemView.img.visibility= View.GONE
                    val onThumbnailLoadedListener: YouTubeThumbnailLoader.OnThumbnailLoadedListener =
                        object : YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                            override fun onThumbnailError(
                                youTubeThumbnailView: YouTubeThumbnailView,
                                errorReason: YouTubeThumbnailLoader.ErrorReason
                            ) {
                            }

                            override fun onThumbnailLoaded(
                                youTubeThumbnailView: YouTubeThumbnailView,
                                s: String
                            ) {
                                youTubeThumbnailView.visibility = View.VISIBLE
                                itemView.ytPlayer.setVisibility(View.VISIBLE)
                            }
                        }
                    itemView.ytPlayer.initialize(
                        "AIzaSyAF5oDfirRQXvCz_-J_DmaArB77-rNSpEs",
                        object : YouTubeThumbnailView.OnInitializedListener {
                            override fun onInitializationSuccess(
                                youTubeThumbnailView: YouTubeThumbnailView,
                                youTubeThumbnailLoader: YouTubeThumbnailLoader
                            ) {
                                //context.showToast(SharaGoPref.getInstance(context).getVideoID(""))
                                youTubeThumbnailLoader.setVideo(SharaGoPref.getInstance(context).getVideoID(""))

                                youTubeThumbnailLoader.setOnThumbnailLoadedListener(
                                    onThumbnailLoadedListener
                                )
                            }

                            override fun onInitializationFailure(
                                youTubeThumbnailView: YouTubeThumbnailView,
                                youTubeInitializationResult: YouTubeInitializationResult
                            ) {
                                //write something for failure
                            }
                        })


                }else{
                    itemView.ytPlayer.visibility= View.GONE
                    itemView.play.visibility= View.GONE
                    itemView.img.visibility= View.VISIBLE
                    Glide.with(context).load(MyConstants.file_Base_URL+bannerList1).into(itemView.offerImage)

                }
            }

            if(imageVideo.equals("image")){
                itemView.ytPlayer.visibility= View.GONE
                itemView.play.visibility= View.GONE
                itemView.img.visibility= View.VISIBLE
                Glide.with(context).load(MyConstants.file_Base_URL+bannerList1).into(itemView.offerImage)

            }

            itemView.ytPlayer.setOnClickListener {
                context.startActivity(Intent(context, PlayVideoOnYoutubePlayer::class.java))
            }
            itemView.img.setOnClickListener {

                val intent = Intent(context, ImageZoomInOutActivity::class.java)
                val args = Bundle()
                args.putSerializable("LIST", bannerList as Serializable)
                args.putString("ImageVideo",imageVideo)
                args.putString("type","Maltimedia")
                intent.putExtra("BUNDLE", args)
                context.startActivity(intent)
            }
        }

        private fun shareUrl(context: Context,shareURl: String?) {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
            i.putExtra(Intent.EXTRA_TEXT, shareURl)
            context.startActivity(Intent.createChooser(i, "Share URL"))
        }

    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        p2: Boolean
    ) {

        youTubePlayer!!.loadVideo("1OxRDJe0pFI")
        youTubePlayer.play()


    }


    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Toast.makeText(context, "Video player Failed", Toast.LENGTH_SHORT)
            .show()
    }

}


