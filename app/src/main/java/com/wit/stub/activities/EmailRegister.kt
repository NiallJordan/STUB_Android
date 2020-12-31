package com.wit.stub.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wit.stub.R
import kotlinx.android.synthetic.main.activity_email_register.*
import kotlinx.android.synthetic.main.fragment_account.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EmailRegister : AppCompatActivity(), AnkoLogger {

    private lateinit var auth : FirebaseAuth
    private var dbReference: DatabaseReference? =null
    private var db: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db?.reference!!.child("account")

        createUser()
    }

    /**
     * Private function createUser(), when register button is clicked take entries in field
     * and create a new user with the entries.
     */
    private fun createUser() {
        registerButton.setOnClickListener{

            if (registerName.text.toString().isEmpty()) {
                registerName.error = "Please enter name"
                registerName.requestFocus()
                return@setOnClickListener
            } else if (registerEmailInput.text.toString().isEmpty()) {
                registerEmailInput.error = "Please enter email"
                registerEmailInput.requestFocus()
                return@setOnClickListener
            } else if (registerPasswordInput.text.toString().isEmpty()) {
                registerPasswordInput.error = "Please enter password"
                registerPasswordInput.requestFocus()
                return@setOnClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(registerEmailInput.text.toString()).matches()) {
                registerEmailInput.error = "Please enter a valid email"
                registerEmailInput.requestFocus()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(registerEmailInput.text.toString(), registerPasswordInput.text.toString()
            ).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userDb = dbReference?.child((user?.uid!!))

                    //Setting name
                    userDb?.child("Name")?.setValue(registerName.text.toString())
//                    userDb?.child("Email")?.setValue(registerEmailInput.text.toString())
//                    userDb?.child("Password")?.setValue(registerPasswordInput.text.toString())

                    Toast.makeText(this, "Registration Completed Successfully",Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }
//
//    private fun updateUI(currentUser: FirebaseUser?){
//        if(currentUser != null){
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
//    }
}
