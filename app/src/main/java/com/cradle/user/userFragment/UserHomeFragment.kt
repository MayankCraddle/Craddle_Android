package com.cradle.user.userFragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.common_fragment.LoaderFragment
import com.cradle.common_screen.CountryActivity
import com.cradle.databinding.FragmentUserHomeBinding
import com.cradle.firebasechat.activity.ChatActivity
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.*
import com.cradle.user.userActivity.UserAddToCartActivity
import com.cradle.user.userActivity.UserLoginActivity
import com.cradle.user.userActivity.WishListActivity
import com.cradle.utils.MyHelper
import com.cradle.utils.SharaGoPref
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_user_home.*
import kotlinx.android.synthetic.main.fragment_user_home.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class UserHomeFragment: Fragment(R.layout.fragment_user_home),View.OnClickListener {
    private lateinit var mainViewModel: MainViewModel
    lateinit var v: View
    private var fragmentUserHomeBindingImpl: FragmentUserHomeBinding?=null
    val colourListList = ArrayList<Any>()
    var bannerMultimediaList: ArrayList<Any?>? = null
    var bannerMarketPlacemediaList: ArrayList<Any?>? = null

     override fun onCreateView(
           inflater: LayoutInflater,
           container: ViewGroup?,
           savedInstanceState: Bundle?
       ): View {
           val response = (requireActivity().applicationContext as ApplicationClass).repository
      fragmentUserHomeBindingImpl = DataBindingUtil.inflate(inflater,
           R.layout.fragment_user_home, container, false)
         mainViewModel= ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]

         v=fragmentUserHomeBindingImpl!!.root
         fragmentUserHomeBindingImpl!!.setVariable(BR.onHomeClick,this)

         fragmentUserHomeBindingImpl!!.userSpeciallyAdapter = UserSpacialityAdapter(requireActivity())
        findId()

        return v
    }

    private fun findId() {

        if (MyHelper.isNetworkConnected(requireActivity())) {
           // gatData()
          //  showData()
           // setColourInList()
            setViewPagerMultimedia()
            if(SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!.isNotEmpty()){
                resultCount()
                apiHitWishListNCard()
            }

            setViewPagerMarketPlace()
        } else requireActivity().showToast(getString(R.string.no_internet_connection))

    }
    private fun apiHitWishListNCard(){
        mainViewModel.getUserWishList(SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString())
        mainViewModel.getCardDetailsByIdParam(SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString(),SharaGoPref.getInstance(requireActivity()).getCartId("").toString())
    }
    private fun gatData(){

        CoroutineScope(Dispatchers.IO).launch {
            launch {
                LoaderFragment.showLoader(requireActivity().supportFragmentManager)
                val country=SharaGoPref.getInstance(requireActivity()).getCountry("")
                val token=SharaGoPref.getInstance(requireActivity()).getLoginToken("")
                if(country!!.isNotEmpty()&&SharaGoPref.getInstance(requireActivity()).getWhichFrag("").equals("Media")){
                    mainViewModel.getContentWithSections("",token!!)
                    mainViewModel.getUserBannerList()
                }else{
                    mainViewModel.getContentWithSections("",token!!)
                    mainViewModel.getUserBannerList()
                }


            }
        }
    }

    private fun showData(){
        mainViewModel.lDataContent.observe(requireActivity()){
            when(it){
                is ExceptionHandler.Loading->{
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                }
                is ExceptionHandler.Success->{
                    //   mainBinding.shimmerFrameLayout.visibility=View.GONE
                    //   v.recycler_wish_list.visibility=View.VISIBLE
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                        val jsonObject = JSONObject(it.data!!.toString())
                    var colourChange=0

                    val categoryJSONObj: JSONObject = jsonObject.getJSONObject("detail")
                    val iterator = categoryJSONObj.keys()

                    while (iterator.hasNext()) {
                        val key = iterator.next()

                        val arrayList = ArrayList<String>()
                        arrayList.add(key)
                        val contentJSONObject=categoryJSONObj.optJSONObject(key)

                        val id= contentJSONObject!!.optString("sectionId")
                        val contentJsonArray= contentJSONObject.optJSONArray("content")

                        if(colourChange==5){
                            colourChange=0
                            val colour=colourListList.get(colourChange)
                            addDynamicCategory(id,arrayList,key,contentJsonArray,colourChange,colour)
                        }else{
                            colourChange++
                            val colour=colourListList.get(colourChange)
                            addDynamicCategory(id,arrayList,key,contentJsonArray,colourChange,colour)
                        }


                    }

                }
                is ExceptionHandler.Error->{
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                }
            }
        }

        mainViewModel.lBannerList.observe(requireActivity()){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                }
                is ExceptionHandler.Success->{
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                    it.data?.let {
                        val adapter = SliderPagerAdapter(
                            requireActivity(),
                            it.bannerList,
                            "image",
                            0,
                            "0"
                            , getString(R.string.blogs),"0","Image"
                        ,"")
                      //  adapter.setItem(imageList)
                        v.photos_viewpager.adapter = adapter

                        TabLayoutMediator(v.tab_layout, v.photos_viewpager) { tab, position ->
                        }.attach()
                    }
                }
                is ExceptionHandler.Error->{
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                }
            }
        }

        mainViewModel.liveDataUserLUserWishList.observe(requireActivity()){ it ->
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{

                    it.data?.let {
                        try {
                            if (it.metaData!!.items!!.isNotEmpty()){
                                v.rl_wishList_vage.visibility=View.VISIBLE
                                v.tv_wishlist_count.text= it.metaData.items!!.size.toString()
                                SharaGoPref.getInstance(requireActivity()).setCartInItem(it.metaData.items.size.toString())
                            }else{
                                v.rl_wishList_vage.visibility=View.GONE
                            }

                        }catch (e:Exception){

                            v.rl_wishList_vage.visibility=View.GONE

                        }

                    }
                }
                is ExceptionHandler.Error->{
                }
            }
        }
        mainViewModel.lGetCardDetailsParam.observe(requireActivity()){
                it->
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
                    it.data?.let {

                        if(it.cartSize!!.isNotEmpty()){
                            v.rl_cart_count.visibility=View.VISIBLE
                            v.tv_cart_count.text=it.cartSize


                        }else{
                           v.rl_cart_count.visibility=View.GONE
                            v.tv_cart_count.text="0"
                        }

                    }
                }
                is ExceptionHandler.Error->{
                    v.rl_cart_count.visibility=View.GONE
                 //   Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }
    }
    private fun resultCount(){

        val cartID=SharaGoPref.getInstance(requireActivity()).getCartId("")
        if(!cartID!!.equals(null)){
            mainViewModel.getCardDetailsByIdParam(SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString(),SharaGoPref.getInstance(requireActivity()).getCartId("").toString())

            //user Server Product List result
            mainViewModel.lGetCardDetailsParam.observe(requireActivity()){
                    it->
                when(it){
                    is ExceptionHandler.Loading->{}
                    is ExceptionHandler.Success->{
                        it.data?.let {

                            if(it.cartSize!!.isNotEmpty()){
                                rl_cart_count.visibility=View.VISIBLE
                                tv_cart_count.text=it.cartSize
                            }else{
                                rl_cart_count.visibility=View.VISIBLE
                                tv_cart_count.text="0"
                            }

                        }
                    }
                    is ExceptionHandler.Error->{

                    }
                }
            }
        }else{
            rl_cart_count.visibility=View.VISIBLE
            tv_cart_count.text="0"
        }

    }

    override fun onResume() {
        super.onResume()
        if (SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!.isNotEmpty())
            apiHitWishListNCard()

    }

    private fun addDynamicCategory(
        id: String,
        list: ArrayList<String>,
        key: String?,
        contentJsonArray: JSONArray?,
        colourChange: Int,
        colour: Any
    ) {
        val recyclerView = RecyclerView(requireActivity())
        val moviesAdapter= AdapterDynamicCategoryList(id,requireActivity(),list,key,contentJsonArray,colourChange,colour)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        recyclerView.adapter = moviesAdapter
        fragmentUserHomeBindingImpl!!.llUserHome.addView(recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentUserHomeBindingImpl=null
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.rl_search_country->{

                startActivity(Intent(requireActivity(), ChatActivity::class.java).putExtra("screen","Home"))
              //  startActivity(Intent(requireActivity(), CountryActivity::class.java).putExtra("screen","Home"))
            }
            R.id.rl_viewpager_multimedia->{

                startActivity(Intent(requireActivity(), CountryActivity::class.java).putExtra("screen","Home"))
            }
            R.id.iv_wishlist_screen->{
                if (SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!.isNotEmpty())
                startActivity(Intent(requireActivity(), WishListActivity::class.java))
                else
                    startActivity(Intent(requireActivity(), UserLoginActivity::class.java))
            }
            R.id.iv_cart_screen->{
                if (SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!.isNotEmpty())
                startActivity(Intent(requireActivity(), UserAddToCartActivity::class.java))
                else
                    startActivity(Intent(requireActivity(), UserLoginActivity::class.java))
            }

        }
    }
    private fun addFragment(fragment: Fragment) {
        val manager = requireActivity().supportFragmentManager
        manager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    private fun setViewPagerMultimedia(){
        bannerMultimediaList= ArrayList()
        bannerMultimediaList!!.add("rahul")
        bannerMultimediaList!!.add("rahul")
        val adapter = MutimediaAdapter(
            requireActivity(),
            bannerMultimediaList as ArrayList<String>,
            "multimedia",
            0,getString(R.string.blogs)
        )
        //  adapter.setItem(imageList)
        v.viewpager_multimedia.adapter = adapter

        TabLayoutMediator(v.tab_layout_multimedia, v.viewpager_multimedia) { tab, position ->
        }.attach()
    }
    private fun setViewPagerMarketPlace(){
        bannerMarketPlacemediaList= ArrayList()
        bannerMarketPlacemediaList!!.add("rahul")
        bannerMarketPlacemediaList!!.add("rahul")
        bannerMarketPlacemediaList!!.add("rahul")
        val adapter = ViewPagerMarketPlaceAdapter(
            requireActivity(),
            bannerMarketPlacemediaList as ArrayList<String>,
            "marketplace",
            0,getString(R.string.blogs)
        )
        //  adapter.setItem(imageList)
        v.viewpager_marketplace.adapter = adapter

        TabLayoutMediator(v.tab_layout_marketplace, v.viewpager_marketplace) { tab, position ->
        }.attach()
    }

}