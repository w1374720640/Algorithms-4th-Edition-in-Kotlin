package chapter3.section2

import chapter3.section1.testOrderedST
import extensions.formatDouble
import extensions.random
import extensions.randomBoolean
import kotlin.math.sqrt

/**
 * Hibbard删除方法的性能问题
 * 编写一个程序，从命令行接受一个参数N并构造一棵由N个随机键生成的二叉查找树，然后进入一个循环
 * 在循环中它先删除一个随机键（delete(select(StdRandom.uniform(N)))），然后再插入一个随机键，如此循环N²次
 * 循环结束后，计算并打印树的内部平均路径长度（内部路径长度除以N再加1）
 * 对于N=10²、10³和10⁴，运行你的程序来验证一个有些违反直觉的假设：
 * 这个过程会增加树的平均路径长度，增加的长度和N的平方根成正比
 * 使用能够随机选择前趋或后继结点的delete()方法重复这个实验
 *
 * 解：使用练习3.2.7中的avgCompares()方法计算树的内部平均路径长度
 */
fun ex42_HibbardDeletionDegradation(N: Int) {
    require(N > 0)
    val st = BinaryTreeST<Int, Int>()
    val nonHibbardST = NonHibbardBinaryTreeST<Int, Int>()

    repeat(N) {
        val value = random(Int.MAX_VALUE)
        st.put(value, 0)
        nonHibbardST.put(value, 0)
    }
    val sqrtN = sqrt(N.toDouble())
    println("N=$N  sqrt(N)=$sqrtN")
    val beforeAvgCompares = st.avgComparesDouble()
    val nonHibbardBeforeAvgCompares = st.avgComparesDouble()

    repeat(N * N) {
        val deleteIndex = random(st.size())
        val newKey = random(Int.MAX_VALUE)
        st.delete(st.select(deleteIndex))
        st.put(newKey, 0)
        nonHibbardST.delete(nonHibbardST.select(deleteIndex))
        nonHibbardST.put(newKey, 0)
    }
    val afterAvgCompares = st.avgComparesDouble()
    val nonHibbardAfterAvgCompares = nonHibbardST.avgComparesDouble()

    println("stage   Hibbard NonHibbard")
    println("before  ${formatDouble(beforeAvgCompares, 2, 7, true)} "
            + formatDouble(nonHibbardBeforeAvgCompares, 2, 10, true))
    println("after   ${formatDouble(afterAvgCompares, 2, 7, true)} "
            + formatDouble(nonHibbardAfterAvgCompares, 2, 10, true))
    println("diff    ${formatDouble(afterAvgCompares - beforeAvgCompares, 2, 7, true)} "
            + formatDouble(nonHibbardAfterAvgCompares - nonHibbardBeforeAvgCompares, 2, 10, true))
    println("ratio   ${formatDouble((afterAvgCompares - beforeAvgCompares) / sqrtN, 2, 7, true)} "
            + formatDouble((nonHibbardAfterAvgCompares - nonHibbardBeforeAvgCompares) / sqrtN, 2, 10, true))
    println()
}

class NonHibbardBinaryTreeST<K : Comparable<K>, V : Any> : BinaryTreeST<K, V>() {

    override fun delete(node: Node<K, V>, key: K): Node<K, V>? {
        when {
            node.key > key -> {
                if (node.left == null) {
                    throw NoSuchElementException()
                } else {
                    node.left = delete(node.left!!, key)
                    node.count = size(node.left) + size(node.right) + 1
                }
            }
            node.key < key -> {
                if (node.right == null) {
                    throw NoSuchElementException()
                } else {
                    node.right = delete(node.right!!, key)
                    node.count = size(node.left) + size(node.right) + 1
                }
            }
            else -> {
                return when {
                    node.left == null -> {
                        node.right
                    }
                    node.right == null -> {
                        node.left
                    }
                    else -> {
                        if (randomBoolean()) {
                            val rightMinNode = min(node.right!!)
                            rightMinNode.right = deleteMin(node.right!!)
                            rightMinNode.left = node.left
                            rightMinNode.count = size(rightMinNode.left) + size(rightMinNode.right) + 1
                            rightMinNode
                        } else {
                            val leftMaxNode = max(node.left!!)
                            leftMaxNode.left = deleteMax(node.left!!)
                            leftMaxNode.right = node.right
                            leftMaxNode.count = size(leftMaxNode.left) + size(leftMaxNode.right) + 1
                            leftMaxNode
                        }
                    }
                }
            }
        }
        return node
    }
}

fun main() {
    testOrderedST(NonHibbardBinaryTreeST())

    var N = 1000
    repeat(4) {
        ex42_HibbardDeletionDegradation(N)
        N *= 2
    }
}