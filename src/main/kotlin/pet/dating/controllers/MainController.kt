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

        return ResponseEntity
            .status(processingResult.status)
            .body(processingResult.message)
    }

    @PostMapping("/login")
    fun login(userAuthDto: UserAuthDto): ResponseEntity<String> {
        val processingResult = authService.processUser(userAuthDto, UserAction.LOGIN)

        return ResponseEntity
            .status(processingResult.status)
            .body(processingResult.message)
    }

    @PostMapping("/delete")
    fun delete(
        @RequestHeader("Token") token: String, // todo test when no header
        @RequestBody userAuthDto: UserAuthDto
    ): ResponseEntity<String> {
        if (!isValidToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized")
        }
        val processingResult = authService.processUser(userAuthDto, UserAction.DELETE) // todo check who delete user

        return ResponseEntity
            .status(processingResult.status)
            .body(processingResult.message)
    }

    @GetMapping("/unliked_users")
    fun getUnlikedUsers(
        @RequestHeader("Token") token: String
    ): ResponseEntity<List<UserProfileDto>> {
        if (!isValidToken(token)) {
            return ResponseEntity.status(401).body(emptyList())
        }
        val username = getUsernameFromToken(token)

        return ResponseEntity
            .status(200)
            .body(userListService.getUnlikedUsers(username))
    }

    @GetMapping("/matched_users")
    fun getMatchedUsers(
        @RequestHeader("Token") token: String,
    ): ResponseEntity<List<UserProfileDto>> {
        if (!isValidToken(token)) {
            return ResponseEntity.status(401).body(emptyList())
        }
        val username = getUsernameFromToken(token)

        return ResponseEntity
            .status(200)
            .body(userListService.getMatchedUsers(username))
    }

    @PostMapping("/change_info")
    fun changeUserProfile(
        @RequestHeader("Token") token: String,
        @RequestBody userProfileDto: UserProfileDto
    ): ResponseEntity<String> {
        if (!isValidToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized")
        }
        val username = getUsernameFromToken(token)

        return ResponseEntity
            .status(200)
            .body(userProfileService.changeUserProfile(username, userProfileDto))
    }

    @GetMapping("/like/{usernameToLike}")
    fun like(
        @RequestHeader("Token") token: String,
        @PathVariable usernameToLike: String
    ): ResponseEntity<String> {
        if (!isValidToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized")
        }
        val username = getUsernameFromToken(token)
        val processingResult = likeService.processLike(username, usernameToLike, LikeAction.LIKE)

        return ResponseEntity
            .status(processingResult.status)
            .body(processingResult.message)
    }

    @GetMapping("/dislike/{usernameToDislike}")
    fun dislike(
        @RequestHeader("Token") token: String,
        @PathVariable usernameToDislike: String
    ): ResponseEntity<String> {
        if (!isValidToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized")
        }
        val username = getUsernameFromToken(token)
        val processingResult = likeService.processLike(username, usernameToDislike, LikeAction.DISLIKE)

        return ResponseEntity
            .status(processingResult.status)
            .body(processingResult.message)
    }

    private fun isValidToken(token: String): Boolean = token.isNotEmpty()
    private fun getUsernameFromToken(token: String) = token
}