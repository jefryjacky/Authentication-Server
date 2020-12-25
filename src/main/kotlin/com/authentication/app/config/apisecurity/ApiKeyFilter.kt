package com.authentication.app.config.apisecurity

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import javax.servlet.http.HttpServletRequest

/**
 * Created by Jefry Jacky on 25/12/20.
 */
class ApiKeyFilter(
        private val principalRequestHeader:String
): AbstractPreAuthenticatedProcessingFilter() {

    override fun getPreAuthenticatedCredentials(request: HttpServletRequest): Any {
        return "N/A"
    }

    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest): Any {
        return request.getHeader(principalRequestHeader)
    }
}