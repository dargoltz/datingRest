package pet.dating.service

import org.springframework.stereotype.Service
import pet.dating.dto.UserAuthDto
import pet.dating.repositories.UserRepository

@Service
class AuthService(
    private val userRepository: UserRepository
) {
    fun createNewUser(userAuthDto: UserAuthDto): String {
        return if (!isExistingUser(userAuthDto.username) && userAuthDto.username.isBlank()) {
            userRepository.save(userAuthDto.toUser())
            "user was created"
        } else {
            "not valid"
        }
    }

    fun deleteUser(userAuthDto: UserAuthDto): String {
        return if (isExistingUser(userAuthDto.username) && userAuthDto.username.isBlank()) {
            userRepository.delete(userAuthDto.toUser())
            "user was deleted"
        } else {
            "not valid"
        }
    }

    private fun isExistingUser(username: String): Boolean {
        return userRepository.findById(username).isPresent
    }

}