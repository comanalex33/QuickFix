package app.quic.mobile.activities

import android.content.Intent
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresFeature
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import app.quic.mobile.R
import app.quic.mobile.dialogs.ProfileDialog
import app.quic.mobile.fragments.HomeFragment
import app.quic.mobile.fragments.MakeRequestFragment
import app.quic.mobile.fragments.RequestsFragment
import app.quic.mobile.interfaces.IUpdateData
import app.quic.mobile.models.BuildingModel
import app.quic.mobile.models.UserModel
import app.quic.mobile.services.ApiClient
import app.quic.mobile.services.LoggedInUser
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, IUpdateData {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    internal lateinit var toolbar: Toolbar
    private lateinit var actionBarToggle: ActionBarDrawerToggle

    private lateinit var userModel: UserModel
    private var buildingName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupDrawer()
        if(savedInstanceState == null) {
            addFragment(HomeFragment())
            navigationView.setCheckedItem(R.id.nav_home)
        }

        setupVisibility()

        getUserData()
        val userProfilePicture = findViewById<ImageView>(R.id.toolbar_profile)
        userProfilePicture.setOnClickListener {
            if(buildingName != null) {
                val dialog = ProfileDialog()
                dialog.setUserData(userModel, buildingName)
                dialog.addListener(this)
                dialog.show(this.supportFragmentManager, "ProfileDialog")
            }
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

    private fun getUserData() {
        val getDataCall: Call<UserModel> = ApiClient.getService().getUserData(LoggedInUser.username!!)

        getDataCall.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    userModel = response.body()!!
                    getBuildingName(userModel.buildingId)
                } else {
                    Toast.makeText(applicationContext, response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(applicationContext, "Failure", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getBuildingName(id: Long) {
        val getBuildingById: Call<BuildingModel> = ApiClient.getService().getBuildingById(id)

        getBuildingById.enqueue(object : Callback<BuildingModel> {
            override fun onResponse(call: Call<BuildingModel>, response: Response<BuildingModel>) {
                buildingName = if(response.isSuccessful) {
                    response.body()!!.name
                } else {
                    Toast.makeText(applicationContext, response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                    "-"
                }
            }

            override fun onFailure(call: Call<BuildingModel>, t: Throwable) {
                Toast.makeText(applicationContext, "Failure", Toast.LENGTH_LONG).show()
            }

        })
    }


    override fun updateData(data: String) {
        buildingName = data
    }


}