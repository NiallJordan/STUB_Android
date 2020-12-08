package com.wit.stub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wit.stub.R
import com.wit.stub.models.AssignmentModel
import org.jetbrains.anko.AnkoLogger

class HomeFragment : Fragment(), AnkoLogger {

    private lateinit var auth : FirebaseAuth
    private var db: FirebaseDatabase? = null
    private var accountReference: DatabaseReference? =null
    private var assignmentReference: DatabaseReference? =null
    var list = ArrayList<AssignmentModel>()
    var searchView: androidx.appcompat.widget.SearchView? = null
    var recyclerView: RecyclerView? = null
    var adapter : AssignmentRecyclerAdapter? = null

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

        //return the inflated view with the recycler populated
        return view
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
                    adapter = AssignmentRecyclerAdapter(list)
                    recyclerView!!.adapter = adapter

                    searchView?.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            adapter!!.filter.filter(newText)
                            return true
                        }

                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}