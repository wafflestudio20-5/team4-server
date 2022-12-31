package com.wafflestudio.toyproject.team4.core.user.database

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long>