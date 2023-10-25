package pet.dating.models

import jakarta.persistence.*

@Entity
@Table(name = "user_profile")
open class UserProfile {
    @Id
    @Column(name = "user_id", nullable = false)
    open var id: Int? = null

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    open var user: User? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "info", nullable = false)
    open var info: String? = null

    @Column(name = "contacts", nullable = false)
    open var contacts: String? = null
}