package com.wit.stub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wit.stub.R
import com.wit.stub.models.AssignmentModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.*

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
    private var db: FirebaseDatabase? = null
    private var accountReference: DatabaseReference? =null
    private var assignmentReference: DatabaseReference? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        accountReference = db?.reference!!.child("account")
        assignmentReference = db?.reference!!.child("assignments")?.child(auth.currentUser?.uid!!)

        loadAssignments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_home, container, false)

        return view
    }

    private fun loadAssignments(){

        assignmentReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var list = ArrayList<AssignmentModel>()
                for(data in snapshot.children){
                    val assignmentModel = data.getValue(AssignmentModel::class.java)
                    list.add(assignmentModel as AssignmentModel)
                }

                val adapter = AssignmentRecyclerAdapter(list)
                recycler_view.adapter = adapter
//
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