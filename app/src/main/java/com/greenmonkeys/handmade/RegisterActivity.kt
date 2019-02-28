package com.greenmonkeys.handmade

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.greenmonkeys.handmade.persistence.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileOutputStream

class RegisterActivity : AppCompatActivity() {
    lateinit var db: AppDatabase

    lateinit var addPhoto: ImageButton
    lateinit var firstNameField: EditText
    lateinit var lastNameField: EditText
    lateinit var emailField: EditText
    lateinit var phoneField: EditText
    lateinit var subscribeToSMS: CheckBox
    lateinit var cgaEmail: EditText
    lateinit var password: EditText
    lateinit var confirmPassword: EditText
    lateinit var errorList: TextView

    private lateinit var image: Bitmap
    private var imageHasBeenTaken = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        db = DatabaseFactory.getDatabase(applicationContext)

        addPhoto = findViewById(R.id.activity_register_add_photo)
        firstNameField = findViewById(R.id.activity_register_first_name)
        lastNameField = findViewById(R.id.activity_register_last_name)
        emailField = findViewById(R.id.activity_register_email)
        phoneField = findViewById(R.id.activity_register_phone)
        subscribeToSMS = findViewById(R.id.activity_register_sms_subscribe)
        cgaEmail = findViewById(R.id.activity_register_cga_email)
        password = findViewById(R.id.activity_register_password)
        confirmPassword = findViewById(R.id.activity_register_confirm_password)
        errorList = findViewById(R.id.activity_register_errors)
    }

    fun onRegisterClick(view: View) {
        val errors = validateFields()
        if (errors.isNotEmpty()) {
            errorList.text = errors.joinToString("") { Html.fromHtml("&#8226; $it<br/>", Html.FROM_HTML_MODE_LEGACY) }
            errorList.visibility = View.VISIBLE
            return
        } else {
            errorList.visibility = View.GONE
        }

        val passwordHash = Security.generatePasswordHash(password.text.toString())
        val artisan = Artisan(
            cgaId = cgaEmail.text.toString(),
            firstName = firstNameField.text.toString(),
            lastName = lastNameField.text.toString(),
            email = emailField.text.toString(),
            password = passwordHash.hash,
            salt = passwordHash.salt,
            phone = phoneField.text.toString(),
            phoneType = PhoneType.SMART,
            smsNotifications = subscribeToSMS.isChecked,
            hasLoggedIn = true
        )

        val successIntent = Intent(this, ArtisanHomeActivity::class.java)
        successIntent.putExtra("EMAIL", artisan.email)
        successIntent.putExtra("CGA_ID", artisan.cgaId)
        doAsync {
            if (!db.cgaDao().containsCGA(cgaEmail.text.toString())) {
                val cga = CGA(cgaEmail.text.toString(), "", false)
                db.cgaDao().insertCGA(cga)
            }
            if (db.artisanDao().containsArtisan(artisan.email, artisan.cgaId)) {
                uiThread {
                    errorList.text = "Artisan Already Exists"
                    errorList.visibility = View.VISIBLE
                }
            } else {
                ImageStorage.saveImageToInternalStorage("${artisan.email}.png", image, applicationContext)
                db.artisanDao().insertArtisan(artisan)
                uiThread {
                    startActivity(successIntent)
                    finish()
                }
            }
        }
    }

    private fun validateFields(): ArrayList<String> {
        val errors = ArrayList<String>()
        if (!imageHasBeenTaken) {
            errors.add("Image has not been taken.")
        }
        if (firstNameField.text.isEmpty()) {
            errors.add("First name field is empty.")
        }
        if (lastNameField.text.isEmpty()) {
            errors.add("Last name field is empty.")
        }
        if (emailField.text.isEmpty()) {
            errors.add("Email field is empty.")
        }
        if (phoneField.text.isEmpty()) {
            errors.add("Phone number field is empty.")
        }
        if (cgaEmail.text.isEmpty()) {
            errors.add("CGA Email field is empty.")
        }
        if (password.text.isEmpty()) {
            errors.add("Password field is empty.")
        }
        if (confirmPassword.text.isEmpty()) {
            errors.add("Confirm Password field is empty")
        }
        if (password.text.toString() != confirmPassword.text.toString()) {
            errors.add("Password does match not Confirm Password")
        }
        return errors
    }

    fun onCameraClick(view: View) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            image = data?.extras?.get("data") as Bitmap
            addPhoto.setImageBitmap(image)
            addPhoto.setBackgroundColor(Color.TRANSPARENT)
            imageHasBeenTaken = true
        }
    }
}
