package com.dclfactor.course.kotlinjpaoauth2swagger.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ObjectNotFoundException::class)
    fun handleObjectNotFound(e: ObjectNotFoundException, request: HttpServletRequest) =
            createResponseEntity(e, request, HttpStatus.NOT_FOUND)

    @ExceptionHandler(ObjectAlreadyExistException::class)
    fun handleObjectAlreadyExist(e: ObjectAlreadyExistException, request: HttpServletRequest) =
            createResponseEntity(e, request, HttpStatus.CONFLICT)

    @ExceptionHandler(ObjectNotEnableException::class)
    fun handleObjectNotEnabled(e: ObjectNotEnableException, request: HttpServletRequest) =
            createResponseEntity(e, request, HttpStatus.UNAUTHORIZED)

    private fun createResponseEntity(e: RuntimeException, request: HttpServletRequest, httpStatus: HttpStatus): ResponseEntity<StandardError> {
        return ResponseEntity
                .status(httpStatus)
                .body(createStandardError(e, request, httpStatus.value()))
    }

    private fun createStandardError(e: RuntimeException, request: HttpServletRequest, httpStatus: Int): StandardError {
        return StandardError(
                System.currentTimeMillis(),
                httpStatus,
                "Not found",
                e.message ?: "",
                request.requestURI
        )
    }

}