package pet.dating.service

import org.springframework.stereotype.Service
import pet.dating.models.Like
import pet.dating.models.LikeId
import pet.dating.repositories.LikeRepository
import pet.dating.repositories.UserProfileRepository

@Service
class LikeService(
    private val userProfileRepository: UserProfileRepository,
    private val likeRepository: LikeRepository
) {

    fun like(user: String, likedUser: String): String {

        if (!validateBeforeLike(user, likedUser)) return "something wrong"

        likeRepository.save(createLike(user, likedUser))

        return "user $likedUser was liked"
    }

    fun removeLike(user: String, dislikedUser: String): String {

        val like = createLike(user, dislikedUser)
        likeRepository.delete(like)

        return "removed like from user $dislikedUser"
    }

    private fun validateBeforeLike(user: String, likedUser: String): Boolean {
        if (user == likedUser) {
            return false
        }

        if (!isExistingUser(likedUser)) {
            return false
        }

        return !alreadyLiked(user, likedUser)
    }

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