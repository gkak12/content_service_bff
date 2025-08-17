package com.service.user.oauth2.api

import com.service.common.response.JwtResponse
import com.service.user.oauth2.service.OAuth2Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/user/oauth2")
class OAuth2Controller(
    private val oauth2Service: OAuth2Service
){

    @GetMapping("/me/{encodedId}/{encodedName}")
    fun findOAuth2Me(@PathVariable encodedId: String, @PathVariable encodedName: String): Mono<JwtResponse> {
        return oauth2Service.createToken(encodedId, encodedName)
    }
}