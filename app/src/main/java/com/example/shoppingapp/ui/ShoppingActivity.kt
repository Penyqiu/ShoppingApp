package com.example.shoppingapp.ui

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.other.ShoppingItemAdapter
import com.example.shoppingapp.data.db.entities.ShoppingItem
import kotlinx.android.synthetic.main.activity_shopping.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ShoppingActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: ShoppingViewModelFactory by instance()
    lateinit var toggle : ActionBarDrawerToggle

    lateinit var viewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        viewModel = ViewModelProvider(this, factory).get(ShoppingViewModel::class.java)

        val adapter = ShoppingItemAdapter(listOf(), viewModel)

        rvShoppingItems.layoutManager = LinearLayoutManager(this)
        rvShoppingItems.adapter = adapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {

                R.id.Glowna->startActivity(Intent(this@ShoppingActivity,ShoppingActivity::class.java))

                R.id.Mapy->startActivity(Intent(this@ShoppingActivity,MapsActivity::class.java))

                R.id.Gallery->startActivity(Intent(this@ShoppingActivity,GalleryActivity::class.java))

                R.id.Player->startActivity(Intent(this@ShoppingActivity,MediaPlayer::class.java))


            }
            true
        }

        viewModel.getAllShoppingItems().observe(this, Observer {

            adapter.items = it
            adapter.notifyDataSetChanged()
        })

        fab.setOnClickListener {
            AddShoppingItemDialog(
                this,
                object : AddDialogListener {
                    override fun onAddButtonClicked(item: ShoppingItem) {
                        viewModel.upsert(item)
                  }
              }).show()
        }
    }

    override fun onStart() {
        super.onStart()

        Toast.makeText(getApplicationContext(),"Aplikacja uruchomiła się!", Toast.LENGTH_LONG).show();
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}