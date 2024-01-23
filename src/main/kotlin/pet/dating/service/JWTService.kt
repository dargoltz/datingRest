package pet.dating.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.*

@Service
class JWTService {

    fun createToken(username: String): String {
        val secretKey = "yourSecretKey"

        return Jwts.builder()
            .setSubject(username)
            .setExpiration(Date())
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    fun validateTokenAndGetUsername(token: String): String? {
        return null
    }
}