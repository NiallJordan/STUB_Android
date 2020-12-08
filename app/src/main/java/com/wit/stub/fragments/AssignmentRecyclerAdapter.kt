package com.wit.stub.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.wit.stub.R
import com.wit.stub.models.AssignmentModel
import kotlinx.android.synthetic.main.fragment_cardview.view.*
import java.util.*
import kotlin.collections.ArrayList

class AssignmentRecyclerAdapter (var assignmentList: ArrayList<AssignmentModel>): RecyclerView.Adapter<AssignmentRecyclerAdapter.ViewHolder>(), Filterable{

    var assignmentFilterList = ArrayList<AssignmentModel>()

    init {
        assignmentFilterList = assignmentList
    }

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
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                assignmentList = results?.values as ArrayList<AssignmentModel>
                notifyDataSetChanged()
            }

        }
    }

//    fun removeAssignment(position: Int){
//        assignmentList.removeAt(position)
//        //Notify any registered observers that the item previously located at position has been removed from the data set.
//        notifyItemRemoved(position)
//    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var module = itemView.moduleTextView
        var title = itemView.assignTitleTextView
        //var weight = itemView.weightingTextView
        var submissionLink = itemView.submissionLinkTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_cardview, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.module.text = assignmentList[position].module
        viewHolder.title.text = assignmentList[position].assignmentTitle
       // holder.weight.text = assignmentList[position].weight.toString()
        viewHolder.submissionLink.text = assignmentList[position].submissionLink
    }

    override fun getItemCount(): Int {
        return assignmentList.size
    }
}