package com.cradle.user.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.model.Countries
import com.cradle.R
import com.cradle.databinding.AdapterUserSpecialityListBinding

class UserSpacialityAdapter (private val context: Context/*, private val moviesList: ArrayList<Countries>*/) :
    RecyclerView.Adapter<UserSpacialityAdapter.MyViewHolder>() {
    private lateinit var adapterUserSpecialityListBinding: AdapterUserSpecialityListBinding


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  movieListBinding  = DataBindingUtil.inflate<AdapterUserSpecialityListBinding>( LayoutInflater.from(parent.context),
            R.layout.adapter_user_speciality_list, parent, false)
        return MyViewHolder(movieListBinding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      //  val movie = moviesList[position]
       // holder.bind(movie)
    }
    inner class MyViewHolder(private val movieListBinding: AdapterUserSpecialityListBinding) :
        RecyclerView.ViewHolder(movieListBinding.root) {
        fun bind(countries: Countries){
            /*  movieListBinding.countries=countries
              movieListBinding.executePendingBindings()*/
            /* movieListBinding.relativeLayout.setOnClickListener {
                 *//*  context.startActivity(Intent(context,ViewDetailsActivity::class.java)
                      .putExtra("movieList",countries))*//*
            }*/
        }
    }
    override fun getItemCount(): Int {
        return /*moviesList.size*/5;
    }
}