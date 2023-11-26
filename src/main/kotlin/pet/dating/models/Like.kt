package pet.dating.models

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "\"like\"")
open class Like {
    @EmbeddedId
    open var id: LikeId? = null

    fun getLike(id: LikeId): Like {

        val like = Like()
        like.id = id

        return like
    }
}