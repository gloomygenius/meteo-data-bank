package com.mdbank.repository

import com.mdbank.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmailIgnoreCase(email: String): User?

    fun existsByEmail(email: String): Boolean
}