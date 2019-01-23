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
//import com.amazon.identity.auth.device.api.workflow.RequestContext

class LoginActivity : AppCompatActivity() {
    private var emailField: EditText? = null
    private var emailBackground: Drawable? = null
    private var passwordField: EditText? = null
    private var cgaField: EditText? = null
    private var accountType: RadioGroup? = null
    private var errorTextField: TextView? = null
    private var db: AppDatabase? = null
    //private lateinit var requestContext: RequestContext


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

        /*
        requestContext = RequestContext.create(applicationContext)

        requestContext.registerListener(object : AuthorizeListener() {

            /* Authorization was completed successfully. */
            override fun onSuccess(result: AuthorizeResult) {
                /* Your app is now authorized for the requested scopes */
                // TODO: IMPLEMENT ON SUCCESS AUTH
            }

            /* There was an error during the attempt to authorize the application. */
            override fun onError(ae: AuthError) {
                /* Inform the user of the error */
                System.out.println("ERROR MAFK")
                // TODO: IMPLEMENT ON ERROR AUTH
            }

            /* Authorization was cancelled before it could be completed. */
            override fun onCancel(cancellation: AuthCancellation) {
                /* Reset the UI to a ready-to-login state */
                // TODO: IMPLEMENT ON CANCEL AUTH
            }
        })
        */


        // TODO: FINISH THIS BELOW :)
        // Find the button with the login_with_amazon ID
        // and set up a click handler
        /*
        val loginButton: View = findViewById(R.id.login_with_amazon)
        loginButton.setOnClickListener {
            AuthorizationManager.authorize(
                AuthorizeRequest.Builder(requestContext)
                    .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
                    .build()
            )
        }
        */
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
