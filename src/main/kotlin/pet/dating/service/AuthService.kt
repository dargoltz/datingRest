package pet.dating.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pet.dating.dto.UserAuthDto
import pet.dating.models.User
import pet.dating.repositories.UserRepository
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository
) {
    enum class UserAction {
        CREATE, DELETE, //CHANGE_ROLE
    }

    fun processUser(userAuthDto: UserAuthDto, action: UserAction): String {
        if (userAuthDto.username.isBlank() || userAuthDto.password.isBlank()) {
            return "empty auth data"
        }

        val foundUser = userRepository.findById(userAuthDto.username)

        return when (action){
            UserAction.CREATE -> createUser(userAuthDto, foundUser)
            UserAction.DELETE -> deleteUser(userAuthDto, foundUser)
        }
    }

    private fun createUser(userAuthDto: UserAuthDto, foundUser: Optional<User>): String {
        if (foundUser.isPresent) {
            return "user ${userAuthDto.username} already exists"
        }
        userRepository.save(userAuthDto.toUser())
        return "created user ${userAuthDto.username}"
    }

    private fun deleteUser(userAuthDto: UserAuthDto, foundUser: Optional<User>): String {
        if (!foundUser.isPresent) {
            return "user not found"
        }

        val inputPassword = BCryptPasswordEncoder().encode(userAuthDto.password)

        if (inputPassword != foundUser.get().password) {
            return "wrong password"
        }

        userRepository.delete(foundUser.get())
        return "deleted user ${userAuthDto.username}"
    }
}