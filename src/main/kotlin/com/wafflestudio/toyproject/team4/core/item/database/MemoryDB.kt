package com.wafflestudio.toyproject.team4.core.item.database

import com.wafflestudio.toyproject.team4.core.board.database.ReviewEntity
import com.wafflestudio.toyproject.team4.core.image.service.ImageService
import com.wafflestudio.toyproject.team4.core.item.domain.Item
import com.wafflestudio.toyproject.team4.core.item.service.ItemService
import com.wafflestudio.toyproject.team4.core.style.api.request.PostStyleRequest
import com.wafflestudio.toyproject.team4.core.style.service.StyleService
import com.wafflestudio.toyproject.team4.core.user.api.request.PatchMeRequest
import com.wafflestudio.toyproject.team4.core.user.api.request.ReviewRequest
import com.wafflestudio.toyproject.team4.core.user.database.PurchaseEntity
import com.wafflestudio.toyproject.team4.core.user.database.PurchaseRepository
import com.wafflestudio.toyproject.team4.core.user.database.UserEntity
import com.wafflestudio.toyproject.team4.core.user.database.UserRepository
import com.wafflestudio.toyproject.team4.core.user.domain.User
import com.wafflestudio.toyproject.team4.core.user.service.UserService
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.lang.Math.round
import java.util.Random

@Component
class MemoryDB(
    private val itemRepository: ItemRepository,
    private val passwordEncoder: PasswordEncoder,
    private val imageService: ImageService,
    private val styleService: StyleService,
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val purchaseRepository: PurchaseRepository,
    private val itemService: ItemService
) {
    private val doMakeMockAll = false // 일괄적으로 Mock Data를 만들려면 true로 설정

    private val doMakeMockItems = false // Mock 아이템을 만들려면 true로 설정
    private val doMakeMockStyles = false // Mock 스타일을 만들려면 true로 설정
    private val doMakeMockReviews = false // Mock 리뷰를 만들려면 true로 설정

    /**
     * Subcategory별로 크롤링 해오는 아이템의 개수
     *
     * 주의: totalItemCount가 makeMockStyles의 styleNum * maxItemNum 보다 크게 할 것
     *
     * 기본값: 100 : 60
     */
    private val itemCountPerSubCategory = 4
    private val totalItemCount = itemCountPerSubCategory * 25L

    /**
     * 서버가 시작하면, 크롤링을 통해 무신사에서 실시간 랭킹 긁어와서 아이템 repository에 저장
     * 각 대분류 - 소분류별로 {itemCountPerSubCategory}개씩
     */
    @EventListener
    fun makeMockData(event: ApplicationStartedEvent) {
        if (doMakeMockItems || doMakeMockAll) {
            /** mainCategory - subCategory
             * TOP   : 001       - SWEATER(001006), HOODIE(001004), SWEAT_SHIRT(001005), SHIRT(001002)
             * OUTER : 002       - COAT(002007), JACKET(002002), PADDING(002016), CARDIGAN(002020)
             * PANTS : 003       - DENIM(003002), SLACKS(003008), JOGGER(003004), LEGGINGS(003005)
             * SKIRT : 022       - MINISKIRT(022001), MEDI_SKIRT(022002), LONG_SKIRT(022003)
             * BAG   : 004       - BACKPACK(004001), CROSS_BAG(004002), ECHO_BAG(004014)
             * SHOES : 005       - GOODOO(005014), SANDAL(005004), SLIPPER(005018) // SNEAKERS(mainCategory = 018)
             * HEAD_WEAR : 007    - CAP(007001), HAT(007004), BEANIE(007005)
             **/

            crawling("001", "001006")
            crawling("001", "001004")
            crawling("001", "001005")
            crawling("001", "001002")
            crawling("002", "002007")
            crawling("002", "002002")
            crawling("002", "002016")
            crawling("002", "002020")
            crawling("003", "003002")
            crawling("003", "003008")
            crawling("003", "003004")
            crawling("003", "003005")
            crawling("022", "022001")
            crawling("022", "022002")
            crawling("022", "022003")
            crawling("004", "004001")
            crawling("004", "004002")
            crawling("004", "004014")
            crawling("005", "005014")
            crawling("005", "005004")
            crawling("005", "005018")
            crawling("018", "")
            crawling("007", "007001")
            crawling("007", "007004")
            crawling("007", "007005")
        }
    }

    @Transactional
    fun crawling(mainCategoryId: String, subCategoryId: String) {
        val url: String = "https://www.musinsa.com/ranking/best?period=now" +
            "&mainCategory=$mainCategoryId&subCategory=$subCategoryId"
        val document: Document = Jsoup.connect(url).get()

        document.run {
            val labelInfoList = this.getElementsByClass("box-icon-right").map { it.text() }
            val detailUrlList = this.select("div a[class=img-block]").map { it.attr("href") }
            val brandInfoList = this.select("div p[class=item_title] a").map { it.text() }
            val itemNameList = this.select("div p[class=list_info] a").map { it.attr("title") }
            val priceInfoList = this.select("div p[class=price]").map { it.text() }
            val sexInfoList = this.select("div [class=icon_group]").map { it.text() }

            for (idx in 0 until itemCountPerSubCategory) {
                val priceList = priceInfoList[idx].replace(",", "").split(" ")
                val itemDetailDoc: Document = Jsoup.connect(detailUrlList[idx]).get()

                val newItem = ItemEntity(
                    name = itemNameList[idx],
                    brand = brandInfoList[idx],
                    label = getLabel(labelInfoList[idx]),
                    oldPrice = priceList[0].dropLast(1).toLong(),
                    newPrice = priceList.getOrNull(1)?.dropLast(1)?.toLong(),
                    category = getMainCategory(mainCategoryId),
                    subCategory = getSubCategory(subCategoryId),
                    sex = getSexInfo(sexInfoList[idx]),
                    rating = round((0.0 + Random().nextDouble() * 10) * 100) / 100.0
                )

                newItem.updateImages(getItemImages(itemDetailDoc))
                newItem.updateOptionList(getOptionList(itemDetailDoc))

                // if "newPrice" is null, then we could not evaluate "sale" field
                if (newItem.newPrice != null) {
                    newItem.sale = round((1.0 - (newItem.newPrice!!.toFloat() / newItem.oldPrice)) * 100)
                }

                itemRepository.save(newItem)
            }
        }
    }

    private fun getItemImages(itemDetailDoc: Document): List<String> {
        return itemDetailDoc
            .select("div ul[class=product_thumb] li img")
            .map {
                it.attr("src")
                    .replace("60.jpg", "500.jpg")
                    .replace("60.png", "500.png")
            }.filterNot { it.contains("thumb-video.gif") }
    }

    private fun getOptionList(itemDetailDoc: Document): List<String> {
        return itemDetailDoc
            .select("div [id=buy_option_area] div div[class=option_cont] select option")
            .map {
                if (it.attr("value") == "none") {
                    it.attr("data-txt")
                } else {
                    it.attr("value")
                }
            }.filterNot { it.isEmpty() }
    }

    private fun getLabel(label: String): Item.Label? {
        val labelDict = mapOf(
            "한정 판매" to Item.Label.LIMITED,
            "부티크" to Item.Label.BOUTIQUE,
            "무신사 단독" to Item.Label.EXCLUSIVE,
            "선발매" to Item.Label.PREORDER,
        )
        return labelDict[label]
    }

    private fun getSexInfo(sex: String): Item.Sex = when (sex) {
        "남성" -> Item.Sex.MALE
        "여성" -> Item.Sex.FEMALE
        "남성 여성" -> Item.Sex.BOTH
        else -> Item.Sex.BOTH
    }

    private fun getMainCategory(mainCategoryId: String): Item.Category {
        val mainCategoryDict = mapOf(
            "001" to Item.Category.TOP,
            "002" to Item.Category.OUTER,
            "003" to Item.Category.PANTS,
            "022" to Item.Category.SKIRT,
            "004" to Item.Category.BAG,
            "005" to Item.Category.SHOES,
            "007" to Item.Category.HEAD_WEAR,
            "018" to Item.Category.SHOES, // SNEAKERS
        )
        return mainCategoryDict[mainCategoryId]!!
    }

    private fun getSubCategory(subCategoryId: String): Item.SubCategory {
        val subCategoryDict = mapOf(
            "001006" to Item.SubCategory.SWEATER,
            "001004" to Item.SubCategory.HOODIE,
            "001005" to Item.SubCategory.SWEAT_SHIRT,
            "001002" to Item.SubCategory.SHIRT,
            "002007" to Item.SubCategory.COAT,
            "002002" to Item.SubCategory.JACKET,
            "002016" to Item.SubCategory.PADDING,
            "002020" to Item.SubCategory.CARDIGAN,
            "003002" to Item.SubCategory.DENIM,
            "003008" to Item.SubCategory.SLACKS,
            "003004" to Item.SubCategory.JOGGER,
            "003005" to Item.SubCategory.LEGGINGS,
            "022001" to Item.SubCategory.MINI_SKIRT,
            "022002" to Item.SubCategory.MEDI_SKIRT,
            "022003" to Item.SubCategory.LONG_SKIRT,
            "004001" to Item.SubCategory.BACKPACK,
            "004002" to Item.SubCategory.CROSS_BAG,
            "004014" to Item.SubCategory.ECHO_BAG,
            "005014" to Item.SubCategory.GOODOO,
            "005004" to Item.SubCategory.SANDAL,
            "005018" to Item.SubCategory.SLIPPER,
            "007001" to Item.SubCategory.CAP,
            "007004" to Item.SubCategory.HAT,
            "007005" to Item.SubCategory.BEANIE,
        )
        return subCategoryDict[subCategoryId] ?: Item.SubCategory.SNEAKERS
    }

    @EventListener
    @Transactional
    fun makeMockStyles(event: ApplicationStartedEvent) {
        if (doMakeMockStyles || doMakeMockAll) {
            val userNum = 10 // 스타일을 작성하는 사용자의 수
            val styleNum = 15 // 각 사용자가 작성하는 스타일 수
            val maxItemNum = 4 // 스타일에 포함되는 상품의 최대 개수
            val users = makeMockUsers(userNum, "style")

            users.forEach {
                var shuffledItemIds = (1..totalItemCount).shuffled()
                val postStyleRequests = (1..styleNum).map {
                    val itemNum = (1..maxItemNum).random() // 스타일에 포함되는 상품의 개수 (1 ~ 4개)
                    val itemIds = shuffledItemIds.take(itemNum)
                    shuffledItemIds = shuffledItemIds.slice(itemNum until shuffledItemIds.size)
                    val images = itemIds.map { itemId -> // 스타일에 게시하는 사진은 임의의 상품 사진
                        val item = itemRepository.findById(itemId).get()
                        item.images.shuffled()[0].imageUrl
                    }
                    val content = "나의 style" // 스타일 내용
                    val hashtag = "#무신4 #맞팔 #선팔" // 스타일 해시태그
                    PostStyleRequest(images, itemIds, content, hashtag)
                }
                val username = it.username
                postStyleRequests.forEach {
                    styleService.postStyle(username, it)
                }
            }
        }
    }

    @EventListener
    @Transactional
    fun makeMockReviews(event: ApplicationStartedEvent) {
        if (doMakeMockReviews || doMakeMockAll) {
            val userNum = 20 // 리뷰를 남기는 사용자의 수
            val users = makeMockUsers(userNum, "review")
            val items = itemService.getItemRankingList( // 리뷰를 작성할 상품은 상위 10개 상품
                null,
                null,
                index = 0L,
                count = 10L,
                null
            )
            items.items.forEach {
                val itemId = it.id
                val item = itemRepository.findById(itemId).get()
                val itemSex = item.sex.toString()
                users.forEach { // 리뷰 작성 전 각 사용자의 구매 내역 생성
                    val userSex = it.sex?.toString()
                    if (itemSex == "BOTH" || itemSex == userSex) { // 성별이 일치하거나 남녀 공용인 상품에 대해서만 구매 및 후기 작성
                        val purchase = purchaseRepository.save(
                            PurchaseEntity(
                                user = it,
                                item = item,
                                optionName = item.options?.get( // 옵션은 랜덤
                                    (0 until item.options!!.size).random()
                                )?.optionName,
                                payment = item.newPrice ?: item.oldPrice,
                                quantity = 1L // 구매 수량은 1로 고정
                            )
                        )
                        val rating = ((1..5).random() * 2).toLong() // 평점은 [2, 4, 6, 8, 10] 중 하나
                        val content = when { // 평점에 따라 후기 내용 지정
                            rating < 6 -> "별로예요..."
                            rating > 6 -> "맘에 들어요!"
                            else -> "평범해요"
                        }
                        val size = ReviewEntity.Size.values()[(0..2).random()].toString().lowercase() // 사이즈는 랜덤
                        val color = ReviewEntity.Color.values()[(0..2).random()].toString().lowercase() // 색상은 랜덤
                        val maxImageCount = if (item.images.size < 3) item.images.size else 3
                        val imageCount = (0..maxImageCount).random()
                        val images = // 사진은 아이템 사진 중 랜덤으로 최대 3개 선택
                            item.images
                                .shuffled()
                                .take(imageCount)
                                .map { imageEntity -> imageEntity.imageUrl }
                        val reviewRequest = ReviewRequest( // 리뷰 작성
                            purchase.id,
                            rating,
                            content,
                            size,
                            color,
                            images
                        )
                        userService.postReview(it.username, reviewRequest)
                    }
                }
            }
        }
    }

    @Transactional
    fun makeMockUsers(
        userNum: Int, // 생성할 사용자의 수
        prefix: String // 생성할 사용자의 아이디(username), 닉네임에 붙일 prefix
    ): List<UserEntity> {
        val maleAverageHeight = 176L // 2023년 대한민국 19~24세 남성 평균 키
        val femaleAverageHeight = 162L // 2023년 대한민국 19~24세 여성 평균 키
        val heightRange = 10L // 키 변동폭
        val maleAverageWeight = 72L // 2023년 대한민국 19~24세 남성 평균 몸무게
        val femaleAverageWeight = 55L // 2023년 대한민국 19~24세 여성 평균 몸무게
        val weightRange = 15L // 몸무게 변동폭

        val users = (1..userNum).map { // 사용자 생성
            val encodedPassword = passwordEncoder.encode("12345678*")  // 비밀번호는 "12345678*"로 통일
            UserEntity(
                username = "${prefix}user${it}", // 아이디는 "{prefix}user{1~userNum}"로 통일
                encodedPassword = encodedPassword,
                nickname = "${prefix}nick${it}" // 닉네임은 "{prefix}nick{1~userNum}"로 통일
            )
        }
        val userEntities = users.map {
            val image = imageService.getDefaultImage(it.username)
            val sex = User.Sex.values()[(0..1).random()].toString().lowercase() // 성별은 랜덤
            val randomHeightOffset = (-1..1).random() * (1..heightRange).random()
            val height = when { // 키는 성별에 따른 평균 키에 임의의 변동폭(-10 ~ 10)을 더한 값
                sex == "male" -> maleAverageHeight + randomHeightOffset
                sex == "female" -> femaleAverageHeight + randomHeightOffset
                else -> (maleAverageHeight + femaleAverageHeight) / 2
            }
            val randomWeightOffset = (-1..1).random() * (1..weightRange).random()
            val weight = when { // 몸무게는 성별에 따른 평균 몸무게에 임의의 변동폭(-15 ~ 15)을 더한 값
                sex == "male" -> maleAverageWeight + randomWeightOffset
                sex == "female" -> femaleAverageWeight + randomWeightOffset
                else -> (maleAverageWeight + femaleAverageWeight) / 2
            }
            val description = "안녕하세요. ${it.nickname}입니다!"
            val instaUsername = "${it.nickname}_instagram"
            val patchMeRequest = PatchMeRequest(
                image = image,
                sex = sex,
                height = height,
                weight = weight,
                description = description,
                instaUsername = instaUsername
            )
            it.update(patchMeRequest, null)
            userRepository.save(it)
        }

        return userEntities
    }
}
