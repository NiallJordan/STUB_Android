package com.wit.stub.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wit.stub.R
import kotlinx.android.synthetic.main.activity_login_screen.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class LoginScreen : AppCompatActivity(), AnkoLogger {
   // lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        auth = Firebase.auth


        //Google Sign-In components
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build()
//        mGoogleSignInClient= GoogleSignIn.getClient(this, gso)
//
//        SignInGoogle.setOnClickListener{view:View->
//            signInGoogle()
//            finish()
//        }

        Signin.setOnClickListener { view: View ->
            val intent = Intent(this, EmailLogin::class.java)
            startActivity(intent)
            finish()
        }



    }

    //Update Ui if a user is logged in
    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(baseContext,"Login Failed.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    //================== Google ==================\\
//    // signInGoogle() function
//    private fun signInGoogle() {
//        val signInIntent: Intent = mGoogleSignInClient.signInIntent
//        startActivityForResult(signInIntent, Req_Code)
//    }
//
//    // onActivityResult() function : this is where we provide the task and data for the Google Account
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == Req_Code) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)!!
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                info("Google Login Failed")
//                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
//                updateUI(null)
//            }
//        }
//    }


//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        info("signInWithCredential:success")
//                        val user = auth.currentUser
//                        updateUI(user)
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        info("signInWithCredential:failure", task.exception)
//                        Toast.makeText(this,"Authentication Failed.", Toast.LENGTH_SHORT).show()
//                        updateUI(null)
//                    }
//                }
//    }

}