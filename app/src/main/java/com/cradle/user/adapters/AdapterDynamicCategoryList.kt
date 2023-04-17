package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cradle.R
import com.cradle.databinding.AdapterUserHomeDynamicCategoryBinding
import com.cradle.user.userActivity.UserViewAllActivity

import org.json.JSONArray

class AdapterDynamicCategoryList(
    private val id: String,
    private val context: Context,
    private val list: ArrayList<String>, private val key: String?,
    private val contentJsonArray: JSONArray?,
    private val colourChange: Int,
   private val colour: Any
) :
    RecyclerView.Adapter<AdapterDynamicCategoryList.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserHomeDynamicCategoryBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_home_dynamic_category, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = list[position]
        holder.bind(list,contentJsonArray,colour)
    }
    inner class MyViewHolder(private val adapterDynamicCategoryList: AdapterUserHomeDynamicCategoryBinding) :
        RecyclerView.ViewHolder(adapterDynamicCategoryList.root) {
        fun bind(
            list: String,
            contentJsonArray: JSONArray?,
            colour: Any
        ) {

            adapterDynamicCategoryList.tvHomeCate.text= list

            adapterDynamicCategoryList. tvViewAll.setOnClickListener {
                context.startActivity(
                    Intent(context, UserViewAllActivity::class.java).putExtra("jsonArray",contentJsonArray.toString()).putExtra("id",id)
                )
            }
            adapterDynamicCategoryList.rlViewAll.setBackgroundColor(ContextCompat.getColor(context, colour as Int))


            for(i in 0..contentJsonArray!!.length()){
                if(contentJsonArray.length()>0){
                    adapterDynamicCategoryList.ivNoDataFound.visibility=View.GONE
                    adapterDynamicCategoryList.recyclerCate.visibility=View.VISIBLE
                    val moviesAdapter= UserMajorExportAdapter(context,contentJsonArray,
                        contentJsonArray.length(),id)
                    adapterDynamicCategoryList.recyclerCate.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapterDynamicCategoryList.recyclerCate.isNestedScrollingEnabled = false
                    adapterDynamicCategoryList.recyclerCate.adapter = moviesAdapter
                }else{
                    adapterDynamicCategoryList.ivNoDataFound.visibility=View.VISIBLE
                    adapterDynamicCategoryList.recyclerCate.visibility=View.GONE
                }


            }

        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

}