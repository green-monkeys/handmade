package com.greenmonkeys.handmade

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.greenmonkeys.handmade.persistence.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RegisterActivity : AppCompatActivity() {
    var accountTypeGroup: RadioGroup? = null
    var linkAmazonAccount: Button? = null

    var cgaIdField: EditText? = null
    var cgaIdFieldBackground: Drawable? = null
    var firstNameField: EditText? = null
    var firstNameFieldBackground: Drawable? = null
    var lastNameField: EditText? = null
    var lastNameFieldBackground: Drawable? = null
    var phoneNumberField: EditText? = null
    var phoneNumberFieldBackground: Drawable? = null
    var emailField: EditText? = null
    var emailFieldBackground: Drawable? = null
    var passwordField: EditText? = null
    var passwordFieldBackground: Drawable? = null
    var passwordConfirmField: EditText? = null
    var passwordConfirmFieldBackground: Drawable? = null

    var errorTextField: TextView? = null

    var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        cgaIdField = findViewById(R.id.cga_id)
        cgaIdFieldBackground = cgaIdField?.background
        firstNameField = findViewById(R.id.first_name)
        firstNameFieldBackground = firstNameField?.background
        lastNameField = findViewById(R.id.last_name)
        lastNameFieldBackground = lastNameField?.background
        phoneNumberField = findViewById(R.id.phone_number)
        phoneNumberFieldBackground = phoneNumberField?.background
        emailField = findViewById(R.id.email)
        emailFieldBackground = emailField?.background
        passwordField = findViewById(R.id.password)
        passwordFieldBackground = passwordField?.background
        passwordConfirmField = findViewById(R.id.password_confirm)
        passwordConfirmFieldBackground = passwordConfirmField?.background
        linkAmazonAccount = findViewById(R.id.link_amazon_button)

        errorTextField = findViewById(R.id.error_text)

        db = DatabaseFactory.getDatabase(applicationContext)

        accountTypeGroup = findViewById(R.id.account_type)
        accountTypeGroup?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.cga) {
                cgaIdField?.visibility = View.GONE
                linkAmazonAccount?.visibility = View.VISIBLE
            } else {
                cgaIdField?.visibility = View.VISIBLE
                linkAmazonAccount?.visibility = View.GONE
            }
        }
    }

    fun onRegisterClick(view: View) {
        var fieldsAreValid = true

        // Validate CGA ID
        val cgaId = cgaIdField?.text.toString()
        if (cgaIdField?.visibility != View.GONE) {
            if (cgaId.isEmpty() || cgaId.toIntOrNull() == null) {
                cgaIdField?.setBackgroundColor(Color.RED)
                fieldsAreValid = false
            } else if (cgaId.toIntOrNull() != null) {
                doAsync {
                    val cgaIsValid = db?.cgaDao()?.cgaIdIsValid(cgaId.toInt())
                    if (cgaIsValid == false || cgaIsValid == null) {
                        uiThread {
                            cgaIdField?.setBackgroundColor(Color.RED)
                            fieldsAreValid = false
                        }
                    } else {
                        uiThread {
                            cgaIdField?.background = cgaIdFieldBackground
                        }
                    }
                }
            }
        }

        // Validate First Name
        val firstName = firstNameField?.text.toString()
        if (firstName.isEmpty()) {
            firstNameField?.setBackgroundColor(Color.RED)
            fieldsAreValid = false
        } else
            firstNameField?.background = firstNameFieldBackground

        // Validate Last Name
        val lastName = lastNameField?.text.toString()
        if (lastName.isEmpty()) {
            lastNameField?.setBackgroundColor(Color.RED)
            fieldsAreValid = false
        } else
            lastNameField?.background = lastNameFieldBackground

        // Validate Phone Number
        val phone = phoneNumberField?.text.toString()
        if (phone.isEmpty()) {
            phoneNumberField?.setBackgroundColor(Color.RED)
            fieldsAreValid = false
        } else
            phoneNumberField?.background = phoneNumberFieldBackground

        // Validate Email
        val email = emailField?.text.toString()
        if (!Security.isValidEmail(email)) {
            emailField?.setBackgroundColor(Color.RED)
            fieldsAreValid = false
        } else
            emailField?.background = emailFieldBackground

        // Validate Password EXISTS (Not that it is correct)
        val password = passwordField?.text.toString()
        val passwordConfirm = passwordConfirmField?.text.toString()

        val hashedPassword =
            if (password == passwordConfirm && (password.length >= 8)) {
                passwordField?.background = passwordFieldBackground
                passwordConfirmField?.background = passwordConfirmFieldBackground
                Security.generatePasswordHash(password)
            } else if (password.length < 8 && passwordConfirm != password) {
                passwordField?.setBackgroundColor(Color.RED)
                passwordConfirmField?.setBackgroundColor(Color.RED)
                fieldsAreValid = false
                null
            } else if (password.length < 8) {
                passwordField?.setBackgroundColor(Color.RED)
                fieldsAreValid = false
                null
            } else {
                passwordConfirmField?.setBackgroundColor(Color.RED)
                fieldsAreValid = false
                null
            }

        val intent = Intent(this, HomeActivity::class.java)

        if (hashedPassword != null && fieldsAreValid) {
            if (accountTypeGroup?.checkedRadioButtonId == R.id.artisan) {
                val artisan = Artisan(
                    cgaId = cgaId.toInt(),
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = hashedPassword.hash,
                    salt = hashedPassword.salt,
                    phone = phone,
                    phoneType = PhoneType.SMART,
                    smsNotifications = true
                )

                // Done asynchronously because DB accesses on the main thread are a no-no
                doAsync {
                    val artisanExists = db?.artisanDao()?.containsArtisan(artisan.email, artisan.cgaId)
                    if (artisanExists == null || artisanExists == false) {
                        db?.artisanDao()?.insertArtisan(artisan)
                        uiThread {
                            startActivity(intent.apply {
                                intent.putExtra("ACCOUNT_TYPE", "artisan")
                                intent.putExtra("cga_id", cgaId)
                                intent.putExtra("email", email)
                            })
                        }
                    } else {
                        uiThread {
                            errorTextField?.text = "E-mail already exists in database for given CGA ID."
                            errorTextField?.visibility = View.VISIBLE
                            emailField?.setBackgroundColor(Color.RED)
                            cgaIdField?.setBackgroundColor(Color.RED)
                            fieldsAreValid = false
                        }
                    }
                }
            } else if (accountTypeGroup?.checkedRadioButtonId == R.id.cga) {
                val cga = CGA(
                    id = 0,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = hashedPassword.hash,
                    salt = hashedPassword.salt,
                    phone = phone
                )

                // Done asynchronously because DB accesses on the main thread are a no-no
                doAsync {
                    if (db?.cgaDao()?.containsCGA(cga.email) == true) {
                        uiThread {
                            errorTextField?.text = "E-mail already exists in database."
                            errorTextField?.visibility = View.VISIBLE
                            fieldsAreValid = false
                        }
                    } else {
                        db?.cgaDao()?.insertCGA(cga)
                        uiThread {
                            startActivity(intent.apply {
                                intent.putExtra("ACCOUNT_TYPE", "cga")
                                intent.putExtra("email", email)
                            })
                        }
                    }
                }
            }
        }
    }
}
