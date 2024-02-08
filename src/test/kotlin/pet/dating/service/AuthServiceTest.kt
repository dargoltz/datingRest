package pet.dating.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mindrot.jbcrypt.BCrypt
import org.mockito.Mockito.*
import pet.dating.dto.UserAuthDto
import pet.dating.enums.UserAction
import pet.dating.models.User
import pet.dating.repositories.UserRepository
import java.util.*

class AuthServiceTest {

    object TestData {
        const val username = "testUser"
        const val correctPassword = "correctPassword"
        const val wrongPassword = "wrongPassword"
        const val token = "testToken"
    }

    // Создаем мок UserRepository для использования в тестах
    private val userRepository = mock(UserRepository::class.java)

    private val jwtService = mock(JWTService::class.java)

    // Создаем экземпляр AuthService с использованием мока userRepository
    private val authService = AuthService(jwtService, userRepository)

    @Test
    fun `test processUser when action is CREATE and user does not exist`() {
        // Arrange
        val userAuthDto = UserAuthDto(TestData.username, TestData.correctPassword)
        `when`(userRepository.findById(TestData.username)).thenReturn(Optional.empty())

        // Act
        val result = authService.processUser(userAuthDto, UserAction.CREATE)

        // Assert
        assertEquals("created user ${TestData.username}", result.message)
        assertEquals(200, result.status)

        // Verify that userRepository.save was called once
        verify(userRepository, times(1)).save(any())
    }

    @Test
    fun `test processUser when action is CREATE and user already exists`() {
        // Arrange
        val userAuthDto = UserAuthDto(TestData.username, TestData.correctPassword)
        `when`(userRepository.findById(TestData.username)).thenReturn(Optional.of(getTestUser()))

        // Act
        val result = authService.processUser(userAuthDto, UserAction.CREATE)

        // Assert
        assertEquals("user ${TestData.username} already exists", result.message)
        assertEquals(400, result.status)
        verify(userRepository, never()).save(any())
    }

    @Test
    fun `test processUser when action is DELETE and user exists with correct password`() {
        // Arrange
        val userAuthDto = UserAuthDto(TestData.username, TestData.correctPassword)
        `when`(userRepository.findById(TestData.username)).thenReturn(Optional.of(getTestUser(true)))

        // Act
        val result = authService.processUser(userAuthDto, UserAction.DELETE)

        // Assert
        assertEquals("deleted user ${TestData.username}", result.message)
        assertEquals(200, result.status)
        verify(userRepository, times(1)).delete(any())
    }

    @Test
    fun `test processUser when action is DELETE and user does not exist`() {
        // Arrange
        val userAuthDto = UserAuthDto(TestData.username, TestData.correctPassword)
        `when`(userRepository.findById(TestData.username)).thenReturn(Optional.empty())

        // Act
        val result = authService.processUser(userAuthDto, UserAction.DELETE)

        // Assert
        assertEquals("user ${TestData.username} not found", result.message)
        assertEquals(400, result.status)
        verify(userRepository, never()).delete(any())
    }

    @Test
    fun `test processUser when action is DELETE and password is incorrect`() {
        // Arrange
        val userAuthDto = UserAuthDto(TestData.username, TestData.correctPassword)
        `when`(userRepository.findById(TestData.username)).thenReturn(Optional.of(getTestUser(false)))

        // Act
        val result = authService.processUser(userAuthDto, UserAction.DELETE)

        // Assert
        assertEquals("wrong password", result.message)
        assertEquals(400, result.status)
        verify(userRepository, never()).delete(any())
    }

    private fun getTestUser(isCorrectPassword: Boolean = false): User {
        val password = if (isCorrectPassword) BCrypt.hashpw(TestData.correctPassword, BCrypt.gensalt())
        else BCrypt.hashpw(TestData.wrongPassword, BCrypt.gensalt())

        val user = User()
        user.username = TestData.username
        user.password = password
        user.role = "USER"
        return user
    }
}