package com.mdbank.exception.repository

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class EntityNotFoundException(msg: String) : RuntimeException(msg)