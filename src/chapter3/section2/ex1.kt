package chapter3.section2

/**
 * 将 E A S Y Q U E S T I O N 作为键插入一个初始为空的二叉查找树中（方便起见设第i个键对应的值为i）
 * 画出生成的二叉查找树
 * 构造这颗树需要多少次比较？
 *
 * 解：使用BinaryTreeGraphics类绘制二叉树图形
 * 构造这棵树需要 E(0) + A(1) + S(1) + Y(2) + Q(2) + U(3) + E(1) + S(2) + T(4) + I(3) + O(4) + N(5) = 28次比较
 */
fun main() {
    val charArray = "EASYQUESTION".toCharArray()
    val binaryTreeST = BinaryTreeST<Char, Int>()
    for (i in charArray.indices) {
        binaryTreeST.put(charArray[i], i)
    }
    drawBinaryTree(binaryTreeST)
}
