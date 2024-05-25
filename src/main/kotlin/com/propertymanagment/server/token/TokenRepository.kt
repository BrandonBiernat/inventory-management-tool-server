package com.propertymanagment.server.token

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface TokenRepository : JpaRepository<Token, String> {
    @Query(value = """
        select t
        from Token t
        inner join User u on t.user.id = u.id
        where u.id = :id and t.loggedOut = false
    """)
    fun findAllValidTokenByUser(id: String): List<Token>
    fun findByToken(token: String): Optional<Token>
}