package pet.dating.repositories

import org.springframework.data.repository.CrudRepository

interface UserProfileRepository : CrudRepository<UserProfile, Int> {
}