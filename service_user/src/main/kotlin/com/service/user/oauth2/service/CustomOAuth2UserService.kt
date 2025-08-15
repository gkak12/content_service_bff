package com.service.user.oauth2.service

import com.service.common.enums.RoleEnums
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Primary
@Component
class CustomOAuth2UserService: ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User>  {

    private val logger = LoggerFactory.getLogger(CustomOAuth2UserService::class.java)

    override fun loadUser(userRequest: OAuth2UserRequest): Mono<OAuth2User> {
        // server_ip:port/oauth2/authorization/naver
        logger.info("CustomOAuth2UserService loadUser is called.")

        val clientRegistration = userRequest.clientRegistration
        val accessToken = userRequest.accessToken.tokenValue
        val userInfoEndpointUri = clientRegistration.providerDetails.userInfoEndpoint.uri

        return WebClient.create()
            .get()
            .uri(userInfoEndpointUri)
            .headers { it.setBearerAuth(accessToken) }
            .retrieve()
            .bodyToMono(Map::class.java)
            .map { attributesMap ->
                val attributes = (attributesMap["response"] as? Map<*, *>)
                    ?: throw OAuth2AuthenticationException("Response not found")

                DefaultOAuth2User(
                    setOf(SimpleGrantedAuthority(RoleEnums.ROLE_USER.value)),
                    attributes as Map<String, Any>,
                    "email"
                )
            }
    }
}
