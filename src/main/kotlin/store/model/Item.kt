package store.model

interface Item {
    fun itemMessage(): String
    fun name(): String

    fun quantity(): Int
    fun price(): Int

    fun addQuantity(q: Int)

    fun takeOutQuantity(q: Int)

    fun setQuantity(q: Int): Item
}
