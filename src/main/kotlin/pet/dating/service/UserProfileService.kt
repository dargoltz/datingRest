package pet.dating.service

import pet.dating.dto.UserProfileDto
import pet.dating.repositories.UserProfileRepository

class UserProfileService(
    private val userProfileRepository: UserProfileRepository
) {
    fun changeUserProfile(username: String, userProfileDto: UserProfileDto): String {

        return if (userProfileDto.username == null || userProfileDto.username == username) {
            userProfileRepository.save(userProfileDto.toUserProfile(username))
            "user profile was changed"
        } else {
            "username can't be changed"
        }

    }
}