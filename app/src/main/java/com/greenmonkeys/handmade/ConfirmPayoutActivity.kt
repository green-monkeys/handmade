package com.greenmonkeys.handmade

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

const val CAPTURE_SIGNATURE_INTENT = 1

class ConfirmPayoutActivity : AppCompatActivity() {
    lateinit var amount: TextView
    lateinit var backButton: Button
    lateinit var signButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_payout)

        val payoutAmount = intent.getDoubleExtra("amount", 0.0)
        amount = findViewById(R.id.activity_confirm_payout_amount)
        amount.text = "$${payoutAmount}"
        backButton = findViewById(R.id.activity_confirm_payout_cancel)
        signButton = findViewById(R.id.activity_confirm_payout_sign)
    }

    fun onSignClick(view: View?) {
        val intent = Intent(this, CaptureSignatureActivity::class.java)
        startActivityForResult(intent, CAPTURE_SIGNATURE_INTENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAPTURE_SIGNATURE_INTENT && resultCode == Activity.RESULT_OK) {
            val signature = data?.extras?.get("data") as String
            val resultIntent = Intent()
            resultIntent.putExtra("data", signature)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
