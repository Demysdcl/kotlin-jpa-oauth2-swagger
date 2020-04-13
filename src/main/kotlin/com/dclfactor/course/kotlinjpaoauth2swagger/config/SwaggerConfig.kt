package com.dclfactor.course.kotlinjpaoauth2swagger.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseMessageBuilder
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger.web.SecurityConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig(
        @Value("\${app.client.id}")
        val clientId: String,
        @Value("\${app.client.secret}")
        val clientSecret: String,
        @Value("\${host.full.dns.auth.link}")
        val authLink: String
) {

    val m201 = customMessage()

    val m204put = simpleMessage(204, "Update performed successfully")
    val m204del = simpleMessage(204, "Delete performed successfully")
    val m403 = simpleMessage(204, "Not authorized")
    val m404 = simpleMessage(204, "Not found")
    val m422 = simpleMessage(204, "Validation error")
    val m500 = simpleMessage(204, "Unexpected Internal error")

    @Bean
    fun securityConfiguration(): SecurityConfiguration =
            SecurityConfigurationBuilder.builder()
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .scopeSeparator(" ")
                    .useBasicAuthenticationWithAccessCodeGrant(true)
                    .build()

    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
            .globalResponseMessage(RequestMethod.GET, listOf(m403, m404, m500))
            .globalResponseMessage(RequestMethod.POST, listOf(m201, m403, m422, m500))
            .globalResponseMessage(RequestMethod.PUT, listOf(m204put, m403, m404, m422, m500))
            .globalResponseMessage(RequestMethod.DELETE, listOf(m204del, m403, m404, m500))
            .select()
            .apis(RequestHandlerSelectors
                    .basePackage("com.dclfactor.course.kotlinjpaoauth2swagger.domain"))
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(listOf(securitySchema()))
            .securityContexts(listOf(securityContect()))
            .pathMapping("/")
            .apiInfo(apiInfo())

    fun apiInfo(): ApiInfo = ApiInfo(
            "Kotlin, Spring Boot, JPA, Security, OAuth2 and Swagger Curso API",
            "Project built to learn Kotlin with Spring",
            "Version 1.0",
            "https://medium.com/@demysdcl",
            Contact("Demys Cota", "https://www.linkedin.com/in/demys-lima/", "demysdcl@gmail.com"),
            "To copy and learning",
            "https://medium.com/@demysdcl",
            listOf()
    )

    fun simpleMessage(code: Int, message: String): ResponseMessage = ResponseMessageBuilder()
            .code(code)
            .message(message)
            .build()

    fun customMessage(): ResponseMessage = ResponseMessageBuilder()
            .code(201)
            .message("Created resource")
            .headersWithDescription(HashMap<String, Header>().apply {
                put("location", Header("location", "New resource URI", ModelRef("string")))
            }
            ).build()

    fun securitySchema() = OAuth(
            "oauth2schema",
            arrayOfAuthorizationScopes().toList(),
            listOf(ResourceOwnerPasswordCredentialsGrant("$authLink/oauth/token"))
    )

    fun securityContect(): SecurityContext = SecurityContext.builder()
            .securityReferences(listOf(defaultAuth()))
            .forPaths(PathSelectors.ant("/users/**"))
            .build()

    private fun defaultAuth() = SecurityReference(
            "oauth2schema",
            arrayOfAuthorizationScopes()
    )

    private fun arrayOfAuthorizationScopes() = arrayOf(
            AuthorizationScope("read", "read all"),
            AuthorizationScope("write", "access all")
    )

} 