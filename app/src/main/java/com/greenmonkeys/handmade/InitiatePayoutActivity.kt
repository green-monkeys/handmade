package com.greenmonkeys.handmade

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText

const val CONFIRM_PAYOUT_INTENT = 1

class InitiatePayoutActivity : AppCompatActivity() {
    lateinit var payoutAmount: EditText
    lateinit var cancelButton: Button
    lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initiate_payout)

        payoutAmount = findViewById(R.id.activity_initiate_payout_amount)
        payoutAmount.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //payoutAmount.setText("$${payoutAmount.text}")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val sString = s.toString()
                confirmButton.isEnabled = sString.isNotBlank()
            }
        })
        cancelButton = findViewById(R.id.activity_initiate_payout_cancel)
        confirmButton = findViewById(R.id.activity_initiate_payout_confirm)
    }

    fun onConfirmClick(view: View?) {
        val intent = Intent(this, ConfirmPayoutActivity::class.java)
        intent.putExtra("amount", payoutAmount.text.toString().toDouble())
        startActivityForResult(intent, CONFIRM_PAYOUT_INTENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CONFIRM_PAYOUT_INTENT && resultCode == Activity.RESULT_OK) {
            val signature = data?.extras?.get("data") as String
            val resultIntent = Intent()
            resultIntent.putExtra("data", signature)
            finish()
        }
    }
}
