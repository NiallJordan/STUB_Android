package com.wit.stub.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.wit.stub.R
import kotlinx.android.synthetic.main.activity_login_screen.*

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        Signin.setOnClickListener { view: View ->
            val intent = Intent(this, EmailLogin::class.java)
            startActivity(intent)
            finish()
        }
    }
}