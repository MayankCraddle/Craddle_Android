package com.cradle.user.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterUserMediaCategoryBinding
import com.cradle.model.media.CatListItem
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import kotlinx.android.synthetic.main.activity_user_main.*
import java.util.*

class UserMediaCategoryAdapter(
    private val id: String,
    private val context: Context/*, private val wishList: List<UserWishList?>?*/,
    private val list: List<CatListItem>?/*, private val key: String?,
    private val contentJsonArray: JSONArray?,*/,
    private val textState: TextState
) :
    RecyclerView.Adapter<UserMediaCategoryAdapter.MyViewHolder>() {
    var lastCheckposition = 0
    var checkPos = 0

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val movieListBinding = DataBindingUtil.inflate<AdapterUserMediaCategoryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_user_media_category, parent, false
        )
        return MyViewHolder(movieListBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = list!![position]
        holder.bind(listItem, position)
    }

    inner class MyViewHolder(private val mBinding: AdapterUserMediaCategoryBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(catListItem:CatListItem, position: Int) {

            mBinding.tvMediaCate.text = catListItem.categoryName
            Glide.with(context).load(MyConstants.file_Base_URL+catListItem?.icon).placeholder(R.drawable.account).into(mBinding.ivCategory)
            mBinding.ivCategory.setColorFilter(
                ContextCompat.getColor(context, R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            if (checkPos == position) {
                mBinding.rlClickCate.setBackgroundResource(R.color.colour_2)

            } else {
                mBinding.rlClickCate.setBackgroundColor(SharaGoPref.getInstance(context)
                    .getColor(0)!!)

            }



//            notifyDataSetChanged()
            mBinding.rlClickCate.setOnClickListener {
                checkPos = position
                //mBinding.rlClickCate.setBackgroundResource(R.color.colour_2)
                textState.clickonMediaCategory(position, list.toString())
                notifyDataSetChanged()
            }
            /*if(position==0){
                mBinding.rlClickCate.setBackgroundColor(ContextCompat.getColor(context, R.color.colour_1))
            }
            if(position==1){
                mBinding.rlClickCate.setBackgroundColor(ContextCompat.getColor(context, R.color.colour_2))
            }*/


        }
    }

    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.adapterPosition
    }
    override fun getItemCount(): Int {
        return list!!.size
    }

    interface TextState {

        fun clickonMediaCategory(int: Int, string: String)
    }

    public  fun setCategoryColor(position: Int) {
        checkPos = position-1
        notifyDataSetChanged()

    }

    private fun setRandomColour(position: Int) {
        val position = position
        val r = Random().nextInt(6)
        val draw = GradientDrawable()
        draw.shape = GradientDrawable.RECTANGLE
        //draw.setColor(Color.parseColor("#" + colourList[Random().nextInt(6)]));
        //  mBinding.rlClickCate.setBackground(draw)
    }


}