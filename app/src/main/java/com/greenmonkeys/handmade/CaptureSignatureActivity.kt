package com.greenmonkeys.handmade

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.github.gcacace.signaturepad.views.SignaturePad
import java.io.OutputStream

class CaptureSignatureActivity : AppCompatActivity() {
    lateinit var signaturePad: SignaturePad
    lateinit var clearButton: Button
    lateinit var doneButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_signature)
        signaturePad = findViewById(R.id.signature_pad)
        signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
            }

            override fun onClear() {
                clearButton.isEnabled = false
                doneButton.isEnabled = false
            }

            override fun onSigned() {
                clearButton.isEnabled = true
                doneButton.isEnabled = true
            }
        })
        clearButton = findViewById(R.id.activity_capture_signature_clear)
        doneButton = findViewById(R.id.activity_capture_signature_done)
    }

    fun onClearClicked(view: View?) {
        signaturePad.clear()
    }

    fun onDoneClicked(view: View?) {
        val signature = signaturePad.signatureSvg
        val intent = Intent()
        intent.putExtra("data", signature)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
