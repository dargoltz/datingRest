package pet.dating.dto

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pet.dating.models.User

data class UserAuthDto(
    val username: String,
    val password: String,
    val confirmPassword: String? = null
) {
    fun toUser(): User {
        val newUser = User()
        newUser.username = username
        newUser.password = BCryptPasswordEncoder().encode(password)
        newUser.role = "USER"
        return newUser
    }
}