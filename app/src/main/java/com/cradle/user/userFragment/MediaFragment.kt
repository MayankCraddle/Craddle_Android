package com.cradle.user.userFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.common_fragment.LoaderFragment
import com.cradle.common_screen.CountryActivity
import com.cradle.databinding.FragmentUserMediaBinding
import com.cradle.model.ContentListItem
import com.cradle.model.MaltiMediaWithProductListItem
import com.cradle.model.SectionsItem
import com.cradle.model.ViewAllMetaData
import com.cradle.model.commit.CommentsItem
import com.cradle.repository.ExceptionHandler
import com.cradle.repository.QuoteRepository
import com.cradle.user.adapters.MediaSectionAdapter
import com.cradle.user.adapters.MediaSectionWithIdAdapter
import com.cradle.user.userActivity.CommentAllListActivity
import com.cradle.user.userActivity.UserAddToCartActivity
import com.cradle.user.userActivity.UserLoginActivity
import com.cradle.user.userActivity.WishListActivity
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_user_media.*
import kotlinx.coroutines.launch


class MediaFragment: Fragment(),MediaSectionAdapter.TextStateAction,MediaSectionWithIdAdapter.SendCommit{
    private lateinit var mediaSectionAdapter: MediaSectionAdapter
    private lateinit var mediaSectionWithIdAdapter: MediaSectionWithIdAdapter
    private var response: QuoteRepository? = null
    private lateinit var parentView: View
    var contentList: ArrayList<ContentListItem?>? = ArrayList()
    private lateinit var mViewModel: MainViewModel
    private lateinit var v: View
    private lateinit var mBinding: FragmentUserMediaBinding
    var country: String? = null
    var sectionId: String? = null
    var productCount: Int? = null
    var count: Int? = 0
    var index: Int? = 0
    var positionProduct: Int? = 0
    var product: Int? = 0
    var contentListSize: Int? = 0
    var productList: ArrayList<MaltiMediaWithProductListItem?>? = ArrayList()
    var productListCopy: ArrayList<MaltiMediaWithProductListItem?>? = null
    var newcontentList: ArrayList<ContentListItem?>? = null
    var apiHit:Boolean=true

    private val PAGE_SIZE = 50
    private var page: Int = 1
    private var isLoading = false
    private var isLastPage: Boolean = false




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        response = (requireActivity().application as ApplicationClass).repository
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user_media, container, false
        )
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response!!))[MainViewModel::class.java]
        v = mBinding.root

        iniUI()
        return v
    }

    private fun iniUI(){
        country= SharaGoPref.getInstance(requireActivity()).getCountry("")
        statusBarColourChange()
        AnalyticsUtils.analyticReport(requireActivity(),MyConstants.BLogScreen)
        val activity: Activity? = activity
        if (isAdded()&&activity != null) {
            if (MyHelper.isNetworkConnected(requireActivity())) {
                try {
                    LoaderFragment.showLoader(requireActivity().supportFragmentManager)

                    allCategoryApi()
                    allClick()
                    apiHitWithResult()

                }catch (e: Exception){}

            } else requireActivity().showToast(getString(R.string.no_internet_connection))

        }

       }

    private fun onClearSearch() {
       // this.search = ""
        page = 1+1;
        isLastPage = false
        contentWithIDApi(sectionId.toString(),page.toString())


    }

    private fun allCategoryApi(){

        mViewModel.userSectionListParam(
            "1", "10",
            "", SharaGoPref.getInstance(requireActivity()).getCountry("").toString()
        )
        mViewModel.lgetSectionList.observe(requireActivity()){
            when(it){
                is ExceptionHandler.Loading->{}
                is ExceptionHandler.Success->{
               //   LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                    it.data?.let {
                      //  LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                        if(it.sections!!.size>0){
                            iv_no_data_found.visibility=View.GONE
                            setAdapterSection(it.sections)

                            val backPress=SharaGoPref.getInstance(requireActivity()).getBACKPRESS("").toString()
                       /*     if(backPress.equals("contentDetail")){
                                contentWithIDApi("47")
                            }else{*/
                                sectionId= it.sections.get(0)!!.id.toString()
                                contentWithIDApi(sectionId!!,"1")
                           // }


                        }else{
                            iv_no_data_found.visibility=View.GONE
                        }


                    }
                }
                is ExceptionHandler.Error->{
                    //  Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }
    }

    private fun setAdapterSection(sectionsItemList: ArrayList<SectionsItem?>?) {


            mediaSectionAdapter = MediaSectionAdapter(
                requireActivity(), sectionsItemList, this
            )
            mBinding.recyclerUserMediaCate.setHasFixedSize(true)
            val layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            /* layoutManager.stackFromEnd = false
              layoutManager.reverseLayout = true*/
            mBinding.recyclerUserMediaCate.layoutManager = layoutManager
            mBinding.recyclerUserMediaCate.adapter = mediaSectionAdapter





    }

    override fun clickOnSection(int: Int, sectionsItem:SectionsItem) {
        try {
            sectionId = sectionsItem.id.toString()
            iv_no_data_found.visibility = View.GONE
            apiHit = true
            contentList!!.clear()
            productList!!.clear()
            mediaSectionWithIdAdapter.notifyDataSetChanged()
        }catch (e: Exception){}
        try {
            apiProduct()
        }catch (e:Exception){
        }
    }

    private fun contentWithIDApi(setionId:String, pagNum: String){

        //Api Hit

        SharaGoPref.getInstance(requireActivity()).setSectionId(setionId)
        mViewModel.getVAllWithID(setionId, "1", "1000", "", country!!)


      //Result Api
        mViewModel.lVAllWithIDReq.observe(requireActivity()) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                //    LoaderFragment.showLoader(requireActivity().supportFragmentManager)
                }
                is ExceptionHandler.Success -> {
                  //  LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                    mViewModel.lVAllWithIDReq.removeObservers(this);

                    lifecycleScope.launch{
                        it.data?.let {

                         //   LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                            try{
                            contentList= it.contentList as ArrayList<ContentListItem?>?
                            contentListSize=it.contentList!!.size
                            if (contentList!!.size>0){
                                iv_no_data_found.visibility=View.GONE
                                isLoading = false
                                logicMaltimediaWithProduct()


                            }else{
                                iv_no_data_found.visibility=View.GONE
                            }
                            }catch (e: Exception){}
                        }
                    }

                }
                is ExceptionHandler.Error -> {

                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)

                }
            }
        }
    }


    var comments: ArrayList<CommentsItem?>? = null

     fun logicMaltimediaWithProduct(){
      //  var files: ArrayList<String?>? =  ArrayList()
        comments= ArrayList()
        newcontentList=ArrayList()
        val metaData = ViewAllMetaData("","","")
        comments!!.add(CommentsItem("","","",""))

        var index = 0
         if (apiHit==true){
             for ( i in 0 until contentList!!.size){

                 if(productList!!.size>0){
                     try {
                         if (((i+1) % 3) == 0) {
                             index += 1
                             // files!!.clear()
                             // positionProduct= product!!
                             var files: ArrayList<String?>? =  ArrayList()

                             val file=productList!!.get(0)!!.metaData!!.images!!
                             if(productList!!.get(0)!!.metaData!!.images!!.isNotEmpty()){
                                 for(k in 0 until productList!!.get(0)!!.metaData!!.images!!.size){
                                     val imagePath = file[k]
                                     files!!.add(imagePath.toString())
                                 }

                             }
                             var contentListItem= ContentListItem(country,true,productList!!.get(0)!!.name,""
                                 ,1,productList!!.get(0)!!.discountedPrice.toString()
                                 ,productList!!.get(0)!!.rating.toString(),"Product","","",metaData,
                                 "",true,files,1,
                                 2,2,"",productList!!.get(0)!!.productId,"",comments)

                             if (i == 2) {
                                 //  contentList.add(mediaProductsList[0], at: i + 1)
                                 contentList!!.add(i+1,contentListItem)

                             } else {
                                 // mediaContentListCopy.insert(mediaProductsList[0], at: i + index)
                                 contentList!!.add(i+index,contentListItem)
                             }
                             Log.e("ContentList",contentList.toString())

                             productList!!.removeAt(0)
                         }
                     }catch (e:Exception){

                     }

                 }

                 Log.e("productList",productList!!.size.toString())

             }
             LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
             setAdapterMediaContent()
         }

    }

    private fun setAdapterMediaContent() {
        try {
            if (apiHit == true) {
                if (contentList!!.isNotEmpty()) {
                    mBinding.ivNoDataFound.visibility = View.GONE
                    mBinding.recyclerMediaProduct.visibility = View.VISIBLE
                    mediaSectionWithIdAdapter =
                        MediaSectionWithIdAdapter(requireActivity(), contentList,this)
                    val manager = LinearLayoutManager(
                        context, LinearLayoutManager.VERTICAL, false
                    )
                    //mBinding.recyclerMediaProduct.recycledViewPool.clear();
                    mBinding.recyclerMediaProduct.layoutManager = manager
                    mBinding.recyclerMediaProduct.adapter = mediaSectionWithIdAdapter
                   // mBinding.recyclerMediaProduct.getRecycledViewPool().clear();
                    mediaSectionWithIdAdapter.notifyDataSetChanged()
                   // mBinding.recyclerMediaProduct.setItemAnimator(null);
                    apiHit = false
                    setPageLimit(manager)
                    contentWithIDApi(sectionId!!, page.toString())

                    // getList(true)


                } else {
                    mBinding.ivNoDataFound.visibility = View.VISIBLE
                    mBinding.recyclerMediaProduct.visibility = View.GONE
                }
            }
        }catch (e: Exception){}

    }
    private fun statusBarColourChange(){
        if(SharaGoPref.getInstance(requireActivity()).getCountryFlag("")!==null){
            val flag=SharaGoPref.getInstance(requireActivity()).getCountryFlag("").toString()
            Glide.with(requireActivity()).load(MyConstants.file_Base_URL_flag+flag).into(mBinding.ivOpenCountry)

            mBinding.tvCountryName.text = SharaGoPref.getInstance(requireActivity()).getCountry("")
                .toString()

            mBinding.rlCusToolbar.setBackgroundColor(SharaGoPref.getInstance(requireActivity()).getColor(0)!!)

        }else{

        }
        /*if(mpref!!.getShowList("").equals(getString(R.string.products))){
          *//*tv_country_name.text = SharaGoPref.getInstance(this).getCountry("")
                .toString()+"'s Products"*//*
            tv_country_name.text = "Products"
        }else{
            tv_country_name.text = SharaGoPref.getInstance(this).getCountry("")
                .toString()
        }*/

    }
    private fun allClick(){
        mBinding.llOpenCountry.setOnClickListener {
            startActivity(Intent(requireActivity(), CountryActivity::class.java).putExtra("screen","mediaNew"))

        }
        mBinding.ivWishlistScreen.setOnClickListener {
            if (SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!.isNotEmpty())
                startActivity(Intent(requireActivity(), WishListActivity::class.java))
            else
                startActivity(Intent(requireActivity(), UserLoginActivity::class.java))


        }
        mBinding.rlOpenCart.setOnClickListener {
            if (SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!.isNotEmpty())
                startActivity(Intent(requireActivity(), UserAddToCartActivity::class.java))
            else
                startActivity(Intent(requireActivity(), UserLoginActivity::class.java))



        }
    }

    private fun apiHitWithResult() {
        mViewModel.multimediaProductsParam(SharaGoPref.getInstance(requireActivity()).getCountry("").toString())
        mViewModel.lMultimediaProducts.observe(requireActivity()){
            when(it){
                is ExceptionHandler.Loading->{
                }
                is ExceptionHandler.Success->{
                    mViewModel.lMultimediaProducts.removeObservers(this);
                    //if (mViewModel.lMultimediaProducts.hasObservers()) return@observe;

                    lifecycleScope.launch {
                         it.data?.let {
                             try {


                                 productList= ArrayList()
                                 productListCopy= ArrayList()
                                 if (it.productList!!.size>0){

                                     productList!!.addAll(it.productList as ArrayList<MaltiMediaWithProductListItem>)
                                     productListCopy!!.addAll(it.productList)
                                 }

                             }catch (e:Exception){
                             }

                         }

                     }
                }
                is ExceptionHandler.Error->{
                    Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }
    }
    private fun apiProduct()
         {
            mViewModel.multimediaProductsParam(SharaGoPref.getInstance(requireActivity()).getCountry("").toString())
            mViewModel.lMultimediaProducts.observe(requireActivity()){
                when(it){
                    is ExceptionHandler.Loading->{
                    }
                    is ExceptionHandler.Success->{
                        mViewModel.lMultimediaProducts.removeObservers(this);
                        lifecycleScope.launch{
                            it.data?.let {
                                try {

                                   // productList= ArrayList()
                                    productList!!.clear()
                                    if (it.productList!!.size>0){
                                        productList!!.addAll(it.productList as ArrayList<MaltiMediaWithProductListItem>)
                                        contentWithIDApi(sectionId!!,"20")

                                    }else{
                                        contentWithIDApi(sectionId!!,"20")

                                    }

                                }catch (e:Exception){
                                }

                            }
                        }

                    }
                    is ExceptionHandler.Error->{
                        Utility.toastMessage(requireActivity(),it.errorMessage)
                    }
                }
            }
        }

    override fun onResume() {
        super.onResume()

        if (MyHelper.isNetworkConnected(requireActivity())) {
                if (SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!.isNotEmpty()) {
                    val  token = SharaGoPref.getInstance(requireActivity()).getLoginToken("")
                    cartDetailsAPi(token!!)
                    //  mViewModel.getCardDetailsUser(token!!)

                    val cartId = SharaGoPref.getInstance(requireActivity()).getCartId("").toString()

                    if (!cartId.equals("null")) {
                        mViewModel.getCardDetailsByIdParam(
                            SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString(),
                            SharaGoPref.getInstance(requireActivity()).getCartId("").toString()
                        )
                        //user Server Product List result
                        mViewModel.lGetCardDetailsParam.observe(this) { it ->
                            when (it) {
                                is ExceptionHandler.Loading -> {}
                                is ExceptionHandler.Success -> {
                                    it.data?.let {
                                        //  cartResponse = it

                                        if (it.cartSize!!.isNotEmpty()) {
                                            mBinding.tvCartCount.text = it.cartSize

                                        } else {
                                            mBinding.tvCartCount.text = "0"
                                        }
                                        // activityUserAddtocartBinding.userAddToCartAdapter= UserAddToCartAdapter(this/*,it.metaData!!.items*/)

                                    }
                                }
                                is ExceptionHandler.Error -> {
                                    //Utility.toastMessage(this,it.errorMessage)
                                }
                            }
                        }
                    } else {
                        mBinding.tvCartCount.text = "0"
                    }


                }
        }


       /* if(backPress.equals("contentDetail")){
            SharaGoPref.getInstance(requireActivity()).setSectionId(sectionId!!)
            mViewModel.getVAllWithID(sectionId!!, "1", "20", "", country!!)


            //Result Api
            mViewModel.lVAllWithIDReq.observe(requireActivity()) { it ->
                when (it) {
                    is ExceptionHandler.Loading -> {
                        LoaderFragment.showLoader(requireActivity().supportFragmentManager)
                    }
                    is ExceptionHandler.Success -> {
                        LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                        mViewModel.lVAllWithIDReq.removeObservers(this);

                        lifecycleScope.launch{
                            it.data?.let {
                                LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                                try{
                                    contentList= it.contentList as ArrayList<ContentListItem?>?
                                    contentListSize=it.contentList!!.size
                                    if (contentList!!.size>0){
                                        iv_no_data_found.visibility=View.GONE
                                        mediaSectionWithIdAdapter.notifyDataSetChanged()

                                    }else{
                                        iv_no_data_found.visibility=View.GONE
                                    }
                                }catch (e: Exception){}
                            }
                        }

                    }
                    is ExceptionHandler.Error -> {
                        LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)

                    }
                }
            }
        }*/


    }
    private fun cartDetailsAPi(token:String){
        mViewModel.getCardDetailsUser(token!!)
        mViewModel.lCartDetailsUser.observe(this) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                }
                is ExceptionHandler.Success -> {

                    it.data?.let {
                        Log.e("cartDetails", it.toString())
                     //   cartResponse = it
                        SharaGoPref.getInstance(requireActivity()).setCartId(it.cartId.toString())
                    }
                }
                is ExceptionHandler.Error -> {
                    Log.e("Error", it.errorMessage.toString())
                }
            }
        }
    }

    override fun bookSession(contentListItem: ContentListItem) {
        //mediaSectionWithIdAdapter.notifyDataSetChanged()
        val intent = Intent(requireActivity(), CommentAllListActivity ::class.java).putExtra("id",contentListItem.id.toString()).putExtra("subject",contentListItem.shortDescription).putExtra("contentListItem",contentListItem.toString())
        startActivityForResult(intent, 2)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
      /*  if (requestCode === 2) {
            //val message = data!!.getSerializableExtra("MESSAGE") as ContentListItem

            mediaSectionWithIdAdapter.notifyDataSetChanged()
          //  val message: String = attr.data.getStringExtra("MESSAGE")
           // textView1.setText(message)
        }
        mediaSectionWithIdAdapter.notifyDataSetChanged()
*/
    }
    fun setPageLimit(manager: LinearLayoutManager) {
        recycler_media_product.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val visibleItemCount = manager.childCount
                    val totalItemCount = manager.itemCount
                    val firstVisibleItemPosition = manager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                            page = page + 1
                            isLoading = true
                            contentWithIDApi(sectionId!!, page.toString())
                        }
                    }
                }
            })
    }




}