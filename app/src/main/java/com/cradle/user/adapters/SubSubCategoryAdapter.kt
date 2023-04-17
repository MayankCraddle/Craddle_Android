package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterSubSubCategoryBinding
import com.cradle.model.category.SubSubCategoriesItem
import com.cradle.user.userActivity.UserProductActivity

class SubSubCategoryAdapter(
    private val context: Context,
    private val subsubCategoriesItem: List<SubSubCategoriesItem?>?
) :
    RecyclerView.Adapter<SubSubCategoryAdapter.MyViewHolder>() {
    var lastCheckposition=-1

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterSubSubCategoryBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_sub_sub_category, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val list = subsubCategoriesItem!![position]
        holder.bind(list)
    }
    inner class MyViewHolder(private val mBinding: AdapterSubSubCategoryBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(subCategoryList: SubSubCategoriesItem?){

            mBinding.tvSubCategoryName.text=subCategoryList!!.name
            mBinding.rlSubCategory.setOnClickListener {
                lastCheckposition=position
                context.startActivity(   Intent(context, UserProductActivity::class.java).putExtra("cat_id",subCategoryList.id))


            }

        }

    }

    override fun getItemCount(): Int {
        return subsubCategoriesItem!!.size
    }
}