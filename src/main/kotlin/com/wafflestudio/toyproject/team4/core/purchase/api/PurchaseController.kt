package com.wafflestudio.toyproject.team4.core.purchase.api

import com.wafflestudio.toyproject.team4.common.Authenticated
import com.wafflestudio.toyproject.team4.common.UserContext
import com.wafflestudio.toyproject.team4.core.purchase.api.request.PatchShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.purchase.api.request.PostShoppingCartRequest
import com.wafflestudio.toyproject.team4.core.purchase.api.request.PurchasesRequest
import com.wafflestudio.toyproject.team4.core.purchase.service.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/me")
class PurchaseController(
    private val purchaseService: PurchaseService
) {

    @Authenticated
    @GetMapping("/purchases")
    fun getPurchases(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(purchaseService.getPurchases(username), HttpStatus.OK)

    @Authenticated
    @PostMapping("/purchases")
    fun postPurchases(
        @RequestBody request: PurchasesRequest,
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(purchaseService.postPurchases(username, request), HttpStatus.CREATED)

    @Authenticated
    @GetMapping("/shopping-cart")
    fun getShoppingCart(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
    ) = ResponseEntity(purchaseService.getShoppingCart(username), HttpStatus.OK)


    @Authenticated
    @PostMapping("/shopping-cart")
    fun postShoppingCart(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @RequestBody postShoppingCartRequest: PostShoppingCartRequest
    ) = ResponseEntity(purchaseService.postShoppingCart(username, postShoppingCartRequest), HttpStatus.CREATED)

    @Authenticated
    @PatchMapping("/shopping-cart")
    fun patchShoppingCart(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @RequestBody patchShoppingCartRequest: PatchShoppingCartRequest
    ) = ResponseEntity(purchaseService.patchShoppingCart(username, patchShoppingCartRequest), HttpStatus.OK)

    @Authenticated
    @DeleteMapping("/shopping-cart/{id}")
    fun deleteShoppingCart(
        @RequestHeader(value = "Authorization") authorization: String,
        @UserContext username: String,
        @PathVariable(value = "id") cartItemId: Long
    ) = ResponseEntity(purchaseService.deleteShoppingCart(username, cartItemId), HttpStatus.OK)

}