package io.example.entity

import java.io.Serializable
import javax.persistence.*

@Table(name = "users")
@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var emailId: String? = null
) : Serializable