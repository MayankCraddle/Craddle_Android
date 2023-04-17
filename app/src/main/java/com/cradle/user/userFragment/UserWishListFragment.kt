package com.cradle.user.userFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.cradle.repository.QuoteRepository
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.databinding.FragmentUserWishlistBinding
import com.cradle.model.UserWishList
import com.cradle.model.cart.UserCartItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.UserWishListAdapter
import com.cradle.user.userActivity.UserProductDetailsActivity
import com.cradle.utils.SharaGoPref
import com.cradle.utils.Utility
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
/*import kotlinx.android.synthetic.main.custom_toolbar_user.view.*
import kotlinx.android.synthetic.main.fragment_user_wishlist.view.**/
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class UserWishListFragment: Fragment (),UserWishListAdapter.TextState{
    private var response: QuoteRepository? =null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var v: View
    private lateinit var fragmentUserWishListBinding: FragmentUserWishlistBinding
    private var token: String? = null
    private var mpref: SharaGoPref? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
         response = (requireActivity().application as ApplicationClass).repository
        fragmentUserWishListBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_user_wishlist, container, false)
        v=fragmentUserWishListBinding.root
        findId()
        return v
    }
    private fun findId() {

        mainViewModel= ViewModelProvider(this, MainViewModelFactory(response!!))[MainViewModel::class.java]
        mpref = SharaGoPref.getInstance(requireActivity())
        token=SharaGoPref.getInstance(requireActivity()).getLoginToken("")

        fragmentUserWishListBinding.rlToolBar.setBackgroundColor(SharaGoPref.getInstance(requireActivity()).getColor(0)!!)

        gatData()
        showData()

    }
    private fun addRecyclerView(whishList: List<UserWishList?>?){
        fragmentUserWishListBinding.userWishListAdapter= UserWishListAdapter(requireActivity(),whishList,this)
    }
   private fun gatData(){

        CoroutineScope(Dispatchers.IO).launch {
            launch {
                mainViewModel.getUserWishList(SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString())
            }
        }
       mainViewModel.getCardDetailsUser(token!!)
    }
    private var cartResponse: UserCartItem?=null
   private fun showData(){
        mainViewModel.liveDataUserLUserWishList.observe(requireActivity()){
                it ->
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        try {
                            if (it.metaData!!.items!!.isNotEmpty()){
                                fragmentUserWishListBinding.ivNoDataFound.visibility=View.GONE
                                fragmentUserWishListBinding.recyclerWishList.visibility=View.VISIBLE
                                Log.e("wishList",it.toString())
                                addRecyclerView(it.metaData.items)
                            }else{
                                fragmentUserWishListBinding.ivNoDataFound.visibility=View.VISIBLE
                                fragmentUserWishListBinding.recyclerWishList.visibility=View.GONE
                            }

                        }catch (e:Exception){
                            fragmentUserWishListBinding.ivNoDataFound.visibility=View.VISIBLE
                            fragmentUserWishListBinding.recyclerWishList.visibility=View.GONE

                        }

                    }
                }
                is ExceptionHandler.Error->{
                    Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }
       mainViewModel.lSaveCartParam.observe(requireActivity()){
           when(it){
               is ExceptionHandler.Loading->{

               }
               is ExceptionHandler.Success->{

                   it.data?.let {
                       mainViewModel.getCardDetailsUser(SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString())
                       Utility.toastMessage(requireActivity(),getString(R.string.item_added))
                       // cartDetailsofUser(""+mpref!!.getToken(""))

                   }
               }
               is ExceptionHandler.Error->{


               }
           }
       }
       mainViewModel.lUpdateCartParam.observe(requireActivity()){
           when(it){
               is ExceptionHandler.Loading->{

               }
               is ExceptionHandler.Success->{

                   it.data?.let {
                       Utility.toastMessage(requireActivity(),getString(R.string.item_added))
                       if(!token.toString().equals("")){
                           mainViewModel.getCardDetailsUser(token!!)
                           // cartDetailsofUser(""+mpref!!.getToken(""))
                       }
                   }
               }
               is ExceptionHandler.Error->{


               }
           }
       }
       mainViewModel.lCartDetailsUser.observe(requireActivity()){ it ->
           when(it){
               is ExceptionHandler.Loading->{

               }
               is ExceptionHandler.Success->{

                   it.data?.let {
                       Log.e("cartDetails",it.toString())
                       cartResponse = it
                       SharaGoPref.getInstance(requireActivity()).setCartId(it.cartId.toString())
                   }
               }
               is ExceptionHandler.Error->{


               }
           }
       }

       mainViewModel.lremoveProductFromWishlistParam.observe(requireActivity()){
           when(it){
               is ExceptionHandler.Loading->{
               }
               is ExceptionHandler.Success->{

                   it.data?.let {
                       val jsonObject= JSONObject(it.toString())
                       Utility.toastMessage(requireActivity(),jsonObject.optString("message"))
                       gatData()
                   }
               }
               is ExceptionHandler.Error->{
                   Utility.toastMessage(requireActivity(),it.errorMessage)
               }
           }
       }
    }

    override fun addtoCard(data: UserWishList?, position: Int) {
        val i = Intent(requireActivity(), UserProductDetailsActivity::class.java)
        i.putExtra("item_id",data!!.itemId)
        i.putExtra("product_id", data.productId)
        Log.e("itemid", data.itemId.toString())

        startActivity(i)
         }
    private fun saveCartApi(jsonMain: JsonObject, cartId: String) {
        if(cartId==""){
            mainViewModel.getSaveCartParam(token.toString(), jsonMain)
        }else{
            mainViewModel.getUpdateCartParam(token.toString(),cartId, jsonMain)
        }
    }

    override fun removeProductToWishList(jsonObject: JsonObject) {
        mainViewModel.removeProductFromWishlistParam(SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!,jsonObject)


    }


}