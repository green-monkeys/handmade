package com.greenmonkeys.handmade

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.amazon.identity.auth.device.AuthError
import com.amazon.identity.auth.device.api.authorization.*
import com.amazon.identity.auth.device.api.workflow.RequestContext

class LoginWithAmazonActivity : AppCompatActivity() {
    private var requestContext: RequestContext? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_with_amazon)

        val loginWIthAmazonButton = findViewById<ImageButton>(R.id.login_with_amazon_button)
        val errorText = findViewById<TextView>(R.id.error_text_view)
        val successfulLoginIntent = Intent(this, HomeActivity::class.java)

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
                startActivity(successfulLoginIntent)
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
        AuthorizationManager.authorize(
            AuthorizeRequest
                .Builder(requestContext)
                .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
                .build()
        )
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
