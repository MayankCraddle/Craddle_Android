package com.cradle.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.example.model.CitiesList
import com.example.model.StateList
import com.google.gson.JsonObject
import com.cradle.repository.QuoteRepository
import com.cradle.R
import com.cradle.model.*
import com.cradle.model.address.AddressList
import com.cradle.model.allcountry.AllCountryListResponse
import com.cradle.model.cart.UserCartItem
import com.cradle.model.category.CategoryResponse
import com.cradle.model.commit.UserCommitResponse
import com.cradle.model.orderDetail.OrderDetailResponse
import com.cradle.model.orderhistory.OrderResponse
import com.cradle.model.trade.ProductListByTradeResponse
import com.cradle.repository.ExceptionHandler
import com.cradle.utils.Utility
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MainViewModel(private val repository: QuoteRepository) : ViewModel() {

    private val logInResult = MutableLiveData<String>()
    private val mResult = MutableLiveData<String>()

    private val mutableLiveDataCountry: MutableLiveData<ExceptionHandler<CountryListResponse>> =
        MutableLiveData()
    val liveDataCounty: LiveData<ExceptionHandler<CountryListResponse>> = mutableLiveDataCountry

    private val mutableLiveDataStateList: MutableLiveData<ExceptionHandler<StateList>> =
        MutableLiveData()
    val liveDataStateList: LiveData<ExceptionHandler<StateList>> = mutableLiveDataStateList

    private val mutableLiveDataCitiesList: MutableLiveData<ExceptionHandler<CitiesList>> =
        MutableLiveData()
    val liveDataCitiesList: LiveData<ExceptionHandler<CitiesList>> = mutableLiveDataCitiesList

    private val mutableLiveDataLoginRequest: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val liveDataUserLoginRequest: LiveData<ExceptionHandler<JsonObject>> =
        mutableLiveDataLoginRequest

    private val mlUserCPssRequest: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val lUserCPssRequest: LiveData<ExceptionHandler<JsonObject>> = mlUserCPssRequest


    private val mMutableUserSignUpRequest: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val mLiveDataUserSignUpRequest: LiveData<ExceptionHandler<JsonObject>> =
        mMutableUserSignUpRequest

    private val mutableOtpUserRequest: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val liveOtpUserRequest: LiveData<ExceptionHandler<JsonObject>> = mutableOtpUserRequest

    private val mutableUserForgotPassRequest: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val liveserForgotPassRequest: LiveData<ExceptionHandler<JsonObject>> =
        mutableUserForgotPassRequest


    private val mutableLiveDataUserWishList: MutableLiveData<ExceptionHandler<UserWishListResponse>> =
        MutableLiveData()
    val liveDataUserLUserWishList: LiveData<ExceptionHandler<UserWishListResponse>> =
        mutableLiveDataUserWishList

    private val mLiveDataContent: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val lDataContent: LiveData<ExceptionHandler<JsonObject>> = mLiveDataContent

    private val mLiveBannerList: MutableLiveData<ExceptionHandler<BannerListResponse>> =
        MutableLiveData()
    val lBannerList: LiveData<ExceptionHandler<BannerListResponse>> = mLiveBannerList

    private val mLUserContentDetails: MutableLiveData<ExceptionHandler<UserBlogDetailsResponse>> =
        MutableLiveData()
    val lUserContentDetails: LiveData<ExceptionHandler<UserBlogDetailsResponse>> =
        mLUserContentDetails

    private val mLCardDetailsUser: MutableLiveData<ExceptionHandler<UserCartItem>> =
        MutableLiveData()
    val lCartDetailsUser: LiveData<ExceptionHandler<UserCartItem>> = mLCardDetailsUser

    private val mLSaveCartParam: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val lSaveCartParam: LiveData<ExceptionHandler<JsonObject>> = mLSaveCartParam

    private val mLUpdateCartParam: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val lUpdateCartParam: LiveData<ExceptionHandler<JsonObject>> = mLUpdateCartParam

    private val mLUserChagePassWithLoginRequest: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val lUserChagePassWithLoginRequest: LiveData<ExceptionHandler<JsonObject>> =
        mLUserChagePassWithLoginRequest

    private val mLUserCateReq: MutableLiveData<ExceptionHandler<CategoryListResponse>> =
        MutableLiveData()
    val lUserCateReq: LiveData<ExceptionHandler<CategoryListResponse>> = mLUserCateReq

    private val mLUserSubCateReq: MutableLiveData<ExceptionHandler<UserSubCategoryListResponse>> =
        MutableLiveData()
    val lUserSubCateReq: LiveData<ExceptionHandler<UserSubCategoryListResponse>> = mLUserSubCateReq


    private val mLUProListWithCatID: MutableLiveData<ExceptionHandler<ProductListResponse>> =
        MutableLiveData()
    val lUProListWithCatID: LiveData<ExceptionHandler<ProductListResponse>> = mLUProListWithCatID

    private val mLGetCardDetailsParam: MutableLiveData<ExceptionHandler<UserCartItem>> =
        MutableLiveData()
    val lGetCardDetailsParam: LiveData<ExceptionHandler<UserCartItem>> = mLGetCardDetailsParam

    private val mLAddProductToWishlistParam: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val lAddProductToWishlistParam: LiveData<ExceptionHandler<JsonObject>> =
        mLAddProductToWishlistParam

    fun findCountry() = viewModelScope.launch {
        repository.getCountryList().collect { values ->
            mutableLiveDataCountry.value = values
        }
    }

    private val mLAllCountryParam: MutableLiveData<ExceptionHandler<AllCountryListResponse>> =
        MutableLiveData()
    val lAllCountryParam: LiveData<ExceptionHandler<AllCountryListResponse>> = mLAllCountryParam

    fun findAllCountry() = viewModelScope.launch {
        repository.AllCountryParam().collect { values ->
            mLAllCountryParam.value = values
        }
    }


    fun findByStateID(id: Int) = viewModelScope.launch {
        repository.getStatesList(id).collect { values ->
            mutableLiveDataStateList.value = values
        }
    }

    fun findByCitiesID(id: Int) = viewModelScope.launch {
        repository.getCitiesList(id).collect { values ->
            mutableLiveDataCitiesList.value = values
        }

    }


    fun userLogin(jsonObject: JsonObject) = viewModelScope.launch {
        repository.getUserLoginRequest(jsonObject).collect { values ->
            mutableLiveDataLoginRequest.value = values
        }
    }

    private fun userSignup(jsonObject: JsonObject) = viewModelScope.launch {
        repository.getUserSignUpRequest(jsonObject).collect { values ->
            mMutableUserSignUpRequest.value = values
        }
    }

    fun userChangePass(jsonObject: JsonObject) = viewModelScope.launch {
        repository.getUserLoginRequest(jsonObject).collect { values ->
            mutableLiveDataLoginRequest.value = values
        }
    }

    //user login Screen
    var username: String = ""
    var password: String = ""

    /**
     * To pass login result to activity
     */
    fun getLogInResult(): LiveData<String> = logInResult

    fun performValidation(context: Context) {
        if (username.isEmpty()) {
            logInResult.value = context.getString(R.string.email_phonno_or_emailid)
            return
        }
        if ((password.isEmpty())) {
            logInResult.value = context.getString(R.string.enter_pass)
            return
        }



        logInResult.value = "go"
    }

    //USER SIGN UP SCREEN
    var fullName: String = ""
    var lastName: String = ""
    var email: String = ""
    var phone_no: String = ""
    var city: String = ""
    var country: String = ""
    var busName: String = ""
    var pass: String = ""
    var conPass: String = ""
    var busAdd: String = ""
    var refer: String = ""
    private var passMatch: String = ""
    private var conPassMatch: String = ""

    fun getUserSignUpResult(): LiveData<String> = mResult
    fun performValidationSignUp(context: Context) {

        if (fullName.isEmpty()) {
            mResult.value = context.getString(R.string.first_name)
            return
        }
        if (lastName.isEmpty()) {
            mResult.value = context.getString(R.string.last_name)
            return
        }
        if (!Utility.isValidEmail(email.trim { it <= ' ' })) {
            mResult.value = context.getString(R.string.emai_invalid)
            return
        }
        if (phone_no.length < 11) {

            mResult.value = context.getString(R.string.enter_phone_no)
            return
        }
        if (city.isEmpty()) {
            mResult.value = context.getString(R.string.enter_city)
            return
        }
        if (country.isEmpty()) {
            mResult.value = context.getString(R.string.enter_country)
            return
        }
        passMatch = pass

        if (!Utility.isValidPass(pass.trim { it <= ' ' })) {
            mResult.value = context.getString(R.string.pass_validation)
            return
        }


        /* if (conPass.equals(pass)) {
             mResult.value = context.getString(R.string.pass_validation)
             return
         }*/
        /* else if (MyHelper.isValidPassword(binding.etPassword.text.toString()))
             MyHelper.showMininumPasswordAlert(requireContext())
       */  passMatch = pass
        conPassMatch = conPass
        if (passMatch != conPassMatch) {
            mResult.value = context.getString(R.string.pass_match)
            return
        }
        if(refer.isNotEmpty()){
            if (!Utility.isValidEmail(refer.trim { it <= ' ' })) {
                mResult.value = context.getString(R.string.enter_valid)
                return
            }
        }
        mResult.value = "go"
        if (mResult.value == "go") {
            val mJsonObject = JsonObject()
            mJsonObject.addProperty("emailMobile", email)
            mJsonObject.addProperty("password", pass)
            mJsonObject.addProperty("country", country)
            mJsonObject.addProperty("referredBy", refer)
            mJsonObject.addProperty("fcmKey",/*SharaGoPref.getInstance(this).getFcmKey("")*/"222")

            val requestPersonalDetailUser = JsonObject()
            requestPersonalDetailUser.addProperty("firstName", fullName)
            requestPersonalDetailUser.addProperty("lastName", lastName)
            mJsonObject.add("metaData", requestPersonalDetailUser)
            userSignup(mJsonObject)
        }

    }

    //OTP verify of user when sign up
    fun userOtpSignUP(jsonObject: JsonObject) = viewModelScope.launch {
        repository.getUserOtpRequest(jsonObject).collect { values ->
            mutableOtpUserRequest.value = values
        }
    }
    private val mLResendOtpRequest: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val lResendOtpRequest: LiveData<ExceptionHandler<JsonObject>> = mLResendOtpRequest


    //OTP verify of user when sign up
    fun resendOtpReq(jsonObject: JsonObject) = viewModelScope.launch {
        repository.resendOtp(jsonObject).collect { values ->
            mLResendOtpRequest.value = values
        }
    }

    //FORGOT PASSWORD API HIT
    fun userForgotPass(jsonObject: JsonObject) = viewModelScope.launch {
        repository.getUserCPssRequest(jsonObject).collect { values ->
            mlUserCPssRequest.value = values
        }
    }

    //FORGOT PASSWORD API HIT
    fun userForgotPassVeri(jsonObject: JsonObject) = viewModelScope.launch {
        repository.getUserForgotPassRequest(jsonObject).collect { values ->
            mutableUserForgotPassRequest.value = values
        }
    }

    //CHANGE PASS WITH LOGIN  API HIT
    fun userChangePassWithLogin(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        repository.getUserChanPassWithLoginRequest(token, jsonObject).collect { values ->
            mLUserChagePassWithLoginRequest.value = values
        }
    }

    //Get wish list of user
    fun getUserWishList(token: String) = viewModelScope.launch {
        repository.getUserWishListRequest(token).collect { values ->
            mutableLiveDataUserWishList.value = values
        }
    }

    //Get CATEGORY list of user
    fun getCategoryList() = viewModelScope.launch {
        repository.getUserCateRequest(/*token*/).collect { values ->
            mLUserCateReq.value = values
        }
    }

    //Get SUB CATEGORY list of user
    fun getSubCateList(cat_id: String) = viewModelScope.launch {
        repository.getUserSunCateRequest(cat_id).collect { values ->
            mLUserSubCateReq.value = values
        }
    }
    private val mLVAllWithIDReq: MutableLiveData<ExceptionHandler<ViewAllResponse>> =
        MutableLiveData()
    val lVAllWithIDReq: LiveData<ExceptionHandler<ViewAllResponse>> = mLVAllWithIDReq


    //Get VIEW ALL WITH ID
    fun getVAllWithID(
        id: String,
        pageNum: String,
        limit: String,
        searchText: String,
        country: String
    ) = viewModelScope.launch {
        repository.getUVAllWithIDRequest(id, pageNum, limit, searchText, country)
            .collect { values ->
                mLVAllWithIDReq.value = values
            }
    }

    //Get PRODCUT LIST WITH ID
    fun getUserProListWithCatID(
        id: String,
        pageNum: String,
        limit: String,
        searchText: String,
        country: String
    ) = viewModelScope.launch {
        repository.getUProListID(id, pageNum, limit, searchText, country).collect { values ->
            mLUProListWithCatID.value = values
        }
    }

    //Get Card details of user
    fun getCardDetailsUser(token: String) = viewModelScope.launch {
        repository.getCartDetailsUserReq(token).collect { values ->
            mLCardDetailsUser.value = values
        }
    }

    //Get Card details
    fun getCardDetailsByIdParam(token: String, cart_id: String) = viewModelScope.launch {
        repository.getCardDetailsByIdReq(token, cart_id).collect { values ->
            mLGetCardDetailsParam.value = values
        }
    }

    fun getSaveCartParam(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        repository.getSaveCartReq(token, jsonObject).collect { values ->
            mLSaveCartParam.value = values
        }
    }

    fun getUpdateCartParam(token: String, cat_id: String, jsonObject: JsonObject) =
        viewModelScope.launch {
            repository.getupdateCartReq(token, cat_id, jsonObject).collect { values ->
                mLUpdateCartParam.value = values
            }
        }

    fun addProductToWishlistParam(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        repository.addProductToWishlistReq(token, jsonObject).collect { values ->
            mLAddProductToWishlistParam.value = values
        }
    }

    private val mLRemoveProductFromWishlistParam: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val lremoveProductFromWishlistParam: LiveData<ExceptionHandler<JsonObject>> =
        mLRemoveProductFromWishlistParam

    fun removeProductFromWishlistParam(token: String, jsonObject: JsonObject) =
        viewModelScope.launch {
            repository.removeProductFromWishlistReq(token, jsonObject).collect { values ->
                mLRemoveProductFromWishlistParam.value = values
            }
        }

    private val mLremoveItemFromCartParam: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val lRemoveItemFromCartParam: LiveData<ExceptionHandler<JsonObject>> = mLremoveItemFromCartParam

    fun removeItemFromCartParam(token: String, cat_id: String, item_id: String) =
        viewModelScope.launch {
            repository.removeItemFromCartReq(token, cat_id, item_id).collect { values ->
                mLremoveItemFromCartParam.value = values
            }
        }

    private val mlAddressListParam: MutableLiveData<ExceptionHandler<AddressList>> =
        MutableLiveData()
    val lAddressListParam: LiveData<ExceptionHandler<AddressList>> = mlAddressListParam

    fun userAddressListParam(token: String) = viewModelScope.launch {
        repository.getUserAddressListReq(token).collect { values ->
            mlAddressListParam.value = values
        }
    }

    private val mLaddAddressParam: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val laddAddressParam: LiveData<ExceptionHandler<JsonObject>> = mLaddAddressParam

    fun userAddAddress(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        repository.addAddressReq(token, jsonObject).collect { values ->
            mLaddAddressParam.value = values
        }
    }

    private val mLUserUpdateAddressParam: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val lUserUpdateAddressParam: LiveData<ExceptionHandler<JsonObject>> = mLUserUpdateAddressParam

    fun userUpdateAddress(token: String, jsonObject: JsonObject, id: String) =
        viewModelScope.launch {
            repository.updateAddressReq(token, jsonObject, id).collect { values ->
                mLUserUpdateAddressParam.value = values
            }
        }


    private val mLDeleteAddressParam: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val lDeleteAddressParam: LiveData<ExceptionHandler<JsonObject>> = mLDeleteAddressParam

    fun userDeleteAddress(token: String, id: String) = viewModelScope.launch {
        repository.userDeleteAddressReq(token, id).collect { values ->
            mLDeleteAddressParam.value = values
        }
    }

    private val mLuserMarkAsDefaultParam: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val luserMarkAsDefaultParam: LiveData<ExceptionHandler<JsonObject>> = mLuserMarkAsDefaultParam

    fun userMarkAsDefault(token: String, id: String) = viewModelScope.launch {
        repository.userMarkAsDefaultReq(token, id).collect { values ->
            mLuserMarkAsDefaultParam.value = values
        }
    }


    private val mLUserDetailByTokenReqParam: MutableLiveData<ExceptionHandler<UserDetailsResponse>> =
        MutableLiveData()
    val luserDetailByTokenReqParam: LiveData<ExceptionHandler<UserDetailsResponse>> =
        mLUserDetailByTokenReqParam

    fun userDetailByTokenParam(token: String) = viewModelScope.launch {
        repository.userDetailByTokenReq(token).collect { values ->
            mLUserDetailByTokenReqParam.value = values
        }
    }

    private val mLUsergetProductDetailParam: MutableLiveData<ExceptionHandler<ProductDetailsResponse>> =
        MutableLiveData()
    val lmLusergetProductDetailParam: LiveData<ExceptionHandler<ProductDetailsResponse>> =
        mLUsergetProductDetailParam

    fun usergetProductDetailParam(item_id: String, uerId: String) = viewModelScope.launch {
        repository.usergetProductDetailReq(item_id, uerId).collect { values ->
            mLUsergetProductDetailParam.value = values
        }
    }

    private val mLSimilarProductsParam: MutableLiveData<ExceptionHandler<ProductSoldByVendor>> =
        MutableLiveData()
    val lSimilarProductsParam: LiveData<ExceptionHandler<ProductSoldByVendor>> =
        mLSimilarProductsParam

    fun userSimilarProductsParam(item_id: String) = viewModelScope.launch {
        repository.userSimilarProductsReq(item_id).collect { values ->
            mLSimilarProductsParam.value = values
        }
    }

    private val mLOnSaleProductsParam: MutableLiveData<ExceptionHandler<OnSaleResponse>> =
        MutableLiveData()
    val lOnSaleProductsParam: LiveData<ExceptionHandler<OnSaleResponse>> = mLOnSaleProductsParam

    fun userOnSaleProductsParam(item_id: String) = viewModelScope.launch {
        repository.usernSaleProductsReq(item_id).collect { values ->
            mLOnSaleProductsParam.value = values
        }
    }

    private val mLotherProductsSoldByVendorParam: MutableLiveData<ExceptionHandler<OnSaleResponse>> =
        MutableLiveData()
    val lotherProductsSoldByVendorParam: LiveData<ExceptionHandler<OnSaleResponse>> =
        mLotherProductsSoldByVendorParam

    fun otherProductsSoldByVendorParam(item_id: String) = viewModelScope.launch {
        repository.otherProductsSoldByVendorReq(item_id).collect { values ->
            mLotherProductsSoldByVendorParam.value = values
        }
    }

    private val mLnewArrivals: MutableLiveData<ExceptionHandler<OnSaleResponse>> = MutableLiveData()
    val lnewArrivalsParam: LiveData<ExceptionHandler<OnSaleResponse>> = mLnewArrivals

    fun newArrivalsParam(country: String) = viewModelScope.launch {
        repository.newArrivalsReq(country).collect { values ->
            mLnewArrivals.value = values
        }
    }

    private val mLnewArrivalsByTrade: MutableLiveData<ExceptionHandler<ProductListByTradeResponse>> =
        MutableLiveData()
    val lnewArrivalsByTrade: LiveData<ExceptionHandler<ProductListByTradeResponse>> =
        mLnewArrivalsByTrade

    fun newArrivalsByTradParam(
        country: String,
        categoryId: String,
        sorting: String,
        pageNumber: String,
        limit: String
    ) = viewModelScope.launch {
        repository.newArrivalsByTradeReq(country, categoryId, sorting, pageNumber, limit)
            .collect { values ->
                mLnewArrivalsByTrade.value = values
            }
    }

    private val mLtopDealsByCountry: MutableLiveData<ExceptionHandler<ProductListByTradeResponse>> =
        MutableLiveData()
    val ltopDealsByCountry: LiveData<ExceptionHandler<ProductListByTradeResponse>> =
        mLtopDealsByCountry

    fun topDealsByCountryParam(
        country: String,
        categoryId: String,
        sorting: String,
        pageNumber: String,
        limit: String
    ) = viewModelScope.launch {
        repository.topDealsByCountryReq(country, categoryId, sorting, pageNumber, limit)
            .collect { values ->
                mLtopDealsByCountry.value = values
            }
    }

    private val mLonSaleByCountry: MutableLiveData<ExceptionHandler<ProductListByTradeResponse>> =
        MutableLiveData()
    val lonSaleByCountryReq: LiveData<ExceptionHandler<ProductListByTradeResponse>> =
        mLonSaleByCountry

    fun onSaleByCountryParam(
        country: String,
        categoryId: String,
        sorting: String,
        pageNumber: String,
        limit: String
    ) = viewModelScope.launch {
        repository.onSaleByCountryReq(country, categoryId, sorting, pageNumber, limit)
            .collect { values ->
                mLonSaleByCountry.value = values
            }
    }

    private val mLUserTopDeals: MutableLiveData<ExceptionHandler<OnSaleResponse>> =
        MutableLiveData()
    val lUserTopDeals: LiveData<ExceptionHandler<OnSaleResponse>> = mLUserTopDeals

    fun userTopDealsParam(country: String) = viewModelScope.launch {
        repository.userTopDealReq(country).collect { values ->
            mLUserTopDeals.value = values
        }
    }

    private val mLUpdateProfileParam: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val lUpdateProfileParam: LiveData<ExceptionHandler<JsonObject>> = mLUpdateProfileParam

    fun userUpdateProfileParamParam(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        repository.updateProfileReq(token, jsonObject).collect { values ->
            mLUpdateProfileParam.value = values
        }
    }

    private val mLgiveRating: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val lgiveRating: LiveData<ExceptionHandler<JsonObject>> = mLgiveRating

    fun userGiveRatingParam(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        repository.userGiveRatingReq(token, jsonObject).collect { values ->
            mLgiveRating.value = values
        }
    }

    private val mLAddCommit: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val lAddCommit: LiveData<ExceptionHandler<JsonObject>> = mLAddCommit

    fun userAddCommitParam(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        repository.userAddCommitReq(token, jsonObject).collect { values ->
            mLAddCommit.value = values
        }
    }


    private val mLGetCommit: MutableLiveData<ExceptionHandler<UserCommitResponse>> =
        MutableLiveData()
    val lGetCommit: LiveData<ExceptionHandler<UserCommitResponse>> = mLGetCommit

    fun getCommitParam(id: Int) = viewModelScope.launch {
        repository.getCommitReq(id).collect { values ->
            mLGetCommit.value = values
        }
    }


    private val mLgetSectionList: MutableLiveData<ExceptionHandler<GetSetionAllListResponse>> =
        MutableLiveData()
    val lgetSectionList: LiveData<ExceptionHandler<GetSetionAllListResponse>> = mLgetSectionList

    fun userSectionListParam(pageNum: String, limit: String, searchText: String, country: String) =
        viewModelScope.launch {
            repository.getSectionListReq(pageNum, limit, searchText, country).collect { values ->
                mLgetSectionList.value = values
            }
        }

    private val mLUserVendorList: MutableLiveData<ExceptionHandler<UserVenorListResponse>> =
        MutableLiveData()
    val lUserVendorList: LiveData<ExceptionHandler<UserVenorListResponse>> = mLUserVendorList

    fun userVendorListParam(type: String, country: String) = viewModelScope.launch {
        repository.getVendorListReq(type, country).collect { values ->
            mLUserVendorList.value = values
        }
    }

    private val mLgetSearchList: MutableLiveData<ExceptionHandler<SearchVendorResponse>> =
        MutableLiveData()
    val lgetSearchList: LiveData<ExceptionHandler<SearchVendorResponse>> = mLgetSearchList

    fun getSearchListParam(
        type: String,
        country: String,
        searchText: String,
        pageNum: Int,
        limit: Int
    ) = viewModelScope.launch {
        repository.getSearchListReq(type, country, searchText, pageNum, limit).collect { values ->
            mLgetSearchList.value = values
        }
    }

    private val mLgetgetSearchContent: MutableLiveData<ExceptionHandler<CotentSearchListResponse>> =
        MutableLiveData()
    val lgetSearchContent: LiveData<ExceptionHandler<CotentSearchListResponse>> =
        mLgetgetSearchContent

    fun getSearchContentParam(
        type: String,
        country: String,
        searchText: String,
        pageNum: Int,
        limit: Int
    ) = viewModelScope.launch {
        repository.getSearchContentReq(type, country, searchText, pageNum, limit)
            .collect { values ->
                mLgetgetSearchContent.value = values
            }
    }

    private val mLUserVendorDetail: MutableLiveData<ExceptionHandler<VendorDetailResponse>> =
        MutableLiveData()
    val lUserVendorDetail: LiveData<ExceptionHandler<VendorDetailResponse>> = mLUserVendorDetail

    fun userVendorDetailParam(vendorId: String) = viewModelScope.launch {
        repository.userVendorDetailReq(vendorId).collect { values ->
            mLUserVendorDetail.value = values
        }
    }

    private val mLUserVendorRatingDetails: MutableLiveData<ExceptionHandler<UserVendorRatingResponse>> =
        MutableLiveData()
    val lUserVendorRatingDetails: LiveData<ExceptionHandler<UserVendorRatingResponse>> =
        mLUserVendorRatingDetails

    fun userVendorRatingDetails(vendorId: String) = viewModelScope.launch {
        repository.userVendorRatingDetailsReq(vendorId).collect { values ->
            mLUserVendorRatingDetails.value = values
        }
    }

    private val mLproductReview: MutableLiveData<ExceptionHandler<UserCommitResponse>> =
        MutableLiveData()
    val lproductReview: LiveData<ExceptionHandler<UserCommitResponse>> =
        mLproductReview

    fun productReviewParam(vendorId: String) = viewModelScope.launch {
        repository.productReviewReq(vendorId).collect { values ->
            mLproductReview.value = values
        }
    }

    private val mLUuserNewProductsofVendor: MutableLiveData<ExceptionHandler<NewProductListOfVendorResponse>> =
        MutableLiveData()
    val lUuserNewProductsofVendor: LiveData<ExceptionHandler<NewProductListOfVendorResponse>> =
        mLUuserNewProductsofVendor

    fun userNewProductsofVendor(vendorId: String) = viewModelScope.launch {
        repository.userNewProductsReq(vendorId).collect { values ->
            mLUuserNewProductsofVendor.value = values
        }
    }

    private val mLDiscountProductsofVendor: MutableLiveData<ExceptionHandler<NewProductListOfVendorResponse>> =
        MutableLiveData()
    val lDiscountProductsofVendor: LiveData<ExceptionHandler<NewProductListOfVendorResponse>> =
        mLDiscountProductsofVendor

    fun getDiscountProductsListofVendor(vendorId: String) = viewModelScope.launch {
        repository.getDiscountProductsReq(vendorId).collect { values ->
            mLDiscountProductsofVendor.value = values
        }
    }

    private val mLRequestForSampleProducts: MutableLiveData<ExceptionHandler<NewProductListOfVendorResponse>> =
        MutableLiveData()
    val lRequestForSampleProducts: LiveData<ExceptionHandler<NewProductListOfVendorResponse>> =
        mLRequestForSampleProducts

    fun getRequestForSampleProducts(vendorId: String) = viewModelScope.launch {
        repository.getRequestForSampleProductsReq(vendorId).collect { values ->
            mLRequestForSampleProducts.value = values
        }
    }

    private val mLuploadSingleFile: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val luploadSingleFile: LiveData<ExceptionHandler<JsonObject>> = mLuploadSingleFile

    fun uploadSingleFile(list: List<MultipartBody.Part>) = viewModelScope.launch {
        repository.uploadSingleFileReq(list).collect { values ->
            mLuploadSingleFile.value = values
        }
    }

    private val mLContentWithSectiot: MutableLiveData<ExceptionHandler<JsonObject>> =
        MutableLiveData()
    val lContentWithSectiot: LiveData<ExceptionHandler<JsonObject>> = mLContentWithSectiot

    fun userSectionListParam(country: String, type: String) = viewModelScope.launch {
        repository.getContentWithSectionReq(country, type).collect { values ->
            mLContentWithSectiot.value = values
        }
    }

    private val mLCategory: MutableLiveData<ExceptionHandler<CategoryResponse>> = MutableLiveData()
    val lCategory: LiveData<ExceptionHandler<CategoryResponse>> = mLCategory

    //Get CATEGORY list of user
    fun categoryParam() = viewModelScope.launch {
        repository.categoryRequest(/*token*/).collect { values ->
            mLCategory.value = values
        }
    }

    private val mLverifyPayment: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val lverifyPayment: LiveData<ExceptionHandler<JsonObject>> = mLverifyPayment

    fun verifyPaymentParam(tx_ref: String, transaction_id: String, cartid: String) =
        viewModelScope.launch {
            repository.verifyPaymentReq(tx_ref, transaction_id, cartid).collect { values ->
                mLverifyPayment.value = values
            }
        }


    //Get Content detail  of user
    fun getContentDetail(id: Int) = viewModelScope.launch {
        repository.getUserContentDetailsReq(id).collect { values ->
            mLUserContentDetails.value = values
        }
    }

    //Get wish list of user
    fun getContentWithSections(string: String, token: String) = viewModelScope.launch {
        repository.getContentWithSectionsRequest(string, token).collect { values ->
            mLiveDataContent.value = values
        }
    }

    //Get Banner list of user
    fun getUserBannerList() = viewModelScope.launch {
        repository.getUserBannerListReq().collect { values ->
            mLiveBannerList.value = values
        }
    }

    private val mLProcessOrder: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val lProcessOrder: LiveData<ExceptionHandler<JsonObject>> = mLProcessOrder


    fun processOrderParam(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        repository.processOrderReq(token, jsonObject).collect { values ->
            mLProcessOrder.value = values
        }
    }

    private val mLOrder: MutableLiveData<ExceptionHandler<OrderResponse>> = MutableLiveData()
    val lOrder: LiveData<ExceptionHandler<OrderResponse>> = mLOrder


    fun orderListParam(token: String) = viewModelScope.launch {
        repository.orderListReq(token).collect { values ->
            mLOrder.value = values
        }
    }

    private val mLtrackOrd: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val ltrackOrd: LiveData<ExceptionHandler<JsonObject>> = mLtrackOrd
    fun trackOrderApi(token: String, orderId: String) = viewModelScope.launch {
        repository.trackOrder(token, orderId).collect { values ->
            mLtrackOrd.value = values
        }
    }

    private val mLOrdDetails: MutableLiveData<ExceptionHandler<OrderDetailResponse>> =
        MutableLiveData()
    val lOrdDetails: LiveData<ExceptionHandler<OrderDetailResponse>> = mLOrdDetails
    fun ordDetailParam(token: String, orderId: String) = viewModelScope.launch {
        repository.orderDetail(token, orderId).collect { values ->
            mLOrdDetails.value = values
        }
    }

    //TODO: get Shipment Rate Api ModelView
    private val mshipmentRate: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val lshipmentRate: LiveData<ExceptionHandler<JsonObject>> = mshipmentRate
    fun getShipmentRateApi(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        repository.getShipmentRate(token, jsonObject).collect { values ->
            mshipmentRate.value = values
        }
    }

    //TODO: OrderCancel APi
    private val mLCancelOrder: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val lCancelOrder: LiveData<ExceptionHandler<JsonObject>> = mLCancelOrder
    fun cancelOrderParam(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        repository.cancelOrderReq(token, jsonObject).collect { values ->
            mLCancelOrder.value = values
        }
    }

    private val mLFbLogin: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val lFbLogin: LiveData<ExceptionHandler<JsonObject>> = mLFbLogin
    fun loginWithSocialMediaParam(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        repository.loginWithSocialMediaReq(token, jsonObject).collect { values ->
            mLFbLogin.value = values
        }
    }

    private val mLChangeUserAccountStatus: MutableLiveData<ExceptionHandler<JsonObject>> = MutableLiveData()
    val lchangeUserAccountStatus: LiveData<ExceptionHandler<JsonObject>> = mLChangeUserAccountStatus
    fun changeUserAccountStatusParam(token: String, jsonObject: JsonObject) = viewModelScope.launch {
        repository.changeUserAccountStatusReq(token, jsonObject).collect { values ->
            mLChangeUserAccountStatus.value = values
        }
    }
    private val mLStateParam: MutableLiveData<ExceptionHandler<NewStateListResponse>> =
        MutableLiveData()
    val lStateParam: LiveData<ExceptionHandler<NewStateListResponse>> = mLStateParam

    fun stateList() = viewModelScope.launch {
        repository.stateListReq().collect { values ->
            mLStateParam.value = values
        }
    }

    private val mLCityList: MutableLiveData<ExceptionHandler<NewCityListResponse>> = MutableLiveData()
    val lCityList: LiveData<ExceptionHandler<NewCityListResponse>> = mLCityList
    fun cityListParam(stateName:String) = viewModelScope.launch {
        repository.cityListReq(stateName).collect { values ->
            mLCityList.value = values
        }
    }

    private val mLNotificationList: MutableLiveData<ExceptionHandler<NotificationListResponse>> = MutableLiveData()
    val lNotificationList: LiveData<ExceptionHandler<NotificationListResponse>> = mLNotificationList
    fun notificationListParam(token: String,pageNumber: String,limit: String) = viewModelScope.launch {
        repository.notificationReq(token,pageNumber,limit).collect { values ->
            mLNotificationList.value = values
        }
    }
    private val mLMultimediaProducts: MutableLiveData<ExceptionHandler<MaltiMediaWithProductResponse>> = MutableLiveData()
    val lMultimediaProducts: LiveData<ExceptionHandler<MaltiMediaWithProductResponse>> = mLMultimediaProducts
    fun multimediaProductsParam(country: String) = viewModelScope.launch {
        repository.multimediaProductsReq(country).collect { values ->
            mLMultimediaProducts.value = values
        }
    }
}