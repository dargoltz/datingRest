package pet.dating.dto

import org.mindrot.jbcrypt.BCrypt
import pet.dating.models.User

data class UserAuthDto(
    val username: String,
    val password: String,
) {
    fun toUser(): User {
        val newUser = User()
        newUser.username = username
        newUser.password = BCrypt.hashpw(password, BCrypt.gensalt())
        newUser.role = "USER"
        return newUser
    }
}