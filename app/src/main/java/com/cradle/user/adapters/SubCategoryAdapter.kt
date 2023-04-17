package com.cradle.user.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterSubcategoryBinding
import com.cradle.model.category.SubCategoriesItem
import com.cradle.model.category.SubSubCategoriesItem

class SubCategoryAdapter(
    private val context: Context,private val subCategoriesItem: List<SubCategoriesItem?>?
) :
    RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder>() {

    var lastCheckposition=-1
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterSubcategoryBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_subcategory, parent, false)
        return MyViewHolder(movieListBinding)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val list = subCategoriesItem!![position]
        holder.bind(list)
    }
    inner class MyViewHolder(private val mBinding: AdapterSubcategoryBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(subCategoryList: SubCategoriesItem?){

            mBinding.executePendingBindings()
            mBinding.tvSubCategoryName.text=subCategoryList!!.name
            if(subCategoryList!!.isSubCategoryAvailable!!){
                mBinding.ivCategoryDown.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.down_arrow))

            }else {

            }
             var isCategory = false
            mBinding.rlSubCategory.setOnClickListener {

                if(subCategoryList!!.isSubCategoryAvailable!!){
                    if(isCategory.equals(false)){
                        isCategory=true
                        mBinding.ivCategoryDown.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.up_arrow))

                        lastCheckposition=position
                        mBinding.recyclerSubSubCategory.visibility= View.VISIBLE
                        setRecycler(mBinding,context,subCategoryList.subCategories)
                    }else{
                        isCategory=false
                        mBinding.ivCategoryDown.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.down_arrow))

                        mBinding.recyclerSubSubCategory.visibility= View.GONE
                    }
                }else {
                }


            /*    if(wishList!!.isSubCategoryAvailable!!){
                    setRecycler(mBinding,context,wishList)
                   // context.startActivity(Intent(context, UserSubCateActivity::class.java).putExtra("cat_id",wishList.id))

                }else {
                  //  context.startActivity(   Intent(context, UserProductActivity::class.java).putExtra("cat_id",wishList.id))
                }*/

            }
        }

    }
    private fun setRecycler(
        mBinding: AdapterSubcategoryBinding,
        context: Context,
        subsubCategoriesItem: List<SubSubCategoriesItem?>?

    ) {
        mBinding.subSubCategoryAdapter= SubSubCategoryAdapter(context,subsubCategoriesItem)

    }
    override fun getItemCount(): Int {
        return subCategoriesItem!!.size
    }
}