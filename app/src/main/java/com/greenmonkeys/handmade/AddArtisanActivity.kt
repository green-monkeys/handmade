package com.greenmonkeys.handmade

import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.greenmonkeys.handmade.persistence.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.textColor
import org.jetbrains.anko.uiThread

const val REQUEST_IMAGE_CAPTURE = 1

class AddArtisanActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var addPhoto: ImageButton
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var phoneType: Spinner
    private lateinit var smsSubscribe: CheckBox
    private lateinit var saveButton: FloatingActionButton

    private lateinit var db: AppDatabase

    private lateinit var cgaId: String

    private lateinit var image: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_artisan)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        cgaId = intent.getStringExtra("USER_EMAIL")

        addPhoto = findViewById(R.id.activity_add_artisan_add_photo)
        firstName = findViewById(R.id.activity_add_artisan_first_name)
        lastName = findViewById(R.id.activity_add_artisan_last_name)
        email = findViewById(R.id.activity_add_artisan_email)
        phone = findViewById(R.id.activity_add_artisan_phone)
        phoneType = findViewById(R.id.activity_add_artisan_phone_type)
        phoneType.onItemSelectedListener = this
        smsSubscribe = findViewById(R.id.activity_add_artisan_sms_subscribe)
        saveButton = findViewById(R.id.activity_add_artisan_save)

        db = DatabaseFactory.getDatabase(applicationContext)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        smsSubscribe.isChecked = false
        smsSubscribe.isClickable = false
        smsSubscribe.textColor = Color.GRAY
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        when (item) {
            "Smart Phone" -> {
                smsSubscribe.isClickable = true
                smsSubscribe.textColor = Color.BLACK
            }
            "Basic Phone" -> {
                smsSubscribe.isClickable = true
                smsSubscribe.textColor = Color.BLACK
            }
            "No Phone" -> {
                smsSubscribe.isChecked = false
                smsSubscribe.isClickable = false
                smsSubscribe.textColor = Color.GRAY
            }
        }
    }

    fun onSaveClick(view: View?) {
        val password = Security.generateTemporaryPassword()
        val passwordHash = Security.generatePasswordHash(password)
        val phoneTypeEnum = when(phoneType.selectedItem.toString()) {
            "Smart Phone" -> PhoneType.SMART
            "Dumb Phone" -> PhoneType.DUMB
            "No Phone" -> PhoneType.NONE
            else -> PhoneType.NONE
        }
        val artisan = Artisan(
            cgaId = cgaId,
            firstName = firstName.text.toString(),
            lastName = lastName.text.toString(),
            email = email.text.toString(),
            password = passwordHash.hash,
            salt = passwordHash.salt,
            phone = phone.text.toString(),
            phoneType = phoneTypeEnum,
            smsNotifications = smsSubscribe.isChecked,
            hasLoggedIn = false
        )
        doAsync {
            db.artisanDao().insertArtisan(artisan)
            ImageStorage.saveImageToInternalStorage("${artisan.email}.png", image, applicationContext)
            Clipboard.copyValueToClipboard("Temporary Password", password, applicationContext)
            uiThread {
                Toast.makeText(applicationContext, "Copied Temporary Password to Clipboard", Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }

    fun onAddPhotoClick(view: View?) {
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
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
