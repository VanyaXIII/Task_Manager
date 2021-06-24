package com.example.taskmanager.users

enum class ChangeDesc(val desc: String) {

    ADDED("added") {
        override fun toString(): String {
            return desc
        }
    },
    DELETED("deleted"){
        override fun toString(): String {
            return desc
        }
    }

}