package com.cradle.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
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
import com.cradle.utils.MyConstants
import kotlinx.android.synthetic.main.pager_item.view.*

class YoutubePlayerAdapter (
    private var context: Context,
    private var bannerList: List<String?>?,
    private var  imageVideo: String,
    private var size: Int
) : RecyclerView.Adapter<YoutubePlayerAdapter.HomeOffersViewHolder>(), YouTubePlayer.OnInitializedListener {

    private var list: List<ImageModel> = listOf()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOffersViewHolder {
        return HomeOffersViewHolder(parent)
    }


    override fun onBindViewHolder(holder: HomeOffersViewHolder, position: Int) {
        val bannerList1 = bannerList!![position]

        holder.bind(bannerList1,context,position,size,imageVideo)
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
                R.layout.pager_item,
                parent, false
            )
        )

        fun bind(bannerList1: String?, context: Context, position: Int, size: Int, imageVideo: String) {
            Glide.with(context).load(MyConstants.file_Base_URL+bannerList1).into(itemView.offerImage)

            if (position.equals(size-1)){
                if(imageVideo.equals("video"))
                    itemView.ytPlayer.visibility= View.VISIBLE
                itemView.img.visibility= View.GONE
            }else{
                itemView.ytPlayer.visibility= View.GONE
                itemView.img.visibility= View.VISIBLE
                Glide.with(context).load(MyConstants.file_Base_URL+bannerList1).into(itemView.offerImage)

            }
            itemView.ytPlayer.setOnClickListener {
                context.startActivity(Intent(context, PlayVideoOnYoutubePlayer::class.java))
                /* val intent = YouTubeStandalonePlayer.createVideoIntent(
                     context as Activity?,"AIzaSyAF5oDfirRQXvCz_-J_DmaArB77-rNSpEs","JoilZgbppC8"
                     )
 
                 context.startActivity(intent)*/}

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
                        youTubeThumbnailLoader.setVideo("JoilZgbppC8")

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

        }
    }

    @Throws(Throwable::class)
    fun retriveVideoFrameFromVideo(videoPath: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            if (Build.VERSION.SDK_INT >= 14) mediaMetadataRetriever.setDataSource(
                videoPath,
                HashMap()
            ) else mediaMetadataRetriever.setDataSource(videoPath)
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
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


