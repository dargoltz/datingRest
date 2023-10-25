package pet.dating.models

import jakarta.persistence.*

@Entity
@Table(name = "user")
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "username", nullable = false)
    open var username: String? = null

    @Column(name = "password", nullable = false)
    open var password: String? = null

    @OneToMany(mappedBy = "user")
    open var likes: MutableSet<Like> = mutableSetOf()

    @OneToOne(mappedBy = "user")
    open var userProfile: UserProfile? = null
}