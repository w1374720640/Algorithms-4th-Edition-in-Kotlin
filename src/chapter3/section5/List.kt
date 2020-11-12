package chapter3.section5

/**
 * 列表数据类型的API
 */
interface List<Item: Any>: Iterable<Item> {
    /**
     * 将item添加到列表的头部
     */
    fun addFront(item: Item)

    /**
     * 将item添加到列表的尾部
     */
    fun addBack(item: Item)

    /**
     * 删除列表头部的元素
     */
    fun deleteFront(): Item

    /**
     * 删除列表尾部的元素
     */
    fun deleteBack(): Item

    /**
     * 从列表中删除item
     * 和书中方法签名不同，因为在kotlin中，如果泛型类型为Int时，两个delete()方法会签名相同无法匹配
     */
    fun deleteByItem(item: Item)

    /**
     * 将item添加为列表的第i个元素
     */
    fun add(i: Int, item: Item)

    /**
     * 从列表中删除第i个元素
     */
    fun deleteByIndex(i: Int)

    /**
     * 列表中是否存在元素item
     */
    fun contains(item: Item): Boolean

    /**
     * 列表是否为空
     */
    fun isEmpty(): Boolean

    /**
     * 列表中元素的总数
     */
    fun size(): Int
}