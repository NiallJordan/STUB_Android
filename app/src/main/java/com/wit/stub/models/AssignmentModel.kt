package com.wit.stub.models

import java.util.*

data class AssignmentModel(
    var module: String ="",
    var assignmentTitle: String = "",
    var weight: Int=0,
    var submissionLink: String ="",
   // var submissionDate: Calendar = Calendar.getInstance()
)

