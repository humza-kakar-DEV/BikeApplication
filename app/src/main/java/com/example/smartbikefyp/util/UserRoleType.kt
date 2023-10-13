package com.example.smartbikefyp.util

sealed class UserRoleType<out User> {
    object Loading : UserRoleType<Nothing>()
    data class User(val data: com.example.smartbikefyp.model.User) :
        UserRoleType<com.example.smartbikefyp.model.User>()

    data class Mechanic(val data: com.example.smartbikefyp.model.User) :
        UserRoleType<com.example.smartbikefyp.model.User>()

    data class Exception(val message: String) : UserRoleType<Nothing>() {
        companion object {
            val USER_NOT_FOUND_EXCEPTION = Throwable("user not found")
        }
    }
}
