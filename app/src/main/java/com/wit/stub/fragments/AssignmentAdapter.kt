package com.wit.stub.fragments

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wit.stub.R
import com.wit.stub.models.AssignmentModel
import kotlinx.android.synthetic.main.fragment_add_assignment.*
import kotlinx.android.synthetic.main.fragment_cardview.view.*
import java.util.*
import kotlin.collections.ArrayList

class AssignmentAdapter(var context: HomeFragment, var assignmentList: ArrayList<AssignmentModel>): RecyclerView.Adapter<AssignmentAdapter.ViewHolder>(), Filterable{

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
//                val intent = Intent(itemView.context, ViewAssignmentActivity::class.java)
//                intent.putExtra("module", module.text)
//                intent.putExtra("title", title.text)
//                intent.putExtra("assignmentID", assignmentID.text)
//                itemView.context.startActivity(intent)
                val assignment = assignmentList[adapterPosition]
                updateAssignment(itemView,assignment)
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

    fun updateAssignment(itemView: View,assignment:AssignmentModel){
        val dialog = AlertDialog.Builder(itemView.context)
        val inflater = LayoutInflater.from(itemView.context)
        val view = inflater.inflate(R.layout.activity_update_assignment,null)

        val updateModuleField = view.findViewById<EditText>(R.id.update_assignment_module)
        val updateTitleField = view.findViewById<EditText>(R.id.update_assignment_title)
        val updateWeightField = view.findViewById<EditText>(R.id.update_assignment_weight)
        val updateSubLinkField = view.findViewById<EditText>(R.id.update_assignment_submissionLink)

        dialog.setTitle("Update Assignment")
        updateModuleField.setText(assignment.module)
        updateTitleField.setText(assignment.assignmentTitle)
        updateWeightField.setText(assignment.weight.toString())
        updateSubLinkField.setText(assignment.submissionLink)

        dialog.setView(view)
        dialog.setPositiveButton("Update"
        ) { m, postive ->

            val currentUser = auth.currentUser
            val dbAssignment = FirebaseDatabase.getInstance().getReference("assignments").child(auth.currentUser?.uid!!)

            var userID = currentUser?.uid!!
            val updatedModule = updateModuleField.text.toString()
            val updatedTitle = updateTitleField.text.toString()
            var updatedWeight = Integer.parseInt(updateWeightField.text.toString())
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
        }

        dialog.setNegativeButton("Cancel"
        ) { dialog, negative ->
            val dbAssign = FirebaseDatabase.getInstance().getReference("assignments")
        }
        val alert = dialog.create()
        alert.show()
    }

    fun removeAssignment(position: Int){
        assignmentList.removeAt(position)
        //Notify any registered observers that the item previously located at position has been removed from the data set.
        notifyItemRemoved(position)
    }
}