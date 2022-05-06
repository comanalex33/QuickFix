package app.quic.mobile.activities

import android.content.Intent
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresFeature
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import app.quic.mobile.R
import app.quic.mobile.fragments.HomeFragment
import app.quic.mobile.fragments.MakeRequestFragment
import app.quic.mobile.fragments.RequestsFragment
import app.quic.mobile.services.LoggedInUser
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    internal lateinit var toolbar: Toolbar
    private lateinit var actionBarToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupDrawer()
        if(savedInstanceState == null) {
            addFragment(HomeFragment())
            navigationView.setCheckedItem(R.id.nav_home)
        }

        setupVisibility()
    }

    private fun setupDrawer() {
        toolbar = findViewById(R.id.nav_toolbar_toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        navigationView = findViewById(R.id.navigation)
        navigationView.setNavigationItemSelectedListener(this)

        actionBarToggle =ActionBarDrawerToggle(
            this,drawerLayout,toolbar,
            R.string.open,
            R.string.close
        )

        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> addFragment(HomeFragment())
            R.id.nav_requests -> addFragment(RequestsFragment())
            R.id.nav_make_request -> addFragment(MakeRequestFragment())
            R.id.nav_logout -> {
                LoggedInUser.token = null
                LoggedInUser.tokenInfo = null
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, fragment)
            .commit()
    }

    private fun setupVisibility() {
        val pages = if(LoggedInUser.getUserRole() == "student") {
            resources.getStringArray(R.array.student_pages)
        } else {
            resources.getStringArray(R.array.handyman_pages)
        }

        setPageVisibility(HomeFragment(), R.id.nav_home, pages)
        setPageVisibility(MakeRequestFragment(), R.id.nav_make_request, pages)
        setPageVisibility(RequestsFragment(), R.id.nav_requests, pages)
    }

    private fun setPageVisibility(fragment: Fragment, menuId: Int, pages: Array<String>) {
        val menuItem = navigationView.menu.findItem(menuId)
        menuItem.isVisible = pages.contains(fragment::class.simpleName)
    }
}