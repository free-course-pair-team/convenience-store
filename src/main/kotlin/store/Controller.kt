package store

import store.domain.ItemManager
import store.domain.Membership
import store.domain.PromotionManager
import store.domain.ShoppingCartFactory
import store.model.Answer
import store.model.Receipt
import store.model.ShoppingCart
import store.model.Stock
import store.util.FileReader
import store.util.Validator
import store.util.retryInput
import store.view.InputView
import store.view.OutputView
import java.time.LocalDate

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
        val shoppingCart = ShoppingCartFactory().generate(validatedProductAndQuantity, stock)
        askPresentPromotionItem(shoppingCart, itemManager)

        askBuyNotApplyPromotionItem(shoppingCart) { name: String, quantity: Int ->
            inputView.inputAskAddNotApplyPromotionItem(name, quantity)
        }
        val membershipDiscountAmount = askTakeMembership(shoppingCart)
        println(Receipt().showReceipt(membershipDiscountAmount, shoppingCart))
    }

    private fun askTakeMembership(shoppingCart: ShoppingCart): Int {
        val answer = inputRetryAsk { inputView.inputAskTakeMembership() }
        if (answer) return membership.getMemberShipDiscountAmount(shoppingCart)
        return 0
    }

    private fun askBuyNotApplyPromotionItem(
        shoppingCart: ShoppingCart,
        input: (name: String, quantity: Int) -> String,
    ) {
        for (item in shoppingCart.getPromotionItems()) {
            if (promotionManager.isExistApplyPromotionProductQuantity(item).not()) continue
            val notApplyItemsQuantity = shoppingCart.getNotApplyItemsQuantity(item)
            val answer = inputRetryAsk {
                input(item.name(), notApplyItemsQuantity[0] + notApplyItemsQuantity[1])
            }
            if (answer.not()) {
                shoppingCart.takeOutNotApplyPromotionQuantity(item.name(), notApplyItemsQuantity)
            }
        }
    }

    private fun askPresentPromotionItem(shoppingCart: ShoppingCart, itemManager: ItemManager) {
        val canAddOfferPromotionProduct =
            promotionManager.getCanAddOfferPromotionProduct(
                shoppingCart.getPromotionItems(),
                itemManager
            )
        val acceptAddPromotionProduct = canAddOfferPromotionProduct.filter {
            inputRetryAsk { inputView.inputAskAddPromotionItem(it.first.name(), it.second) }
        }
        shoppingCart.addPromotionItemQuantity(acceptAddPromotionProduct)
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