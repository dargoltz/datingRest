package pet.dating.models

import jakarta.persistence.*
import pet.dating.dto.UserProfileDto

@Entity
@Table(name = "user_profile")
open class UserProfile {
    @Id
    @Column(name = "\"username\"", nullable = false)
    open var username: String? = null

    @Column(name = "info", nullable = false)
    open var info: String? = null

    @Column(name = "contacts", nullable = false)
    open var contacts: String? = null

    fun toUserProfileDto(showContacts: Boolean): UserProfileDto {
        return if(showContacts) {
            UserProfileDto(
                username = username,
                info = info!!,
                contacts = contacts!!
            )
        } else {
            UserProfileDto(
                username = username,
                info = info!!,
            )
        }
    }
}