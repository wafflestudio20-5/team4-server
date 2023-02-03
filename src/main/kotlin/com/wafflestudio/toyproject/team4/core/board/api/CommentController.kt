package com.wafflestudio.toyproject.team4.core.board.api

import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.common.UserContext
import com.wafflestudio.toyproject.team4.core.board.api.request.PostCommentRequest
import com.wafflestudio.toyproject.team4.core.board.api.request.PutCommentRequest
import com.wafflestudio.toyproject.team4.core.board.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/comment")
class CommentController(
    val commentService: CommentService
) {
    @Authenticated
    @PostMapping
    fun postComment(
        @RequestBody request: PostCommentRequest,
        @UserContext username: String,
    ) = ResponseEntity(commentService.postComment(username, request), HttpStatus.CREATED)

    @Authenticated
    @PutMapping("/{commentId}")
    fun putComment(
        @PathVariable(value = "commentId") commentId: Long,
        @RequestBody request: PutCommentRequest,
        @UserContext username: String,
    ) = ResponseEntity(commentService.putComment(username, request, commentId), HttpStatus.OK)

    @Authenticated
    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable(value = "commentId") commentId: Long,
        @UserContext username: String,
    ) = ResponseEntity(commentService.deleteComment(username, commentId), HttpStatus.OK)
}
