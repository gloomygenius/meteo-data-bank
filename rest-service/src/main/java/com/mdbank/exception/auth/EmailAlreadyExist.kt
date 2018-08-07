package com.mdbank.exception.auth

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class EmailAlreadyExist(email: String) : AuthorizationException("Email $email already in use!")