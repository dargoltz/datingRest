package pet.dating.models

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Embeddable
open class LikeId : Serializable {
    @Column(name = "\"username\"", nullable = false)
    open var user: String? = null

    @Column(name = "liked_user", nullable = false)
    open var likedUser: String? = null
    override fun hashCode(): Int = Objects.hash(user, likedUser)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as LikeId

        return user == other.user &&
                likedUser == other.likedUser
    }

    companion object {
        private const val serialVersionUID = 8059207814646204713L
    }
}