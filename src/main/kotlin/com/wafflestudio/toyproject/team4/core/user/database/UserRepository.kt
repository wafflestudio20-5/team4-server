package com.wafflestudio.toyproject.team4.core.user.database

import com.wafflestudio.toyproject.team4.core.user.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long>