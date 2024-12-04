package store.model

data class Promotion(
    val name: String,
    val buy: Int,
    val get: Int,
    val startDate: String,
    val endDate: String,
) {

    companion object {
        const val SPRITE_PROMOTION = "탄산2+1"
        const val MD_PROMOTION = "MD추천상품"
        const val SHINY_PROMOTION = "반짝할인"

        fun from(promotion: String, promotionList: List<List<String>>): Promotion {
            val promotionInfo = promotionList.find { it[0] == promotion }!!
            when(promotion) {
                SPRITE_PROMOTION -> {
                    return Promotion(promotionInfo[0], promotionInfo[1].toInt(), promotionInfo[2].toInt(), promotionInfo[3], promotionInfo[4])
                }
                MD_PROMOTION -> {
                    return Promotion(promotionInfo[0], promotionInfo[1].toInt(), promotionInfo[2].toInt(), promotionInfo[3], promotionInfo[4])
                }
                SHINY_PROMOTION -> {
                    return Promotion(promotionInfo[0], promotionInfo[1].toInt(), promotionInfo[2].toInt(), promotionInfo[3], promotionInfo[4])
                }
            }
            return Promotion("", 0, 0, "", "")
        }
    }
}