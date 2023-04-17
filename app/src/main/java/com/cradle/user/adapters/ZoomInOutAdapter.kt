package com.cradle.user.adapters
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterZoominoutBinding
import com.cradle.utils.MyConstants


class ZoomInOutAdapter(
    private val context: Context,
    private val wishList: ArrayList<Any>?,
    private val imageAndVideo: String?
) :
    RecyclerView.Adapter<ZoomInOutAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterZoominoutBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_zoominout, parent, false)
        return MyViewHolder(movieListBinding)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(wishList!!,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterZoominoutBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(list: ArrayList<Any>?, position: Int){

            Glide.with(context).load(MyConstants.file_Base_URL + list!!.get(position).toString()).placeholder(R.drawable.loading)
                .into(mBinding.imageZoom)
        }
    }
    override fun getItemCount(): Int {
        if ((imageAndVideo.equals("video"))){
            return wishList?.size!!-1
        }else{
            return wishList?.size!!
        }

    }

}