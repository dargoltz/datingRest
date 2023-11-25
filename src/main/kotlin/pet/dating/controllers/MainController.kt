package pet.dating.controllers

import org.springframework.web.bind.annotation.*
import pet.dating.dto.UserAuthDto
import pet.dating.dto.UserProfileDto
import pet.dating.service.UserService

@RestController
class MainController(
    private val userService: UserService,
) {

    @PostMapping("/registration")
    fun registration(@RequestBody userAuthDto: UserAuthDto): String {
        return userService.createNewUser(userAuthDto)
    }

    @GetMapping("/unliked_users")
    fun getUnlikedUsers(): List<UserProfileDto> {
        return userService.getUnlikedUsers()
    }

    @GetMapping("/matched_users")
    fun getMatchedUsers(): List<UserProfileDto> {
        return userService.getMatchedUsers()
    }

    @PostMapping("/change_info")
    fun changeUserProfile(@RequestBody userProfileDto: UserProfileDto): String {
        return userService.changeUserProfile(userProfileDto)
    }

    @GetMapping("/like/{username}")
    fun like(@PathVariable username: String): String {
        return userService.like(username)
    }

    @GetMapping("/remove_like/{username}")
    fun unlike(@PathVariable username: String): String {
        return userService.removeLike(username)
    }

}