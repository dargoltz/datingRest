package pet.dating.models

import jakarta.persistence.*

@Entity
@Table(name = "user_profile")
open class UserProfile {
    @Id
    @Column(name = "\"user\"", nullable = false)
    open var user: String? = null

    @Column(name = "info", nullable = false)
    open var info: String? = null

    @Column(name = "contacts", nullable = false)
    open var contacts: String? = null
}