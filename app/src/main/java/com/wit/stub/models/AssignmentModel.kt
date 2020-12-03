package com.wit.stub.models

class AssignmentModel {

    var module: String? = ""
    var assignmentTitle: String? = ""
    var weight: Int? = 0
    var submissionLink: String? = ""
    var email: String? = ""
    // var submissionDate: Calendar = Calendar.getInstance()

    companion object Factory{
        fun create(): AssignmentModel = AssignmentModel()
    }
}

