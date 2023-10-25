package pet.dating.models

import jakarta.persistence.*

@Entity
@Table(name = "likes")
open class Like {
    @EmbeddedId
    open var id: LikeId? = null

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    open var userProfile: UserProfile? = null

    @MapsId("likedUserId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "liked_user_id", nullable = false)
    open var likedUserProfile: UserProfile? = null

    @Column(name = "is_match")
    open var isMatch: Boolean? = null

    fun initLikeId(userId: Int, likedUserId: Int) {
        this.id = LikeId().apply {
            this.userId = userId
            this.likedUserId = likedUserId
        }
    }

}