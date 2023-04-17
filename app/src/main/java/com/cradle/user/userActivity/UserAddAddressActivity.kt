package com.cradle.user.userActivity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityUserAddaddressBinding
import com.cradle.intarfaces.clickItem
import com.cradle.model.address.AddressListItem
import com.cradle.model.allcountry.CountryListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.CityAdapter
import com.cradle.user.adapters.CountryAllAdapter
import com.cradle.user.adapters.StateAdapter
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_user_addaddress.*
import kotlinx.android.synthetic.main.custom_toolbar_user.*
import kotlinx.android.synthetic.main.dialog_country.*
import kotlinx.android.synthetic.main.dialog_state.*
import kotlinx.android.synthetic.main.dialog_state.country_search
import kotlinx.android.synthetic.main.dialog_state.llCloseDialog
import kotlinx.android.synthetic.main.forgot_password_user.*
import kotlinx.android.synthetic.main.fragment_user_home.tv_usertoolbartitle
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class UserAddAddressActivity: BaseActivity(),View.OnClickListener,clickItem,CountryAllAdapter.TextBookNow{
    private lateinit var mViewModel: MainViewModel
    private lateinit var countryAllAdapter: CountryAllAdapter
    private lateinit var stateAdapter: StateAdapter
    private lateinit var cityAdapter: CityAdapter
    private lateinit var mBinding: ActivityUserAddaddressBinding
    var countryList: List<CountryListItem?>? = null
    var stateList: List<String?>? = null
    var cityList: ArrayList<String?>? = ArrayList()
    var stateListNew: ArrayList<String?>? = null
    var abiaZipCodeList: List<String?>? = null
    val zipCodeList=ArrayList<String>()

    var oyoZipCodeList: ArrayList<String?>? = null

    var data: AddressListItem?=null
    var landMark:String?="Home"
    var filterZipCode:String?=null
    var stateName:String?=null
    var isShowing =false
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val response = (application as ApplicationClass).repository
        mBinding = ActivityUserAddaddressBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onAddAdressClick, this)
        findId()

    }

    private fun findId() {
        AnalyticsUtils.analyticReport(this,"AddAddressScreen")

        stateList= arrayListOf("Abia","Lagos","Oyo")
        oyoZipCodeList= arrayListOf("200251","200281","200213","200235","200285","200222"
            ,"200224","200244","200262","200232","200263","200254","200231","200225","200001"
            ,"200284","200283","200223","200261","200272","200241","200255","200002","200273","200221"
            ,"200242","200258","200252","200212","200243","200256","200234","200257","200233","200253"
            ,"200271","200282","200214","200009","200005","200211")
        abiaZipCodeList= arrayListOf("440001","440002","440003","440004","440005","440006","440007","440008","440009","440010","440112","440014","440015","440021","441002","441008","441010","441011","441012","441013","442001","442002","442003","443001","443003","443004","443005","450001","450002","450003","451001","451002","451003","451004","452001")

        countryName="Nigeria"
        getApiReshult()
        getIntentData()
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(SharaGoPref.getInstance(this).getEmail("").toString())
        if (matcher.matches()) {
            edtEmail.setText(SharaGoPref.getInstance(this).getEmail("").toString())
            edtEmail.setFocusable(false);
            edtEmail.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            edtEmail.setClickable(false)
        }else{
            edtEmail.setFocusable(true);
            edtEmail.setFocusableInTouchMode(true);
            edtEmail.setClickable(true);
            edtEmail.setText(null)
        }
        resultAllCountry()

        iv_back.setOnClickListener { finish() }

        ll_home.setOnClickListener { onClickHome()}
        ll_office.setOnClickListener { onClickOffice()}
        ll_other.setOnClickListener { onClickOther()}

        //address inisilize
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), MyConstants.GoogleAddressApiKey);
        }
        llOpenAddress.setOnClickListener {
            onSearchCalled()
        }

       /* edtLocality.setOnClickListener {

            mViewModel.findCountry()
            mViewModel.findAllCountry()
        }*/
        openAllCountry()
        edtState.setOnClickListener {
            stateDialog()

        }
        edtCity.setOnClickListener {
            if(stateName!=null)
          /*  openCityListApi()*/
            cityDialog()
            else
                showToast("Please first select your state")

        }

        getResultCountryList()
        statusBarColourChange()
        mBinding.btnAddAddress.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

         rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

    }
    private fun statusBarColourChange(){
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)
    }
    private fun setLAndMark(){
        if(data?.metaData!!.addressType.toString().equals("Work")){
            onClickOffice()
        }else{
            onClickHome()
        }
    }



    private fun onClickHome(){
        landMark=getString(R.string.home)
        ll_home.setBackgroundResource(R.drawable.rac_rounded_green_new)
        ll_office.setBackgroundResource(R.drawable.rac_rounded)
        ll_other.setBackgroundResource(R.drawable.rac_rounded)

    }
    private fun onClickOffice(){
        landMark=getString(R.string.work)
        ll_home.setBackgroundResource(R.drawable.rac_rounded)
        ll_office.setBackgroundResource(R.drawable.rac_rounded_green_new)
        ll_other.setBackgroundResource(R.drawable.rac_rounded)
    }
    private fun onClickOther(){
        landMark=getString(R.string.other)
        ll_home.setBackgroundResource(R.drawable.rac_rounded)
        ll_office.setBackgroundResource(R.drawable.rac_rounded)
        ll_other.setBackgroundResource(R.drawable.rac_rounded_green_new)
    }
    var from:String?=null
    var id=""
    private fun getIntentData(){
        try {
            data=intent.getParcelableExtra("data")
            if(data!=null){
                Log.e("addData",data.toString())
                id=intent.getStringExtra("id").toString()
                tv_usertoolbartitle.text=getString(R.string.edit_address)
                edtFirstName.setText(data?.metaData!!.firstName.toString())

                val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
                val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
                val matcher = pattern.matcher(SharaGoPref.getInstance(this).getEmail("").toString())
                if (matcher.matches()) {
                    edtEmail.setText(SharaGoPref.getInstance(this).getEmail("").toString())
                }else{
                    edtEmail.setText("rahul")
                }
                edtLastName.setText(data?.metaData!!.lastName.toString())
                edtMobileNo.setText(data?.metaData!!.phone.toString())
                edtZipcode.setText(data?.metaData!!.zipcode.toString())
                edtState.setText(data?.metaData!!.state.toString())
               // edtCity.setText(data?.metaData!!.city.toString())
                edtCity.setText(data?.metaData!!.city.toString())
                ccp.setCountryForPhoneCode(data!!.metaData!!.phoneCode!!.toInt())
                edtLocality.setText(data?.metaData!!.country.toString())
                edtLandMark.setText(data?.metaData!!.landmark.toString())
             //   edtAddress.setText(data?.metaData!!.streetAddress.toString())
                tvAddressSugg.setText(data?.metaData!!.streetAddress.toString())
              //  countryName=data?.metaData!!.country.toString()

                setLAndMark()

            }else {
                tv_usertoolbartitle.text = getString(R.string.add_address)
            }
        }catch (e:Exception){
            tv_usertoolbartitle.text = getString(R.string.add_address)
        }

    }

    private fun getApiReshult() {
        mViewModel.laddAddressParam.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {

                      //  val jsonObject = JSONObject(response.body().toString())
                        showToast(getString(R.string.sussfully_add_adress))
                        onBackPressed()

                    }

                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    showToast(it.errorMessage)
                }
            }
        }
        mViewModel.lUserUpdateAddressParam.observe(this){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    it.data?.let {
                        SharaGoPref.getInstance(this).setEmail(edtEmail.text.toString().trim())
                      //  val jsonObject = JSONObject(response.body().toString())
                        showToast(getString(R.string.sussfully_add_adress))
                        onBackPressed()

                    }

                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    showToast(it.errorMessage)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()

    }

    private fun checkVAlidation(): Boolean {
        if (edtFirstName.text.toString().trim().isEmpty()) {
            showToast(getString(R.string.a_first_name))
            return false
        }
        else if (edtLastName.text.toString().trim().isEmpty()) {
            showToast(getString(R.string.a_last_name))
            return false
        } else  if (!Utility.isValidEmail(edtEmail.text!!.toString().trim { it <= ' ' })) {
            showToast(resources.getString(R.string.enter_valid_email))
            return false
        }
        else if (edtEmail.text.toString().trim().isEmpty()) {
            showToast(getString(R.string.email))
            return false
        }
        else if (edtMobileNo.text.toString().trim().isEmpty()) {
            showToast(getString(R.string.a_mobile_no))
            return false
        }
        /*else if (edtAddress.text.toString().trim().isEmpty()) {
            showToast(getString(R.string.a_address))
            return false
        }*/else if (tvAddressSugg.text.isEmpty()) {
            showToast(getString(R.string.a_address))
            return false
        }
        else if (edtState.text.isEmpty()) {
            showToast(getString(R.string.a_state))
            return false
        }
        else if (edtCity.text.isEmpty()) {
            showToast(getString(R.string.a_city))
            return false
        }
      /*  else if (edtAddress.text.toString().trim().isEmpty()) {
            showToast(getString(R.string.a_address))
            return false
        }*//*else if (tvAddressSugg.text.isEmpty()) {
            showToast(getString(R.string.a_address))
            return false
        }*/
        /*else if (edtZipcode.text.toString().trim().isEmpty()) {
            showToast(getString(R.string.pinecode_valide))
            return false
        }*//*else if (filterZipCode!!.equals("false")) {
            showToast(getString(R.string.pinecode_valide))
            return false
        }*/
        else if (countryName!!.isEmpty()) {
            showToast(getString(R.string.a_country))
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_AddAddress -> {

               // onFilterZipCode()
                if (checkVAlidation()) {
                    val params= JsonObject()
                    params.addProperty("firstName", edtFirstName.text.toString().trim())
                    params.addProperty("lastName", edtLastName.text.toString().trim())
                    params.addProperty("email", edtEmail.text.toString().trim())
                    params.addProperty("phone",edtMobileNo.text.toString().trim())
                    params.addProperty("country", countryName)
                    params.addProperty("state", edtState.text.toString().trim())
                    params.addProperty("city", edtCity.text.toString().trim())
                    params.addProperty("zipcode", edtZipcode.text.toString().trim())
                  //  params.addProperty("streetAddress", edtAddress.text.toString().trim())
                    params.addProperty("streetAddress", tvAddressSugg.text.toString().trim())
                    params.addProperty("landmark", edtLandMark.text.toString())
                    params.addProperty("phoneCode",  ccp.selectedCountryCodeWithPlus.toString())
                    params.addProperty("addressType", landMark)
                    val mainjson= JsonObject()
                    mainjson.add("metaData",params)
                    Log.e("param",mainjson.toString())
                    apiHit(mainjson)
                }
            }
        }
    }

    private fun apiHit(jsonObject: JsonObject){
        if(data!=null){
            Log.e("update","update")
            showLoader()
            AnalyticsUtils.analyticReport(this,MyConstants.AddressUpdated)
           mViewModel.userUpdateAddress(SharaGoPref.getInstance(this).getLoginToken("").toString(),jsonObject,id)
          //  call = apiInterface!!.updateAddress(SharaGoPref.getInstance(this).getToken("").toString(),jsonObject,data!!.id!!)
        }else{
            Log.e("add","add")
            showLoader()
            AnalyticsUtils.analyticReport(this,MyConstants.AddressAdded)
            mViewModel.userAddAddress(SharaGoPref.getInstance(this).getLoginToken("").toString(),jsonObject)
        }
    }
    private var dialog: Dialog? = null
    private fun countryDialog(list: List<CountryListItem?>?) {
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_country)
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))

        val  linearLayoutManager= LinearLayoutManager(this)
        dialog!!.recycler_country_list.layoutManager = linearLayoutManager
   //     countryAllAdapter= CountryAllAdapter(this,list,this,this,"")
        dialog!!.recycler_country_list.adapter=countryAllAdapter
        dialog!!.setCanceledOnTouchOutside(true)
      dialog!!.llCloseDialog.setOnClickListener { dialog!!.dismiss() }
      //  onSearchCountry()
        dialog!!.show()
    }

    private var dialogState: Dialog? = null
    private fun stateDialog() {
        dialogState = Dialog(this)
        dialogState!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogState!!.setContentView(R.layout.dialog_state)
        dialogState!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogState!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))

        val  linearLayoutManager= LinearLayoutManager(this)
        dialogState!!.recycler_state.layoutManager = linearLayoutManager
        stateAdapter= StateAdapter(this, stateList as ArrayList<String?>?,this)
        dialogState!!.recycler_state.adapter=stateAdapter
        dialogState!!.setCanceledOnTouchOutside(true)
        dialogState!!.llCloseDialog.setOnClickListener { dialogState!!.dismiss() }
       // dialogState!!.llCloseDialog.setOnClickListener { dialogState!!.dismiss() }
      //  onSearchCountry()
        onSearchCountry()
        dialogState!!.show()
    }


    private var dialogCity: Dialog? = null
    private fun cityDialog() {
        dialogCity = Dialog(this)
        dialogCity!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogCity!!.setContentView(R.layout.dialog_state)
        dialogCity!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogCity!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))

        val  linearLayoutManager= LinearLayoutManager(this)
        dialogCity!!.recycler_state.layoutManager = linearLayoutManager
        dialogCity!!.country_search.queryHint = "City"
        cityAdapter= CityAdapter(this, cityList as ArrayList<String?>?,this)
        dialogCity!!.recycler_state.adapter=cityAdapter
        dialogCity!!.setCanceledOnTouchOutside(true)
        onSearchCity()
        dialogCity!!.llCloseDialog.setOnClickListener {
            isShowing=false
            dialogCity!!.dismiss() }
        //  onSearchCountry()

        dialogCity!!.show()
    }

    override fun bookSession(position: Int, data: CountryListItem?) {
        edtLocality.text=data!!.countryName
        countryName=data.countryName
        countryCode=data.isdCode
        dialog!!.dismiss()

    }
    //COUNTRY LIST RESULT
    var countryName: String? =""
    var countryCode: String? =null
    fun getResultCountryList(){
        //user Local Product List result
        mViewModel.liveDataCounty.observe(this){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    dismissLoader()
                    //   activityUserProductListInCountryBinding.recyclerLocalProduct.visibility=View.VISIBLE
                    it.data?.let {

                        dismissLoader()

                       // countryList=it.countryColorCodeList
                       // countryDialog(it.countryColorCodeList)
                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }

    }

    override fun onClickItem(position: Int, requestcode: Int,name:String) {
        if(MyConstants.STATE_REQ_CODE==requestcode){
             stateName=name
            edtState.text=name
            edtCity.text=null
            dialogState!!.dismiss()
            if("Abia".equals(stateName)){
                zipCodeList.addAll(abiaZipCodeList!! as ArrayList<String>)

            }else if("Lagos".equals(stateName)){

            }else if("Oyo".equals(stateName)){
                zipCodeList.addAll(oyoZipCodeList!!as ArrayList<String>)
            }
            openCityListApi()

            dialogState!!.dismiss()
        }
        if(MyConstants.CITY_REQ_CODE==requestcode){
          //   stateName=stateList!!.get(position).toString()
            edtCity.text=name
            dialogCity!!.dismiss()
        }

    }

    private fun resultAllCountry(){
        mViewModel.lAllCountryParam.observe(this){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    dismissLoader()
                    //   activityUserProductListInCountryBinding.recyclerLocalProduct.visibility=View.VISIBLE
                    it.data?.let {

                        dismissLoader()
                        val mCountry=ArrayList<CountryListItem>()
                        // countryList=it.countryColorCodeList
                        for ( i in 0 until it.countryList!!.size)  {

                            mCountry.add(it.countryList[i]!!)
                        }
                        countryList=mCountry
                     //   countryDialog(it.countryList)
                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }

    private fun onFilterZipCode(){
        val value=edtZipcode.text.toString().trim()
        filterZipCode= zipCodeList.contains(value).toString()
    }
    private fun openAllCountry(){
        //Api hit
        mViewModel.stateList()

        //Api result
        mViewModel.lStateParam.observe(this){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    dismissLoader()
                    //   activityUserProductListInCountryBinding.recyclerLocalProduct.visibility=View.VISIBLE
                    it.data?.let {

                        dismissLoader()

                        val mCountry=ArrayList<String>()
                        // countryList=it.countryColorCodeList
                        for ( i in 0 until it.list!!.size)  {

                            mCountry.add(it.list[i]!!)
                        }
                        stateList=mCountry


                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }
    private fun openCityListApi(){
        //Api hit
        mViewModel.cityListParam(stateName!!)

        //Api result
        mViewModel.lCityList.observe(this){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    dismissLoader()
                    //   activityUserProductListInCountryBinding.recyclerLocalProduct.visibility=View.VISIBLE
                    it.data?.let {

                        dismissLoader()

                        val mCity=ArrayList<String>()
                        // countryList=it.countryColorCodeList
                        for ( i in 0 until it.list!!.size)  {

                            mCity.add(it.list[i]!!)
                        }
                        cityList!!.addAll(mCity)
                       /* if(isShowing==false){
                            dialogCity = Dialog(this)
                        }*/



                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }
    fun onSearchCountry(){
        dialogState!!.country_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
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
        var filterdNames: ArrayList<String?>? = ArrayList()

        //looping through existing elements
        for (s in this.stateList!!) {
            //if the existing elements contains the search input
            if (s!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames!!.add(s)
            }
        }
        filterdNames = stateAdapter.filterList(filterdNames)

    }

    fun onSearchCity(){
        dialogCity!!.country_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCity(newText.toString())
                //  countryListAdapter!!.filter.filter(newText)
                return false
            }

        })
    }
    private fun filterCity(text: String) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<String?>? = ArrayList()

        //looping through existing elements
        for (s in this.cityList!!) {
            //if the existing elements contains the search input
            if (s!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames!!.add(s)
            }
        }
        filterdNames = cityAdapter.filterCity(filterdNames)

    }

    fun onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        val fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )
        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        ).setCountry("NG") //NIGERIA
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                           Log.i("TAG", "Place: ${place.name}, ${place.id}")
                        tvAddressSugg.text=place.name
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        //  Log.i(TAG, status.statusMessage ?: "")
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }






}