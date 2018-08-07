package com.mdbank.exception

class NetCdfReadException : RuntimeException {
    constructor(e: Exception) : super(e)

    constructor(msg: String) : super(msg)
}