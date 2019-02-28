package com.greenmonkeys.handmade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.greenmonkeys.handmade.persistence.Artisan

class ArtisanHomeActivity : AppCompatActivity() {
    lateinit var cgaId: String
    lateinit var artisanEmail: String

    lateinit var information: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artisan_home)

        cgaId = intent.getStringExtra("CGA_ID")
        artisanEmail = intent.getStringExtra("EMAIL")

        information = findViewById(R.id.activity_artisan_home_information)

        information.text = "Welcome, ${artisanEmail}"
    }
}
