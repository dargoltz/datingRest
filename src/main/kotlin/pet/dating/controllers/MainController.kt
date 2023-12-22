package pet.dating.controllers

import org.springframework.web.bind.annotation.*
import pet.dating.dto.UserAuthDto
import pet.dating.dto.UserProfileDto
import pet.dating.service.UserService

@RestController
class MainController(
    private val userService: UserService,
) {

    @PostMapping("/sign_up")
    fun signUp(@RequestBody userAuthDto: UserAuthDto): String {
        return userService.createNewUser(userAuthDto)
    }

    @GetMapping("/unliked_users")
    fun getUnlikedUsers(): List<UserProfileDto> {
        return userListService.getUnlikedUsers(getAuthenticatedUsername())
    }

    @GetMapping("/matched_users")
    fun getMatchedUsers(): List<UserProfileDto> {
        return userListService.getMatchedUsers(getAuthenticatedUsername())
    }

    @PostMapping("/change_info")
    fun changeUserProfile(@RequestBody userProfileDto: UserProfileDto): String {
        return userService.changeUserProfile(getAuthenticatedUsername(), userProfileDto)
    }

    @GetMapping("/like/{username}")
    fun like(@PathVariable username: String): String {
        return userService.like(getAuthenticatedUsername(), username)
    }

    @GetMapping("/remove_like/{username}")
    fun unlike(@PathVariable username: String): String {
        return userService.removeLike(getAuthenticatedUsername(), username)
    }

    private fun getAuthenticatedUsername(): String {
        return SecurityContextHolder.getContext().authentication.name
    }
}