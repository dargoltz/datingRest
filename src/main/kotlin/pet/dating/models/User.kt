package pet.dating.models

import jakarta.persistence.*

@Entity
@Table(name = "users")
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "info", nullable = false)
    open var info: String? = null

    @Column(name = "contacts", nullable = false)
    open var contacts: String? = null

    @OneToMany(mappedBy = "user")
    open var likes: MutableSet<Like> = mutableSetOf()
}