package com.cradle.user.userActivity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.JsonObject
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.base_utils.BaseActivity
import com.cradle.databinding.ActivityUserEditProfileBinding
import com.cradle.intarfaces.ItemClickListner
import com.cradle.model.CountryColorCodeListItem
import com.cradle.model.address.AddressListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.AdapterCountryList
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_user_addaddress.edtFirstName
import kotlinx.android.synthetic.main.activity_user_addaddress.edtLastName
import kotlinx.android.synthetic.main.activity_user_edit_profile.*
import kotlinx.android.synthetic.main.custom_toolbar_user.*
import kotlinx.android.synthetic.main.dialog_country.*
import kotlinx.android.synthetic.main.fragment_user_home.tv_usertoolbartitle
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

class UserEditProfileActivity : BaseActivity(), View.OnClickListener,
    ItemClickListner, AdapterCountryList.TextBookNow {
    private lateinit var mBinding: ActivityUserEditProfileBinding
    private lateinit var mViewModel: MainViewModel
    val PERMISSION_REQUEST_CODE = 10001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialiseUI()
    }

    private fun initialiseUI() {
        val response = (application as ApplicationClass).repository
        mBinding = ActivityUserEditProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]

        AnalyticsUtils.analyticReport(this,"EditProfileScreen")

        getResultCountryList()
        getApiResult()
        btn_updateProfile.setOnClickListener(this)
        getIntentData()
        rl_user_profile.setOnClickListener(this)
        statusBarColourChange()
    }

    private fun statusBarColourChange() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)
        btn_updateProfile.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)

    }

    private fun apiHit(jsonObject: JsonObject) {

        mViewModel.userUpdateProfileParamParam(
            SharaGoPref.getInstance(this).getLoginToken("").toString(), jsonObject
        )
    }

    var from: String? = null
    var id = ""
    var data: AddressListItem? = null
    private fun getIntentData() {
        //   data=intent.getParcelableExtra("data")
        val firstName = intent.getStringExtra("first_name").toString()
        val last_name = intent.getStringExtra("last_name").toString()
        val emailID = intent.getStringExtra("email_id").toString()
        val image = intent.getStringExtra("image").toString()
        val country = intent.getStringExtra("country").toString()
        Glide.with(this).load(MyConstants.file_Base_URL + image).error(
            R.drawable.avatar
        ).into(mBinding.ivUserProfile)

        tv_usertoolbartitle.text = "Update Profile"
        edtFirstName.setText(firstName)
        edtLastName.setText(last_name)
        edtEmailPhoneNo.setText(emailID)
        etCountry.text = country

        iv_back.setOnClickListener {
            finish()
        }
        rl_cus_toolbar.setBackgroundColor(SharaGoPref.getInstance(this).getColor(0)!!)


    }

    private var userIamge = ""
    private fun getApiResult() {
        mViewModel.lUpdateProfileParam.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {}
                is ExceptionHandler.Success -> {
                    dismissLoader()
                    it.data?.let {
                        dismissLoader()
                        showToast("Update Profile")
                        finish()

                    }
                }
                is ExceptionHandler.Error -> {
                    dismissLoader()
                    Utility.toastMessage(this, it.errorMessage)
                }
            }
        }
        mViewModel.luploadSingleFile.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {}
                is ExceptionHandler.Success -> {
                    dismissLoader()
                    try {
                        val jsonObject = JSONObject(it.data!!.toString())
                        userIamge = jsonObject.optString("file")
                        Glide.with(this).load(MyConstants.file_Base_URL + userIamge).error(
                            R.drawable.avatar
                        ).into(mBinding.ivUserProfile)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                is ExceptionHandler.Error -> {
                    dismissLoader()
                    Utility.toastMessage(this, it.errorMessage)
                }
            }
        }
    }

    private fun checkVAlidation(): Boolean {
        if (edtFirstName.text.toString().trim().isEmpty()) {
            showToast(getString(R.string.a_first_name))
            return false
        } else if (edtLastName.text.toString().trim().isEmpty()) {
            showToast(getString(R.string.a_last_name))
            return false
        } else if (edtEmailPhoneNo.text.toString().trim().isEmpty()) {
            showToast(getString(R.string.a_mobile_email_id))
            return false
        }
        /* else if (countryName!!.isEmpty()) {
               showToast(getString(R.string.a_country))
               return false
           }*/
        return true
    }

    //COUNTRY LIST RESULT
    var countryName: String? = ""
    var countryCode: String? = null
    fun getResultCountryList() {
        //user Local Product List result
        mViewModel.liveDataCounty.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {}
                is ExceptionHandler.Success -> {
                    dismissLoader()
                    //   activityUserProductListInCountryBinding.recyclerLocalProduct.visibility=View.VISIBLE
                    it.data?.let {

                        dismissLoader()

                        // countryList=it.countryColorCodeList
                        countryDialog(it.countryColorCodeList)
                    }
                }
                is ExceptionHandler.Error -> {
                    dismissLoader()
                    Utility.toastMessage(this, it.errorMessage)
                }
            }
        }

    }

    private var dialog: Dialog? = null
    private fun countryDialog(list: List<CountryColorCodeListItem?>?) {
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_country)
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))

        val linearLayoutManager = LinearLayoutManager(this)
        dialog!!.recycler_country_list.layoutManager = linearLayoutManager
        val moviesAdapter = AdapterCountryList(this, list, this, this, "")
        dialog!!.recycler_country_list.adapter = moviesAdapter
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_updateProfile -> {
                if (checkVAlidation()) {
                    val params = JsonObject()
                    params.addProperty("firstName", edtFirstName.text.toString().trim())
                    params.addProperty("lastName", edtLastName.text.toString().trim())
                    params.addProperty("image", userIamge)
                    val mainjson = JsonObject()
                    mainjson.add("metaData", params)
                    Log.e("param", mainjson.toString())
                    apiHit(mainjson)
                }


            }
            R.id.rl_user_profile -> {
                if (PermissionKeys.checkPermission1(this)) {
                    ImagePicker.with(this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .start()
                }else{
                    requestPermission()
                }
            }
        }

    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onClickItem(position: Int, requestcode: Int) {

    }

    override fun bookSession(position: Int, data: CountryColorCodeListItem) {

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            val imageFile = File(getPath(uri)!!)
            // Use Uri object instead of File to avoid storage permissions
            //   iv_user_profile.setImageURI(uri)
            showLoader()
            uploadLogoApi(imageFile)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadLogoApi(imageFile: File) {
        val bodyList = ArrayList<MultipartBody.Part>()
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile)
        val bodydata = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
        bodyList.add(bodydata)
        mViewModel.uploadSingleFile(bodyList)
    }

    fun getPath(uri: Uri?): String? {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = this.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val path = cursor.getString(column_index)
            cursor.close()
            return path
        }
        // this is our fallback here
        return uri.path
    }
}