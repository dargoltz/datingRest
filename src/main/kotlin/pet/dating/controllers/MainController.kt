package pet.dating.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pet.dating.dto.UserAuthDto
import pet.dating.dto.UserProfileDto
import pet.dating.enums.LikeAction
import pet.dating.enums.UserAction
import pet.dating.service.AuthService
import pet.dating.service.LikeService
import pet.dating.service.UserListService
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
        if(!isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized")
        }
        val processingResult = authService.processUser(userAuthDto, UserAction.DELETE) // todo check who delete user
        return ResponseEntity.status(processingResult.status).body(processingResult.message)
    }

    @GetMapping("/unliked_users")
    fun getUnlikedUsers(): ResponseEntity<List<UserProfileDto>> {
        if(!isAuthenticated()) {
            return ResponseEntity.status(401).body(emptyList())
        }
        return ResponseEntity.status(200).body(userListService.getUnlikedUsers(getAuthenticatedUsername()))
    }

    @GetMapping("/matched_users")
    fun getMatchedUsers(): ResponseEntity<List<UserProfileDto>> {
        if(!isAuthenticated()) {
            return ResponseEntity.status(401).body(emptyList())
        }
        return ResponseEntity.status(200).body(userListService.getMatchedUsers(getAuthenticatedUsername()))
    }

    @PostMapping("/change_info")
    fun changeUserProfile(@RequestBody userProfileDto: UserProfileDto): ResponseEntity<String> {
        if(!isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized")
        }
        return ResponseEntity
            .status(200)
            .body(userProfileService.changeUserProfile(getAuthenticatedUsername(), userProfileDto))
    }

    @GetMapping("/like/{username}")
    fun like(@PathVariable username: String): ResponseEntity<String> {
        if(!isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized")
        }
        val processingResult = likeService.processLike(getAuthenticatedUsername(), username, LikeAction.LIKE)
        return ResponseEntity.status(processingResult.status).body(processingResult.message)
    }

    @GetMapping("/dislike/{username}")
    fun unlike(@PathVariable username: String): ResponseEntity<String> {
        if(!isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized")
        }
        val processingResult = likeService.processLike(getAuthenticatedUsername(), username, LikeAction.DISLIKE)
        return ResponseEntity.status(processingResult.status).body(processingResult.message)
    }
    private fun getAuthenticatedUsername(): String = ""
    private fun isAuthenticated(): Boolean = false
}