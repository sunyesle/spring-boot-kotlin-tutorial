package com.sunyesle.spring_boot_kotlin_tutorial.security

import org.springframework.security.authentication.AbstractAuthenticationToken

class JwtAuthenticationToken : AbstractAuthenticationToken {

    var jwtToken: String? = null
        private set

    private var principal: Any? = null
    private var credentials: Any? = null

    constructor(jwtToken: String) : super(null) {
        this.jwtToken = jwtToken
        isAuthenticated = false
    }

    override fun getCredentials(): Any? = credentials

    override fun getPrincipal(): Any? = principal
}
