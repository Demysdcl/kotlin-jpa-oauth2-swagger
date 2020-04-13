package com.dclfactor.course.kotlinjpaoauth2swagger.security.cors

import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CORSFilter : Filter {

    override fun doFilter(req: ServletRequest?, res: ServletResponse?, chain: FilterChain?) {
        val response = res as HttpServletResponse
        val request = req as HttpServletRequest

        response.apply {
            setHeader("Access-Control-Allow-Origin", "*")
            setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS,DELETE, PUT")
            setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, x-auth-token,origin, content-type, accept")
            setHeader("Access-Control-Origin", "*")
        }

        when (request.method) {
            "OPTIONS" -> response.status = HttpServletResponse.SC_OK
            else -> chain?.doFilter(req, res)
        }
    }
}
