package pet.dating.controllers

import org.springframework.web.bind.annotation.*
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

    @GetMapping("/users")
    fun getUnlikedUsers() {

    }

    @PostMapping("/user")
    fun changeUserInfo() {

    }

    @GetMapping("/user/{id}")
    fun getUserInfo(@PathVariable id: String) {

    }

    @GetMapping("/like/{id}")
    fun like(@PathVariable id: String) {

    }

    @GetMapping("/matches")
    fun getMatchedUsers() {

    }
}