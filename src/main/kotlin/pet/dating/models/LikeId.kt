package pet.dating.models

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Embeddable
open class LikeId : Serializable {
    @Column(name = "user_id", nullable = false)
    open var userId: Int? = null

    @Column(name = "liked_user_id", nullable = false)
    open var likedUserId: Int? = null
    override fun hashCode(): Int = Objects.hash(userId, likedUserId)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as LikeId

        return userId == other.userId &&
                likedUserId == other.likedUserId
    }

    companion object {
        private const val serialVersionUID = 5192855294742587588L
    }
}