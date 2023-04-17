package com.cradle.user.userActivity

import android.app.Dialog
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.cradle.R
import com.cradle.common_screen.CountryActivity
import com.cradle.databinding.ActivityUserMainBinding
import com.cradle.user.userFragment.*
import com.cradle.utils.SharaGoPref
import kotlinx.android.synthetic.main.activity_user_main.*
import kotlinx.android.synthetic.main.logout_dialog.*

class UserMainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var userMainBindingImpl: ActivityUserMainBinding
    private var mpref: SharaGoPref? = null
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        uiinitialise()
    }

    private fun uiinitialise() {
        userMainBindingImpl = DataBindingUtil.setContentView(this, R.layout.activity_user_main)
        mpref = SharaGoPref.getInstance(this)
        relativeHome.setOnClickListener(this)
        rl_wish_list.setOnClickListener(this)
        rl_blogs.setOnClickListener(this)
        rl_cart.setOnClickListener(this)
        relativeAccount.setOnClickListener(this)

        val whichfrag = SharaGoPref.getInstance(this@UserMainActivity).getWhichFrag("")
        if (whichfrag!!.isNotEmpty()) {
            if (whichfrag.equals("Home")) {
                onBottomCLickHome()
              //  addFragment(UserHomeFragment())
                addFragment(CotentWithProductFragment(),"media")
              //  statusBarColourChange()
            }else if (whichfrag.equals("Account")) {
                addFragment(UserAccountFragment(),"media")
                onAccountClick()
            } else if (whichfrag.equals("WishList")) {
                SharaGoPref.getInstance(this).setShowList(getString(R.string.vendor_new))
                addFragment(UserSearchResultFragment(),"media")
                onBottomClickWishList()
            } else {
              //  addFragment(UserHomeFragment())
                addFragment(CotentWithProductFragment(),"media")
                onBottomCLickHome()
            }

        } else {
          //  addFragment(UserHomeFragment())
            addFragment(CotentWithProductFragment(),"media")
            onBottomCLickHome()
        }
        statusBarColourChange()
    }

    override fun onResume() {
        super.onResume()
        statusBarColourChange()
        val backPress=SharaGoPref.getInstance(this).getBACKPRESS("").toString()
        val list=mpref!!.getList("")
    /*    if (backPress.equals("contentDetail")&&mpref!!.getList("Other").equals("Other")){
            onBottomCLickHome()
            //  addFragment(UserHomeFragment())
            addFragment(CotentWithProductFragment(),"media")

            SharaGoPref.getInstance(this).setBACKPRESS("other")
        }else{
            //  showToast("Search")
        }*/

     //   SharaGoPref.getInstance(this).setShowList(getString(R.string.products))

    }



    private fun statusBarColourChange() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(SharaGoPref.getInstance(this).getColor(0)!!)
    }

    fun addFragment(fragment: Fragment,fragmentName:String) {
        val manager = supportFragmentManager
        manager.beginTransaction().replace(R.id.container, fragment,fragmentName).commit()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.relativeHome -> {
                backStack=0
                SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.home))
                onBottomCLickHome()
              //  addFragment(UserHomeFragment())

                val myFragment: Fragment? =
                    supportFragmentManager.findFragmentByTag("media")
                Log.e("FindFragment",myFragment.toString())
                if (myFragment != null && myFragment.isVisible()) {
                    // add your code here
                }else{
                   // addFragment(CotentWithProductFragmentNew(),"media")
                    addFragment(CotentWithProductFragment(),"media")
                }

            }
            R.id.rl_wish_list -> {
                if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()) {
                    SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.wishList))
                    onBottomClickWishList()
                    // addFragment(UserWishListFragment())
                    SharaGoPref.getInstance(this).setShowList(getString(R.string.products))
                    backStack=0

                    val myFragment: Fragment? =
                        supportFragmentManager.findFragmentByTag("search")
                    Log.e("FindFragment",myFragment.toString())
                    if (myFragment != null && myFragment.isVisible()) {
                        // add your code here
                    }else{
                        addFragment(UserSearchResultFragment(),"search")

                    }

                } else {
                   /* SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.wishList))
                    onBottomClickWishList()
                    startActivity(
                        Intent(this, UserLoginActivity::class.java)
                    )*/
                   // addFragment(WelcomeScreenActivity())
                    backStack=0
                    SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.wishList))
                    onBottomClickWishList()
                    SharaGoPref.getInstance(this).setShowList(getString(R.string.products))
                    val myFragment: Fragment? =
                        supportFragmentManager.findFragmentByTag("search")
                    Log.e("FindFragment",myFragment.toString())
                    if (myFragment != null && myFragment.isVisible()) {
                        // add your code here
                    }else{
                        addFragment(UserSearchResultFragment(),"search")

                    }

                   // addFragment(UserSearchResultFragment(),"search")
                }
                statusBarColourChange()


            }
            R.id.relativeAccount -> {
                onAccountClick()
                if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()) {
                    SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.account))
                    val myFragment: Fragment? =
                        supportFragmentManager.findFragmentByTag("account")
                    Log.e("FindFragment",myFragment.toString())
                    if (myFragment != null && myFragment.isVisible()) {
                        // add your code here
                    }else{
                        addFragment(UserAccountFragment(),"account")
                    }

                    backStack=0
                } else {
                    startActivity(
                        Intent(this, UserLoginActivity::class.java)
                    )
                   /* SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.account))
                    addFragment(WelcomeScreenActivity())*/
                }
                statusBarColourChange()

            }
            R.id.rl_cart -> {
                onCartClick()
                SharaGoPref.getInstance(this).setColor(-16743602)
                SharaGoPref.getInstance(this).setCountry("Nigeria")

                SharaGoPref.getInstance(this).setCountryFlag("nigeria.png")
                SharaGoPref.getInstance(this).setShowList(this.getString(R.string.products))

                if (SharaGoPref.getInstance(this).getLoginToken("")!!.isNotEmpty()) {
                    onCartClick()
                    val myFragment: Fragment? =
                        supportFragmentManager.findFragmentByTag("notification")
                    Log.e("FindFragment",myFragment.toString())
                    if (myFragment != null && myFragment.isVisible()) {
                        // add your code here
                    }else{
                     //   addFragment(NotificationFragment(),"notification")
                        addFragment(TradeFragment(),"notification")
                    }

                } else {
                    onCartClick()
                    addFragment(TradeFragment(),"notification")

                    /* startActivity(
                         Intent(this, UserLoginActivity::class.java)
                     )*/
                  /*  SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.cart))
                    addFragment(WelcomeScreenActivity())*/
                }
                backStack=0
                statusBarColourChange()

            }
            R.id.rl_blogs -> {
                if (SharaGoPref.getInstance(this).getCountry("")!!.isNotEmpty()) {
                    onBlogsClick()
                    backStack=0
                    //  addFragment(UserCategoryFrag())
                    SharaGoPref.getInstance(this).setToolBarInCate("Yes")

                    val myFragment: Fragment? =
                        supportFragmentManager.findFragmentByTag("category")
                    Log.e("FindFragment",myFragment.toString())
                    if (myFragment != null && myFragment.isVisible()) {
                        // add your code here
                    }else{
                        addFragment(CategoryFragment(),"category")
                    }

                } else {
                    SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.blogs))
                    startActivity(
                        Intent(this, CountryActivity::class.java).putExtra(
                            "screen",
                            "Blogs"
                        )
                    )

                }
                statusBarColourChange()

            }
        }
    }

    private fun onBottomCLickHome() {
        if (mpref!!.getColor(0)!!.toString().equals("0"))
        { SharaGoPref.getInstance(this).setColor(-16743602)
            home_image.setColorFilter(mpref!!.getColor(0)!!, PorterDuff.Mode.SRC_ATOP)
        }else{
            home_image.setColorFilter(mpref!!.getColor(0)!!, PorterDuff.Mode.SRC_ATOP)

        }

        iv_blogs.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_wishlist.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_cart.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        more_image.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )

        textHome.setTextColor(mpref!!.getColor(0)!!)
        tv_blogs.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_wish_list.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_cart.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_account.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun onBottomClickWishList() {
        home_image.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_blogs.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_wishlist.setColorFilter(mpref!!.getColor(0)!!, PorterDuff.Mode.SRC_ATOP)
        iv_cart.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        more_image.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )


        textHome.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_blogs.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_wish_list.setTextColor(mpref!!.getColor(0)!!)
        tv_cart.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_account.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun onAccountClick() {
        home_image.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_blogs.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        more_image.setColorFilter(mpref!!.getColor(0)!!, PorterDuff.Mode.SRC_ATOP)
        iv_cart.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_wishlist.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )

        textHome.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_blogs.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_wish_list.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_cart.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_account.setTextColor(mpref!!.getColor(0)!!)

    }

    private fun onCartClick() {
        home_image.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_blogs.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )

        more_image.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_wishlist.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )

        textHome.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_blogs.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_wish_list.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_cart.setTextColor(mpref!!.getColor(0)!!)
        tv_account.setTextColor(ContextCompat.getColor(this, R.color.black))
        iv_cart.setColorFilter(mpref!!.getColor(0)!!, PorterDuff.Mode.SRC_ATOP)
    }

    private fun onBlogsClick() {
        home_image.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_cart.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_blogs.setColorFilter(mpref!!.getColor(0)!!, PorterDuff.Mode.SRC_ATOP)
        more_image.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_wishlist.setColorFilter(
            ContextCompat.getColor(this, R.color.home_text),
            PorterDuff.Mode.SRC_ATOP
        )

        textHome.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_blogs.setTextColor(mpref!!.getColor(0)!!)
        tv_wish_list.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_cart.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv_account.setTextColor(ContextCompat.getColor(this, R.color.black))
    }
    var backStack=0
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
     //   super.onBackPressed()
       // logOutDialog()

        if (backStack==0){
            SharaGoPref.getInstance(this).setWhichFrag(getString(R.string.home))
            val myFragment: Fragment? =
                supportFragmentManager.findFragmentByTag("media")
          Log.e("FindFragment",myFragment.toString())
            if (myFragment != null && myFragment.isVisible()) {
                // add your code here
                logOutDialog()
            }else{
                onBottomCLickHome()
                //   addFragment(UserHomeFragment())
                addFragment(CotentWithProductFragment(),"media")
                backStack++
            }

        }else{

            logOutDialog()
        }
    }

    private fun logOutDialog() {
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.logout_dialog)
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.full_transparent)))
        dialog!!.tvTitle.text=getString(R.string.are_you_sure_you_want_to_exit_app)
        dialog!!.rlYes.setOnClickListener {
            dialog!!.dismiss()
            finish()
             }
        dialog!!.rlNo.setOnClickListener { dialog!!.dismiss() }
        dialog!!.rlLogOut.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }
}