package pet.dating.service.integration

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class JWTAuthTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    object TestData {
        const val username = "testUser"
        const val password = "password"
        const val wrongPassword = "wrongPassword"
        const val wrongToken = "wrongToken"
    }

    @Test
    fun `get users list without token`() {
        mockMvc.perform(get("/unliked_users"))
            .andExpect(status().isUnauthorized)
    }  // 401

    @Test
    fun `login with wrong testData`() {
        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"username": "${TestData.username}", "password": ${TestData.wrongPassword}}""")
        )
            .andExpect(status().isBadRequest)
    }  // 400

    @Test
    fun `get users list with wrong token`() {
        mockMvc.perform(
            get("/unliked_users")
                .header("Token", TestData.wrongToken)
        )
            .andExpect(status().isUnauthorized)
    }  // 401

    @Test
    fun `get users list with correct token`() {
        val token = getToken()
        mockMvc.perform(
            get("/unliked_users")
                .header("Token", token)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }  // 200 []

    private fun getToken(): String {
        return mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"username": "${TestData.username}", "password": "${TestData.password}"}""")
        )
            .andReturn()
            .response
            .contentAsString
    }
}
