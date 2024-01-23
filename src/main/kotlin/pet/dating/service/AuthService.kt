package pet.dating.service

import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service
import pet.dating.dto.ProcessingResult
import pet.dating.dto.UserAuthDto
import pet.dating.enums.UserAction
import pet.dating.models.User
import pet.dating.repositories.UserRepository
import java.util.*

@Service
class AuthService(
    private val jwtService: JWTService,
    private val userRepository: UserRepository
) {

    fun processUser(userAuthDto: UserAuthDto, action: UserAction): ProcessingResult {
        if (userAuthDto.username.isBlank() || userAuthDto.password.isBlank()) {
            return ProcessingResult("empty auth data", 400)
        }
        val foundUser = userRepository.findById(userAuthDto.username)

        return when (action){
            UserAction.CREATE -> createUser(userAuthDto, foundUser)
            UserAction.LOGIN -> loginUser(userAuthDto, foundUser)
            UserAction.DELETE -> deleteUser(userAuthDto, foundUser)
        }
    }

    private fun createUser(userAuthDto: UserAuthDto, foundUser: Optional<User>): ProcessingResult {
        if (foundUser.isPresent) {
            return ProcessingResult("user ${userAuthDto.username} already exists", 400)
        }

        userRepository.save(userAuthDto.toUser())

        return ProcessingResult("created user ${userAuthDto.username}", 200)
    }

    fun loginUser(userAuthDto: UserAuthDto, foundUser: Optional<User>): ProcessingResult {
        if (!foundUser.isPresent) {
            return ProcessingResult("user ${userAuthDto.username} not found", 400)
        }
        if (!isCorrectPassword(userAuthDto.password, foundUser)) {
            return ProcessingResult("wrong password", 400)
        }

        return ProcessingResult(jwtService.createToken(userAuthDto.username),200)
    }

    private fun deleteUser(userAuthDto: UserAuthDto, foundUser: Optional<User>): ProcessingResult {
        if (!foundUser.isPresent) {
            return ProcessingResult("user ${userAuthDto.username} not found", 400)
        }
        if (!isCorrectPassword(userAuthDto.password, foundUser)) {
            return ProcessingResult("wrong password", 400)
        }

        userRepository.delete(foundUser.get())

        return ProcessingResult("deleted user ${userAuthDto.username}", 200)
    }

    private fun isCorrectPassword(password: String, foundUser: Optional<User>): Boolean {
        return BCrypt.checkpw(password, foundUser.get().password)
    }
}