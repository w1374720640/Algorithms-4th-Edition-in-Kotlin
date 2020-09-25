package chapter3.section2

/**
 * 将 E A S Y Q U E S T I O N 作为键插入一个初始为空的二叉查找树中（方便起见设第i个键对应的值为i）
 * 画出生成的二叉查找树
 * 构造这颗树需要多少次比较？
 */
fun main() {
    val charArray = "EASYQUESTION".toCharArray()
    val binaryTreeST = BinaryTreeST<Char, Int>()
    for (i in charArray.indices) {
        binaryTreeST.put(charArray[i], i)
    }
    val graphics = BinaryTreeGraphics(binaryTreeST, true)
    graphics.showKey = true
    graphics.showValue = true
    graphics.draw()
}
