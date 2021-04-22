package com.meetfriend.app.ui.activities

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.meetfriend.app.R
import com.meetfriend.app.connectivity.base.ConnectivityProvider
import com.meetfriend.app.utilclasses.MyFcmListenerService
import com.meetfriend.app.utilclasses.UtilsClass
import com.meetfriend.app.viewmodal.NotificationsViewModel
import contractorssmart.app.utilsclasses.PreferenceHandler
import de.hdodenhof.circleimageview.CircleImageView

class HomeActivity : AppCompatActivity(), ConnectivityProvider.ConnectivityStateListener {
    lateinit var ivSearchIcon: ImageView
    val provider: ConnectivityProvider by lazy { ConnectivityProvider.createProvider(this@HomeActivity) }
    private var snackbar: Snackbar? = null
    var activityHeader: ConstraintLayout? = null
    var nav_view: BottomNavigationView? = null
    var ivBackIcon: ImageView? = null
//    var ivSearchIcon: ImageView? = null
    var mNotification: RelativeLayout? = null
    var mCount: AppCompatTextView? = null
    var toolbar: Toolbar? = null
    var notificationCount: Int? = 0
    private lateinit var notificationsViewModel: NotificationsViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        val receiver = ComponentName(applicationContext, MyFcmListenerService::class.java)

        packageManager?.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        setSupportActionBar(toolbar)
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
        toggle.isDrawerIndicatorEnabled = false

        activityHeader = findViewById(R.id.activityHeader)
        nav_view = findViewById(R.id.nav_view)
        ivBackIcon = findViewById(R.id.ivBackIcon)
        ivSearchIcon = findViewById(R.id.ivSearchIcon)
        mNotification = findViewById(R.id.mNotification)
        mCount = findViewById(R.id.mCount)
        mNotification?.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, NotificationTempActivity::class.java)
            startActivity(intent)

        })
        val navViewHeader: NavigationView = findViewById(R.id.nav_view_slider)
        val navHeader = navViewHeader.getHeaderView(0)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_friend,
                R.id.navigation_message,
                R.id.navigation_mychallenge,
                R.id.navigation_more
            )
        )
        // setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val view = findViewById<View>(android.R.id.content)
        if (view != null) {
            snackbar = Snackbar
                .make(view, "No internet connection", Snackbar.LENGTH_INDEFINITE)
        }

        //toolbar
        val imageView = navHeader?.findViewById<CircleImageView>(R.id.imageView)
        val mLocation = navHeader?.findViewById<TextView>(R.id.mLocation)
        val tvUserName = navHeader?.findViewById<TextView>(R.id.tvUserName)
        tvUserName?.setText(
            PreferenceHandler.readString(
                this,
                "FIRSTNAME",
                ""
            ) + " " + PreferenceHandler.readString(this, "LASTNAME", "")
        )
        mLocation?.setText(PreferenceHandler.readString(this, "CITY", ""))
        Glide.with(this).load(PreferenceHandler.readString(this, "PROFILE_PHOTO", ""))
            .into(imageView!!)
        val menu_drawer = toolbar?.findViewById<ImageView>(R.id.nav_drawer_icon)
        menu_drawer?.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        navHeader.findViewById<RelativeLayout>(R.id.navGiftLayout).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this, GiftGalleryActivity::class.java)
            intent.putExtra("from", "side")
            startActivity(intent)
        }
        navHeader.findViewById<RelativeLayout>(R.id.navEarninglayout).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this, MyEarningActivity::class.java)
            startActivity(intent)
        }
        navHeader.findViewById<RelativeLayout>(R.id.navGiftlayout).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this, GiftTransactionActivity::class.java)
            startActivity(intent)
        }
        navHeader.findViewById<RelativeLayout>(R.id.navSettingLayout).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        navHeader.findViewById<RelativeLayout>(R.id.navLogoutLayout).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            logout()
        }
        initializeObservers()
    }

    override fun onResume() {
        super.onResume()
        if (notificationCount!! > 0) {
            mCount?.visibility = View.VISIBLE

            if (notificationCount!! < 10)
                mCount?.setText("" + notificationCount)
            else
                mCount?.setText("9+")
        } else {
            mCount?.visibility = View.GONE

        }
        notificationsViewModel?.countNotification()
        UtilsClass.updateUserStatus(this, true)
    }

    override fun onPause() {
        super.onPause()
        UtilsClass.updateUserStatus(this, false)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initializeObservers() {
        notificationsViewModel?.notifcationCountResponse!!.observe(this, Observer {
            it.let {
                if (it.status) {
                    notificationCount = it.result
                    if (notificationCount!! > 0) {
                        mCount?.visibility = View.VISIBLE

                        if (notificationCount!! < 10) {
                            mCount?.setText("" + notificationCount)
                        } else {
                            mCount?.setText("9+")
                        }
                    } else
                        mCount?.visibility = View.GONE
                } else {
                }
            }
        })
        notificationsViewModel?.notifcationReadAllResponse!!.observe(this, Observer {
            it.let {
                if (it.status) {
                    notificationCount = 0
                    if (notificationCount!! > 0) {
                        mCount?.visibility = View.VISIBLE
                        if (notificationCount!! < 10) {
                            mCount?.setText("" + notificationCount)
                        } else {
                            mCount?.setText("+9")
                        }
                    } else
                        mCount?.visibility = View.GONE
                } else {
                }
            }
        })

    }


    private fun logout() {
        val builder =
            AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setMessage("Are you sure, you want to logout?")
        builder.setPositiveButton(
            "Yes"
        ) { dialog, _ ->
            dialog.dismiss()
            UtilsClass.updateUserStatus(this, false)

            PreferenceHandler.clearSharePreferences(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ ->
            dialog.dismiss()

        }
        val alert = builder.create()
        alert.show()
    }


    fun showAndHideBottomNavigation(status: Boolean) {
        if (status) {
            nav_view!!.visibility = View.VISIBLE
        } else {
            nav_view!!.visibility = View.GONE
        }

    }

    override fun onStart() {
        super.onStart()
        provider.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        provider.removeListener(this)
    }

    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        val hasInternet = state.hasInternet()
        if (snackbar != null) {
            if (hasInternet) {
                snackbar!!.dismiss()
            } else {
                snackbar!!.show()
            }
        }
    }

    companion object {
        fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
            return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
        }
    }
}