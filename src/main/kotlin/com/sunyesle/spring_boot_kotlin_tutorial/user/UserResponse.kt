package com.sunyesle.spring_boot_kotlin_tutorial.user

data class UserResponse(
    val id: Long?,
    val username: String,
    val role: Role,
    val firstname: String,
    val lastname: String,
    val description: String? = null
){
    companion object{
        fun of(user: User): UserResponse{
            return UserResponse(
                id = user.id,
                username = user.username,
                role = user.role,
                firstname = user.firstname,
                lastname = user.lastname,
                description = user.description
            )
        }
    }
}
