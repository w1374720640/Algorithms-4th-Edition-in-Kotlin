package chapter3.section2

/**
 * 编写一个静态方法optCompares()，接受一个整型参数N并计算一颗最优（完美平衡的）二叉查找树中的
 * 一次随机查找命中平均所需的比较次数，
 * 如果树中的链接数量为2的幂，那么所有的空链接都应该在同一层，否则则分布在最底部的两层中。
 *
 * 解：第i层的一个元素需要比较i次才可以查找命中（根结点为第一层），
 * i不断自增，将每层所有元素需要比较的次数相加，直到完全二叉树的总数大于等于N
 */
fun optCompares(N: Int): Int {
    if (N <= 0) return 0
    //第i层的结点数量
    var compares = 0
    //当前二叉树的总结点数
    var count = 0
    //结点的层级，等于命中查找需要的比较次数
    var i = 1
    while (true) {
        //增加一层后，总结点数量为原结点数量的两倍加一
        val maxCount = count * 2 + 1
        if (maxCount >= N) {
            //最后一层未填满或刚好填满
            compares += (N - count) * i
            count = maxCount
            break
        } else {
            //最后一层填满后仍然后剩余
            compares += (maxCount - count) * i
            count = maxCount
            i++
        }
    }
    return compares / N
}

fun main() {
    val checkArray = arrayOf(
            1 to 1,
            3 to 1,
            7 to 2,
            4 to 2,
            15 to 3,
            17 to 3,
            27 to 4
    )
    checkArray.forEach {
        check(optCompares(it.first) == it.second)
    }
    println("check succeed.")
}