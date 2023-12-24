package pet.dating.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pet.dating.dto.UserAuthDto
import pet.dating.enums.UserAction
import pet.dating.models.User
import pet.dating.repositories.UserRepository
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository
) {
    data class UserProcessingResult(val message: String, val status: Int)

    fun processUser(userAuthDto: UserAuthDto, action: UserAction): UserProcessingResult {
        if (userAuthDto.username.isBlank() || userAuthDto.password.isBlank()) {
            return UserProcessingResult("empty auth data", 400)
        }

        val foundUser = userRepository.findById(userAuthDto.username)

        return when (action){
            UserAction.CREATE -> createUser(userAuthDto, foundUser)
            UserAction.DELETE -> deleteUser(userAuthDto, foundUser)
        }
    }

    private fun createUser(userAuthDto: UserAuthDto, foundUser: Optional<User>): UserProcessingResult {
        if (foundUser.isPresent) {
            return UserProcessingResult("user ${userAuthDto.username} already exists", 400)
        }
        userRepository.save(userAuthDto.toUser())
        return UserProcessingResult("created user ${userAuthDto.username}", 200)
    }

    private fun deleteUser(userAuthDto: UserAuthDto, foundUser: Optional<User>): UserProcessingResult {
        if (!foundUser.isPresent) {
            return UserProcessingResult("user not found", 400)
        }

        val inputPassword = BCryptPasswordEncoder().encode(userAuthDto.password)

        if (inputPassword != foundUser.get().password) {
            return UserProcessingResult("wrong password", 400)
        }

        userRepository.delete(foundUser.get())
        return UserProcessingResult("deleted user ${userAuthDto.username}", 200)
    }
}