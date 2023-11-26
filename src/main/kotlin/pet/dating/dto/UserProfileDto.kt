package pet.dating.dto

import pet.dating.models.UserProfile

data class UserProfileDto(
    val username: String? = null,
    val info: String,
    val contacts: String = "no access"
) {
    fun toUserProfile(username: String): UserProfile {
        val newUserProfile = UserProfile()
        newUserProfile.username = username  // not this.username
        newUserProfile.info = info  // this.info
        newUserProfile.contacts = contacts
        return newUserProfile
    }
}