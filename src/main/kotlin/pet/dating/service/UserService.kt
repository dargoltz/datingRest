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

        val thoseILiked = likes.filter { it.id?.user == user }.map { it.id?.likedUser }
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
            users.filter { it.username != user && it.username !in thoseILiked }.mapNotNull { it.username }

        return unlikedUsernames.map { getUserProfile(it, false) }
    }

    fun like(user: String, likedUser: String): String {

        if (user == likedUser) {
            return "you can't like yourself"
        }

        if (!isExistingUser(likedUser)) {
            return "user $likedUser doesn't exist"
        }

        if (alreadyLiked(user, likedUser)) {
            return "user $likedUser is already liked"
        }

        likeRepository.save(createLike(user, likedUser))

        return "user $likedUser was liked"
    }

    fun removeLike(user: String, dislikedUser: String): String {

        val like = createLike(user, dislikedUser)
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

    //проверить
    private fun isExistingUser(username: String): Boolean {

        return userProfileRepository.findById(username).isPresent
    }

    private fun alreadyLiked(user: String, likedUser: String): Boolean {

        return likeRepository.findById(createLike(user, likedUser).id!!).isPresent
    }

    private fun createLike(user: String, likedUser: String): Like {

        return Like().getLike(LikeId().getLikeId(user, likedUser))
    }

}