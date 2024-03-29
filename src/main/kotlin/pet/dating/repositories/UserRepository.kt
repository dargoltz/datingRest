package pet.dating.repositories

import org.springframework.data.repository.CrudRepository
import pet.dating.models.User

interface UserRepository : CrudRepository<User, String> {
    fun findByUsername(username: String): User?

}