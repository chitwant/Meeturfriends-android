package com.meetfriend.app.ui.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.meetfriend.app.R
import com.sanojpunchihewa.updatemanager.UpdateManager
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var mUpdateManager: UpdateManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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
        toggle.isDrawerIndicatorEnabled = true
        val navView: NavigationView = findViewById(R.id.nav_view_slider)
        val navHeader = navView.getHeaderView(0)
        //val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*appBarConfiguration = AppBarConfiguration(setOf(
                 R.id.nav_gallery,R.id.nav_earning, R.id.nav_setting,R.id.nav_logout), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

    /* override fun onSupportNavigateUp(): Boolean {
         val navController = findNavController(R.id.nav_host_fragment)
         return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
     }*/
}