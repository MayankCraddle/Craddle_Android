package com.cradle.user.userActivity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.common_screen.OtpActivity
import com.cradle.databinding.ActivityUserSignupBinding
import com.cradle.intarfaces.ItemClickListner
import com.cradle.intarfaces.LogInHandler
import com.cradle.model.CountryColorCodeListItem
import com.cradle.model.allcountry.CountryListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.AdapterCountryList
import com.cradle.user.adapters.CountryAllAdapter
import com.cradle.utils.AnalyticsUtils
import com.cradle.utils.MyConstants
import com.cradle.utils.Utility
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_signup.*
import kotlinx.android.synthetic.main.dialog_country.*
import kotlinx.android.synthetic.main.dialog_country.country_search
import org.json.JSONObject


class UserSignUpActivity : BaseActivity(), LogInHandler, ItemClickListner, View.OnClickListener,AdapterCountryList.TextBookNow,CountryAllAdapter.TextBookNow {

    private lateinit var moviesAdapter: AdapterCountryList
    private lateinit var countryAllAdapter: CountryAllAdapter
    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityUserSignupBinding
    private var dialog: Dialog? = null
    var countryList: List<CountryColorCodeListItem?>? = null

    var allCountryList: List<CountryListItem?>? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        uiInitialise()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun uiInitialise() {
        val response = (application as ApplicationClass).repository
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_signup)
        mViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.viewModel=mViewModel
        mBinding.handler=this
        mBinding.setVariable(BR.onSignInClick,this)
        AnalyticsUtils.analyticReport(this,MyConstants.SignupScreen)
        mBinding.view.setOnClickListener{
         //   showLoader()
          //  mViewModel.findCountry()
            countryDialogNew(allCountryList)

        }
        onClickButtonResult()
        openAllCountry()
        signUpApiResult()
        getResultCountryList()
        ll_countryClick.setOnClickListener(this)

        statusBarColourChange()

    }
    // Watching for Sign Up button click result
    fun onClickButtonResult(){
        mViewModel.getUserSignUpResult().observe(this) { result ->
            if (result.equals("go")){
                showLoader()
            }else{
                Utility.toastMessage(this,result)
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tv_signInBack->{
                startActivity(Intent(this,UserLoginActivity::class.java))
                finish()
            }  R.id.pass_eye->{
            //   showHidePassword(v)
                finish()
            }  R.id.ll_countryClick->{
          //  showLoader()
          //  mViewModel.findCountry()
            countryDialogNew(allCountryList)


                 } R.id.open_con->{
         /*   showLoader()
            mViewModel.findCountry()*/
                 }
        }

    }



    override fun onClickItem(position: Int, requestcode: Int) {
        if (MyConstants.COUNTRY_SELECTBY_SIGNUP_REQ_CODE==requestcode){
            mBinding.tvCountrySelectName.visibility=View.VISIBLE
         //   mBinding.tvCountrySelectName.text=countryList!!.get(position)!!.countryName
            dialog!!.dismiss()
        }
    }

    override fun onLogInClicked() {
        mViewModel.performValidationSignUp(this)
    }



    //user login result
    private fun signUpApiResult(){
        mViewModel.mLiveDataUserSignUpRequest.observe(this){
            when(it){
                is ExceptionHandler.Loading->{
                    dismissLoader()
                }
                is ExceptionHandler.Success->{
                    dismissLoader()
                    /*{"message":"User Already Exist!","code":208,"success":false}*/
                    val jsonObject = JSONObject(it.data!!.toString())
                    Log.e("SignUP",jsonObject.toString())
                  val success=  jsonObject.optString("success")
                    if (success.equals("false")){
                        showToast("User Already Exist")
                    }else{
                         startActivity(Intent(this,OtpActivity::class.java).putExtra("otpId",jsonObject.optString("otpId")))
                     finish()

                    }

                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }

    private fun countryDialog(list:List<CountryColorCodeListItem?>?) {
        dialog = Dialog(this@UserSignUpActivity)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_country)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))

        val  linearLayoutManager= LinearLayoutManager(this)
        dialog!!.card_country.visibility=View.VISIBLE
        dialog!!.recycler_country_list.layoutManager = linearLayoutManager
         moviesAdapter= AdapterCountryList(this@UserSignUpActivity,countryList,this,this,"")
        dialog!!.recycler_country_list.adapter=moviesAdapter
        dialog!!.setCanceledOnTouchOutside(true)
       // onSearchCountry()
        dialog!!.show()
    }

    //COUNTRY LIST RESULT
    val activeCountry=ArrayList<CountryColorCodeListItem>()
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
                        val noActivieCountry=ArrayList<CountryColorCodeListItem>()
                        for ( i in 0 until it.countryColorCodeList!!.size)  {
                            if((it.countryColorCodeList[i]!!.active!!))
                                activeCountry.add(it.countryColorCodeList[i]!!)
                            else
                                noActivieCountry.add(it.countryColorCodeList[i]!!)
                        }
                        activeCountry.addAll(noActivieCountry)
                        countryList=activeCountry
                        countryDialog(it.countryColorCodeList)
                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }

    }

    override fun bookSession(position: Int, data: CountryColorCodeListItem) {
        mBinding.tvCountrySelectName.visibility=View.VISIBLE
        mBinding.tvCountrySelectName.setText(data.countryName.toString())
        dialog!!.dismiss()

    }

    fun showHidePassword(view: View) {
        view.isSelected = !view.isSelected

        val positon: Int = et_pass.text!!.length
        if (view.isSelected) {
            //show
            et_pass.transformationMethod = null
            et_pass.setSelection(positon)
        } else {
            //hide
            et_pass.transformationMethod = PasswordTransformationMethod.getInstance()
            et_pass.setSelection(positon)
        }
    }
    //Country list api
    fun onSearchCountry(){
        dialog1!!.country_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
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
        var filterdNames: ArrayList<CountryListItem> = ArrayList()

        //looping through existing elements
        for (s in this.allCountryList!!) {
            //if the existing elements contains the search input
            if (s!!.countryName!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        filterdNames = countryAllAdapter.filterList(filterdNames)

    }
    private fun openAllCountry(){
        //Api hit
        mViewModel.findAllCountry()

        //Api result
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
                        allCountryList=mCountry



                    }
                }
                is ExceptionHandler.Error->{
                    dismissLoader()
                    Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }
    private var dialog1: Dialog? = null
    private fun countryDialogNew(list: List<CountryListItem?>?) {
        dialog1= Dialog(this)
        dialog1!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1!!.setContentView(R.layout.dialog_country)
        dialog1!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog1!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))

        val  linearLayoutManager= LinearLayoutManager(this)
        dialog1!!.recycler_country_list.layoutManager = linearLayoutManager
        countryAllAdapter= CountryAllAdapter(this,list,this,this,"")
        dialog1!!.recycler_country_list.adapter=countryAllAdapter
        dialog1!!.setCanceledOnTouchOutside(true)
        onSearchCountry()
        dialog1!!.show()
        dialog1!!.llCloseDialog.setOnClickListener { dialog1!!.dismiss() }

    }

    override fun bookSession(position: Int, data: CountryListItem?) {
        mBinding.tvCountrySelectName.visibility=View.VISIBLE
        mBinding.tvCountrySelectName.setText(data!!.countryName.toString())
        dialog1!!.dismiss()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun statusBarColourChange() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(getColor(R.color.blue_light))
    }
}