package com.wit.stub.activities

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wit.stub.R
import kotlinx.android.synthetic.main.activity_view_assignment_info.*
import org.jetbrains.anko.AnkoLogger

class ViewAssignmentInfoActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var auth : FirebaseAuth
    private var db: FirebaseDatabase? = null
    private var assignment: DatabaseReference? =null
    private var assignmentReference: DatabaseReference? =null
    private var url :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_assignment_info)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        assignmentReference = db?.reference!!.child("assignments").child(auth.currentUser?.uid!!)

        val actionBar = supportActionBar
        actionBar!!.title = "View Assignment"

        actionBar.setDisplayHomeAsUpEnabled(true)

        val assignID =intent.getStringExtra("assignmentID").toString()
        assignment = assignmentReference!!.child(assignID)

        loadAssignment()

        viewSubLinkText.setOnClickListener{
            val uri = Uri.parse("https://$url") // missing 'https://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun loadAssignment(){
        assignment?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Get values from the database
                viewModuleText.text = snapshot.child("module").value.toString()
                viewTitleText.text = snapshot.child("assignmentTitle").value.toString()
                viewWeightText.text = "${snapshot.child("weight").value.toString()} %"
                viewWeightLabel.paintFlags = viewWeightLabel.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                viewModuleText.paintFlags = viewModuleText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                viewSubLinkLabel.paintFlags = viewSubLinkLabel.paintFlags or Paint.UNDERLINE_TEXT_FLAG


                viewSubLinkText.text = snapshot.child("submissionLink").value.toString()
                url = viewSubLinkText.text.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                viewModuleText.error
                viewTitleText.error
                viewWeightText.error
                viewSubLinkText.error
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}