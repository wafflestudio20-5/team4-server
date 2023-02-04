package com.wafflestudio.toyproject.team4.core.board.service

import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.core.board.database.ReviewRepository
import com.wafflestudio.toyproject.team4.core.board.api.request.PostCommentRequest
import com.wafflestudio.toyproject.team4.core.board.api.request.PutCommentRequest
import com.wafflestudio.toyproject.team4.core.board.api.response.CommentResponse
import com.wafflestudio.toyproject.team4.core.board.domain.Comment
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface CommentService {
    fun postComment(username: String, request: PostCommentRequest): CommentResponse
    fun putComment(username: String, request: PutCommentRequest, commentId: Long)
    fun deleteComment(username: String, commentId: Long)
}

@Service
class CommentServiceImpl(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
) : CommentService {
    @Transactional
    override fun postComment(username: String, request: PostCommentRequest): CommentResponse {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val reviewEntity = reviewRepository.findByIdOrNull(request.reviewId)
            ?: throw CustomHttp404("존재하지 않는 구매후기입니다.")
        val commentEntity = reviewEntity.addComment(userEntity, request.content)
        return CommentResponse(Comment.of(commentEntity))
    }

    @Transactional
    override fun putComment(username: String, request: PutCommentRequest, commentId: Long) {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val commentEntity = userEntity.comments.find { it.id == commentId }
            ?: throw CustomHttp404("존재하지 않는 댓글입니다.")
        commentEntity.update(request.content)
    }

    @Transactional
    override fun deleteComment(username: String, commentId: Long) {
        val userEntity = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val commentEntity = userEntity.comments.find { it.id == commentId }
            ?: throw CustomHttp404("존재하지 않는 댓글입니다.")
        userEntity.comments.remove(commentEntity)
    }
}
