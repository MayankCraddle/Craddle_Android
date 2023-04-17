package com.cradle.user.adapters.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.FilterSubSubCategoryItemBinding
import com.cradle.model.category.SubSubCategoriesItem

class FilterSubSubCategoryAdapter(
    private val context: Context,
    var subCategories: List<SubSubCategoriesItem?>?,
    var filterSubSubCatInterface: FilterSubSubCatInterface
) :
    RecyclerView.Adapter<FilterSubSubCategoryAdapter.MyViewHolder>() {
    var lastCheckposition = -1
    private var isCategory = false

    interface FilterSubSubCatInterface {
        fun filterSubSubCatPos(id: String)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val filterCatListBinding = DataBindingUtil.inflate<FilterSubSubCategoryItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.filter_sub_sub_category_item, parent, false
        )
        return MyViewHolder(filterCatListBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subCategories?.get(position)!!)
    }

    inner class MyViewHolder(private val mBinding: FilterSubSubCategoryItemBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        // fun bind(wishList: CategoryItem?, wishList1: List<CategoryItem?>){
        fun bind(subCategoriesItem: SubSubCategoriesItem) {
            try {
                mBinding.title.text = subCategoriesItem?.name
                mBinding.relativeLayout.setOnClickListener {
                    filterSubSubCatInterface.filterSubSubCatPos(subCategoriesItem!!.id.toString())
                }
            } catch (e: Exception) {
            }
        }

    }

    override fun getItemCount(): Int {
        return subCategories!!.size
    }
}