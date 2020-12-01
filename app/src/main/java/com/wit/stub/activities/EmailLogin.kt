package com.wit.stub.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wit.stub.R
import kotlinx.android.synthetic.main.activity_email_login.*

class EmailLogin : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)
        auth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            loginUser()
        }
        registerTextView.setOnClickListener{
            val intent = Intent(this, EmailRegister::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loginUser(){
        if(emailInput.text.toString().isEmpty()){
            emailInput.error = "Please enter email"
            emailInput.requestFocus()
            return
        }

        if(passwordInput.text.toString().isEmpty()){
            passwordInput.error = "Please enter password"
            passwordInput.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailInput.text.toString()).matches()){
            emailInput.error = "Please enter a valid email"
            emailInput.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(emailInput.text.toString(), passwordInput.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed. Email or password is wrong.", Toast.LENGTH_SHORT).show()
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
        if(currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
//            if (currentUser.isEmailVerified)
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//        }else if (currentUser!=null && !currentUser.isEmailVerified ){
//            Toast.makeText(this, "Please Verify Email", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, EmailLogin::class.java))
//            finish()
//        }
    }
}
