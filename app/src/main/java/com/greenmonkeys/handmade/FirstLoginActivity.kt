package com.greenmonkeys.handmade

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.greenmonkeys.handmade.persistence.AppDatabase
import com.greenmonkeys.handmade.persistence.DatabaseFactory
import com.greenmonkeys.handmade.persistence.Security
import org.jetbrains.anko.doAsync

class FirstLoginActivity : AppCompatActivity() {
    lateinit var password: EditText
    lateinit var confirmPassword: EditText
    lateinit var errorText: TextView

    lateinit var email: String
    lateinit var cgaId: String

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_login)

        db = DatabaseFactory.getDatabase(applicationContext)

        email = intent.getStringExtra("EMAIL")
        cgaId = intent.getStringExtra("CGA_ID")

        password = findViewById(R.id.activity_first_login_password)
        confirmPassword = findViewById(R.id.activity_first_login_confirm_password)
        errorText = findViewById(R.id.activity_first_login_error_text)
    }

    fun onSubmitClick(view: View?) {
        val errors = ArrayList<String>()
        if (password.text.isEmpty()) {
            errors.add("Password field is empty")
        }
        if (confirmPassword.text.isEmpty()) {
            errors.add("Confirm Password field is empty")
        }
        if (password.text.toString() != confirmPassword.text.toString()) {
            errors.add("Password does not match Confirm Password")
        }

        if (errors.isNotEmpty()) {
            errorText.text = errors.joinToString("") { Html.fromHtml("&#8226; $it<br/>", Html.FROM_HTML_MODE_LEGACY) }
            errorText.visibility = View.VISIBLE
            return
        } else {
            errorText.visibility = View.GONE
        }

        val passwordHash = Security.generatePasswordHash(password.text.toString())
        doAsync { db.artisanDao().updateArtisanPassword(email, cgaId, passwordHash.hash, passwordHash.salt) }

        val successIntent = Intent(this, ArtisanHomeActivity::class.java)
        successIntent.putExtra("EMAIL", email)
        successIntent.putExtra("CGA_ID", cgaId)
        startActivity(successIntent)
        finish()
    }
}
