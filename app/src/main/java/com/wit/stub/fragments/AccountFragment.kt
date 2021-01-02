package com.wit.stub.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.wit.stub.R
import com.wit.stub.activities.LoginScreen
import kotlinx.android.synthetic.main.fragment_account.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] class.
 * Use the loadAccountInfo method to get the logged in account info from firebase.
 */
class AccountFragment : Fragment(), AnkoLogger {
    private lateinit var auth : FirebaseAuth
    private var accountReference: DatabaseReference? =null
    private var assignmentReference: DatabaseReference? =null
    private var db: FirebaseDatabase? = null
    var adapter : AssignmentAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        accountReference = db?.reference!!.child("account")
        assignmentReference = db?.reference!!.child("assignments").child(auth.currentUser?.uid!!)

        loadAccountInfo()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_account, container, false)

        //Set a listener to the delete text
        val delete: View = view.findViewById(R.id.deleteAccountButton)
        delete.setOnClickListener{view ->
            //call delete account
            deleteAccount()
        }

        val logout : View = view.findViewById(R.id.logoutButton)
        logout.setOnClickListener { view ->
            Firebase.auth.signOut()
            val intent = Intent(activity, LoginScreen::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun loadAccountInfo(){
        val currentUser = auth.currentUser
        val cuReference = accountReference?.child(currentUser?.uid!!)

        cuReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                nameText.text = snapshot.child("Name").value.toString()
                emailText.text = currentUser?.email
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun deleteAccount(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Account")
        builder.setMessage("Are you sure you want to delete this account?")

        //When pressed remove from view and delete from db.
        builder.setPositiveButton("Delete"){ dialog, postive ->
            assignmentReference?.removeValue()
            accountReference?.child(auth.currentUser?.uid!!)?.removeValue()
            val user = auth.currentUser
            user!!.delete()
            Firebase.auth.signOut()

            //go to login welcome page
            val intent = Intent(activity, LoginScreen::class.java)
            startActivity(intent)

            info("Account deleted")
            Toast.makeText(context,"Account Deleted", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel"){dialog,negative ->
            info("Cancelled deletion")
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }
}