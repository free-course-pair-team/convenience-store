package store

import store.domain.ItemManager
import store.domain.Membership
import store.domain.PromotionManager
import store.model.Answer
import store.model.PromotionItem
import store.model.Receipt
import store.model.ShoppingCart
import store.model.Stock
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
    private val membership: Membership,
) {


    fun run() {
        val (products, promotions) = readProductsAndPromotionsFile()
        val stock = Stock.from(products, promotions)
        val itemManager = ItemManager(stock)

        outputView.introduceStore()
        outputView.introduceProducts(stock)

        val validatedProductAndQuantity = inputProductAndQuantity {
            inputView.inputProductAndQuantity()
        }
        ShoppingCart.init(validatedProductAndQuantity, itemManager)
        askPresentPromotionItem(ShoppingCart.getPromotionItems(), itemManager)

        askBuyNotApplyPromotionItem { name: String, quantity: Int ->
            inputView.inputAskAddNotApplyPromotionItem(name, quantity)
        }
        val membershipDiscountAmount = askTakeMembership()
        println(Receipt().showReceipt(membershipDiscountAmount))
    }

    private fun askTakeMembership(): Int {
        val answer = inputRetryAsk { inputView.inputAskTakeMembership() }
        if (answer) return membership.getMemberShipDiscountAmount()
        return 0
    }

    private fun askBuyNotApplyPromotionItem(input: (name: String, quantity: Int) -> String) {
        for (promotionItem in ShoppingCart.getPromotionItems()) {
            if (promotionManager.isExistApplyPromotionProductQuantity(promotionItem).not()) continue

            val notApplyPromotionItemQuantity =
                ShoppingCart.getNotApplyPromotionItemQuantity(promotionItem)
            val generalItemQuantity = ShoppingCart.getGeneralItemQuantity(promotionItem.name)
            val answer = inputRetryAsk {
                input(promotionItem.name, notApplyPromotionItemQuantity + generalItemQuantity)
            }
            if (answer.not()) {
                ShoppingCart.takeOutNotApplyPromotionItemQuantity(
                    promotionItem.name,
                    listOf(notApplyPromotionItemQuantity, generalItemQuantity)
                )
            }
        }
    }

    private fun askPresentPromotionItem(promotionItems: List<PromotionItem>, itemManager: ItemManager) {
        val canAddOfferPromotionProduct =
            promotionManager.getCanAddOfferPromotionProduct(promotionItems, itemManager)
        val acceptAddPromotionProduct = canAddOfferPromotionProduct.filter {
            inputRetryAsk { inputView.inputAskAddPromotionItem(it.first.name, it.second) }
        }
        ShoppingCart.addPromotionItemQuantity(acceptAddPromotionProduct)
    }


    private fun readProductsAndPromotionsFile(): Pair<List<String>, List<String>> =
        fileReader.readProducts() to fileReader.readPromotions()

    private fun inputProductAndQuantity(input: () -> String) = retryInput {
        val inputProductAndQuantityList = input().split(",")
        inputProductAndQuantityList.map {
            validator.validateInputProductAndQuantity(it, Stock.getInstance().items)
        }
    }


    private fun inputRetryAsk(input: () -> String) = retryInput {
        val validatedInput = validator.validateInputYesOrNo(input())
        return@retryInput when (validatedInput) {
            Answer.YES -> true
            Answer.NO -> false
        }
    }
}