package com.wafflestudio.toyproject.team4.core.purchase.service

import com.wafflestudio.toyproject.team4.common.CustomHttp404
import com.wafflestudio.toyproject.team4.common.CustomHttp409
import com.wafflestudio.toyproject.team4.core.item.database.ItemRepository
import com.wafflestudio.toyproject.team4.core.purchase.api.request.PatchShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.purchase.api.request.PostShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.purchase.api.request.PurchasesRequest
import com.wafflestudio.toyproject.team4.core.purchase.domain.CartItem
import com.wafflestudio.toyproject.team4.core.purchase.domain.Purchase
import com.wafflestudio.toyproject.team4.core.purchase.api.response.CartItemsResponse
import com.wafflestudio.toyproject.team4.core.purchase.api.response.PurchaseItemsResponse
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface PurchaseService {
    fun getPurchases(username: String): PurchaseItemsResponse
    fun postPurchases(username: String, request: PurchasesRequest)
    fun getShoppingCart(username: String): CartItemsResponse
    fun postShoppingCart(username: String, postShoppingCartRequest: PostShoppingCartRequest)
    fun patchShoppingCart(username: String, patchShoppingCartRequest: PatchShoppingCartRequest)
    fun deleteShoppingCart(username: String, cartItemId: Long)
}

@Service
class PurchaseServiceImpl(
    private val userRepository: UserRepository,
    private val itemRepository: ItemRepository
) : PurchaseService {

    /* **********************************************************
    //                        Purchases                       //
    ********************************************************** */

    @Transactional
    override fun getPurchases(username: String): PurchaseItemsResponse {
        val user = userRepository.findByUsernameFetchJoinPurchases(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        return PurchaseItemsResponse(
            purchaseItems = user.purchases.map { entity -> Purchase.of(entity) }
        )
    }

    @Transactional
    override fun postPurchases(username: String, request: PurchasesRequest) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        request.purchaseItems.forEach { info ->
            val orderItem = itemRepository.findByIdOrNull(info.id)
                ?: throw CustomHttp404("아이템 정보가 올바르지 않습니다.")

            user.purchase(orderItem, info)
        }
    }

    /* **********************************************************
   //                      Shopping Cart                      //
   ********************************************************** */

    @Transactional
    override fun getShoppingCart(username: String): CartItemsResponse {
        val user = userRepository.findByUsernameFetchJoinCartItems(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")

        return CartItemsResponse(
            cartItems = user.cartItems.map { entity -> CartItem.of(entity) }
        )
    }

    @Transactional
    override fun postShoppingCart(username: String, postShoppingCartRequest: PostShoppingCartRequest) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val orderItem = itemRepository.findByIdOrNull(postShoppingCartRequest.id)
            ?: throw CustomHttp404("존재하지 않는 상품입니다.")

        // 이미 장바구니에 해당 상품이 존재하는 경우
        user.cartItems.find {
            it.item.id == postShoppingCartRequest.id &&
            it.optionName == postShoppingCartRequest.option
        }?.let { throw CustomHttp409("이미 장바구니에 있는 상품입니다.") }

        user.addToCart(orderItem, postShoppingCartRequest)
    }

    @Transactional
    override fun patchShoppingCart(username: String, patchShoppingCartRequest: PatchShoppingCartRequest) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val cartItem = user.cartItems.find { it.id == patchShoppingCartRequest.id }
            ?: throw CustomHttp404("장바구니에 해당 상품이 없습니다.")

        cartItem.updateQuantity(patchShoppingCartRequest.quantity)
    }

    @Transactional
    override fun deleteShoppingCart(username: String, cartItemId: Long) {
        val user = userRepository.findByUsername(username)
            ?: throw CustomHttp404("해당 아이디로 가입된 사용자 정보가 없습니다.")
        val cartItem = user.cartItems.find { it.id == cartItemId }
            ?: throw CustomHttp404("장바구니에 해당 상품이 없습니다.")

        user.cartItems.remove(cartItem)
    }
}
