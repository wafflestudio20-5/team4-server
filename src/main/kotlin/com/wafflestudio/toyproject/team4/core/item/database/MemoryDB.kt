package com.wafflestudio.toyproject.team4.core.item.database

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class MemoryDB {
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
    
    
    private fun crawling(mainCategoryId: String, subCategoryId: String){
        val url: String = "https://www.musinsa.com/ranking/best?period=now" +
                           "&mainCategory=$mainCategoryId&subCategory=$subCategoryId"
        val document: Document = Jsoup.connect(url).get()
        val itemInfoList: List<List<Node>> = document
            .select("div[class=list-box box]")
            .select("ul li[class=li_box]")
            .subList(0, 5) // 각 대분류-소분류별로 5개씩 값 긁어오기
            .map {
                it.childNodes().filterIndexed { idx,_ -> idx%2!=0 }
            }

        return itemInfoList.forEach { itemInfo -> parseItemInfo(itemInfo) }
    }
    
    private fun parseItemInfo(itemInfo: List<Node>) {
        // 라벨
        val label = itemInfo[1].childNode(1).childNode(0)
        // 브랜드
        
        // 품목명
        // 브랜드
        // 이미지 url
        // 라벨
        // 기존가
        // 신규가
        // 할인율
        // 성별
        // 별점
        
    }

}