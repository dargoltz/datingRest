package pet.dating.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pet.dating.dto.UserAuthDto
import pet.dating.dto.UserProfileDto
import pet.dating.enums.LikeAction
import pet.dating.enums.UserAction
import pet.dating.service.*

@RestController
class MainController(
    private val jwtService: JWTService,
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
    fun login(@RequestBody userAuthDto: UserAuthDto): ResponseEntity<String> {
        val processingResult = authService.processUser(userAuthDto, UserAction.LOGIN)

        return ResponseEntity
            .status(processingResult.status)
            .body(processingResult.message)
    }

    @PostMapping("/delete")
    fun delete(
        @RequestHeader("Token") token: String?,
        @RequestBody userAuthDto: UserAuthDto
    ): ResponseEntity<String> {
        val username = jwtService.parseTokenAndGetUsername(token)
            ?: return ResponseEntity
                .status(401)
                .body("Invalid Token")
        if(username != userAuthDto.username) {
            return ResponseEntity
                .status(403)
                .body("Forbidden")
        }
        val processingResult = authService.processUser(userAuthDto, UserAction.DELETE)

        return ResponseEntity
            .status(processingResult.status)
            .body(processingResult.message)
    }

    @GetMapping("/unliked_users")
    fun getUnlikedUsers(
        @RequestHeader("Token") token: String?
    ): ResponseEntity<List<UserProfileDto>> {
        val username = jwtService.parseTokenAndGetUsername(token)
            ?: return ResponseEntity
                .status(401)
                .body(emptyList())

        return ResponseEntity
            .status(200)
            .body(userListService.getUnlikedUsers(username))
    }

    @GetMapping("/matched_users")
    fun getMatchedUsers(
        @RequestHeader("Token") token: String?,
    ): ResponseEntity<List<UserProfileDto>> {
        val username = jwtService.parseTokenAndGetUsername(token)
            ?: return ResponseEntity
                .status(401)
                .body(emptyList())

        return ResponseEntity
            .status(200)
            .body(userListService.getMatchedUsers(username))
    }

    @PostMapping("/change_info")
    fun changeUserProfile(
        @RequestHeader("Token") token: String?,
        @RequestBody userProfileDto: UserProfileDto
    ): ResponseEntity<String> {
        val username = jwtService.parseTokenAndGetUsername(token)
            ?: return ResponseEntity
                .status(401)
                .body("Invalid Token")

        return ResponseEntity
            .status(200)
            .body(userProfileService.changeUserProfile(username, userProfileDto))
    }

    @GetMapping("/like/{usernameToLike}")
    fun like(
        @RequestHeader("Token") token: String?,
        @PathVariable usernameToLike: String
    ): ResponseEntity<String> {
        val username = jwtService.parseTokenAndGetUsername(token)
            ?: return ResponseEntity
                .status(401)
                .body("Invalid Token")
        val processingResult = likeService.processLike(username, usernameToLike, LikeAction.LIKE)

        return ResponseEntity
            .status(processingResult.status)
            .body(processingResult.message)
    }

    @GetMapping("/dislike/{usernameToDislike}")
    fun dislike(
        @RequestHeader("Token") token: String?,
        @PathVariable usernameToDislike: String
    ): ResponseEntity<String> {
        val username = jwtService.parseTokenAndGetUsername(token)
            ?: return ResponseEntity
                .status(401)
                .body("Invalid Token")
        val processingResult = likeService.processLike(username, usernameToDislike, LikeAction.DISLIKE)

        return ResponseEntity
            .status(processingResult.status)
            .body(processingResult.message)
    }

}