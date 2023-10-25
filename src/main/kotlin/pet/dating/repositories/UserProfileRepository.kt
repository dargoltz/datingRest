package pet.dating.repositories

import org.springframework.data.repository.CrudRepository
import pet.dating.models.UserProfile

interface UserProfileRepository : CrudRepository<UserProfile, Int> {
}