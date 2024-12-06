package store

import store.domain.ItemManager
import store.domain.PromotionManager
import store.model.Answer
import store.model.PromotionItem
import store.model.ShoppingCart
import store.util.FileReader
import store.util.Validator
import store.util.retryInput
import store.view.InputView
import store.view.OutputView

class Controller(
    private val fileReader: FileReader,
    private val outputView: OutputView,
    private val inputView: InputView,
    private val validator: Validator,
    private val promotionManager: PromotionManager,
) {


    fun run() {
        val (products, promotions) = readProductsAndPromotionsFile()
        val itemManager = ItemManager.from(products, promotions)

        outputView.introduceStore()
        outputView.introduceProducts(itemManager)

        val validatedProductAndQuantity = inputProductAndQuantity {
            inputView.inputProductAndQuantity()
        }
        ShoppingCart.init(validatedProductAndQuantity)
        presentPromotionItem(validatedProductAndQuantity)

        askBuyNotApplyPromotionItem()

        println(ShoppingCart.shoppingCartItems)


    }

    private fun askBuyNotApplyPromotionItem() {

        for (promotionItem in ShoppingCart.getPromotionItems()) {
            if (promotionItem.quantity % (promotionItem.promotion.buy + promotionItem.promotion.get) == 0) continue

            val notApplyPromotionItemQuantity =
                promotionItem.quantity % (promotionItem.promotion.buy + promotionItem.promotion.get)
            val generalItemQuantity = ShoppingCart.getGeneralItemQuantity(promotionItem.name)
            val answer = inputRetryAsk {
                inputView.inputAskAddNotApplyPromotionItem(
                    promotionItem.name,
                    notApplyPromotionItemQuantity + generalItemQuantity
                )
            }.not()
            if (answer) {
                ShoppingCart.takeOutNotApplyPromotionItemQuantity(
                    promotionItem.name,
                    listOf(notApplyPromotionItemQuantity, generalItemQuantity)
                )
            }
        }
    }

    private fun presentPromotionItem(validatedProductAndQuantity: List<Pair<String, Int>>) {
        val canAddOfferPromotionProduct =
            getCanAddOfferPromotionProduct(validatedProductAndQuantity)
        val t = canAddOfferPromotionProduct.filter {
            inputRetryAsk { inputView.inputAskAddPromotionItem(it.first.name, it.second) }
        }
        ShoppingCart.addPromotionItemQuantity(t)
    }


    private fun readProductsAndPromotionsFile(): Pair<List<String>, List<String>> =
        fileReader.readProducts() to fileReader.readPromotions()

    private fun inputProductAndQuantity(input: () -> String) = retryInput {
        val inputProductAndQuantityList = input().split(",")
        inputProductAndQuantityList.map {
            validator.validateInputProductAndQuantity(it, ItemManager.getInstance().getItems())
        }
    }


    private fun inputRetryAsk(input: () -> String) = retryInput {
        val validatedInput = validator.validateInputYesOrNo(input())
        return@retryInput when (validatedInput) {
            Answer.YES -> true
            Answer.NO -> false
        }
    }

    private fun getCanAddOfferPromotionProduct(
        productAndQuantity: List<Pair<String, Int>>,
    ): List<Pair<PromotionItem, Int>> {

        val promotionProductAndQuantity = productAndQuantity.filter {
            ItemManager.getInstance().findPromotionItem(it.first) != null
        }

        return ShoppingCart.getPromotionItems().filter {
            promotionManager.isOfferPromotionProduct(it, it.quantity)
        }.map { it to it.promotion.get }
    }
}