package com.cradle.common_screen

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityCountryBinding
import com.cradle.intarfaces.ItemClickListner
import com.cradle.intarfaces.LogInHandler
import com.cradle.model.CountryColorCodeListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.AdapterCountryList
import com.cradle.user.userActivity.UserMainActivity
import com.cradle.user.userActivity.UserProductListInCountryActivity
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_country.*
import kotlinx.android.synthetic.main.custom_toolbar_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountryActivity  : BaseActivity(), ItemClickListner, View.OnClickListener, LogInHandler,AdapterCountryList.TextBookNow {
   private var screen:String = ""
    private lateinit var mViewModel: MainViewModel

    private lateinit var mBinding: ActivityCountryBinding
    var countryList: List<CountryColorCodeListItem?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiInitialise()
    }

    private fun uiInitialise() {
        val response = (application as ApplicationClass).repository
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_country)
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.viewModel=mViewModel
        mBinding.handler=this
        mBinding.setVariable(BR.onSignInClick,this)

         screen=intent.getStringExtra("screen").toString()

        tv_usertoolbartitle.text=getString(R.string.select_country)
        iv_back.setOnClickListener(this)

        if (MyHelper.isNetworkConnected(this)) {
            CoroutineScope(Dispatchers.IO).launch {
                launch {
                    showLoader()
                    mViewModel.findCountry()
                }
            }
            getResultCountryList()
            onSearchCountry()
        } else showToast(getString(R.string.no_internet_connection))

    }


    //COUNTRY LIST RESULT
    fun getResultCountryList(){
        //user Local Product List result
        mViewModel.liveDataCounty.observe(this){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    dismissLoader()
                         it.data?.let {

                        dismissLoader()
                             val activeCountry=ArrayList<CountryColorCodeListItem>()
                             val noActivieCountry=ArrayList<CountryColorCodeListItem>()
                             for ( i in 0 until it.countryColorCodeList!!.size)  {
                                 if((it.countryColorCodeList[i]!!.active!!))
                                     activeCountry.add(it.countryColorCodeList[i]!!)
                                 else
                                     noActivieCountry.add(it.countryColorCodeList[i]!!)
                             }
                             activeCountry.addAll(noActivieCountry)

                        mBinding.adapterCountryList= AdapterCountryList(this@CountryActivity,activeCountry,this,this,"country")
                        countryList=it.countryColorCodeList

                    }
                }
                is ExceptionHandler.Error->{
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }

    }

    override fun onClickItem(position: Int, requestcode: Int) {
    if(requestcode== MyConstants.COUNTRY_SELECTBY_SIGNUP_REQ_CODE){
        setColorCode(countryList!!.get(position)!!.colorCode.toString())
        SharaGoPref.getInstance(this).setCountry(countryList!!.get(position)!!.countryName.toString())



    }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }

    override fun onLogInClicked() {

    }
    //Country list api
    fun onSearchCountry(){
        country_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText.toString())
                //  countryListAdapter!!.filter.filter(newText)
                return false
            }

        })
    }

    private fun filter(text: String) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<CountryColorCodeListItem> = ArrayList()

        //looping through existing elements
        for (s in this.countryList!!) {
            //if the existing elements contains the search input
            if (s!!.countryName!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = mBinding.adapterCountryList!!.filterList(filterdNames)

    }

    fun setColorCode(colorcode:String ) {
        val fontcolor2 = colorcode.replace("rgb(", "").replace("rgba(", "").replace(")", "")
        val rgbarray = ArrayList<String>()
        try {
            for (i in 0 until fontcolor2.split(",").size) {
                rgbarray.add(fontcolor2.split(",")[i])
            }
            SharaGoPref.getInstance(this).setColor(getIntColor1(rgbarray))
            Log.e("colournew",getIntColor1(rgbarray).toString())


        }catch (e:Exception){}
    }
    fun getIntColor1(color: ArrayList<String>): Int {
        var colorCode:Int = 0
        if (color.size == 3) {
            colorCode = Color.rgb(color[0].toInt(), color[1].toInt(), color[2].toInt())
        } else if (color.size == 4) {
            val aa=(color[3].toFloat()*255).toInt()
            Log.e("a", aa.toString())
            Log.e("r", color[0].toString())
            Log.e("g", color[1].toString())
            Log.e("b",color[2].toString())

            colorCode = Color.argb((color[3].toFloat()*255).toInt(), color[0].toInt(), color[1].toInt(), color[2].toInt())
        }
        // Log.e("colorcode",colorCode.toString());
        return colorCode
    }

    override fun bookSession(position: Int, data: CountryColorCodeListItem) {
        setColorCode(data.colorCode!!)
        Log.e("colourcode",data.colorCode)
        SharaGoPref.getInstance(this).setCountry(data.countryName.toString())
        Log.e("countryName",data.countryName.toString())
        SharaGoPref.getInstance(this).setCountryFlag(data.flag.toString())
       Log.e("flag",data.flag.toString())
        if(screen=="login"){

            startActivity(Intent(this@CountryActivity, UserMainActivity::class.java))
        }else if(screen=="Home"){
            startActivity(Intent(this@CountryActivity, UserProductListInCountryActivity::class.java).putExtra("screen","product"))

            finish()
        } else if(screen=="mediaNew"){
            SharaGoPref.getInstance(this).setWhichFrag("Home")
            startActivity(Intent(this@CountryActivity, UserMainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            finish()
        }
        else if(screen=="multimedia"){

            if(data.countryName.equals("Nigeria")){
                SharaGoPref.getInstance(this).setShowList("multimedia")
                startActivity(Intent(this@CountryActivity, UserProductListInCountryActivity::class.java).putExtra("screen","multimedia"))

                finish()
            }else{
                startActivity(Intent(this@CountryActivity, UserProductListInCountryActivity::class.java).putExtra("screen","trade"))
                finish()
            }


        }else if(screen=="marketplace"){
            startActivity(Intent(this@CountryActivity, UserProductListInCountryActivity::class.java).putExtra("screen","marketplace"))
            finish()

        }else if(screen=="trade"){
            startActivity(Intent(this@CountryActivity, UserProductListInCountryActivity::class.java).putExtra("screen","trade"))
            finish()

        }
        else{
            finish()
        }



    }

}