package pet.dating.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "\"user\"")
open class User {
    @Id
    @Column(name = "username", nullable = false)
    open var username: String? = null

    @Column(name = "password", nullable = false)
    open var password: String? = null

    @Column(name = "role", nullable = false)
    open var role: String? = null
}