package com.greenmonkeys.handmade

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.greenmonkeys.handmade.persistence.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.image
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileInputStream

class ArtisanInformationActivity : AppCompatActivity() {
    lateinit var profilePictureView: ImageView
    lateinit var nameView: TextView
    lateinit var emailView: TextView
    lateinit var phoneView: TextView
    lateinit var generateReportButton: Button
    lateinit var payoutButton: Button

    lateinit var db: AppDatabase

    lateinit var cgaId: String
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artisan_information)

        cgaId = intent.getStringExtra("CGA_ID")
        email = intent.getStringExtra("EMAIL")

        db = DatabaseFactory.getDatabase(applicationContext)

        profilePictureView = findViewById(R.id.activity_artisan_information_profile_picture)
        nameView = findViewById(R.id.activity_artisan_information_name)
        emailView = findViewById(R.id.activity_artisan_information_email)
        phoneView = findViewById(R.id.activity_artisan_information_phone)
        generateReportButton = findViewById(R.id.activity_artisan_information_generate_report)
        payoutButton = findViewById(R.id.activity_artisan_information_payout)

        doAsync {
            val artisan = db.artisanDao().getArtisanByEmail(email, cgaId)
            val imageName = "${artisan.email}.png"
            val imageBitmap = ImageStorage.retrieveImageFromInternalStorage(imageName, applicationContext)
            uiThread {
                profilePictureView.setImageBitmap(imageBitmap)
                nameView.text = artisan.getFullName()
                emailView.text = artisan.email
                phoneView.text = "${artisan.phone} (${artisan.phoneType})"
            }
        }
    }

    fun onPasswordResetClicked(view: View?) {
        doAsync {
            val artisan = db.artisanDao().getArtisanByEmail(email, cgaId)
            val newPassword = Security.generateTemporaryPassword()
            val newPasswordHash = Security.generatePasswordHash(newPassword)
            db.artisanDao().updateArtisanPassword(artisan.email, artisan.cgaId, newPasswordHash.hash, newPasswordHash.salt)
            db.artisanDao().setArtisanNotLoggedIn(artisan.email, artisan.cgaId)
            Clipboard.copyValueToClipboard("Temporary Password", newPassword, applicationContext)
            uiThread {
                Toast.makeText(applicationContext, "Copied Temporary Password to Clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
