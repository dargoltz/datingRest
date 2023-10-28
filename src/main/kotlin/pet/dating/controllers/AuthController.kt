package pet.dating.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/auth")
class AuthController {

    @GetMapping("/login")
    fun login() {

    }

    @GetMapping("/registration")
    fun registration() {

    }


}