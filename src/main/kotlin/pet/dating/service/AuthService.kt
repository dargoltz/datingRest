package pet.dating.service

import org.springframework.stereotype.Service
import pet.dating.dto.UserAuthDto
import pet.dating.repositories.UserRepository

@Service
class AuthService(
    private val userRepository: UserRepository
) {
    fun createNewUser(userAuthDto: UserAuthDto): String {

        return if (validateNewUser(userAuthDto)) {
            userRepository.save(userAuthDto.toUser())
            "user was created"
        } else {
            "not valid"
        }
    }

    fun validateNewUser(userAuthDto: UserAuthDto): Boolean {
        if (userAuthDto.password != userAuthDto.confirmPassword) {
            return false
        }
        if (userAuthDto.username.isBlank()) {
            return false
        }
        return !userRepository.findById(userAuthDto.username).isPresent
    }
}