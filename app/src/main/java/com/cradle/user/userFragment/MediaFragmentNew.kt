package com.cradle.user.userFragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.common_fragment.LoaderFragment
import com.cradle.common_screen.CountryActivity
import com.cradle.databinding.FragmentMediaBinding
import com.cradle.databinding.FragmentMediaNewBinding
import com.cradle.databinding.FragmentUserMediaBinding
import com.cradle.model.ContentListItem
import com.cradle.model.SectionsItem
import com.cradle.model.media.CatListItem
import com.cradle.model.media.CategoryList
import com.cradle.repository.ExceptionHandler
import com.cradle.repository.QuoteRepository
import com.cradle.user.adapters.UserMediaCategoryAdapter
import com.cradle.user.adapters.UserMediaProductAdapter
import com.cradle.user.adapters.UserMediaSectionAdapter
import com.cradle.user.userActivity.UserAddToCartActivity
import com.cradle.user.userActivity.UserLoginActivity
import com.cradle.user.userActivity.WishListActivity
import com.cradle.utils.MyConstants
import com.cradle.utils.MyHelper
import com.cradle.utils.SharaGoPref
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_product_list_in_country.*
import kotlinx.android.synthetic.main.custom_toolbar_user.*
import kotlinx.android.synthetic.main.custom_toolbar_user.iv_open_country
import kotlinx.android.synthetic.main.custom_toolbar_user.rl_cus_toolbar
import kotlinx.android.synthetic.main.custom_toolbar_user.tv_country_name
import kotlinx.android.synthetic.main.fragment_media_new.*
import kotlinx.android.synthetic.main.fragment_media_new.iv_wishlist_screen
import kotlinx.android.synthetic.main.fragment_media_new.llOpenCountry
import kotlinx.android.synthetic.main.fragment_media_new.rlOpenCart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.json.JSONObject

class MediaFragmentNew : Fragment(), UserMediaCategoryAdapter.TextState,
    UserMediaSectionAdapter.TextBookNow {
    private lateinit var userMediaCategoryAdapter: UserMediaCategoryAdapter
    private lateinit var userMediaProductAdapter: UserMediaProductAdapter
    private var response: QuoteRepository? = null
    private lateinit var mViewModel: MainViewModel
    private lateinit var v: View
    private lateinit var mBinding: FragmentMediaNewBinding
    var country: String? = null
    private var categoryImage: String? = null
    private val colourList: ArrayList<String> = ArrayList<String>()
    var cotentList: ContentListItem? = ContentListItem()

    private val sessionIdListList = ArrayList<JSONObject>()
    var categoryListres = CategoryList()
    var count: Int = 1
    val arrayList = ArrayList<String>()
    val categoryList = ArrayList<String>()
    private val imageList = ArrayList<String>()
    private val oneArray = ArrayList<String>()
    private val secondArray = ArrayList<String>()
    private val threeArray = ArrayList<String>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        response = (requireActivity().application as ApplicationClass).repository
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_media_new, container, false
        )
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response!!))[MainViewModel::class.java]
        v = mBinding.root
        if (MyHelper.isNetworkConnected(requireActivity())) {
            contentListWithSection()
            statusBarColourChange()
            allClick()
            margeTwoArray()
            //   allCategoryApi()
        } else requireActivity().showToast(getString(R.string.no_internet_connection))


        return v
    }


    private fun margeTwoArray(){

        oneArray.add("rahul")
        oneArray.add("rahul1")
        oneArray.add("rahul2")
        oneArray.add("rahul3")

        secondArray.add("tamrakar")
        secondArray.add("tamrakar1")
        secondArray.add("tamrakar2")
        secondArray.add("tamrakar3")
     /*   for ( i in 0 until 7){

            threeArray.add(oneArray.get(i).toString())
        }*/

        oneArray.addAll(secondArray)
        Log.e("oneArray",oneArray.toString())
      //  for()
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
                    //   mainBinding.shimmerFrameLayout.stopShimmer()
                    //   mainBinding.shimmerFrameLayout.visibility=View.GONE
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                    // mBinding.reUserCateList.visibility=View.VISIBLE
                    it.data?.let {
                        LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                        try {
                            CoroutineScope(Dispatchers.IO).async {
                                async {
                                    Log.e("allCateList",it.sections.toString())
                                    /*          val parentCatList=ArrayList<CategoryListItem>()
                                              for ( i in 0 until it.categoryList!!.size)  {
                                                  if((it.categoryList[i]!!.parentCategory!!).equals("0")){
                                                      parentCatList.add(it.categoryList[i]!!)
                                                  }
                                              }
                                       */}

                            }


                        }catch (e:Exception){

                        }

                    }
                }
                is ExceptionHandler.Error->{
                    //  Utility.toastMessage(requireActivity(),it.errorMessage)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //setAdapterMediaContent(sessionIdListList)
        if (MyHelper.isNetworkConnected(requireActivity())) {
            // contentListWithSection()
        } else requireActivity().showToast(getString(R.string.no_internet_connection))

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


    private fun contentListWithSection() {
        //Api hit
        mViewModel.userSectionListParam(
            SharaGoPref.getInstance(requireActivity()).getCountry("").toString(), "all"
        )

        //result
        mViewModel.lContentWithSectiot.observe(requireActivity()) { it ->
            when (it) {
                is ExceptionHandler.Loading -> {
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                }
                is ExceptionHandler.Success -> {
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)

                    it.data?.let {

                        val jsonObject = JSONObject(it.toString())
                        val categoryJSONObj: JSONObject = jsonObject.getJSONObject("detail")

                        val iterator = categoryJSONObj.keys()

                        while (iterator.hasNext()) {
                            val key = iterator.next().toString()
                            val contentJSONObject = categoryJSONObj.optJSONObject(key)
                            val contentJsonArray = contentJSONObject.optJSONArray("content")
                            if (contentJsonArray.length() > 0) {
                                var catListItem = CatListItem()
                                catListItem.categoryName = key
                                catListItem.icon = contentJSONObject.optString("icon")
                                catListItem.priority = contentJSONObject.optInt("priority")
                                (categoryListres.catlistItem as ArrayList).add(catListItem)
                                categoryListres.catlistItem?.sortedBy { it.priority }
                                /*categoryList.add(key)
                                categoryImage = contentJSONObject.optString("icon")
                                imageList.add(categoryImage!!)*/
                                for (i in 0 until contentJsonArray.length()) {
                                    sessionIdListList.add(contentJsonArray.get(i) as JSONObject)
                                }
                            }
                            // }
                        }

                        Log.e("catttlist", categoryListres.toString())
                        for (i in 1 until categoryListres.catlistItem!!.size) {
                            categoryListres.catlistItem!!.get(i).pos = i
                        }
                        Log.e("catttlist", categoryListres.toString())

                        setAdapterMediaCategory(categoryListres.catlistItem)
                        //categoryImage = ""
                        setAdapterMediaContent(sessionIdListList)
                    }
                }
                is ExceptionHandler.Error -> {
                    LoaderFragment.dismissLoader(requireActivity().supportFragmentManager)
                    // dismissLoader()
                    // Toast.makeText(this,it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setAdapterMediaCategory(catlistItem: List<CatListItem>?) {
        //    val recyclerView = RecyclerView(requireActivity())
        userMediaCategoryAdapter = UserMediaCategoryAdapter(
            "id", requireActivity(), catlistItem, this
        )
        mBinding.recyclerUserMediaCate.setHasFixedSize(true)
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        /*layoutManager.stackFromEnd = false
         layoutManager.reverseLayout = true*/
        mBinding.recyclerUserMediaCate.layoutManager = layoutManager
        mBinding.recyclerUserMediaCate.adapter = userMediaCategoryAdapter


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapterMediaContent(contentList: ArrayList<JSONObject>) {
        if (contentList.isNotEmpty()) {

            mBinding.ivNoDataFound.visibility = View.GONE
            mBinding.recyclerMediaProduct.visibility = View.VISIBLE
            userMediaProductAdapter =
                UserMediaProductAdapter(requireActivity(), contentList, contentList.size)
            val manager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )
            userMediaProductAdapter.notifyDataSetChanged()
            mBinding.recyclerMediaProduct.layoutManager = manager
            mBinding.recyclerMediaProduct.adapter = userMediaProductAdapter


            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val currentVisblePostion = manager.findFirstVisibleItemPosition()
                    try {
                        val item = contentList.get(currentVisblePostion)
                        for (i in 0 until categoryListres.catlistItem!!.size) {
                            if (item.optInt("priority")
                                    .equals(categoryListres!!.catlistItem?.get(i)?.priority)
                            ) {
                                userMediaCategoryAdapter.setCategoryColor(categoryListres!!.catlistItem?.get(i)?.pos!!)
                                mBinding.recyclerUserMediaCate.scrollToPosition(categoryListres!!.catlistItem?.get(i)?.pos!!)
                                //mBinding.recyclerUserMediaCate.requestFocus(categoryListres!!.catlistItem?.get(i)?.pos!!)
                            }
                        }

                    } catch (e: Exception) {

                    }


                }
            }
            mBinding.recyclerMediaProduct.addOnScrollListener(scrollListener)


        } else {
            mBinding.ivNoDataFound.visibility = View.VISIBLE
            mBinding.recyclerMediaProduct.visibility = View.GONE
        }


    }


    override fun clickonMediaCategory(position: Int, string: String) {
        try {
            var obj = sessionIdListList.get(position)
            mBinding.recyclerMediaProduct.scrollToPosition(4)
        } catch (e: Exception) {
        }
    }


    override fun bookSession(position: Int, data: SectionsItem) {
        LoaderFragment.showLoader(requireActivity().supportFragmentManager)
        SharaGoPref.getInstance(requireActivity()).setSectionId(data.id!!.toString())
        mViewModel.getVAllWithID(data.id.toString(), "1", "10", "", country!!)
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

}