package com.sunyesle.spring_boot_kotlin_tutorial.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken : AbstractAuthenticationToken {

    var jwtToken: String? = null
        private set

    private var principal: Any? = null
    private var credentials: Any? = null

    constructor(jwtToken: String) : super(null) {
        this.jwtToken = jwtToken
        isAuthenticated = false
    }

    constructor(
        principal: Any?,
        credentials: Any?,
        authorities: Collection<GrantedAuthority>
    ) : super(authorities) {
        this.principal = principal
        this.credentials = credentials
        isAuthenticated = true
    }

    override fun getCredentials(): Any? = credentials

    override fun getPrincipal(): Any? = principal
}
