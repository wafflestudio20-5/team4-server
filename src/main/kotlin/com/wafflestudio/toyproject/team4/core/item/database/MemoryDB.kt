package com.wafflestudio.toyproject.team4.core.item.database

import com.wafflestudio.toyproject.team4.core.item.domain.Item
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MemoryDB (
    private val itemRepository: ItemRepository
) {
    /**
     * 서버가 시작하면, 크롤링을 통해 무신사에서 실시간 랭킹 긁어와서 아이템 repository에 저장
     * 각 대분류 - 소분류별로 5개씩
     */

    @EventListener
    fun makeMockData(event: ApplicationStartedEvent) {

        /** mainCategory - subCategory
         * TOP   : 001       - SWEATER(001006), HOODIE(001004), SWEATSHIRT(001005), SHIRT(001002)
         * OUTER : 002       - COAT(002007), JACKET(002002), PADDING(002016), CARDIGAN(002020)
         * PANTS : 003       - DENIM(003002), SLACKS(003008), JOGGER(003004), LEGGINGS(003005)
         * SKIRT : 022       - MINISKIRT(022001), MEDISKIRT(022002), LONGSKIRT(022003)
         * BAG   : 004       - BACKPACK(004001), CROSSBAG(004002), ECHOBAG(004014)
         * SHOES : 005       - GOODOO(005014), SANDAL(005004), SLIPPER(005018) // SNEAKERS(mainCategory = 18)
         * HEADWEAR : 007    - CAP(007001), HAT(007004), BEANIE(007005)
         **/

        crawling("001", "001006")
    }


    @Transactional
    fun crawling(mainCategoryId: String, subCategoryId: String) {
        val url: String = "https://www.musinsa.com/ranking/best?period=now" +
            "&mainCategory=$mainCategoryId&subCategory=$subCategoryId"
        val document: Document = Jsoup.connect(url).get()

        document.run {
            val labelInfoList = this.getElementsByClass("box-icon-right").map { it.text() }
            val imageInfoList = this.getElementsByClass("lazyload lazy").map { it.attr("data-original") }
            val brandInfoList = this.select("div p[class=item_title] a").map { it.text() }
            val itemNameList = this.select("div p[class=list_info] a").map { it.attr("title") }
            val priceInfoList = this.select("div p[class=price]").map { it.text() }

            for (idx in 0..4) {
                val newItem = ItemEntity(
                    name = itemNameList[idx],
                    brand = brandInfoList[idx],
                    imageUrl = imageInfoList[idx],
                    //label = labelInfoList[idx],
                    oldPrice = priceInfoList[idx].substringBefore(" ").dropLast(1).replace(",", "").toLong(),
                    newPrice = priceInfoList[idx].substringAfter(" ").dropLast(1).replace(",", "").toLong(),
                    category = Item.Category.TOP,
                    subCategory = Item.SubCategory.SWEATER
                )
                itemRepository.save(newItem)
            }
        }
    }
}