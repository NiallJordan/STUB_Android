package com.wit.stub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wit.stub.R
import com.wit.stub.models.AssignmentModel
import kotlinx.android.synthetic.main.fragment_add_assignment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddAssignmentFragment.newInstance] factory method to
 * create an instance of this fragment.
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
        assignmentReference = db?.reference!!.child("assignments")?.child(auth.currentUser?.uid!!)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View= inflater.inflate(R.layout.fragment_add_assignment, container, false)
        val submit: View = view.findViewById(R.id.submitButton)
        submit.setOnClickListener {
            addNewAssignment()
        }
        return view
    }

    private fun addNewAssignment() {
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

        // User
        val currentUser = auth.currentUser
        val key = assignmentReference?.push()?.key

        var userID = currentUser?.uid!!
        var module = moduleField.text.toString()
        var title = assignmentTitleField.text.toString()
        var weight = Integer.parseInt(weightingField.text.toString())
        var submissionLink = submissionLinkField.text.toString()

        val assignment = AssignmentModel(module,title,weight,submissionLink,userID)

        assignmentReference?.child(key!!)?.setValue(assignment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddAssignmentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AddAssignmentFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}