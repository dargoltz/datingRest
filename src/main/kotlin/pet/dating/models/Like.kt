package pet.dating.models

import jakarta.persistence.*

@Entity
@Table(name = "like")
open class Like {
    @EmbeddedId
    open var id: LikeId? = null

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    open var user: User? = null

    @MapsId("likedUserId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "liked_user_id", nullable = false)
    open var likedUser: User? = null

    @Column(name = "is_match")
    open var isMatch: Boolean? = null
}