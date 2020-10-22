package chapter3.section3

/**
 * 真假判断：如果你按照升序将键顺序插入一颗红黑树中，树的高度是单调递增的
 */
fun main() {
    val bst = RedBlackBST<Int, Int>()
    val array = IntArray(100){it}
    var result = true
    var preHeight = 0
    array.forEach {
        bst.put(it, 0)
        val height = bst.height()
        if (height < preHeight) {
            result = false
        }
        preHeight = height
    }
    println("result=$result")
}