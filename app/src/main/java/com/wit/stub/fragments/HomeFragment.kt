package com.wit.stub.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wit.stub.R
import com.wit.stub.models.AssignmentModel
import kotlinx.android.synthetic.main.activity_email_register.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_add_assignment_dialog.*
import kotlinx.android.synthetic.main.fragment_add_assignment_dialog.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), AnkoLogger {
    private lateinit var auth : FirebaseAuth
    private var dbReference: DatabaseReference? =null
    private var dbReferenceAssignment: DatabaseReference? =null
    private var db: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db?.reference!!.child("account")
        dbReferenceAssignment = db?.reference!!.child("assignments")

        //loadAssignments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_home, container, false)

        //Action Listener for FloatingActionButton
        val fab: View = view.findViewById(R.id.fab_add)
        fab.setOnClickListener { view ->
            info("Assignment Add fab pressed")
            val alert = AlertDialog.Builder(activity)
            alert.setView(R.layout.fragment_add_assignment_dialog)
            alert.setPositiveButton("Submit"){dialog,positiveButton ->

            }
            alert.setNegativeButton("Cancel"){dialog, negativeButton->

            }
            alert.show()
        }
        return view
    }

    private fun addNewAssignment(){

    }

    private fun loadAssignments(){
        val currentUser = auth.currentUser
        val cuReference = dbReference?.child(currentUser?.uid!!)
        val cuReferenceAssignment = dbReference?.child(currentUser?.uid!!)



        cuReferenceAssignment?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                moduleTextView.text = snapshot.child("module").value.toString()
                assignTitleTextView.text =snapshot.child("assignment_title").value.toString()
                weightingTextView.text = snapshot.child("weighting").value.toString()
                submissionLinkTextView.text = snapshot.child("module").value.toString()
                emailText.text = currentUser?.email
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}