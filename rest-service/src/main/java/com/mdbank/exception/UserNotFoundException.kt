package com.mdbank.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(userId: Long) : RuntimeException("User with id " + userId + "is not found")
