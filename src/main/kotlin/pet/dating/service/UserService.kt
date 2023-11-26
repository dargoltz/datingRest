package pet.dating.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pet.dating.dto.UserAuthDto
import pet.dating.dto.UserProfileDto
import pet.dating.models.Like
import pet.dating.models.LikeId
import pet.dating.repositories.LikeRepository
import pet.dating.repositories.UserProfileRepository
import pet.dating.repositories.UserRepository

@Service
class UserService(
    private val validationService: ValidationService,
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository,
    private val likeRepository: LikeRepository
) {

    fun getAuthenticatedUsername(): String {
        return SecurityContextHolder.getContext().authentication.name
    }

    fun createNewUser(userAuthDto: UserAuthDto): String {

        return if (validationService.validateNewUser(userAuthDto)) {
            userRepository.save(userAuthDto.toUser())
            "user was created"
        } else {
            "not valid"
        }
    }

    private fun getUserProfile(username: String, isMatch: Boolean): UserProfileDto {

        val user = userProfileRepository.findById(username).orElseThrow()

        return if (isMatch) {
            user.toUserProfileDto(true)
        } else {
            user.toUserProfileDto(false)
        }

    }

    fun getMatchedUsers(user: String): List<UserProfileDto> {

        val likes = likeRepository.findAll()

        val thoseILiked = likes.filter { it.id?.user == user }.map { it.id?.likedUser}
        val whoLikeMe = likes.filter { it.id?.likedUser == user }.map { it.id?.user }

        val matches = thoseILiked.intersect(whoLikeMe.toSet()).filterNotNull()

        return matches.map { getUserProfile(it, true) }
    }

    fun getUnlikedUsers(user: String): List<UserProfileDto> {

        val likes = likeRepository.findAll()
        val users = userProfileRepository.findAll()

        val thoseILiked =
            likes.filter { it.id?.user == user }.mapNotNull { it.id?.likedUser }
        val unlikedUsernames =
            users.filter { it.username != user  && it.username !in thoseILiked }.mapNotNull { it.username }

        return unlikedUsernames.map { getUserProfile(it, false) }
    }

    fun like(user: String, likedUser: String): String {

        val likeId = LikeId()
        likeId.user = user
        likeId.likedUser = likedUser

        val like = Like()
        like.id = likeId
        likeRepository.save(like)

        return "user $likedUser was liked"
    }

    fun removeLike(user: String, dislikedUser: String): String {

        val likeId = LikeId()
        likeId.user = user
        likeId.likedUser = dislikedUser

        val like = Like()
        like.id = likeId
        likeRepository.delete(like)

        return "removed like from user $dislikedUser"
    }

    fun changeUserProfile(username: String, userProfileDto: UserProfileDto): String {

        return if (userProfileDto.username == null || userProfileDto.username == username) {
            userProfileRepository.save(userProfileDto.toUserProfile(username))
            "user profile was changed"
        } else {
            "username can't be changed"
        }

    }

}