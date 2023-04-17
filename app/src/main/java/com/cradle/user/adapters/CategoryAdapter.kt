package com.cradle.user.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdapterCategoryBinding
import com.cradle.model.category.CategoryItem
import com.cradle.model.category.SubCategoriesItem
import com.cradle.utils.MyConstants

class CategoryAdapter(private val context: Context, private var wishList: List<CategoryItem?>?) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    var lastCheckposition=-1
    private var isCategory = false

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterCategoryBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_category, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = wishList!![position]
        holder.bind(list, wishList!!)
    }
    inner class MyViewHolder(private val mBinding: AdapterCategoryBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(wishList: CategoryItem?, wishList1: List<CategoryItem?>){


            mBinding.userCateListResponse=wishList
            mBinding.executePendingBindings()
            mBinding.tvParentCategory.text=wishList!!.name
            val image = wishList!!.metaData!!.mobileImage
            Glide.with(context)
                .load(MyConstants.file_Base_URL+image).error(R.drawable.loading)
                .into(mBinding.imgProductImage)


            mBinding.rlUserCate.setOnClickListener {
                if(wishList.isSubCategoryAvailable!!){
                   if(isCategory.equals(false)){
                       isCategory=true
                       mBinding.ivCategoryDown.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.arrow_up))


                       lastCheckposition=position
                       mBinding.recyclerSubCategory.visibility=View.VISIBLE
                       setRecycler(mBinding,context,wishList.subCategories)
                   }else{
                       isCategory=false
                       mBinding.ivCategoryDown.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.category_down_icon))

                        mBinding.recyclerSubCategory.visibility=View.GONE
                   }

                 //   context.startActivity(Intent(context, UserSubCateActivity::class.java).putExtra("cat_id",wishList.id))

                }else {

                 //   context.startActivity(   Intent(context, UserProductActivity::class.java).putExtra("cat_id",wishList.id))
                }

            }
        }

    }
    private fun setRecycler(
        mBinding: AdapterCategoryBinding,
        context: Context,
        subCategoriesItem: List<SubCategoriesItem?>?
    ) {
        mBinding.subCategoryAdapter= SubCategoryAdapter(context,subCategoriesItem)

    }
    override fun getItemCount(): Int {
        return wishList!!.size
    }
}