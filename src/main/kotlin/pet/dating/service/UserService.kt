package pet.dating.service

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

    fun getMatchedUsers(): List<UserProfileDto> {

        val likes = likeRepository.findAll()
        val username = "me"

        val thoseILiked = likes.filter { it.id?.user == username }.map { it.id?.likedUser}
        val whoLikeMe = likes.filter { it.id?.likedUser == username }.map { it.id?.user }

        val matches = thoseILiked.intersect(whoLikeMe.toSet()).filterNotNull()

        return matches.map { getUserProfile(it, true) }
    }

    fun getUnlikedUsers(): List<UserProfileDto> {

        val likes = likeRepository.findAll()
        val users = userProfileRepository.findAll()
        val username = "me"

        val thoseILiked =
            likes.filter { it.id?.user == username }.mapNotNull { it.id?.likedUser }
        val unlikedUsernames =
            users.filter { it.username != username  && it.username !in thoseILiked }.mapNotNull { it.username }

        return unlikedUsernames.map { getUserProfile(it, false) }
    }

    fun like(username: String): String {

        val likeId = LikeId()
        likeId.user = "me"
        likeId.likedUser = username

        val like = Like()
        like.id = likeId
        likeRepository.save(like)

        return "user $username was liked"
    }

    fun removeLike(username: String): String {

        val likeId = LikeId()
        likeId.user = "me"
        likeId.likedUser = username

        val like = Like()
        like.id = likeId
        likeRepository.delete(like)

        return "removed like from user $username"
    }

    fun changeUserProfile(userProfileDto: UserProfileDto): String {

        val username = "me"
        return if (userProfileDto.username != username) {
            "username can't be changed"
        } else {
            userProfileRepository.save(userProfileDto.toUserProfile())
            "user profile was changed"
        }
    }

}