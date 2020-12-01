package com.wit.stub.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wit.stub.R
import kotlinx.android.synthetic.main.activity_email_register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EmailRegister : AppCompatActivity(), AnkoLogger {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_register)
        auth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener{
            createUser()
        }

    }

    private fun createUser() {
        if (registerEmailInput.text.toString().isEmpty()) {
            registerEmailInput.error = "Please enter email"
            registerEmailInput.requestFocus()
            return
        }

        if (registerPasswordInput.text.toString().isEmpty()) {
            registerPasswordInput.error = "Please enter password"
            registerPasswordInput.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(registerEmailInput.text.toString()).matches()) {
            registerEmailInput.error = "Please enter a valid email"
            registerEmailInput.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(registerEmailInput.text.toString(), registerPasswordInput.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                updateUI(user)
//                user!!.sendEmailVerification().addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            info("Email sent.")
//                        }
//                    }

            } else {
                Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
