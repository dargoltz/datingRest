package pet.dating.dto

import pet.dating.models.UserProfile

data class UserProfileDto(
    val username: String,
    val info: String,
    val contacts: String? = "no access"
) {
    fun toUserProfile(): UserProfile {
        val newUserProfile = UserProfile()
        newUserProfile.info = info
        newUserProfile.contacts = contacts
        return newUserProfile
    }
}