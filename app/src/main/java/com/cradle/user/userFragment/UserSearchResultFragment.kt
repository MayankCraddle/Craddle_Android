package com.cradle.user.userFragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.common_fragment.LoaderFragment.Companion.dismissLoader
import com.cradle.common_fragment.LoaderFragment.Companion.showLoader
import com.cradle.common_screen.CountryActivity
import com.cradle.databinding.ActivityUserSearchResultBinding
import com.cradle.model.ContentListItem
import com.cradle.model.CountryColorCodeListItem
import com.cradle.model.SearchProductProductListItem
import com.cradle.model.SearchVendorVendorListItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.SearchProductResultAdapter
import com.cradle.user.adapters.SearchVendorResultAdapter
import com.cradle.user.adapters.UserMediaProductAdapter
import com.cradle.user.adapters.UserMediaSetionWithIdAdapter
import com.cradle.utils.*
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_user_search_result.*
import org.json.JSONObject


class UserSearchResultFragment : Fragment(), View.OnClickListener,
    UserMediaSetionWithIdAdapter.SendCommit ,SearchProductResultAdapter.AddToWishList{

    private lateinit var vendorListNew: java.util.ArrayList<SearchVendorVendorListItem>
    private lateinit var mBinding: ActivityUserSearchResultBinding
    private lateinit var mViewModel: MainViewModel
    private lateinit var v: View

    var manager: LinearLayoutManager? = null
    var managerProduct: LinearLayoutManager? = null
    lateinit var adapter: SearchVendorResultAdapter
    lateinit var adapterProduct: SearchProductResultAdapter
    lateinit var mUserMediaSetionWithIdAdapter: UserMediaSetionWithIdAdapter

    //pagination
    private var page: Int = 1
    private var isLoading = false
    private var screen: String ="Product"

    val sessionIdListList = ArrayList<JSONObject>()
    var count: Int = 1
    val arrayList = ArrayList<String>()
    val categoryList = ArrayList<String>()
    var countryList: List<CountryColorCodeListItem?>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val response = (requireActivity().applicationContext as ApplicationClass).repository
        mBinding = ActivityUserSearchResultBinding.inflate(layoutInflater)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        v = mBinding.root
        mBinding.setVariable(BR.onWelcomeClick, this)

        return v
    }



    private fun initialiseUI() {
        setCustomToolBar()
        //  apiHit("")
        AnalyticsUtils.analyticReport(requireActivity(),"SearchScreen")
        showApiResult()
        vendorListNew = ArrayList<SearchVendorVendorListItem>()
        mBinding.llOpenDialog.setOnClickListener {
            if(SharaGoPref.getInstance(requireActivity()).getCountry("").equals("Nigeria"))
            moreDialog()

        }

      //  nestedScroll()
       // setRecyclerViewScrollListener()


        mBinding.ivOpenCountry.setOnClickListener {
            startActivity(Intent(requireActivity(), CountryActivity::class.java))
        }
        if(SharaGoPref.getInstance(requireActivity()).getShowList("").equals("Product")&&SharaGoPref.getInstance(requireActivity()).getCountry("").equals("Nigeria")){
            screen=getString(R.string.products_new)
            mBinding.tvProductVendor.text = getString(R.string.products_new)
        }
        if(SharaGoPref.getInstance(requireActivity()).getShowList("").equals("maltimedia")&&SharaGoPref.getInstance(requireActivity()).getCountry("").equals("Nigeria")){
            screen=getString(R.string.multimedia_new)
            mBinding.tvProductVendor.text = getString(R.string.media)
        }

        if(SharaGoPref.getInstance(requireActivity()).getShowList("").equals("PRODUCTS")&&SharaGoPref.getInstance(requireActivity()).getCountry("").equals("Nigeria")){
            screen=getString(R.string.products_new)
            mBinding.tvProductVendor.text = getString(R.string.products_new)
        }else if(SharaGoPref.getInstance(requireActivity()).getShowList("").equals(getString(R.string.vendor_new))&&SharaGoPref.getInstance(requireActivity()).getCountry("").equals("Nigeria")){
            screen=getString(R.string.vendor_new)
            mBinding.tvProductVendor.text = getString(R.string.vendor_new)
        }
        else{
            screen=getString(R.string.multimedia_new)
            mBinding.tvProductVendor.text = getString(R.string.media)
        }
        onSearchProdcuctVendor(screen)
    }

    override fun onResume() {
        super.onResume()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
        mBinding.rlToolbar.setBackgroundColor(
            SharaGoPref.getInstance(requireActivity()).getColor(0)!!
        )
        val flag=SharaGoPref.getInstance(requireActivity()).getCountryFlag("").toString()
        val countryName=SharaGoPref.getInstance(requireActivity()).getCountry("").toString()
        Glide.with(this).load(MyConstants.file_Base_URL_flag+flag).into(mBinding.ivOpenCountry)

        try {
            mBinding.tvCountryName.text=countryName
        }catch (e:Exception){

        }
        requireView().clearFocus()
        initialiseUI()
       // hideKeyBoard(requireActivity())

    }
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }


    var curentItem: Int = 0
    var totalItem: Int = 0
    var scrolOutItem: Int = 0
    private fun setRecyclerViewScrollListener() {
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isLoading = true
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                curentItem = manager!!.childCount
                totalItem = manager!!.itemCount
                scrolOutItem = manager!!.findFirstVisibleItemPosition()

                if (isLoading && (curentItem + scrolOutItem == totalItem)) {
                    isLoading = false
                    page++
                    apiHit("Vendor")
                    adapter.notifyDataSetChanged()
                }
            }
        }
        mBinding.recyclerSerchResultVendor.addOnScrollListener(scrollListener)
    }


    private fun apiHit(whichVendor: String) {
        if (MyHelper.isNetworkConnected(requireActivity())) {
            if (whichVendor.equals(getString(R.string.media))) {
                showLoader(requireActivity().supportFragmentManager)
                mViewModel.userSectionListParam(
                    SharaGoPref.getInstance(requireActivity()).getCountry("").toString(), " "
                )

            } else {
                showLoader(requireActivity().supportFragmentManager)
                mViewModel.getSearchListParam(
                    whichVendor,
                    SharaGoPref.getInstance(requireActivity()).getCountry("")!!,
                    "",
                    page,
                    100
                )

            }
        } else requireActivity().showToast(getString(R.string.no_internet_connection))

    }

    var productList: List<SearchProductProductListItem?>? = null
    var cotentList: List<ContentListItem?>? = null
    private fun showApiResult() {
        mViewModel.lgetSearchList.observe(requireActivity()) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader(requireActivity().supportFragmentManager)
                }
                is ExceptionHandler.Success -> {
                    dismissLoader(requireActivity().supportFragmentManager)
                    it.data?.let {

                        try {
                            if (it.vendorList!!.isNotEmpty()) {
                                vendorListNew.clear()
                                mBinding.llNoSearchResult.visibility = View.GONE
                                mBinding.llSearchItem.visibility = View.VISIBLE
                                for (i in 0 until it.vendorList.size) {

                                    vendorListNew.add(it.vendorList[i]!!)
                                }

                                Log.e("vendorList", it.vendorList.toString())

                                mBinding.recyclerSerchResultVendor.visibility = View.VISIBLE
                                mBinding.recyclerSearchProduct.visibility = View.GONE
                                mBinding.recyclerCotentList.visibility = View.GONE

                                setRecyclerView1(vendorListNew)

                            }

                        } catch (e: Exception) {
                            if (it.productList!!.isNotEmpty()) {
                                productList = it.productList
                                mBinding.llNoSearchResult.visibility = View.GONE
                                mBinding.llSearchItem.visibility = View.VISIBLE

                                Log.e("produtList", it.productList.toString())
                                /* mBinding.recyclerVendorList.visibility=View.VISIBLE
                                 mBinding.ivNoDataFound.visibility=View.GONE*/
                                mBinding.recyclerSerchResultVendor.visibility = View.GONE
                                mBinding.recyclerSearchProduct.visibility = View.VISIBLE
                                mBinding.recyclerCotentList.visibility = View.GONE
                                setRecyclerViewProduct()
                                //     mBinding.mSearchProductResultAdapter= SearchProductResultAdapter(requireActivity(),it.productList)

                            }else{
                                mBinding.llNoSearchResult.visibility = View.VISIBLE
                                mBinding.llSearchItem.visibility = View.GONE
                                mBinding.recyclerCotentList.visibility = View.GONE
                            }
                        }


                    }

                }
                is ExceptionHandler.Error -> {
                    Log.e("error", it.errorMessage.toString())
                    requireActivity().showToast(it.errorMessage)
                    dismissLoader(requireActivity().supportFragmentManager)

                }
            }
        }
        mViewModel.lgetSearchContent.observe(requireActivity()) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader(requireActivity().supportFragmentManager)
                }
                is ExceptionHandler.Success -> {
                    dismissLoader(requireActivity().supportFragmentManager)
                    it.data?.let {

                        try {
                            if (it.contentList!!.isNotEmpty()) {
                                mBinding.llNoSearchResult.visibility = View.GONE
                                mBinding.llSearchItem.visibility = View.VISIBLE
                                mBinding.recyclerCotentList.visibility = View.VISIBLE

                                cotentList = it.contentList

                                mBinding.recyclerSerchResultVendor.visibility = View.GONE
                                mBinding.recyclerSearchProduct.visibility = View.GONE
                                mBinding.recyclerCotentList.visibility = View.VISIBLE

                                 setRecyclerCotent()

                            }

                        } catch (_: Exception) {
                        }


                    }

                }
                is ExceptionHandler.Error -> {
                    Log.e("error", it.errorMessage.toString())
                    requireActivity().showToast(it.errorMessage)
                    dismissLoader(requireActivity().supportFragmentManager)

                }
            }
        }

    }


    private fun setCustomToolBar() {
      val whichfrag=  SharaGoPref.getInstance(requireActivity()).getWhichFrag("")

        if(whichfrag.equals(getString(R.string.search))){
            mBinding.rlToolbar.visibility=View.VISIBLE
        }else{
            mBinding.rlToolbar.visibility=View.VISIBLE
        }
    }

    override fun onClick(p0: View?) {

    }

    fun onSearchProdcuctVendor(whichListSearch: String) {
        if (whichListSearch.isNotEmpty()) {
            mBinding.countrySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {


                    if (screen.equals(getString(R.string.media))) {
                        mViewModel.getSearchContentParam(   whichListSearch,
                            SharaGoPref.getInstance(requireActivity()).getCountry("")!!,
                            newText.toString(),
                            1,
                            50)
                     /*   mViewModel.userSectionListParam(
                            SharaGoPref.getInstance(requireActivity()).getCountry("").toString(), "all"
                        )*/
                    } else {
                        mViewModel.getSearchListParam(
                            whichListSearch,
                            SharaGoPref.getInstance(requireActivity()).getCountry("")!!,
                            newText.toString(),
                            1,
                            50
                        )

                    }


                    return false
                }

            })
        }

    }


    private fun openProductList() {
        curentItem = 0
        totalItem = 0
        scrolOutItem = 0
        mBinding.recyclerSerchResultVendor.visibility = View.GONE
        mBinding.recyclerSearchProduct.visibility = View.VISIBLE

        apiHit(getString(R.string.products_new))
        screen = getString(R.string.products_new)
        onSearchProdcuctVendor(screen)

        //  onSearchProdcuctVendor(screen)

    }

    private fun openVendorList() {
        curentItem = 0
        totalItem = 0
        scrolOutItem = 0
        mBinding.recyclerSerchResultVendor.visibility = View.VISIBLE
        mBinding.recyclerSearchProduct.visibility = View.GONE
        apiHit(getString(R.string.vendor_new))
        screen = getString(R.string.vendor_new)
        onSearchProdcuctVendor(screen)

    }

    private fun openMediaContentList() {
        curentItem = 0
        totalItem = 0
        scrolOutItem = 0
        mBinding.recyclerSerchResultVendor.visibility = View.GONE
        mBinding.recyclerSearchProduct.visibility = View.GONE
        mBinding.recyclerCotentList.visibility = View.VISIBLE
        screen = getString(R.string.media)
        //apiHit(getString(R.string.media))
        onSearchProdcuctVendor(screen)
    }

    private fun setRecyclerView1(vendorListNew: ArrayList<SearchVendorVendorListItem>) {
        adapter = SearchVendorResultAdapter(
            requireActivity(),
            vendorListNew as ArrayList<SearchVendorVendorListItem?>?
        )
        manager = LinearLayoutManager(requireActivity())
        mBinding.recyclerSerchResultVendor.layoutManager = manager
        mBinding.recyclerSerchResultVendor.adapter = adapter
        //  setPageLimit(manager)
    }

    private fun setRecyclerViewProduct() {
        managerProduct = LinearLayoutManager(requireActivity())
        mBinding.recyclerSearchProduct.layoutManager = managerProduct
        adapterProduct = SearchProductResultAdapter(requireActivity(), productList,this)
        mBinding.recyclerSearchProduct.adapter = adapterProduct
        //  setPageLimit(manager)
    }

    private fun setRecyclerCotent() {
        mBinding.llNoSearchResult.visibility = View.GONE
        mBinding.llSearchItem.visibility = View.VISIBLE
        recycler_cotent_list.visibility=View.VISIBLE
        recycler_search_product.visibility=View.GONE
        recycler_serch_result_vendor.visibility=View.GONE
        ll_no_search_result.visibility=View.GONE
        managerProduct = LinearLayoutManager(requireActivity())
        mBinding.recyclerCotentList.layoutManager = managerProduct
        mUserMediaSetionWithIdAdapter =
            UserMediaSetionWithIdAdapter(requireActivity(), cotentList, this)
        mBinding.recyclerCotentList.adapter = mUserMediaSetionWithIdAdapter
        //  setPageLimit(manager)
    }

    private var dialog: Dialog? = null
    private fun moreDialog() {
        dialog = Dialog(requireActivity())
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_search)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))

        dialog!!.setCanceledOnTouchOutside(true)

        val tv_product_click = dialog!!.findViewById(R.id.tv_product_click) as TextView
        val tv_vendor_click = dialog!!.findViewById(R.id.tv_vendor_click) as TextView
        val tv_malti_click = dialog!!.findViewById(R.id.tv_malti_click) as TextView
        val tv_dismiss = dialog!!.findViewById(R.id.tv_dismiss) as TextView
        val rl_dialog = dialog!!.findViewById(R.id.rl_dialog) as RelativeLayout

        tv_product_click.setOnClickListener {
            mBinding.tvProductVendor.text = getString(R.string.products_new)
            openProductList()
            dialog!!.dismiss()
        }
        rl_dialog.setOnClickListener {
            dialog!!.dismiss()
        }
        tv_vendor_click.setOnClickListener {
            mBinding.tvProductVendor.text = getString(R.string.vendor_new)
            openVendorList()
            dialog!!.dismiss()
        }
        tv_malti_click.setOnClickListener {
            mBinding.tvProductVendor.text = getString(R.string.media)
          // resultMediaContent()
           openMediaContentList()
            dialog!!.dismiss()
        }
        tv_dismiss.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.show()
    }

    override fun bookSession(jsonObject: JsonObject) {
        //mViewModel.userAddCommitParam(SharaGoPref.getInstance(requireActivity()).getLoginToken("").toString(),jsonObject)

    }

    private fun resultMediaContent(){
        //Api hit
        curentItem = 0
        totalItem = 0
        scrolOutItem = 0
        mBinding.recyclerSerchResultVendor.visibility = View.GONE
        mBinding.recyclerSearchProduct.visibility = View.GONE
        mBinding.recyclerCotentList.visibility = View.VISIBLE
        screen = getString(R.string.media)
      /*  onSearchProdcuctVendor(screen)
        apiHit(getString(R.string.media))*/
        mViewModel.userSectionListParam(
            SharaGoPref.getInstance(requireActivity()).getCountry("").toString(), "all"
        )

        //result
        mViewModel.lContentWithSectiot.observe(requireActivity()) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                    dismissLoader(requireActivity().supportFragmentManager)
                }
                is ExceptionHandler.Success -> {
                    dismissLoader(requireActivity().supportFragmentManager)
                    //    dismissLoader()
                    it.data?.let {
                        val jsonObject = JSONObject(it.toString())
                        val categoryJSONObj: JSONObject = jsonObject.getJSONObject("detail")
                        val iterator = categoryJSONObj.keys()

                        while (iterator.hasNext()) {
                            val key = iterator.next().toString()
                            //val arrayList = ArrayList<String>()
                            categoryList.add(key)
                            //Log.e("catelist", key.toString())
                            val contentJSONObject = categoryJSONObj.optJSONObject(key)
                            val id = contentJSONObject!!.optString("sectionId")

                            if (count == id.toInt()) {
                                count++

                                val contentJsonArray = contentJSONObject.optJSONArray("content")
                                for (i in 0 until contentJsonArray.length()) {

                                    sessionIdListList.add(contentJsonArray.get(i) as JSONObject)
                                }
                            }

                        }

                        setAdapterMediaContent(sessionIdListList)
                    }
                }
                is ExceptionHandler.Error -> {
                    dismissLoader(requireActivity().supportFragmentManager)
                    // dismissLoader()
                    // Toast.makeText(this,it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun setAdapterMediaContent(contentList: ArrayList<JSONObject>) {
        if (contentList.size > 0) {
            mBinding.llNoSearchResult.visibility = View.GONE
            mBinding.llSearchItem.visibility = View.VISIBLE
            recycler_cotent_list.visibility=View.VISIBLE
            recycler_search_product.visibility=View.GONE
            recycler_serch_result_vendor.visibility=View.GONE
            ll_no_search_result.visibility=View.GONE
        mBinding.userMediaProductAdapter = UserMediaProductAdapter(requireActivity(),
            contentList, contentList.size)



        } else {
    }


    }
    //Country list api
    fun onSearchCountry(){
        country_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
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
        val filterdNames: ArrayList<CountryColorCodeListItem> = ArrayList()

        //looping through existing elements
        for (s in this.countryList!!) {
            //if the existing elements contains the search input
            if (s!!.countryName!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)

            }
        }
      //  filterdNames = mBinding.userMediaProductAdapter!!.filterList(filterdNames)

    }

    override fun addWishList(position: Int, dataList: SearchProductProductListItem?) {

        val jsonObject = JsonObject()
        jsonObject.addProperty("itemId", dataList!!.itemId)
   /*     if (wishListItem) {
            mViewModel.removeProductFromWishlistParam(token!!, jsonObject)
        } else {
            mViewModel.addProductToWishlistParam(token!!, jsonObject)
        }*/
        mViewModel.addProductToWishlistParam(SharaGoPref.getInstance(requireActivity()).getLoginToken("")!!, jsonObject)


        mViewModel.lAddProductToWishlistParam.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {

                }
                is ExceptionHandler.Success -> {

                    val jsonObject = JSONObject(it.data.toString())
                 //   Utility.toastMessage(requireActivity(), jsonObject.optString("message"))

                }
                is ExceptionHandler.Error -> {


                }
            }
        }
    }

    private fun nestedScroll(){

        // adding on scroll change listener method for our nested scroll view.

        // adding on scroll change listener method for our nested scroll view.
        nestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                page++
              //  loadingPB.setVisibility(View.VISIBLE)
                apiHit("Vendor")
              //  getDataFromAPI(page, limit)
            }
        })

    }


}

