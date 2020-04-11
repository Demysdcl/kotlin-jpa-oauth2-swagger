package com.dclfactor.course.kotlinjpaoauth2swagger.expection

class StandardError (
        val timestamp: Long,
        val status: Int,
        val error: String,
        val message: String,
        val path: String
)