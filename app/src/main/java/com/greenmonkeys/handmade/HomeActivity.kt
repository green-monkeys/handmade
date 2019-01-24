package com.greenmonkeys.handmade

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenmonkeys.handmade.adapters.ArtisanListRecyclerAdapter
import com.greenmonkeys.handmade.persistence.DatabaseFactory
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class HomeActivity : AppCompatActivity() {
    private var accountType: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val db = DatabaseFactory.getDatabase(applicationContext)
        val artisanListRecycler = findViewById<RecyclerView>(R.id.artisan_recycler_view)
        artisanListRecycler.layoutManager = LinearLayoutManager(this)

        accountType = intent.getStringExtra("ACCOUNT_TYPE")
        if (accountType == "cga") {
            val email = intent.getStringExtra("email")
            doAsync {
                val cga = db.cgaDao().getCGAByEmail(email)
                val artisans = db.cgaDao().getArtisansForCGA(cga.id)
                uiThread {
                    artisanListRecycler.adapter = ArtisanListRecyclerAdapter(artisans)
                    //userInformationView.text = cga.toString()
                }
            }
        } else if (accountType == "artisan") {
            val cgaId = intent.getIntExtra("cga_id", -1)
            val email = intent.getStringExtra("email")
            doAsync {
                val artisan = db.artisanDao().getArtisanByEmail(email, cgaId)
                println(artisan)
                //userInformationView.text = artisan.toString()
                artisanListRecycler.visibility = View.GONE
            }
        }
    }
}
