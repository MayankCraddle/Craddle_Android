package com.cradle.user.userActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.common_screen.CountryActivity
import com.cradle.databinding.ActivityProductListInCountryBinding
import com.cradle.repository.ExceptionHandler
import com.cradle.repository.QuoteRepository

import com.cradle.user.userFragment.*
import com.cradle.utils.MyConstants
import com.cradle.utils.SharaGoPref
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_product_list_in_country.*
import kotlinx.android.synthetic.main.custom_toolbar_user.iv_back
import kotlinx.android.synthetic.main.custom_toolbar_user.iv_open_country
import kotlinx.android.synthetic.main.custom_toolbar_user.rl_cus_toolbar
import kotlinx.android.synthetic.main.custom_toolbar_user.tv_country_name

class UserProductListInCountryActivity: BaseActivity(), View.OnClickListener {

    private var mpref: SharaGoPref? = null
     private  lateinit var response: QuoteRepository
    private lateinit var userProducListViewModel: MainViewModel
    private lateinit var activityUserProductListInCountryBinding: ActivityProductListInCountryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        uiInitialise()
    }

    @SuppressLint("SetTextI18n")
    private fun uiInitialise() {

        response = (application as ApplicationClass).repository
        activityUserProductListInCountryBinding=DataBindingUtil.setContentView(this, R.layout.activity_product_list_in_country)
        userProducListViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        activityUserProductListInCountryBinding.mainViewModel=userProducListViewModel
        activityUserProductListInCountryBinding.setVariable(BR.onButtonClick,this)
        mpref = SharaGoPref.getInstance(this)
        resultCount()
        mpref!!.setList("Other")


        //ivSearch.visibility=View.GONE
        iv_back.visibility=View.VISIBLE
       // llCountryContainer.visibility=View.VISIBLE
        iv_back.setOnClickListener(this)
        llOpenCountry.setOnClickListener {
            startActivity(Intent(this, CountryActivity::class.java).putExtra("screen","trade"))
            finish()
        }
        iv_wishlist_screen.setOnClickListener {
            if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty())
                startActivity(Intent(this, WishListActivity::class.java))
            else
                startActivity(Intent(this, UserLoginActivity::class.java))


        }
        rlOpenCart.setOnClickListener {
            if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty())
                startActivity(Intent(this, UserAddToCartActivity::class.java))
            else
                startActivity(Intent(this, UserLoginActivity::class.java))



        }


        if(SharaGoPref.getInstance(this).getCountry("").equals("Nigeria")&&
            intent.getStringExtra("screen").toString().equals("marketplace")){
            activityUserProductListInCountryBinding.cardProduct.visibility=View.VISIBLE
            activityUserProductListInCountryBinding.llSelectionHeader.visibility=View.GONE
            onProduct()
        }else if(SharaGoPref.getInstance(this).getCountry("").
            equals("Nigeria")&&intent.getStringExtra("screen").toString().equals("multimedia")){
            activityUserProductListInCountryBinding.cardProduct.visibility=View.VISIBLE
            activityUserProductListInCountryBinding.llSelectionHeader.visibility=View.GONE
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
            onMultimedia()
        }else if(SharaGoPref.getInstance(this).getCountry("").equals("Nigeria")&&
            intent.getStringExtra("screen").toString().equals("trade")){
           // activityUserProductListInCountryBinding.cardProduct.visibility=View.VISIBLE
            activityUserProductListInCountryBinding.llSelectionHeader.visibility=View.GONE
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
            activityUserProductListInCountryBinding.rlProductNew.visibility=View.VISIBLE
            activityUserProductListInCountryBinding.rlMutiMediaNew.visibility=View.VISIBLE
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
            onMultimedia()
           // openNewFeed()
        }else if(!SharaGoPref.getInstance(this).getCountry("").equals("Nigeria")&&
            intent.getStringExtra("screen").toString().equals("trade")){
           // activityUserProductListInCountryBinding.cardProduct.visibility=View.VISIBLE
            activityUserProductListInCountryBinding.llSelectionHeader.visibility=View.GONE
            activityUserProductListInCountryBinding.llSelectionHeader.visibility=View.GONE
            activityUserProductListInCountryBinding.rlProductNew.visibility=View.GONE
            activityUserProductListInCountryBinding.rlMutiMediaNew.visibility=View.GONE
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE

            onMultimedia()
           // openNewFeed()
        }
        else if(SharaGoPref.getInstance(this).getCountry("").equals("Nigeria")||
            intent.getStringExtra("screen").toString().equals("trade")){
          //  activityUserProductListInCountryBinding.cardProduct.visibility=View.VISIBLE
            activityUserProductListInCountryBinding.llSelectionHeader.visibility=View.GONE
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
            onMultimedia()
          //  openNewFeed()
        }
        else{
            //activityUserProductListInCountryBinding.cardProduct.visibility=View.VISIBLE
            activityUserProductListInCountryBinding.llSelectionHeader.visibility=View.GONE
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
            onMultimedia()
        }


    }

    //Add fragment
    fun addFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        manager.beginTransaction().replace(R.id.container_probycountry, fragment).commit()
    }
    private fun statusBarColourChange(){
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)

        val flag=SharaGoPref.getInstance(this).getCountryFlag("").toString()
        Glide.with(this).load(MyConstants.file_Base_URL_flag+flag).into(iv_open_country)
        if(mpref!!.getShowList("").equals(getString(R.string.products))){
            /*tv_country_name.text = SharaGoPref.getInstance(this).getCountry("")
                .toString()+"'s Products"*/
            tv_country_name.text = "Products"
        }else{
            tv_country_name.text = SharaGoPref.getInstance(this).getCountry("")
                .toString()
        }

        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

    }

    override fun onResume() {
        super.onResume()
        val backPress=SharaGoPref.getInstance(this).getBACKPRESS("").toString()
        val list=mpref!!.getList("")
        if (backPress.equals("contentDetail")&&mpref!!.getList("Other").equals("Other")){
            openNewFeed()

            SharaGoPref.getInstance(this).setBACKPRESS("other")
        }else{
          //  showToast("Search")
        }
        resultCount()
        statusBarColourChange()
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back->{
                finish()
            }R.id.rl_newFeed->{
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
            openNewFeed()
            }R.id.rlMultimedia->{

            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
            openMultimedia()
            }R.id.rlMarketPlace->{
            openMarketPlace()
            }R.id.rl_product->{
            openProduct()
            }R.id.rl_service->{
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
            openService()
            }R.id.rl_innovation->{
            openInovation()
            }R.id.rl_exporter->{
            openExporter()
            openVendorScreen(getString(R.string.exporter))
            }R.id.rl_manufacturer->{
            openManufacturer()
            openVendorScreen(getString(R.string.manufacturer))
            }R.id.rl_wholesaler->{
            openWholesaler()
            openVendorScreen(getString(R.string.wholesaler))
            }R.id.rlProductNew->{

                onProduct()
            }R.id.rlMutiMediaNew->{
            mpref!!.setList("Other")
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
            onMultimedia()
            }R.id.rlHome->{
         /*   tv_country_name.text = SharaGoPref.getInstance(this).getCountry("")
                .toString()+"'s Products" */
            tv_country_name.text = "Products"
            onHome()
            }R.id.rlSearch->{
            mpref!!.setList("Search")
            statusBarColourChange()
            onSearch()
            }R.id.relativeAccount->{
                onAccount()
            }
        }
    }

    private fun openVendorScreen(whichVendor:String){
        startActivity(Intent(this,UserVendorListActivity::class.java).putExtra("WichVendor",whichVendor))

     /*   if(whichVendor.equals(getString(R.string.exporter))){
             }else{
            startActivity(Intent(this, UserSearchResultFragment::class.java).putExtra("WichVendor",whichVendor))

        }*/
          }

    private fun openNewFeed(){
        activityUserProductListInCountryBinding.rlNewFeed.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        activityUserProductListInCountryBinding.rlProduct.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlService.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlInnovation.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.tvProduct .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvNewFeed .setTextColor(ContextCompat.getColor(this, R.color.white))
        activityUserProductListInCountryBinding.tvService .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvInnovation .setTextColor(ContextCompat.getColor(this, R.color.black))
        SharaGoPref.getInstance(this).setWhichFrag("Media")
       addFragment(UserMediaFragment())
     //  addFragment(MediaFragment())
    }

    private fun openProduct(){
        activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.VISIBLE
        activityUserProductListInCountryBinding.rlNewFeed.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlProduct.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        activityUserProductListInCountryBinding.rlService.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlInnovation.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))

        activityUserProductListInCountryBinding.tvProduct .setTextColor(ContextCompat.getColor(this, R.color.white))
        activityUserProductListInCountryBinding.tvNewFeed .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvService .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvInnovation .setTextColor(ContextCompat.getColor(this, R.color.black))
       // addFragment(ProByCountryFragment())
        if(SharaGoPref.getInstance(this).getCountry("").equals("Nigeria")){
           // addFragment(UserCategoryFrag())
            addFragment(TradeFragment())
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
        }else{
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
            addFragment(UserBlogFragment())
        }

    }
    private fun openService(){
        activityUserProductListInCountryBinding.rlNewFeed.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlProduct.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlService.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        activityUserProductListInCountryBinding.rlInnovation.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))

        activityUserProductListInCountryBinding.tvProduct .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvNewFeed .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvService .setTextColor(ContextCompat.getColor(this, R.color.white))
        activityUserProductListInCountryBinding.tvInnovation .setTextColor(ContextCompat.getColor(this, R.color.black))
        addFragment(UserBlogFragment())
    }
    private fun openInovation(){
        activityUserProductListInCountryBinding.rlNewFeed.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlProduct.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlService.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlInnovation.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

        activityUserProductListInCountryBinding.tvProduct .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvNewFeed .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvService .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvInnovation .setTextColor(ContextCompat.getColor(this, R.color.white))

    }
    private fun openExporter(){
        activityUserProductListInCountryBinding.rlManufacturer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlWholesaler.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlExporter.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

        activityUserProductListInCountryBinding.tvManufacturer .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvWholesaler .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvExporter .setTextColor(ContextCompat.getColor(this, R.color.white))
    }
    private fun openManufacturer(){
        activityUserProductListInCountryBinding.rlManufacturer.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        activityUserProductListInCountryBinding.rlWholesaler.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlExporter.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))

        activityUserProductListInCountryBinding.tvManufacturer .setTextColor(ContextCompat.getColor(this, R.color.white))
        activityUserProductListInCountryBinding.tvWholesaler .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvExporter .setTextColor(ContextCompat.getColor(this, R.color.black))
    }
    private fun openWholesaler(){
        activityUserProductListInCountryBinding.rlManufacturer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlWholesaler.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        activityUserProductListInCountryBinding.rlExporter.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))

        activityUserProductListInCountryBinding.tvManufacturer .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvWholesaler .setTextColor(ContextCompat.getColor(this, R.color.white))
        activityUserProductListInCountryBinding.tvExporter .setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun openMarketPlace(){
        activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.VISIBLE
        activityUserProductListInCountryBinding.rlNewFeed.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlProduct.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        activityUserProductListInCountryBinding.rlService.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlInnovation.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))

        activityUserProductListInCountryBinding.tvProduct .setTextColor(ContextCompat.getColor(this, R.color.white))
        activityUserProductListInCountryBinding.tvNewFeed .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvService .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvInnovation .setTextColor(ContextCompat.getColor(this, R.color.black))

        activityUserProductListInCountryBinding.ivMarketPlace.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.marketplace_new_active))
        activityUserProductListInCountryBinding.rlMarketPlace.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

        activityUserProductListInCountryBinding.ivMultimedia.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.multimedia_new))
        activityUserProductListInCountryBinding.rlMultimedia.setBackgroundColor(ContextCompat.getColor(this, R.color.white))


        // addFragment(ProByCountryFragment())
        if(SharaGoPref.getInstance(this).getCountry("").equals("Nigeria")){
            // addFragment(UserCategoryFrag())
            addFragment(TradeFragment())
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
        }else{
            activityUserProductListInCountryBinding.llExporterToolbar.visibility=View.GONE
            addFragment(UserBlogFragment())
        }

    }
    private fun openMultimedia(){
        activityUserProductListInCountryBinding.rlNewFeed.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)
        activityUserProductListInCountryBinding.rlProduct.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlService.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.rlInnovation.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightWhite))
        activityUserProductListInCountryBinding.tvProduct .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvNewFeed .setTextColor(ContextCompat.getColor(this, R.color.white))
        activityUserProductListInCountryBinding.tvService .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvInnovation .setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.ivMultimedia.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.multimedia_new_active))
        activityUserProductListInCountryBinding.rlMultimedia.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

        activityUserProductListInCountryBinding.ivMarketPlace.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.marketplace_new))
        activityUserProductListInCountryBinding.rlMarketPlace.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        rl_cus_toolbar.visibility=View.VISIBLE
        SharaGoPref.getInstance(this).setWhichFrag("Media")
        addFragment(UserMediaFragment())
      //  addFragment(MediaFragment())
    }

    private fun onHome() {
        activityUserProductListInCountryBinding.homeImage.setColorFilter(mpref!!.getColor(0)!!, PorterDuff.Mode.SRC_ATOP)
        activityUserProductListInCountryBinding.ivProductNew.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding.ivSearch.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding.ivMutiMedia.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding. ivAccountNew.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )

        activityUserProductListInCountryBinding.textHome.setTextColor(mpref!!.getColor(0)!!)
        activityUserProductListInCountryBinding.tvProductNew.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvSearch.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvMultiMediaNew.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvAccountNew.setTextColor(ContextCompat.getColor(this, R.color.black))
      //  tv_country_name.text = SharaGoPref.getInstance(this).getCountry("").toString()+" Product's"
        tv_country_name.text = "Product's"
        rl_cus_toolbar.visibility=View.GONE
        SharaGoPref.getInstance(this).setToolBarInCate("Yes")
        addFragment(CategoryFragment())

    }
    private fun onProduct() {
        activityUserProductListInCountryBinding.homeImage.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP)

        activityUserProductListInCountryBinding.ivProductNew.setColorFilter(mpref!!.getColor(0)!!, PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding.ivSearch.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding.ivMutiMedia.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding. ivAccountNew.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )

        activityUserProductListInCountryBinding.textHome.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvProductNew.setTextColor(mpref!!.getColor(0)!!)
        activityUserProductListInCountryBinding.tvSearch.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvMultiMediaNew.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvAccountNew.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_country_name.text = /*SharaGoPref.getInstance(this).getCountry("").toString()+*/"Products"
        rl_cus_toolbar.visibility=View.VISIBLE
        openMarketPlace()

    }
    private fun onSearch() {
        activityUserProductListInCountryBinding.homeImage.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP)

        activityUserProductListInCountryBinding.ivProductNew.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding.ivSearch.setColorFilter(mpref!!.getColor(0)!!, PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding.ivMutiMedia.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )

        activityUserProductListInCountryBinding. ivAccountNew.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding.textHome.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvProductNew.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvSearch.setTextColor(mpref!!.getColor(0)!!)
        activityUserProductListInCountryBinding.tvMultiMediaNew.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvAccountNew.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_country_name.text = SharaGoPref.getInstance(this).getCountry("").toString()

        SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.search))
      //  SharaGoPref.getInstance(this).setShowList(getString(R.string.products))
       // SharaGoPref.getInstance(this).setShowList(getString(R.string.products))
     //   SharaGoPref.getInstance(this).setShowList(getString(R.string.multimedia))
        rl_cus_toolbar.visibility=View.GONE
        addFragment(UserSearchResultFragment())
    }
    private fun onMultimedia() {
        activityUserProductListInCountryBinding.homeImage.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP)
        activityUserProductListInCountryBinding.ivProductNew.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding.ivSearch.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding.ivMutiMedia.setColorFilter(mpref!!.getColor(0)!!, PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding. ivAccountNew.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )

        activityUserProductListInCountryBinding.textHome.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvProductNew.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvSearch.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvMultiMediaNew.setTextColor(mpref!!.getColor(0)!!)
        activityUserProductListInCountryBinding.tvAccountNew.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_country_name.text = SharaGoPref.getInstance(this).getCountry("").toString()
        rl_cus_toolbar.visibility=View.VISIBLE
        openMultimedia()
    }
    private fun onAccount() {
        activityUserProductListInCountryBinding.homeImage.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP)
        activityUserProductListInCountryBinding.ivProductNew.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding.ivSearch.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding.ivMutiMedia.setColorFilter(ContextCompat.getColor(this, R.color.home_text), PorterDuff.Mode.SRC_ATOP
        )
        activityUserProductListInCountryBinding. ivAccountNew.setColorFilter(mpref!!.getColor(0)!!, PorterDuff.Mode.SRC_ATOP
        )

        activityUserProductListInCountryBinding.textHome.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvProductNew.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvSearch.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvMultiMediaNew.setTextColor(ContextCompat.getColor(this, R.color.black))
        activityUserProductListInCountryBinding.tvAccountNew.setTextColor(mpref!!.getColor(0)!!)
        SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.account_multimedia))
        tv_country_name.text = SharaGoPref.getInstance(this).getCountry("").toString()
        rl_cus_toolbar.visibility=View.GONE
        if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()) {
            SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.account))
            addFragment(UserAccountFragment())
        } else {
            startActivity(
                Intent(this, UserLoginActivity::class.java)
            )
            /* SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.account))
             addFragment(WelcomeScreenActivity())*/
        }

    }
    private fun resultCount(){
        val cartId=SharaGoPref.getInstance(this).getCartId("")
        if (!cartId.equals("null")){
            Log.e("tokenCartID",SharaGoPref.getInstance(this).getLoginToken("").toString()+"CardID"+SharaGoPref.getInstance(this).getCartId("").toString())
            userProducListViewModel.getCardDetailsByIdParam(SharaGoPref.getInstance(this).getLoginToken("").toString(),SharaGoPref.getInstance(this).getCartId("").toString())

            //user Server Product List result
            userProducListViewModel.lGetCardDetailsParam.observe(this){
                    it->
                when(it){
                    is ExceptionHandler.Loading->{}
                    is ExceptionHandler.Success->{
                        it.data?.let {

                            if(it.cartSize!!.isNotEmpty()){
                                activityUserProductListInCountryBinding.rlCartCount.visibility=View.VISIBLE
                                activityUserProductListInCountryBinding.tvCartCount.text=it.cartSize
                            }else{
                                activityUserProductListInCountryBinding.rlCartCount.visibility=View.VISIBLE
                                activityUserProductListInCountryBinding.tvCartCount.text="0"

                            }
                            // activityUserAddtocartBinding.userAddToCartAdapter= UserAddToCartAdapter(this/*,it.metaData!!.items*/)

                        }
                    }
                    is ExceptionHandler.Error->{
                        //  Utility.toastMessage(requireActivity(),it.errorMessage)
                    }
                }
            }
        }else{
            activityUserProductListInCountryBinding.rlCartCount.visibility=View.VISIBLE
            activityUserProductListInCountryBinding.tvCartCount.text="0"

        }

    }
}