package com.wit.stub.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wit.stub.R
import com.wit.stub.models.AssignmentModel
import kotlinx.android.synthetic.main.fragment_cardview.view.*

class AssignmentRecyclerAdapter (var assignmentList: ArrayList<AssignmentModel>): RecyclerView.Adapter<AssignmentRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var module = itemView.moduleTextView
        var title = itemView.assignTitleTextView
        var weight = itemView.weightingTextView
        var submissionLink = itemView.submissionLinkTextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_cardview, parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.module.text = assignmentList[position].module
        holder.title.text = assignmentList[position].assignmentTitle
        holder.weight.text = assignmentList[position].weight.toString()
        holder.submissionLink.text = assignmentList[position].submissionLink


    }

    override fun getItemCount(): Int {
        return assignmentList.size
    }

}