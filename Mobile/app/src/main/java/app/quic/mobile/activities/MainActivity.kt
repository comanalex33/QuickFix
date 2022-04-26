package app.quic.mobile.activities

import android.content.Intent
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.RequiresFeature
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import app.quic.mobile.R
import app.quic.mobile.fragments.HomeFragment
import app.quic.mobile.fragments.RequestsFragment
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
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, fragment)
            .commit()
    }
}