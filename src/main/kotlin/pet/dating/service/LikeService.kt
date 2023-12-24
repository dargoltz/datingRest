package pet.dating.service

import org.springframework.stereotype.Service
import pet.dating.dto.ProcessingResult
import pet.dating.enums.LikeAction
import pet.dating.models.Like
import pet.dating.models.LikeId
import pet.dating.repositories.LikeRepository
import pet.dating.repositories.UserProfileRepository
import java.util.*

@Service
class LikeService(
    private val userProfileRepository: UserProfileRepository,
    private val likeRepository: LikeRepository
) {

    fun processLike(user: String, processingUser: String, action: LikeAction): ProcessingResult {
        val validationResult = validateBefore(user, processingUser)

        if (validationResult != null) {
            return validationResult
        }

        val createdLike = createEntityLike(user, processingUser)
        val foundLike = likeRepository.findById(createdLike.id!!)

        return when (action) {
            LikeAction.LIKE -> like(createdLike, foundLike)
            LikeAction.DISLIKE -> dislike(createdLike, foundLike)
        }

    }

    private fun validateBefore(user: String, processingUser: String): ProcessingResult? {
        if (user == processingUser) {
            return ProcessingResult("can't like/dislike yourself", 400)
        }

        if (processingUser.isBlank()) {
            return ProcessingResult("empty username", 400)
        }

        if (isExistingUser(processingUser)) {
            return ProcessingResult("user not found", 400)
        }

        return null
    }

    private fun like(createdLike: Like, foundLike: Optional<Like>): ProcessingResult {
        if (foundLike.isPresent) {
            return ProcessingResult("already liked", 400)
        }

        likeRepository.save(createdLike)

        return ProcessingResult("user ${createdLike.id?.likedUser} was liked", 200)
    }

    private fun dislike(createdLike: Like, foundLike: Optional<Like>): ProcessingResult {
        if (!foundLike.isPresent) {
            return ProcessingResult("user not liked", 400)
        }

        likeRepository.delete(createdLike)

        return ProcessingResult("user ${createdLike.id?.likedUser} was disliked", 200)
    }

    private fun isExistingUser(username: String): Boolean {
        return userProfileRepository.findById(username).isPresent
    }

    private fun createEntityLike(user: String, likedUser: String): Like {
        return Like().getLike(LikeId().getLikeId(user, likedUser))
    }

}