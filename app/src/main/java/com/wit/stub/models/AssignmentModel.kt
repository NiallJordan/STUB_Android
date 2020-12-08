package com.wit.stub.models

import java.util.*

data class AssignmentModel(  var module: String,
                             var assignmentTitle: String,
                             var weight: Int,
                             var submissionLink: String,
                             var userID: String)
    // var submissionDate: Calendar = Calendar.getInstance()
{
    constructor(): this("","",0,"","")
}

