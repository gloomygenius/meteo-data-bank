package com.mdbank.exception.auth

open class AuthorizationException : RuntimeException {
    constructor(msg: String) : super(msg)
    constructor(msg: String, cause: Exception) : super(msg, cause)
}