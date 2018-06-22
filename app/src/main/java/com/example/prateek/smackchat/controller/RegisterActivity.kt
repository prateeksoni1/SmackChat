package com.example.prateek.smackchat.controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.example.prateek.smack.services.AuthService
import com.example.prateek.smack.services.UserDataService
import com.example.prateek.smackchat.R
import kotlinx.android.synthetic.main.activity_register.*
import utilities.BROADCAST_USER_DATA_CHANGE
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var userAvatar = "profiledefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerSpinner.visibility = View.INVISIBLE

    }



    fun generateUserAvatar(view: View) {

        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if(color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        registerAvatarImgView.setImageResource(resourceId)

    }

    fun registerChangeBgBtnClicked(view: View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        registerAvatarImgView.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"

    }

    fun registerRegisterClicked(view: View) {

        enableSpinner(true)


        val username = registerUsernameTxt.text.toString()
        val email = registerEmailTxt.text.toString()
        val password = registerPasswordTxt.text.toString()

        if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.registerUser(this, email, password) {registerSuccess ->

                if(registerSuccess) {
                    AuthService.loginUser(this, email, password) {loginSuccess ->
                        if(loginSuccess) {
                            AuthService.createUser(this, username, email, avatarColor, userAvatar) { createSuccess ->

                                if(createSuccess) {

                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                    enableSpinner(false)
                                    finish()

                                } else {
                                    errorToast()
                                }

                            }
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }

            }
        } else {
            Toast.makeText(this, "Fill details bish!", Toast.LENGTH_SHORT).show()
            enableSpinner(false)
        }



    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        if(enable) {
            registerSpinner.visibility = View.VISIBLE

        } else {
            registerSpinner.visibility = View.INVISIBLE

        }

        registerChangeBgBtn.isEnabled = !enable
        registerAvatarImgView.isEnabled = !enable
        registerRegisterBtn.isEnabled = !enable
    }
}
