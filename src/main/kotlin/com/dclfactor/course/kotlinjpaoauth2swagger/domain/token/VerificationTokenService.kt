package com.dclfactor.course.kotlinjpaoauth2swagger.domain.token

import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.User
import com.dclfactor.course.kotlinjpaoauth2swagger.domain.user.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class VerificationTokenService(
        val verificationTokenRepository: VerificationTokenRepository,
        val userRepository: UserRepository
) {


}