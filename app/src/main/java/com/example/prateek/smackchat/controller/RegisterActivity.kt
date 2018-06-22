package com.example.prateek.smackchat.controller

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.prateek.smack.services.AuthService
import com.example.prateek.smack.services.UserDataService
import com.example.prateek.smackchat.R
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var userAvatar = "profiledefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


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


        val username = registerUsernameTxt.text.toString()
        val email = registerEmailTxt.text.toString()
        val password = registerPasswordTxt.text.toString()

        AuthService.registerUser(this, email, password) {registerSuccess ->

            if(registerSuccess) {
                AuthService.loginUser(this, email, password) {loginSuccess ->
                    if(loginSuccess) {
                        AuthService.createUser(this, username, email, avatarColor, userAvatar) { createSuccess ->

                            if(createSuccess) {
                                println(UserDataService.avatarName)
                                println(UserDataService.avatarColor)
                                println(UserDataService.name)
                                finish()

                            }

                        }
                    }
                }
            }

        }

    }
}
