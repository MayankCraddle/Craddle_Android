package com.cradle.user.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cradle.databinding.AdapterSearchVendorResultBinding
import com.cradle.model.SearchVendorVendorListItem

class SearchAdapter : RecyclerView.Adapter<FaqsItemHolder>() {
    private var list : MutableList<SearchVendorVendorListItem> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqsItemHolder {
        return FaqsItemHolder(
            AdapterSearchVendorResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FaqsItemHolder, position: Int) {
        holder.setData(list.get(position))
   /*     holder.binding.liHeader.setOnClickListener {
       *//*     it.isSelected = !it.isSelected
            holder.binding.tvAnswer.visibility = if (it.isSelected) View.VISIBLE else View.GONE
            holder.binding.viewLine.visibility = if (it.isSelected) View.VISIBLE else View.GONE
        *//*}*/
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addAllItem(it: List<SearchVendorVendorListItem>) {
        list.addAll(it)
        notifyDataSetChanged()
    }

    fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }

}
class FaqsItemHolder(var binding : AdapterSearchVendorResultBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(get: SearchVendorVendorListItem) {
        binding.tvVendorSearchAddress.setText(get.metaData!!.streetAddress)
       // binding.tvAnswer.setText(get.question)
    }

}