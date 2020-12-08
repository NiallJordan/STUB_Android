package com.wit.stub.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wit.stub.R
import com.wit.stub.models.AssignmentModel
import kotlinx.android.synthetic.main.fragment_add_assignment.*
import kotlinx.android.synthetic.main.fragment_cardview.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*
import kotlin.collections.ArrayList

class AssignmentAdapter(var context: HomeFragment, var assignmentList: ArrayList<AssignmentModel>): RecyclerView.Adapter<AssignmentAdapter.ViewHolder>(), Filterable, AnkoLogger{

    //Array list for filtered results
    var assignmentFilterList = ArrayList<AssignmentModel>()
    private lateinit var auth : FirebaseAuth
    private var db: FirebaseDatabase? = null
    private var accountReference: DatabaseReference? =null
    private var assignmentReference: DatabaseReference? =null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var module: TextView = itemView.moduleTextView
        var title: TextView = itemView.assignTitleTextView
        //var submissionLink: TextView = itemView.submissionLinkTextView

        init{
            assignmentFilterList = assignmentList
            itemView.setOnClickListener(){
                val assignment = assignmentList[adapterPosition]
                //updateAssignment(itemView, assignment)
                deleteAssignment(itemView,assignment)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_cardview, parent,false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        accountReference = db?.reference!!.child("account")
        assignmentReference = db?.reference!!.child("assignments").child(auth.currentUser?.uid!!)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.module.text = assignmentList[position].module
        viewHolder.title.text = assignmentList[position].assignmentTitle
//        viewHolder.weight.text = assignmentList[position].weight.toString()
//        viewHolder.submissionLink.text = assignmentList[position].submissionLink
    }

    override fun getItemCount(): Int {
        return assignmentList.size
    }

    //Filter results
    override fun getFilter(): Filter {
        return object : Filter() {
            //checks if text is typed in search view
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val search = constraint.toString()
                assignmentFilterList = if(search.isEmpty()){
                    assignmentList
                }else{
                    val resultList = ArrayList<AssignmentModel>()
                    for(row in assignmentList){
                        if (row.module.toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = assignmentFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
                assignmentList = filterResults?.values as ArrayList<AssignmentModel>
                notifyDataSetChanged()
            }

        }
    }

    private fun deleteAssignment(itemView: View,assignment: AssignmentModel){
        val builder = AlertDialog.Builder(itemView.context)
        builder.setTitle("Delete Assignment")
        builder.setMessage("Are you sure you want to delete this assignment?")

        builder.setPositiveButton("Delete"){ dialog, postive ->
            val currentUser = auth.currentUser
            val dbAssignment = FirebaseDatabase.getInstance().getReference("assignments").child(auth.currentUser?.uid!!)
            val id = assignment.assignmentID

            dbAssignment.child(id!!).removeValue()
            info("Assignment $id deleted")
            Toast.makeText(itemView.context,"Assignment Deleted", Toast.LENGTH_SHORT)
        }
        builder.setNegativeButton("Cancel"){dialog,negative ->
            info("Cancelled deletion")
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
        notifyDataSetChanged()
    }
    private fun updateAssignment(itemView: View,assignment:AssignmentModel){
        val builder = AlertDialog.Builder(itemView.context)
        val inflater = LayoutInflater.from(itemView.context)
        val view = inflater.inflate(R.layout.activity_update_assignment,null)

        val updateModuleField = view.findViewById<EditText>(R.id.update_assignment_module)
        val updateTitleField = view.findViewById<EditText>(R.id.update_assignment_title)
        val updateWeightField = view.findViewById<EditText>(R.id.update_assignment_weight)
        val updateSubLinkField = view.findViewById<EditText>(R.id.update_assignment_submissionLink)

        builder.setTitle("Update Assignment")
        updateModuleField.setText(assignment.module)
        updateTitleField.setText(assignment.assignmentTitle)
        updateWeightField.setText(assignment.weight.toString())
        updateSubLinkField.setText(assignment.submissionLink)

        builder.setView(view)
        builder.setPositiveButton("Update"
        ) { dialog, postive ->

            val currentUser = auth.currentUser
            val dbAssignment = FirebaseDatabase.getInstance().getReference("assignments").child(auth.currentUser?.uid!!)

            var userID = currentUser?.uid!!
            val updatedModule = updateModuleField.text.toString()
            val updatedTitle = updateTitleField.text.toString()
            val updatedWeight = Integer.parseInt(updateWeightField.text.toString())
            val updatedSubLink = updateSubLinkField.text.toString()
            val id = assignment.assignmentID

            if(updatedModule.isEmpty()){
                updateModuleField.error = "Enter a module"
                updateModuleField.requestFocus()
                return@setPositiveButton
            }
            if(updatedTitle.isEmpty()){
                updateTitleField.error = "Enter a title"
                updateTitleField.requestFocus()
                return@setPositiveButton
            }
            if(updatedWeight.toString().isEmpty()){
                updateWeightField.error = "Enter a weight"
                updateWeightField.requestFocus()
                return@setPositiveButton
            }
            if(updatedSubLink.isEmpty()){
                updateSubLinkField.error = "Enter a Submission Link"
                updateSubLinkField.requestFocus()
                return@setPositiveButton
            }

            val assignment = AssignmentModel(id,updatedModule,updatedTitle,updatedWeight,updatedSubLink,assignment.userID)
            dbAssignment.child(id!!).setValue(assignment)
            info("Updating Assignment:  ${assignment.assignmentID}")
        }

        builder.setNegativeButton("Cancel"
        ) { dialog, negative ->
            info("Cancelling Update on Assignment: ${assignment.assignmentID}" )
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }
}