package pet.dating.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JWTService {

    @Value("\${keys.jwt_signature}")
    private val signature: String? = null

    fun createToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(Date())
            .signWith(SignatureAlgorithm.HS512, signature)
            .compact()
    }

    fun parseTokenAndGetUsername(token: String): String? {
        return try {
            Jwts.parser()
                .setSigningKey(signature)
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            null
        }?.subject
    }

}