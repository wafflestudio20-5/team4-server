package com.wafflestudio.toyproject.team4

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class Team4Application

fun main(args: Array<String>) {
    runApplication<Team4Application>(*args)
}
