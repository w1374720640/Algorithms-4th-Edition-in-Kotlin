package chapter3.section3

/**
 * 在我们的标准索引测试用例中插入键P并画出插入的过程中每次变换（颜色转换或旋转）后的红黑树
 *
 * 解：标准索引测试按以下顺序依次插入： S E A R C H E X A M P L E
 */
fun main() {
    val array = "SEARCHEXAM".toCharArray()
    val bst = ShowChangeProcessRedBlackBST<Char, Int>()
    array.forEach {
        bst.put(it, 0)
    }
    bst.delay = 2000L
    bst.showProcess = true
    bst.draw()
    bst.put('P', 0)
    bst.drawText("end")
}
