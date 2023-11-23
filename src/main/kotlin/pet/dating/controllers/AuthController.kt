package pet.dating.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pet.dating.dto.UserAuthDto
import pet.dating.repositories.UserRepository
import pet.dating.service.ValidationService

@RestController
class AuthController(
    private val validationService: ValidationService,
    private val userRepository: UserRepository
) {

    @PostMapping("/registration")
    fun registration(@RequestBody userAuthDto: UserAuthDto): String {
        return if (validationService.validateNewUser(userAuthDto)) {
            userRepository.save(userAuthDto.toUser())
            "user was created"
        } else {
            "not valid"
        }
    }

}