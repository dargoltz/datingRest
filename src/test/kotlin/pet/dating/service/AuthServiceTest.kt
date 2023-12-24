package pet.dating.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pet.dating.dto.UserAuthDto
import pet.dating.enums.UserAction
import pet.dating.models.User
import pet.dating.repositories.UserRepository
import java.util.*

class AuthServiceTest {

    // Создаем мок UserRepository для использования в тестах
    private val userRepository = mock(UserRepository::class.java)

    // Создаем экземпляр AuthService с использованием мока userRepository
    private val authService = AuthService(userRepository)

    @Test
    fun `test processUser when action is CREATE and user does not exist`() {
        // Arrange
        val userAuthDto = UserAuthDto("newUser", "password")
        `when`(userRepository.findById("newUser")).thenReturn(Optional.empty())

        // Act
        val result = authService.processUser(userAuthDto, UserAction.CREATE)

        // Assert
        assertEquals("created user newUser", result.message)
        assertEquals(200, result.status)

        // Verify that userRepository.save was called once
        verify(userRepository, times(1)).save(any())
    }

    @Test
    fun `test processUser when action is CREATE and user already exists`() {
        // Arrange
        val userAuthDto = UserAuthDto("existingUser", "password")
        `when`(userRepository.findById("existingUser")).thenReturn(Optional.of(getTestUser()))

        // Act
        val result = authService.processUser(userAuthDto, UserAction.CREATE)

        // Assert
        assertEquals("user existingUser already exists", result.message)
        assertEquals(400, result.status)
        verify(userRepository, never()).save(any())
    }

    @Test
    fun `test processUser when action is DELETE and user exists with correct password`() {
        // Arrange
        val userAuthDto = UserAuthDto("existingUser", "password")
        `when`(userRepository.findById("existingUser")).thenReturn(Optional.of(getTestUser(true)))

        // Act
        val result = authService.processUser(userAuthDto, UserAction.DELETE)

        // Assert
        assertEquals("deleted user existingUser", result.message)
        assertEquals(200, result.status)
        verify(userRepository, times(1)).delete(any())
    }

    @Test
    fun `test processUser when action is DELETE and user does not exist`() {
        // Arrange
        val userAuthDto = UserAuthDto("nonExistingUser", "password")
        `when`(userRepository.findById("nonExistingUser")).thenReturn(Optional.empty())

        // Act
        val result = authService.processUser(userAuthDto, UserAction.DELETE)

        // Assert
        assertEquals("user not found", result.message)
        assertEquals(400, result.status)
        verify(userRepository, never()).delete(any())
    }

    @Test
    fun `test processUser when action is DELETE and password is incorrect`() {
        // Arrange
        val userAuthDto = UserAuthDto("existingUser", "wrongPassword")
        `when`(userRepository.findById("existingUser")).thenReturn(Optional.of(getTestUser(true)))

        // Act
        val result = authService.processUser(userAuthDto, UserAction.DELETE)

        // Assert
        assertEquals("wrong password", result.message)
        assertEquals(400, result.status)
        verify(userRepository, never()).delete(any())
    }
    
    private fun getTestUser(encodePassword: Boolean = false): User {
        val password = if (encodePassword) BCryptPasswordEncoder().encode("password") else "password"
        val user = User()
        user.username = "existingUser"
        user.password = password
        user.role = "USER"
        return user
    }
}