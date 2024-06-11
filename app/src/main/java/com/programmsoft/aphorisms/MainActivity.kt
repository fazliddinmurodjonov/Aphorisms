package com.programmsoft.aphorisms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.programmsoft.aphorisms.databinding.ActivityMainBinding
import com.programmsoft.utils.Functions
import com.programmsoft.utils.SharedPreference

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreference.init(this)
        Functions.connectivityManager(this)
        setContentView(R.layout.activity_main)
        Functions.appearanceStatusNavigationBars(window, 0,
            appearanceLightStatusBars = false,
            appearanceNavigationBars = false
        )
        Functions.statusNavigationBarsColor(window, this, R.color.gray_custom, R.color.black_custom)
        createNav()
        isAppFirstRun()
    }

    private fun createNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = binding.btnNav
        bottomNavigationView.itemIconTintList = null
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_menu,
                R.id.nav_bookmark,
                R.id.nav_settings,
            )
        )
        binding.btnNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->

        }
    }

    private fun isAppFirstRun() {
//        if (SharedPreference.isAppFirstOpen != 1) {
//            Functions.insertCategories()
//            SharedPreference.isAppFirstOpen = 1
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}