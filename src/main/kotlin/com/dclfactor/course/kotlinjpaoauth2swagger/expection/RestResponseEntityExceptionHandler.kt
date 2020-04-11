package com.dclfactor.course.kotlinjpaoauth2swagger.expection

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ObjectNotFoundException::class)
    fun objectNotFound(e: ObjectNotFoundException, request: HttpServletRequest) = ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(StandardError(
                    System.currentTimeMillis(),
                    HttpStatus.NOT_FOUND.value(),
                    "Not found",
                    e.message ?: "",
                    request.requestURI
            ))

}