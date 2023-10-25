package pet.dating.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/login")
class RegistrationController {

    @GetMapping("/")
    fun registration() {}


}