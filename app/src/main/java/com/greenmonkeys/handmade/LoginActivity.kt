package com.greenmonkeys.handmade

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import com.greenmonkeys.handmade.persistence.AppDatabase
import com.greenmonkeys.handmade.persistence.DatabaseFactory
import com.greenmonkeys.handmade.persistence.Security
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LoginActivity : AppCompatActivity() {
    private var emailField: EditText? = null
    private var emailBackground: Drawable? = null
    private var passwordField: EditText? = null
    private var cgaField: EditText? = null
    private var accountType: RadioGroup? = null
    private var errorTextField: TextView? = null
    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailField = findViewById(R.id.email_field)
        emailBackground = emailField?.background
        passwordField = findViewById(R.id.password_field)
        cgaField = findViewById(R.id.cga_id_field)
        errorTextField = findViewById(R.id.error_text_field)
        accountType = findViewById(R.id.account_type_group)
        accountType?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.artisan_group_button) {
                cgaField?.visibility = View.VISIBLE
            } else {
                cgaField?.visibility = View.GONE
            }
        }
        db = DatabaseFactory.getDatabase(applicationContext)
    }

    fun onRegisterClick(view: View) {
        val intent = Intent(this, RegisterActivity::class.java).apply {}
        startActivity(intent)
    }

    fun onSubmitClick(view: View) {
        val requiresCGAId = accountType?.checkedRadioButtonId == R.id.artisan_group_button
        val cgaId = cgaField?.text.toString().toIntOrNull()
        val email = emailField?.text.toString()
        if (!Security.isValidEmail(email)) {
            emailField?.setBackgroundColor(Color.RED)
        } else if (Security.isValidEmail(email)) {
            emailField?.background = emailBackground
        }

        val successIntent = Intent(this, HomeActivity::class.java)

        doAsync {
            val storedPassword: Security.Password?
            if (requiresCGAId && cgaId != null) {
                storedPassword = db?.artisanDao()?.getArtisanPasswordByEmail(email, cgaId)
            } else {
                if (requiresCGAId) {
                    errorTextField?.text = "Invalid CGA ID"
                    errorTextField?.visibility = View.VISIBLE
                    storedPassword = null
                } else {
                    storedPassword = db?.cgaDao()?.getCGAPasswordByEmail(email)
                }
            }
            if (storedPassword == null) {
                uiThread {
                    if (requiresCGAId) {
                        errorTextField?.text = "Could not find e-mail linked to that CGA ID."
                    } else {
                        errorTextField?.text = "Could not find e-mail."
                    }
                    errorTextField?.visibility = View.VISIBLE
                }
            } else {
                val passwordIsCorrect = Security.passwordIsCorrect(storedPassword, passwordField?.text.toString())
                if (!passwordIsCorrect) {
                    uiThread {
                        errorTextField?.text = "Incorrect e-mail or password"
                        errorTextField?.visibility = View.VISIBLE
                    }
                } else {
                    startActivity(successIntent.apply {
                        if (accountType?.checkedRadioButtonId == R.id.cga_group_button) {
                            successIntent.putExtra("ACCOUNT_TYPE", "cga")
                            successIntent.putExtra("email", email)
                        } else if (accountType?.checkedRadioButtonId == R.id.artisan_group_button) {
                            successIntent.putExtra("ACCOUNT_TYPE", "artisan")
                            successIntent.putExtra("cga_id", cgaId)
                            successIntent.putExtra("email", email)
                        }
                    })
                }
            }
        }
    }
}
