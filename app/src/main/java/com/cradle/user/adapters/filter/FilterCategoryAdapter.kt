package com.cradle.user.adapters.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.CategoryFilterItemBinding
import com.cradle.model.category.CategoryItem

class FilterCategoryAdapter(
    private val context: Context,
    val categoryList: List<CategoryItem?>?,
    var filterCatergoryInterface: FilterCatergoryInterface
) :
    RecyclerView.Adapter<FilterCategoryAdapter.MyViewHolder>() {
    var lastCheckposition = 0
    private var isCategory = false

    interface FilterCatergoryInterface {
        fun filterCategoryPos(categoryItem: CategoryItem?)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val filterCatListBinding = DataBindingUtil.inflate<CategoryFilterItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.category_filter_item, parent, false
        )
        return MyViewHolder(filterCatListBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // val list = wishList!![position]
        holder.bind(categoryList!![position], categoryList)

    }

    inner class MyViewHolder(private val mBinding: CategoryFilterItemBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(categoryItem: CategoryItem?, categoryList: List<CategoryItem?>?) {
            try {
                mBinding.title.text = categoryItem!!.name
                filterCatergoryInterface.filterCategoryPos(categoryList!![0])
                if (lastCheckposition == adapterPosition) {
                    mBinding.relativeLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
                }
                mBinding.relativeLayout.setOnClickListener {
                    lastCheckposition = adapterPosition
                    filterCatergoryInterface.filterCategoryPos(categoryItem)
                    //notifyDataSetChanged()
                }
            } catch (e: Exception) {
            }
            /*mBinding.userCateListResponse=wishList
            mBinding.executePendingBindings()
            mBinding.tvParentCategory.text=wishList!!.name*/


        }

    }

    /* private fun setRecycler(
         mBinding: AdapterCategoryBinding,
         context: Context,
         subCategoriesItem: List<SubCategoriesItem?>?
     ) {
         mBinding.subCategoryAdapter= SubCategoryAdapter(context,subCategoriesItem)

     }*/
    override fun getItemCount(): Int {
        return categoryList!!.size
    }
}