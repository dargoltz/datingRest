package pet.dating.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.*

@Service
class JWTService {

    val secretKey = "yourSecretKey"

    fun createToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(Date())
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    fun parseTokenAndGetUsername(token: String): String? {
        return try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
        } catch (e: Exception) {
            null
        }?.subject
    }

}