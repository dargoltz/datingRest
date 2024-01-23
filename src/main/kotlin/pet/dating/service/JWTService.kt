package pet.dating.service

import org.springframework.stereotype.Service

@Service
class JWTService {

    fun createToken(username: String): String {
        return ""
    }

    fun validateTokenAndGetUsername(token: String): String? {
        return null
    }
}