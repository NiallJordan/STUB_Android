package com.wit.stub.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wit.stub.R
import com.wit.stub.activities.MainActivity
import com.wit.stub.models.AssignmentModel
import kotlinx.android.synthetic.main.fragment_add_assignment.*


/**
 * A simple [Fragment] class for adding an assignment to the app.
 */
class AddAssignmentFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private var db: FirebaseDatabase? = null
    private var accountReference: DatabaseReference? =null
    private var assignmentReference: DatabaseReference? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        accountReference = db?.reference!!.child("account")
        assignmentReference = db?.reference!!.child("assignments").child(auth.currentUser?.uid!!)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View= inflater.inflate(R.layout.fragment_add_assignment, container, false)
        val submit: View = view.findViewById(R.id.submitButton)

        //On submit switch back to teh main activity
        submit.setOnClickListener {
            addNewAssignment()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
        return view
    }


    /**
     * Private function addNewAssignment for adding a new assignment to the Realtime Db.
     */
    private fun addNewAssignment() {
        //Validation on the fields
        if(moduleField.text.toString().isEmpty()){
            moduleField.error = "Enter a module"
            moduleField.requestFocus()
        }

        if(assignmentTitleField.text.toString().isEmpty()){
            assignmentTitleField.error = "Enter a Title"
            assignmentTitleField.requestFocus()
        }

        if(weightingField.text.toString().isEmpty()){
            weightingField.error = "Enter a weighting"
            weightingField.requestFocus()
        }
        if(submissionLinkField.text.toString().isEmpty()){
            submissionLinkField.error = "Enter a Submission Link"
            submissionLinkField.requestFocus()
        }

        // Init User
        val currentUser = auth.currentUser
        val id = assignmentReference?.push()?.key

        //set values to the field
        val userID = currentUser?.uid!!
        val module = moduleField.text.toString()
        val title = assignmentTitleField.text.toString()
        val weight = Integer.parseInt(weightingField.text.toString())
        val submissionLink = submissionLinkField.text.toString()

        //Send data to database using reference
        if(module.isNotEmpty() && title.isNotEmpty() && weight.toString().isNotEmpty() && submissionLink.isNotEmpty()) {
            val assignment = AssignmentModel(id,module, title, weight, submissionLink, userID)
            assignmentReference?.child(id!!)?.setValue(assignment)
        }
    }
}