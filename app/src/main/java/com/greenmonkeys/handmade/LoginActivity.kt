package com.greenmonkeys.handmade

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.room.RoomDatabase
import com.greenmonkeys.handmade.persistence.DatabaseFactory
import com.greenmonkeys.handmade.persistence.Security

class LoginActivity : AppCompatActivity() {
    var emailField: EditText? = null
    var emailBackground: Drawable? = null
    var passwordField: EditText? = null
    var db: RoomDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailField = findViewById(R.id.email_field)
        emailBackground = emailField?.background
        passwordField = findViewById(R.id.password_field)
        db = DatabaseFactory.getDatabase(applicationContext)
    }

    fun onRegisterClick(view: View) {
        val intent = Intent(this, RegisterActivity::class.java).apply {}
        startActivity(intent)
    }

    fun onSubmitClick(view: View) {
        val email = emailField?.text.toString()
        if (!Security.isValidEmail(email)) {
            emailField?.setBackgroundColor(Color.RED)
        } else if (Security.isValidEmail(email)) {
            emailField?.background = emailBackground
        }

        val password = Security.generatePassword(passwordField?.text.toString())
    }
}
