package com.cradle.user.userActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cradle.BR
import com.cradle.R
import com.cradle.application.ApplicationClass
import com.cradle.common_fragment.LoaderFragment
import com.cradle.databinding.ActivityCategoryFilterBinding
import com.cradle.model.category.CategoryItem
import com.cradle.model.category.SubCategoriesItem
import com.cradle.repository.ExceptionHandler
import com.cradle.user.adapters.filter.FilterCategoryAdapter
import com.cradle.user.adapters.filter.FilterSubCategoryAdapter
import com.cradle.utils.MyHelper
import com.cradle.utils.SharaGoPref
import com.cradle.utils.showToast
import com.cradle.viewmodel.MainViewModel
import com.cradle.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_view_all.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


class CategoryFilterActivity : AppCompatActivity(), FilterCategoryAdapter.FilterCatergoryInterface,
    FilterSubCategoryAdapter.FilterSubCategoryInterface {
    private lateinit var mBinding: ActivityCategoryFilterBinding
    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val response = (application as ApplicationClass).repository
        mBinding = ActivityCategoryFilterBinding.inflate(layoutInflater)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(response))[MainViewModel::class.java]
        mBinding.setVariable(BR.onContentClick, this)
        setContentView(mBinding.root)
        findId()
    }

    fun findId() {
        iv_back_trade.setOnClickListener { onBackPressed() }
        // setSubCategoryRecyler()
        apiHit()
    }


    private fun apiHit() {
        if (MyHelper.isNetworkConnected(this)) {
            LoaderFragment.showLoader(this.supportFragmentManager)
            mViewModel.categoryParam()
            mViewModel.getCardDetailsByIdParam(
                SharaGoPref.getInstance(this).getLoginToken("").toString(),
                SharaGoPref.getInstance(this).getCartId("").toString()
            )
            apiResult()
        } else this.showToast(getString(R.string.no_internet_connection))
    }

    private fun apiResult() {

        mViewModel.lCategory.observe(this) {
            when (it) {
                is ExceptionHandler.Loading -> {}
                is ExceptionHandler.Success -> {
                    //   mainBinding.shimmerFrameLayout.stopShimmer()
                    //   mainBinding.shimmerFrameLayout.visibility=View.GONE
                    LoaderFragment.dismissLoader(this.supportFragmentManager)
                    it.data?.let {
                        LoaderFragment.dismissLoader(this.supportFragmentManager)
                        try {
                            CoroutineScope(Dispatchers.IO).async {
                                async {
                                    Log.e("catList", it.categoryList.toString())

                                    setCategoryRecyler(it.categoryList)

                                }

                            }


                        } catch (_: Exception) {

                        }

                    }
                }
                is ExceptionHandler.Error -> {
                    //  Utility.toastMessage(this,it.errorMessage)
                }
            }
        }
    }

    private fun setCategoryRecyler(categoryList: List<CategoryItem?>?) {
        mBinding.categoryAdapter = FilterCategoryAdapter(this, categoryList, this)
    }

    private fun setSubCategoryRecyler(subCategories: List<SubCategoriesItem?>?) {
        mBinding.subCategoryAdapter = FilterSubCategoryAdapter(this, subCategories, this)
    }

    override fun filterCategoryPos(categoryItem: CategoryItem?) {
        setSubCategoryRecyler(categoryItem?.subCategories)
    }

    override fun filterSubCategoryPos(id: String) {
        val resultIntent = Intent()
        resultIntent.putExtra("id", id)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}