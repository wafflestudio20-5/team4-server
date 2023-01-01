package com.wafflestudio.toyproject.team4.common

import org.springframework.http.HttpStatus

open class SeminarException(msg: String, val status: HttpStatus) : RuntimeException(msg)

class Seminar404(msg: String) : SeminarException(msg, HttpStatus.NOT_FOUND)

class Seminar400(msg: String) : SeminarException(msg, HttpStatus.BAD_REQUEST)

class Seminar409(msg: String) : SeminarException(msg, HttpStatus.CONFLICT)

class Seminar401(msg: String) : SeminarException(msg, HttpStatus.UNAUTHORIZED)

class Seminar403(msg: String) : SeminarException(msg, HttpStatus.FORBIDDEN)

class Seminar200(msg: String) : SeminarException(msg, HttpStatus.OK)