package com.wit.stub.models

data class AssignmentModel(
    var assignmentID: String?,
    var module: String,
    var assignmentTitle: String,
    var weight: Int,
    var submissionLink: String,
    var userID: String)
// var submissionDate: Calendar = Calendar.getInstance()
{
    constructor(): this("","","",0,"","")
}

