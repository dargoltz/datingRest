package pet.dating.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import pet.dating.dto.UserAuthDto
import pet.dating.dto.UserProfileDto
import pet.dating.enums.UserAction
import pet.dating.service.AuthService
import pet.dating.service.UserListService
import pet.dating.service.LikeService
import pet.dating.service.UserProfileService

@RestController
class MainController(
    private val authService: AuthService,
    private val likeService: LikeService,
    private val userProfileService: UserProfileService,
    private val userListService: UserListService,
) {

    @PostMapping("/register")
    fun register(@RequestBody userAuthDto: UserAuthDto): ResponseEntity<String> {
        val processingResult = authService.processUser(userAuthDto, UserAction.CREATE)
        return ResponseEntity.status(processingResult.status).body(processingResult.message)
    }

    @PostMapping("/delete")
    fun delete(@RequestBody userAuthDto: UserAuthDto): ResponseEntity<String> {
        val processingResult = authService.processUser(userAuthDto, UserAction.DELETE)
        return ResponseEntity.status(processingResult.status).body(processingResult.message)
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
        return userProfileService.changeUserProfile(getAuthenticatedUsername(), userProfileDto)
    }

    @GetMapping("/like/{username}")
    fun like(@PathVariable username: String): String {
        return likeService.like(getAuthenticatedUsername(), username)
    }

    @GetMapping("/remove_like/{username}")
    fun unlike(@PathVariable username: String): String {
        return likeService.removeLike(getAuthenticatedUsername(), username)
    }

    private fun getAuthenticatedUsername(): String {
        return SecurityContextHolder.getContext().authentication.name
    }
}