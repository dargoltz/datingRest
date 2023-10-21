package pet.dating.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pet.dating.models.Like
import pet.dating.models.LikeId
import pet.dating.models.User
import pet.dating.repositories.LikeRepository
import pet.dating.repositories.UserRepository

@RestController("/users")
class UserController(
    private val userRepository: UserRepository,
    private val likeRepository: LikeRepository
) {

    @GetMapping("/")
    fun getUsers(): MutableIterable<User> {
        return userRepository.findAll()
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Int): User {
        return userRepository.findById(id).get()
    }

    @GetMapping("/like/{id}")
    fun like(@PathVariable id: Int) {
        val newLike = Like()
        newLike.id = LikeId()
        newLike.id?.userId = 1 // todo
        newLike.id?.likedUserId = id
        newLike.isMatch = false
        likeRepository.save(newLike)
    }
}