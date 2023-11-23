package pet.dating.service

import org.springframework.stereotype.Service
import pet.dating.dto.UserAuthDto
import pet.dating.repositories.UserRepository

@Service
class ValidationService(
    private val userRepository: UserRepository
) {
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