package com.wafflestudio.toyproject.team4.core.item.database

import com.wafflestudio.toyproject.team4.core.item.domain.Item
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.lang.Math.round
import java.util.Random

@Component
class MemoryDB(
    private val itemRepository: ItemRepository
) {
    /**
     * 서버가 시작하면, 크롤링을 통해 무신사에서 실시간 랭킹 긁어와서 아이템 repository에 저장
     * 각 대분류 - 소분류별로 10개씩
     */

    fun makeMockData(event: ApplicationStartedEvent) {

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

            for (idx in 0..9) {
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
}
