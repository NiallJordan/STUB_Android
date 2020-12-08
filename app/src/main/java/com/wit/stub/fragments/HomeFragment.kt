package com.wit.stub.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wit.stub.R
import com.wit.stub.models.AssignmentModel
import org.jetbrains.anko.AnkoLogger
import androidx.appcompat.widget.SearchView

class HomeFragment : Fragment(), AnkoLogger {

    private lateinit var auth : FirebaseAuth
    private var db: FirebaseDatabase? = null
    private var accountReference: DatabaseReference? =null
    private var assignmentReference: DatabaseReference? =null
    var list = ArrayList<AssignmentModel>()
    var searchView: SearchView? = null
    var recyclerView: RecyclerView? = null
    var adapter : AssignmentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        accountReference = db?.reference!!.child("account")
        assignmentReference = db?.reference!!.child("assignments").child(auth.currentUser?.uid!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        //Load data
        recyclerView = view.findViewById(R.id.recycler_view)
        searchView = view.findViewById(R.id.search_assignments)

        loadAssignments()
        search()

        //return the inflated view with the recycler populated
        return view
    }

    fun updateAssignment(){
        val dialog = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.activity_view_assignment,null)

        val module = view.findViewById<EditText>(R.id.update_assignment_module)
        val title = view.findViewById<EditText>(R.id.update_assignment_title)
        val weight = view.findViewById<EditText>(R.id.update_assignment_weight)
        val subLink = view.findViewById<EditText>(R.id.update_assignment_submissionLink)

        dialog.setView(view)
        dialog.setPositiveButton("Update"
        ) { dialog, postive ->

        }

        dialog.setNegativeButton("Update"
        ) { dialog, negative ->
            TODO("Not yet implemented")
        }
        val alert = dialog.create()
        alert.show()
    }

    private fun search(){
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter!!.filter.filter(query)
                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter!!.filter.filter(newText)
                return true
            }
        })
    }

    //Load Data from firebase into recycler view
    private fun loadAssignments() {
        assignmentReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Clear the current list
                list.clear()

                //Add an assignment for each data object in the firebase db
                for (data in snapshot.children) {
                    val assignmentModel = data.getValue(AssignmentModel::class.java)
                    list.add(assignmentModel as AssignmentModel)
                }
                //Populate the recycler view
                if (list.size > 0) {
                    adapter = AssignmentAdapter(this@HomeFragment, list)
                    recyclerView!!.adapter = adapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}