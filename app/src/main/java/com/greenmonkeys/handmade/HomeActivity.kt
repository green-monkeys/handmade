package com.greenmonkeys.handmade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.greenmonkeys.handmade.persistence.Artisan
import com.greenmonkeys.handmade.persistence.CGA
import com.greenmonkeys.handmade.persistence.DatabaseFactory
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class HomeActivity : AppCompatActivity() {
    private var accountType: String? = null
    private var cga: CGA? = null
    private var artisan: Artisan? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userInformationView = findViewById<TextView>(R.id.user_information_view)

        val db = DatabaseFactory.getDatabase(applicationContext)

        accountType = intent.getStringExtra("ACCOUNT_TYPE")
        if (accountType == "cga") {
            val email = intent.getStringExtra("email")
            doAsync {
                cga = db.cgaDao().getCGAByEmail(email)
                println(cga)
                uiThread {
                    userInformationView.text = cga.toString()
                }
            }
        } else if (accountType == "artisan") {
            val cgaId = intent.getIntExtra("cga_id", -1)
            val email = intent.getStringExtra("email")
            doAsync {
                artisan = db.artisanDao().getArtisanByEmail(email, cgaId.toInt())
                println(artisan)
                userInformationView.text = artisan.toString()
            }
        }
    }
}
