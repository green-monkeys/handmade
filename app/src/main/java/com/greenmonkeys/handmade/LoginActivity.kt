package com.greenmonkeys.handmade

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.amazon.identity.auth.device.api.workflow.RequestContext
import com.amazon.identity.auth.device.AuthError
import com.amazon.identity.auth.device.api.authorization.*
import com.amazon.identity.auth.device.api.authorization.ProfileScope
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager

class LoginActivity : AppCompatActivity() {
    private lateinit var requestContext: RequestContext


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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


        // TODO: FINISH THIS BELOW :)
        // Find the button with the login_with_amazon ID
        // and set up a click handler
        val loginButton: View = findViewById(R.id.login_with_amazon)
        loginButton.setOnClickListener {
            AuthorizationManager.authorize(
                AuthorizeRequest.Builder(requestContext)
                    .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
                    .build()
            )
        }
    }

    fun onRegisterClick(view: View) {
        val intent = Intent(this, RegisterActivity::class.java).apply {}
        startActivity(intent)
    }
}
