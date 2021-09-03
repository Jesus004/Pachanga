package com.txus.pachangas.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.App
import com.bumptech.glide.Glide
import com.txus.pachangas.R
import com.txus.pachangas.databinding.ActivityMainBinding
import com.txus.pachangas.databinding.AppBarMainBinding
import com.txus.pachangas.databinding.NavHeaderMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val cordinatorBinding = AppBarMainBinding.bind(binding.cordinatorLayout.root)
        val toolbar: Toolbar = cordinatorBinding.toolbar
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout

        drawerLayout.addDrawerListener(object :
            ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)


                val navView: NavigationView = findViewById(R.id.nav_view)
                val headerView = navView.getHeaderView(0)
                val headerViewBinding = NavHeaderMainBinding.bind(headerView)
                //


                val user = App.getAuth().currentUser!!
                headerViewBinding.headerUsername.text = user.displayName
                headerViewBinding.headerEmail.text = user.email

                if (user.photoUrl != null) {

                    val circularProgress = CircularProgressDrawable(this@MainActivity)
                    circularProgress.strokeWidth = 5f
                    circularProgress.centerRadius = 30f
                    circularProgress.start()

                    Glide             //recuperar la imagen de perfil de firestore
                        .with(this@MainActivity)
                        .load(user.photoUrl)
                        .centerCrop()
                        .placeholder(circularProgress)
                        .into(headerViewBinding.headerImg)

                }
            }
        })

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)





        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,

                ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {// funcion para enlazar menú de puntos(derecha) con el fragmento deseado

        when (item.itemId) {

            R.id.action_perfil -> {

                val navController = findNavController(R.id.nav_host_fragment)

                navController.navigate(R.id.profileFragment)
                return true


            }

            R.id.action_close -> {
                App.getAuth().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()

            }


        }
        return false
    }
}