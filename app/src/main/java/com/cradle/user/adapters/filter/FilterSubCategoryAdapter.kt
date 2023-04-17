package com.cradle.user.adapters.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.FilterSubCategoryItemBinding
import com.cradle.model.category.SubCategoriesItem
import com.cradle.model.category.SubSubCategoriesItem

class FilterSubCategoryAdapter(
    private val context: Context,
    var subCategories: List<SubCategoriesItem?>?,
    var filterSubCategoryInterface: FilterSubCategoryInterface
) : FilterSubSubCategoryAdapter.FilterSubSubCatInterface,
    RecyclerView.Adapter<FilterSubCategoryAdapter.MyViewHolder>() {
    var lastCheckposition = -1
    private var isCategory = false

    interface FilterSubCategoryInterface {
        fun filterSubCategoryPos(id: String)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val filterCatListBinding = DataBindingUtil.inflate<FilterSubCategoryItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.filter_sub_category_item, parent, false
        )
        return MyViewHolder(filterCatListBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subCategories!![position]!!)
    }

    inner class MyViewHolder(private val mBinding: FilterSubCategoryItemBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(subCategories: SubCategoriesItem) {
            try {
                mBinding.title.text = subCategories!!.name
                if (subCategories!!.subCategories!!.isNotEmpty()) {
                    setRecycler(mBinding, context, subCategories?.subCategories)
                }
            } catch (e: Exception) {
            }

        }

    }

    private fun setRecycler(
        mBinding: FilterSubCategoryItemBinding,
        context: Context,
        subCategories: List<SubSubCategoriesItem?>?
    ) {
        mBinding.subsubCategoryAdapter = FilterSubSubCategoryAdapter(context, subCategories, this)

    }

    override fun getItemCount(): Int {
        return subCategories!!.size
    }

    override fun filterSubSubCatPos(id: String) {
        //context.showToast("idd:"+id)
        filterSubCategoryInterface.filterSubCategoryPos(id)
    }
}