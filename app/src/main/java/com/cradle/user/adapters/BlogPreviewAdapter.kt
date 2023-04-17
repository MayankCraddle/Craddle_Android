package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterBlogPreviewBinding
import com.cradle.model.ContentListItem
import com.cradle.user.userActivity.ContentDetailsActivity
import com.cradle.utils.MyConstants

class BlogPreviewAdapter(
    private val context: Context, private val list: List<ContentListItem?>?,
) :
    RecyclerView.Adapter<BlogPreviewAdapter.MyViewHolder>() {
    //private lateinit var userHomeMajorExportListBinding: AdatperUserMajorExportListBinding

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterBlogPreviewBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_blog_preview, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contentlist = list!![position]
        holder.bind(contentlist!!,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterBlogPreviewBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(contentListItem: ContentListItem, position: Int){

            //   val jsonObject: JSONObject = countries.getJSONObject(position)
            mBinding.tvProductName.text= contentListItem.subject
          //  mBinding.tvMediaDis.text= contentListItem.shortDescription

            val file=contentListItem.files

            if(file!!.size>0){
                val imagePath = file[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath).error(R.drawable.loading)
                    .into(mBinding.imgProductImage)

            }
            val sectionId=contentListItem.sectionId.toString()
            mBinding.imgProductImage.setOnClickListener {
                context.startActivity(
                    Intent(context, ContentDetailsActivity::class.java)
                        .putExtra("cotent_id",contentListItem.id.toString())
                        .putExtra("screen","home").putExtra("sectionId",sectionId))
            }
        }
    }
    override fun getItemCount(): Int {
        return /*moviesList.size*/list!!.size;
    }
}