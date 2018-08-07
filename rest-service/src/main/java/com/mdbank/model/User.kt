package com.mdbank.model

import com.mdbank.model.dto.Role
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Table(name = "app_users")
data class User(
        @Id
        @Column(name = "user_id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
        @SequenceGenerator(name = "user_seq_gen", sequenceName = "user_id_seq", allocationSize = 1)
        val id: Long? = null,

        @Email
        @Column(nullable = false, unique = true)
        var email: String? = null,

        @Column(nullable = false)
        var password: String? = null,

        @ElementCollection(targetClass = Role::class)
        @JoinTable(
                name = "user_roles",
                joinColumns = [JoinColumn(name = "user_id")],
                indexes = [
                    Index(
                            name = "role_id_unique_index",
                            columnList = "user_id, role_name",
                            unique = true)])
        @Column(name = "role_name", nullable = false)
        @Enumerated(EnumType.STRING)
        var roles: MutableCollection<Role>? = null
)