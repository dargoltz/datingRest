package pet.dating.service

import org.springframework.stereotype.Service
import pet.dating.dto.UserProfileDto
import pet.dating.repositories.LikeRepository
import pet.dating.repositories.UserProfileRepository


@Service
class UserListService(
    private val userProfileRepository: UserProfileRepository,
    private val likeRepository: LikeRepository
) {

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
}