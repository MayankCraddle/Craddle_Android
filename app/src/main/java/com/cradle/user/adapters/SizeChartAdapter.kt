package com.cradle.user.adapters
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterSizeChartBinding
import com.cradle.model.VariantsItem

class SizeChartAdapter(
    private val context: Context,
   private val variants: ArrayList<VariantsItem?>?,var clickNowSize:ClickNowSize
) :
    RecyclerView.Adapter<SizeChartAdapter.MyViewHolder>() {
    var lastCheckposition=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterSizeChartBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_size_chart, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = variants!![position]
        holder.bind(list,position)
    }
    inner class MyViewHolder(private val mBinding: AdapterSizeChartBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(data: VariantsItem?,position: Int){
            mBinding.executePendingBindings()
            mBinding.tvVarient.text=data!!.variant

            if((data.discountValue.toString())!="0.0"){
                mBinding.tvdiscountPrice.text= data.discountedPrice.toString()
                mBinding.tvDiscountValue.text= data.discountValue.toString()+"% off"
                mBinding.tvAcutalPrice.text= context.getString(R.string.currency) +" "+data.actualPrice.toString()
                mBinding.tvAcutalPrice.paintFlags =
                    mBinding.tvAcutalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            }else{
                mBinding.tvdiscountPrice.text= context.getString(R.string.currency) +" "+data.discountedPrice.toString()
            }

            mBinding.llSelectSize.setOnClickListener {
                lastCheckposition=position
                clickNowSize.varient(position,data)
                notifyDataSetChanged()
            }
            if(lastCheckposition==position){
                mBinding.llSelectSize.setBackgroundDrawable(context.resources.getDrawable(R.drawable.select_size))
            }else{
                mBinding.llSelectSize.setBackgroundDrawable(context.resources.getDrawable(R.drawable.size_shap))
            }

        }

    }
    interface ClickNowSize{
        fun varient(position: Int,data: VariantsItem)
    }
    override fun getItemCount(): Int {
        return variants!!.size
    }
}