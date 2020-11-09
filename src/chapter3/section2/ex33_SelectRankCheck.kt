package chapter3.section2

/**
 * 选择/排名检查
 * 编写一个方法，对于0到size()-1之间的所有i，检查i和rank(select(i))是否相等
 * 并检查二叉查找树中的任意键key和select(rank(key))是否相等
 */
fun <K : Comparable<K>> checkRankAndSelect(st: BinarySearchTree<K, *>): Boolean {
    for (i in 0 until st.size()) {
        if (st.rank(st.select(i)) != i) return false
    }
    st.keys().forEach { key ->
        if (st.select(st.rank(key)) != key) return false
    }
    return true
}

fun main() {
    val charArray = "EASYQUESTION".toCharArray()
    val bst = BinarySearchTree<Char, Int>()
    for (i in charArray.indices) {
        bst.put(charArray[i], i)
    }
    println(checkRankAndSelect(bst))
}