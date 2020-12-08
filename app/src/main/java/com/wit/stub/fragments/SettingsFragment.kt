package com.wit.stub.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wit.stub.R
import com.wit.stub.activities.LoginScreen


class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_settings, container, false)

//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        mGoogleSignInClient= activity?.let { GoogleSignIn.getClient(it, gso) }!!

        var logout : View = view.findViewById(R.id.logoutButton)
        logout.setOnClickListener{view->
            Firebase.auth.signOut()
            val intent = Intent(activity, LoginScreen::class.java)
            startActivity(intent)
//            mGoogleSignInClient.signOut().addOnCompleteListener {
//                val intent = Intent(activity, LoginScreen::class.java)
//                startActivity(intent)
//            }
        }
        return view
    }
}