package com.cradle.api

import com.example.model.CitiesList
import com.example.model.StateList
import com.google.gson.JsonObject
import com.cradle.model.*
import com.cradle.model.address.AddressList
import com.cradle.model.allcountry.AllCountryListResponse
import com.cradle.model.cart.UserCartItem
import com.cradle.model.category.CategoryResponse
import com.cradle.model.commit.UserCommitResponse
import com.cradle.model.orderDetail.OrderDetailResponse
import com.cradle.model.orderhistory.OrderResponse
import com.cradle.model.trade.ProductListByTradeResponse
import okhttp3.MultipartBody

import retrofit2.Response
import retrofit2.http.*


interface RetrofitService {
    @GET("saharaGo/getCountryListWithColor")
    suspend fun getCountry(): Response<CountryListResponse>

    @GET("saharaGo/getAllCountryList")
    suspend fun allCountryReq(): Response<AllCountryListResponse>

    @GET("hc/v1/getStates/{country}")
    suspend fun getStates(@Path("country") country: Int?): Response<StateList>

    @GET("hc/v1/getCities/{state}")
    suspend fun getCities(@Path("state") state: Int?): Response<CitiesList>
    // @POST("hc/v1/loginAsPatient") suspend fun getVendorLoginRequest(@Body loginRequest: JsonObject):Response<LoginRequest>

    //USER APIS

    @POST("user/login")
    suspend fun getUserLoginRequest(@Body loginRequest: JsonObject): Response<JsonObject>

    @POST("user/signUp")
    suspend fun getUserSignUpRequest(@Body loginRequest: JsonObject): Response<JsonObject>

    @POST("user/signUpOtpVerification")
    suspend fun getUsersignUpOtpVerificationRequest(@Body loginRequest: JsonObject): Response<JsonObject>

    @POST("user/resendOtp")
    suspend fun resendOtpApi(@Body loginRequest: JsonObject): Response<JsonObject>

    @PUT("user/forgotPassword")
    suspend fun getUserForgotPassApi(@Body loginRequest: JsonObject): Response<JsonObject>

    @PUT("user/forgotPasswordVerification")
    suspend fun userCPVerification(@Body loginRequest: JsonObject): Response<JsonObject>

    @PUT("user/changePassword")
    suspend fun getUserChanPassWithLoginApi(
        @Header("token") token: String?, @Body loginRequest: JsonObject
    ): Response<JsonObject>


    @GET("user/getWishlist")
    suspend fun getuserWishList(@Header("token") token: String?): Response<UserWishListResponse>

    @GET("saharaGo/getAllCategory")
    suspend fun getUserCatApi(): Response<CategoryListResponse>

    @GET("saharaGo/getCategoriesWithSubCategory")
    suspend fun categoryApi(): Response<CategoryResponse>

    @GET("saharaGo/getSubCategory/{categoryId}")
    suspend fun getUserSubCatelApi(@Path("categoryId") id: String?): Response<UserSubCategoryListResponse>

    @GET("user/getContentWithSections/")
    suspend fun contentWithSectionsApi(@Query("country") country: String?): Response<JsonObject>


    @GET("user/getProducts/{pageNumber}/{limit}")
    suspend fun getUProListApi(
        @Path("pageNumber") pageNumber: String,
        @Path("limit") limit: String,
        @Query("country") country: String,
        @Query("categoryId") categoryId: String,
        @Query("search") search: String,
        @Query("sort") sort: String
    ): Response<ProductListResponse>

    @GET("saharaGo/getBanners")
    suspend fun userBannerApi(): Response<BannerListResponse>

    //cart api
    @GET("user/getCartDetailOfAUser")
    suspend fun cartDetailsUserApi(@Header("token") token: String?): Response<UserCartItem>

    @GET("user/getCartDetail/{cartId}")
    suspend fun cardDetailsApi(
        @Header("token") token: String?, @Path("cartId") categoryId: String
    ): Response<UserCartItem>

    @POST("user/saveCart")
    suspend fun saveCartApi(
        @Header("token") token: String?, @Body loginRequest: JsonObject
    ): Response<JsonObject>

    @PUT("user/updateCart/{cartId}")
    suspend fun updateCartApi(
        @Header("token") token: String?, @Path("cartId") categoryId: String, @Body data: JsonObject
    ): Response<JsonObject>

    //WISH LIST OF OUSER
    @POST("user/addProductToWishlist")
    suspend fun addProductToWishlist(
        @Header("token") token: String?, @Body data: JsonObject
    ): Response<JsonObject>

    @PUT("user/removeProductFromWishlist")
    suspend fun removeProductFromWishlist(
        @Header("token") token: String?, @Body data: JsonObject
    ): Response<JsonObject>


    @PUT("user/removeItemFromCart/{cartId}/{itemId}")
    suspend fun removeItemFromCart(
        @Header("token") token: String?,
        @Path("cartId") cartId: String,
        @Path("itemId") itemId: String
    ): Response<JsonObject>

    @GET("user/getContentDetails/{id}")
    suspend fun getuserContentDetailApi(@Path("id") id: Int?): Response<UserBlogDetailsResponse>

    @GET("/api/v1/user/getAllAddresses")
    suspend fun getAllAddresses(@Header("token") token: String): Response<AddressList>

    @POST("/api/v1/user/addAddress")
    suspend fun addAddressApi(
        @Header("token") token: String?, @Body data: JsonObject
    ): Response<JsonObject>

    @DELETE("/api/v1/user/deleteAddress/{id}")
    suspend fun deleteAddress(
        @Header("token") token: String?, @Path("id") id: String
    ): Response<JsonObject>

    @PUT("/api/v1/user/updateAddress/{id}")
    suspend fun updateAddress(
        @Header("token") token: String?, @Body data: JsonObject, @Path("id") id: String
    ): Response<JsonObject>

    @PUT("/api/v1/user/markAsDefault/{id}")
    suspend fun markAsDefault(
        @Header("token") token: String?, @Path("id") id: String
    ): Response<JsonObject>

    @GET("user/getUserDetailByToken")
    suspend fun getUserDetailByToken(@Header("token") token: String?): Response<UserDetailsResponse>

    @GET("user/getProductDetail/{itemId}")
    suspend fun getProductDetail(
        @Path("itemId") itemId: String, @Query("userId") userId: String?
    ): Response<ProductDetailsResponse>

    @GET("user/getSimilarProducts/{itemId}")
    suspend fun similarProducts(@Path("itemId") itemId: String): Response<ProductSoldByVendor>

    @GET("user/getOnSaleProducts/{itemId}")
    suspend fun onSaleProducts(@Path("itemId") itemId: String): Response<OnSaleResponse>

    @GET("user/getOnSaleProducts/{itemId}")
    suspend fun otherProductsSoldByVendor(@Path("itemId") itemId: String): Response<OnSaleResponse>

    @GET("user/getNewProductsByCountry")
    suspend fun getNewArrivals(@Query("country") country: String?): Response<OnSaleResponse>

    @GET("user/getNewProductsByCountry")
    suspend fun getNewArrivalsByTrade(
        @Query("country") country: String?,
        @Query("categoryId") categoryId: String?,
        @Query("sorting") sorting: String?,
        @Query("pageNumber") pageNumber: String?,
        @Query("limit") limit: String?
    ): Response<ProductListByTradeResponse>

    @GET("user/getTopDealsByCountry")
    suspend fun getTopDealsByCountry(
        @Query("country") country: String?,
        @Query("categoryId") categoryId: String?,
        @Query("sorting") sorting: String?,
        @Query("pageNumber") pageNumber: String?,
        @Query("limit") limit: String?
    ): Response<ProductListByTradeResponse>

    @GET("user/getOnSaleByCountry")
    suspend fun getOnSaleByCountry(
        @Query("country") country: String?,
        @Query("categoryId") categoryId: String?,
        @Query("sorting") sorting: String?,
        @Query("pageNumber") pageNumber: String?,
        @Query("limit") limit: String?
    ): Response<ProductListByTradeResponse>

    @GET("user/getTopDeals")
    suspend fun getTopDeals(@Query("country") country: String?): Response<OnSaleResponse>

    @POST("user/giveRating")
    suspend fun getRating(
        @Header("token") token: String?, @Body data: JsonObject
    ): Response<JsonObject>

    @POST("user/addComment")
    suspend fun userAddCommit(
        @Header("token") token: String?, @Body data: JsonObject
    ): Response<JsonObject>

    @GET("saharaGo/getComments/{id}")
    suspend fun getCommit(@Path("id") id: Int?): Response<UserCommitResponse>

    @PUT("user/updateProfile")
    suspend fun updateProfile(
        @Header("token") token: String?, @Body data: JsonObject
    ): Response<JsonObject>

    @GET("user/getSectionList")
    suspend fun getSectionList(
       /* @Path("pageNumber") pageNum: String?,
        @Path("limit") limit: String?,
        @Query("searchText") searchText: String?,*/
        @Query("country") country: String?
    ): Response<GetSetionAllListResponse>

    @GET("user/getContentListBySection/{id}/{pageNumber}/{limit}")
    suspend fun getVAllWithID(
        @Path("id") id: String?,
        @Path("pageNumber") pageNum: String?,
        @Path("limit") limit: String?,
        @Query("searchText") searchText: String?,
        @Query("country") country: String?
    ): Response<ViewAllResponse>

    @GET("user/getVendorList")
    suspend fun getVendorList(
        @Query("type") type: String?, @Query("country") country: String?
    ): Response<UserVenorListResponse>

    @GET("user/search")
    suspend fun getSearchList(
        @Query("country") country: String?,
        @Query("searchType") searchType: String?,
        @Query("searchText") searchText: String?,
        @Query("pageNumber") pageNumber: Int?,
        @Query("limit") limit: Int?
    ): Response<SearchVendorResponse>

    @GET("user/searchContent")
    suspend fun getSearchContent(
        @Query("country") country: String?,
        @Query("searchText") searchText: String?,
        @Query("pageNumber") pageNumber: Int?,
        @Query("limit") limit: Int?
    ): Response<CotentSearchListResponse>

    @GET("user/getVendorDetail/{vendorId}")
    suspend fun getVendorDetail(@Path("vendorId") vendorId: String?): Response<VendorDetailResponse>

    @GET("user/getRatingDetails/{vendorId}")
    suspend fun getVendorRatingDetails(@Path("vendorId") vendorId: String?): Response<UserVendorRatingResponse>

    @GET("user/getNewProducts/{vendorId}")
    suspend fun getNewProducts(@Path("vendorId") vendorId: String?): Response<NewProductListOfVendorResponse>

    @GET("user/getDiscountProducts/{vendorId}")
    suspend fun getDiscountProducts(@Path("vendorId") vendorId: String?): Response<NewProductListOfVendorResponse>

    @GET("user/getRequestForSampleProducts/{vendorId}")
    suspend fun getRequestForSampleProducts(@Path("vendorId") vendorId: String?): Response<NewProductListOfVendorResponse>


    @Multipart
    @POST("/api/v1/saharaGo/uploadSingleFile")
    suspend fun uploadSingleFile(@Part list: List<MultipartBody.Part>): Response<JsonObject>

    @GET("user/getContentWithSections")
    suspend fun getContentWithSection(
        @Query("country") country: String?, @Query("type") type: String?
    ): Response<JsonObject>

    @GET("user/getOrderHistory")
    suspend fun orderList(@Header("token") token: String?): Response<OrderResponse>

    @GET("payment/verifyPayment")
    suspend fun verifyPayment(
        @Query("tx_ref") country: String?,
        @Query("transaction_id") type: String?,
        @Query("cartId") cartId: String?
    ): Response<JsonObject>




    //VENDOR API
    @POST("/api/v1/vendor/login")
    suspend fun vendorLogin(@Body data: JsonObject): Response<JsonObject>

    //VENDOR APIS
    @POST("user/login")
    suspend fun getVendorLoginRequest(@Body loginRequest: JsonObject): Response<JsonObject>


    // payment apis

    @POST("user/processOrder")
    suspend fun processOrder(
        @Header("token") token: String?, @Body data: JsonObject
    ): Response<JsonObject>

    @GET("user/trackOrder/{orderId}")
    suspend fun trackOrder(
        @Header("token") token: String?, @Path("orderId") orderId: String?
    ): Response<JsonObject>

    @GET("user/getOrderDetails/{orderId}")
    suspend fun orderDetail(
        @Header("token") token: String?, @Path("orderId") orderId: String?
    ): Response<OrderDetailResponse>

    @POST("user/getShipmentRate")
    suspend fun getShipmentRate(
        @Header("token") token: String?, @Body data: JsonObject
    ): Response<JsonObject>

    @GET("user/getRatingDetails/{itemId}")
    suspend fun getProductReview(@Path("itemId") itemId: String?): Response<UserCommitResponse>

    @POST("user/cancelOrder")
    suspend fun cancelOrder(
        @Header("token") token: String?, @Body data: JsonObject
    ): Response<JsonObject>

    @POST("user/loginWithSocialMedia")
    suspend fun loginWithSocialMediaApi(
        @Header("token") token: String?, @Body data: JsonObject
    ): Response<JsonObject>

    @POST("user/changeUserAccountStatus")
    suspend fun changeUserAccountStatusApi(
        @Header("token") token: String?, @Body data: JsonObject
    ): Response<JsonObject>

    @GET("saharaGo/getStates")
    suspend fun stateList(): Response<NewStateListResponse>

    @GET("saharaGo/getCities")
    suspend fun cityList(@Query("state") statName: String?): Response<NewCityListResponse>

    @GET("user/getNotifications")
    suspend fun getNotifications(
        @Header("token") token: String?,
        @Query("pageNumber") pageNumber: String?,
        @Query("limit") limit: String?,
    ): Response<NotificationListResponse>


    @GET("user/getMultimediaProducts/{country}")
    suspend fun getMultimediaProducts(
        @Path("country") id: String?,

        ): Response<MaltiMediaWithProductResponse>
}