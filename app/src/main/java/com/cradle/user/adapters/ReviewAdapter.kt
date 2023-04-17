package com.cradle.user.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.ProductReviewAdapterBinding
import com.cradle.model.RatingListItem

class ReviewAdapter (
    private val context: Context,
    private val ratingListItem: ArrayList<RatingListItem?>?,
) :
    RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<ProductReviewAdapterBinding>( LayoutInflater.from(parent.context),
            R.layout.product_review_adapter, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = ratingListItem!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: ProductReviewAdapterBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(rating: RatingListItem?, position: Int){
            mBinding.tvCommitCreate.text= rating!!.review
           /* mBinding.tvCommitCreate.text= rating.createdBy

            val string= parseDateToddMMyyyy(wishList.createdOn)

            mBinding.tvRemainingDay.text= parseData(string!!)*/
            mBinding.view.visibility= View.VISIBLE
        }
    }
    override fun getItemCount(): Int {
        return ratingListItem!!.size
    }
}