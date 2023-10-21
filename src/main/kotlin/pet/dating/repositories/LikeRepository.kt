package pet.dating.repositories;

import org.springframework.data.repository.CrudRepository
import pet.dating.models.Like
import pet.dating.models.LikeId

interface LikeRepository : CrudRepository<Like, LikeId> {
}