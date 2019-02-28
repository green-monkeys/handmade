package com.greenmonkeys.handmade

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.amazon.identity.auth.device.AuthError
import com.amazon.identity.auth.device.api.authorization.*
import com.amazon.identity.auth.device.api.workflow.RequestContext
import com.greenmonkeys.handmade.persistence.AppDatabase
import com.greenmonkeys.handmade.persistence.CGA
import com.greenmonkeys.handmade.persistence.DatabaseFactory
import org.jetbrains.anko.doAsync

const val DEBUG = false

class LoginWithAmazonActivity : AppCompatActivity() {
    private var requestContext: RequestContext? = null
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_with_amazon)

        db = DatabaseFactory.getDatabase(applicationContext)

        val errorText = findViewById<TextView>(R.id.error_text_view)
        val successfulLoginIntent = Intent(this, CGAHomeActivity::class.java)

        requestContext = RequestContext.create(applicationContext)
        requestContext?.registerListener(object : AuthorizeListener() {
            override fun onSuccess(result: AuthorizeResult) {
                successfulLoginIntent.putExtra("ACCESS_TOKEN", result.accessToken)
                successfulLoginIntent.putExtra("AUTH_CODE", result.authorizationCode)
                successfulLoginIntent.putExtra("CLIENT_ID", result.clientId)
                successfulLoginIntent.putExtra("REDIRECT_URI", result.redirectURI)
                successfulLoginIntent.putExtra("USER_EMAIL", result.user.userEmail)
                successfulLoginIntent.putExtra("USER_ID", result.user.userId)
                successfulLoginIntent.putExtra("USER_NAME", result.user.userName)
                successfulLoginIntent.putExtra("USER_POSTAL", result.user.userPostalCode)
                doAsync {
                    val cgaExists = db.cgaDao().containsCGA(result.user.userEmail)
                    if (!cgaExists) {
                        val cga = CGA(email = result.user.userEmail, name = result.user.userName, ownsPhone = true)
                        db.cgaDao().insertCGA(cga)
                    }
                }
                startActivity(successfulLoginIntent)
                finish()
            }

            override fun onError(ae: AuthError) {
                errorText.text = "Error Category: ${ae.category}, Error Type: ${ae.type}"
            }

            override fun onCancel(cancellation: AuthCancellation) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        requestContext?.onResume()
    }

    fun loginWithAmazonButtonClicked(view: View?) {
        if (DEBUG) {
            val intent = Intent(this, CGAHomeActivity::class.java)
            intent.putExtra("ACCESS_TOKEN", "0000")
            intent.putExtra("AUTH_CODE", "1111")
            intent.putExtra("CLIENT_ID", "2222")
            intent.putExtra("REDIRECT_URI", "3333")
            intent.putExtra("USER_EMAIL", "max@maxray.me")
            intent.putExtra("USER_ID", "maxwellray")
            intent.putExtra("USER_NAME", "Maxwell Ray")
            intent.putExtra("USER_POSTAL", "93401")
            doAsync {
                val cgaExists = db.cgaDao().containsCGA("max@maxray.me")
                if (!cgaExists) {
                    val cga = CGA(email = "max@maxray.me", name = "Maxwell Ray", ownsPhone = true)
                    db.cgaDao().insertCGA(cga)
                }
            }
            startActivity(intent)
            finish()
        } else {
            AuthorizationManager.authorize(
                AuthorizeRequest
                    .Builder(requestContext)
                    .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
                    .build()
            )
        }
    }

    fun loginAsExistingArtisanButtonClicked(view: View?) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun loginAsNewArtisanButtonClicked(view: View?) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
