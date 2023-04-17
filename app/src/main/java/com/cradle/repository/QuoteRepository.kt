package com.cradle.repository

import android.content.Context
import com.example.model.CitiesList
import com.example.model.StateList
import com.google.gson.JsonObject
import com.cradle.api.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.cradle.api.BaseApiResponse
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


class QuoteRepository(private val retrofitService: RetrofitService, context: Context) :
    BaseApiResponse(context) {

    suspend fun getCountryList(): Flow<ExceptionHandler<CountryListResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getCountry() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun AllCountryParam(): Flow<ExceptionHandler<AllCountryListResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.allCountryReq() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getStatesList(countryId: Int?): Flow<ExceptionHandler<StateList>> {
        return flow {
            emit(safeApiCall { retrofitService.getStates(countryId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCitiesList(statesId: Int?): Flow<ExceptionHandler<CitiesList>> {
        return flow {
            emit(safeApiCall { retrofitService.getCities(statesId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserLoginRequest(loginRequest: JsonObject): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.getUserLoginRequest(loginRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserSignUpRequest(loginRequest: JsonObject): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.getUserSignUpRequest(loginRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserOtpRequest(loginRequest: JsonObject): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.getUsersignUpOtpVerificationRequest(loginRequest) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun resendOtp(loginRequest: JsonObject): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.resendOtpApi(loginRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserForgotPassRequest(loginRequest: JsonObject): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.getUserForgotPassApi(loginRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserChanPassWithLoginRequest(
        token: String,
        loginRequest: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.getUserChanPassWithLoginApi(token, loginRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserCPssRequest(loginRequest: JsonObject): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.userCPVerification(loginRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserWishListRequest(token: String): Flow<ExceptionHandler<UserWishListResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getuserWishList(token) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserCateRequest(): Flow<ExceptionHandler<CategoryListResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getUserCatApi() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun categoryRequest(): Flow<ExceptionHandler<CategoryResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.categoryApi() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserSunCateRequest(cat_id: String): Flow<ExceptionHandler<UserSubCategoryListResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getUserSubCatelApi(cat_id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUVAllWithIDRequest(
        id: String,
        pageNum: String,
        limit: String,
        searchText: String,
        country: String
    ): Flow<ExceptionHandler<ViewAllResponse>> {
        return flow {
            emit(safeApiCall {
                retrofitService.getVAllWithID(
                    id,
                    pageNum,
                    limit,
                    searchText,
                    country
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUProListID(
        id: String,
        pageNum: String,
        limit: String,
        searchText: String,
        country: String
    ): Flow<ExceptionHandler<ProductListResponse>> {
        return flow {
            emit(safeApiCall {
                retrofitService.getUProListApi(
                    pageNum,
                    limit,
                    country,
                    id,
                    searchText,
                    ""
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserContentDetailsReq(id: Int): Flow<ExceptionHandler<UserBlogDetailsResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getuserContentDetailApi(id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCartDetailsUserReq(token: String): Flow<ExceptionHandler<UserCartItem>> {
        return flow {
            emit(safeApiCall { retrofitService.cartDetailsUserApi(token) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCardDetailsByIdReq(
        token: String,
        cat_id: String
    ): Flow<ExceptionHandler<UserCartItem>> {
        return flow {
            emit(safeApiCall { retrofitService.cardDetailsApi(token, cat_id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSaveCartReq(
        token: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.saveCartApi(token, jsonObject) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getupdateCartReq(
        token: String,
        cat_id: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.updateCartApi(token, cat_id, jsonObject) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addProductToWishlistReq(
        token: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.addProductToWishlist(token, jsonObject) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun removeProductFromWishlistReq(
        token: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.removeProductFromWishlist(token, jsonObject) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun removeItemFromCartReq(
        token: String,
        cat_id: String,
        item_id: String
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.removeItemFromCart(token, cat_id, item_id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getContentWithSectionsRequest(
        string: String,
        token: String
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.contentWithSectionsApi(string) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserBannerListReq(): Flow<ExceptionHandler<BannerListResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.userBannerApi() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserAddressListReq(token: String): Flow<ExceptionHandler<AddressList>> {
        return flow {
            emit(safeApiCall { retrofitService.getAllAddresses(token) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addAddressReq(
        token: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.addAddressApi(token, jsonObject) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateAddressReq(
        token: String,
        jsonObject: JsonObject,
        id: String
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.updateAddress(token, jsonObject, id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun userDeleteAddressReq(
        token: String,
        id: String
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.deleteAddress(token, id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun userMarkAsDefaultReq(
        token: String,
        id: String
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.markAsDefault(token, id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun userDetailByTokenReq(token: String): Flow<ExceptionHandler<UserDetailsResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getUserDetailByToken(token) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun usergetProductDetailReq(
        item_id: String,
        userId: String
    ): Flow<ExceptionHandler<ProductDetailsResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getProductDetail(item_id, userId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateProfileReq(
        token: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.updateProfile(token, jsonObject) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun userGiveRatingReq(
        token: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.getRating(token, jsonObject) })
        }.flowOn(Dispatchers.IO)
    }


    suspend fun userAddCommitReq(
        token: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.userAddCommit(token, jsonObject) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCommitReq(id: Int): Flow<ExceptionHandler<UserCommitResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getCommit(id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun userSimilarProductsReq(item_id: String): Flow<ExceptionHandler<ProductSoldByVendor>> {
        return flow {
            emit(safeApiCall { retrofitService.similarProducts(item_id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun usernSaleProductsReq(item_id: String): Flow<ExceptionHandler<OnSaleResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.onSaleProducts(item_id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun otherProductsSoldByVendorReq(item_id: String): Flow<ExceptionHandler<OnSaleResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.otherProductsSoldByVendor(item_id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun newArrivalsReq(country: String): Flow<ExceptionHandler<OnSaleResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getNewArrivals(country) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun newArrivalsByTradeReq(country: String,categoryId: String,sorting: String,pageNumber: String,limit: String): Flow<ExceptionHandler<ProductListByTradeResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getNewArrivalsByTrade(country,categoryId, sorting, pageNumber, limit) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun topDealsByCountryReq(country: String,categoryId: String,sorting: String,pageNumber: String,limit: String): Flow<ExceptionHandler<ProductListByTradeResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getTopDealsByCountry(country,categoryId, sorting, pageNumber, limit) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun onSaleByCountryReq(country: String,categoryId: String,sorting: String,pageNumber: String,limit: String): Flow<ExceptionHandler<ProductListByTradeResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getOnSaleByCountry(country,categoryId, sorting, pageNumber, limit) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun userTopDealReq(country: String): Flow<ExceptionHandler<OnSaleResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getTopDeals(country) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSectionListReq(
        pageNum: String,
        limit: String,
        searchText: String,
        country: String
    ): Flow<ExceptionHandler<GetSetionAllListResponse>> {
        return flow {
            emit(safeApiCall {
                retrofitService.getSectionList(
                  /*  pageNum,
                    limit,
                    searchText,*/
                    country
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getVendorListReq(
        type: String,
        country: String
    ): Flow<ExceptionHandler<UserVenorListResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getVendorList(type, country) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSearchListReq(
        type: String,
        country: String,
        searchText: String,
        pageNum: Int,
        limit: Int
    ): Flow<ExceptionHandler<SearchVendorResponse>> {
        return flow {
            emit(safeApiCall {
                retrofitService.getSearchList(
                    country,
                    type,
                    searchText,
                    pageNum,
                    limit
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSearchContentReq(
        type: String,
        country: String,
        searchText: String,
        pageNum: Int,
        limit: Int
    ): Flow<ExceptionHandler<CotentSearchListResponse>> {
        return flow {
            emit(safeApiCall {
                retrofitService.getSearchContent(
                    country,
                    searchText,/* searchText,*/
                    pageNum,
                    limit
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun userVendorDetailReq(vendorId: String): Flow<ExceptionHandler<VendorDetailResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getVendorDetail(vendorId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun userVendorRatingDetailsReq(vendorId: String): Flow<ExceptionHandler<UserVendorRatingResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getVendorRatingDetails(vendorId) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun productReviewReq(vendorId: String): Flow<ExceptionHandler<UserCommitResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getProductReview(vendorId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun userNewProductsReq(vendorId: String): Flow<ExceptionHandler<NewProductListOfVendorResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getNewProducts(vendorId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDiscountProductsReq(vendorId: String): Flow<ExceptionHandler<NewProductListOfVendorResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getDiscountProducts(vendorId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getRequestForSampleProductsReq(vendorId: String): Flow<ExceptionHandler<NewProductListOfVendorResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getRequestForSampleProducts(vendorId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun uploadSingleFileReq(list: List<MultipartBody.Part>): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.uploadSingleFile(list) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getContentWithSectionReq(
        country: String,
        type: String
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.getContentWithSection(country, type) })
        }.flowOn(Dispatchers.IO)
    }


    suspend fun processOrderReq(
        token: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.processOrder(token, jsonObject) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun orderListReq(token: String): Flow<ExceptionHandler<OrderResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.orderList(token) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun verifyPaymentReq(
        tx_ref: String,
        transaction_id: String,
        cartid: String
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.verifyPayment(tx_ref, transaction_id, cartid) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun trackOrder(
        token: String,
        orderId: String
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.trackOrder( token,orderId) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun orderDetail(
        token: String,
        orderId: String
    ): Flow<ExceptionHandler<OrderDetailResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.orderDetail( token,orderId) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun getShipmentRate(
        token: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.getShipmentRate( token,jsonObject) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun cancelOrderReq(
        token: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.cancelOrder(token, jsonObject) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun loginWithSocialMediaReq(
        token: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.loginWithSocialMediaApi(token, jsonObject) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun changeUserAccountStatusReq(
        token: String,
        jsonObject: JsonObject
    ): Flow<ExceptionHandler<JsonObject>> {
        return flow {
            emit(safeApiCall { retrofitService.changeUserAccountStatusApi(token, jsonObject) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun stateListReq(
    ): Flow<ExceptionHandler<NewStateListResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.stateList() })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun cityListReq(stateName:String
    ): Flow<ExceptionHandler<NewCityListResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.cityList(stateName) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun notificationReq(token: String,pageNumber: String,limit: String
    ): Flow<ExceptionHandler<NotificationListResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getNotifications(token,pageNumber,limit) })
        }.flowOn(Dispatchers.IO)
    }
    suspend fun multimediaProductsReq(country: String): Flow<ExceptionHandler<MaltiMediaWithProductResponse>> {
        return flow {
            emit(safeApiCall { retrofitService.getMultimediaProducts(country) })
        }.flowOn(Dispatchers.IO)
    }
}