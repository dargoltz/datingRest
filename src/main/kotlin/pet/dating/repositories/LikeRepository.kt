package pet.dating.repositories

import org.springframework.data.repository.CrudRepository

interface LikeRepository : CrudRepository<Like, LikeId> {
}