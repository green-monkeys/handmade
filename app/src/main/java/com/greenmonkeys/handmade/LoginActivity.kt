package com.greenmonkeys.handmade

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.greenmonkeys.handmade.persistence.AppDatabase
import com.greenmonkeys.handmade.persistence.DatabaseFactory
import com.greenmonkeys.handmade.persistence.Security
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LoginActivity : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var emailBackground: Drawable
    private lateinit var passwordField: EditText
    private lateinit var cgaField: EditText
    private lateinit var errorTextField: TextView
    private lateinit var db: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailField = findViewById(R.id.activity_login_email_field)
        emailBackground = emailField.background
        passwordField = findViewById(R.id.activity_login_password_field)
        cgaField = findViewById(R.id.activity_login_cga_id_field)
        errorTextField = findViewById(R.id.activity_login_error_text_field)

        db = DatabaseFactory.getDatabase(applicationContext)
    }

    fun onSubmitClick(view: View) {
        val cgaId = cgaField.text.toString()
        val email = emailField.text.toString()
        val password = passwordField.text.toString()
        if (!Security.isValidEmail(email)) {
            emailField.setBackgroundColor(Color.RED)
        } else if (Security.isValidEmail(email)) {
            emailField.background = emailBackground
        }

        val hasLoggedInIntent = Intent(this, ArtisanHomeActivity::class.java)
        val hasNotLoggedInIntent = Intent(this, FirstLoginActivity::class.java)
        doAsync {
            val artisanExists = db.artisanDao().containsArtisan(email, cgaId)
            if (!artisanExists) {
                uiThread {
                    errorTextField.text = "Artisan $email not found for CGA $cgaId"
                    errorTextField.visibility = View.VISIBLE
                }
            } else {
                val passwordHash = db.artisanDao().getArtisanPasswordByEmail(email, cgaId)
                val passwordIsValid = Security.passwordIsCorrect(passwordHash, password)
                if (passwordIsValid) {
                    val artisan = db.artisanDao().getArtisanByEmail(email, cgaId)
                    if (artisan.hasLoggedIn) {
                        hasLoggedInIntent.putExtra("EMAIL", email)
                        hasLoggedInIntent.putExtra("CGA_ID", cgaId)
                        startActivity(hasLoggedInIntent)
                        finish()
                    } else {
                        hasNotLoggedInIntent.putExtra("EMAIL", email)
                        hasNotLoggedInIntent.putExtra("CGA_ID", cgaId)
                        startActivity(hasNotLoggedInIntent)
                        finish()
                    }
                }
            }
        }
    }
}
