package com.example.todo

data class DataToDo (
    var item : String,
    var checkBox : Boolean
    ){
    //basically for getting the data from firebase.
    constructor() : this("", false)
}