package com.cradle.user.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.databinding.AdatperUserMajorExportListBinding
import com.cradle.user.userActivity.ContentDetailsActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import org.json.JSONArray
import org.json.JSONObject


class UserMajorExportAdapter(
    private val context: Context/*, private val moviesList: ArrayList<Countries>*/,
    private val contentJsonArray: JSONArray,
    private val length: Int, private val setionId: String,
) :
    RecyclerView.Adapter<UserMajorExportAdapter.MyViewHolder>() {
    //private lateinit var userHomeMajorExportListBinding: AdatperUserMajorExportListBinding

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdatperUserMajorExportListBinding>( LayoutInflater.from(parent.context),
            R.layout.adatper_user_major_export_list, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(contentJsonArray,position)
    }
    inner class MyViewHolder(private val movieListBinding:AdatperUserMajorExportListBinding) :
        RecyclerView.ViewHolder(movieListBinding.root) {
        fun bind(countries: JSONArray,position: Int){

                val jsonObject: JSONObject = countries.getJSONObject(position)
                movieListBinding.tvProductName.text= jsonObject.optString("subject")
                val file=jsonObject.optJSONArray("files")

            if(file!!.length()>0){
                val imagePath = file[0]
                Glide.with(context)
                    .load(MyConstants.file_Base_URL+imagePath).error(R.drawable.loading)
                    /*.override(100, 100)*/
                    .into(movieListBinding.imgProductImage)

            }
             movieListBinding.imgProductImage.setOnClickListener {
                 SharaGoPref.getInstance(context).setSectionId(jsonObject.optString("sectionId"))
                   context.startActivity(
                       Intent(context,ContentDetailsActivity::class.java).putExtra("cotent_id",jsonObject.optString("id")).putExtra("screen","home"))
            }
        }
    }
    override fun getItemCount(): Int {
        return /*moviesList.size*/length;
    }
}