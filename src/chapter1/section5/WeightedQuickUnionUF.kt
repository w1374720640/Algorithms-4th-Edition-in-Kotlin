package chapter1.section5

open class WeightedQuickUnionUF(N: Int) : QuickUnionUF(N) {
    val treeSize = IntArray(N) { 1 }

    override fun union(p: Int, q: Int) {
        val pRoot = find(p)
        val qRoot = find(q)
        if (pRoot != qRoot) {
            count--
            if (treeSize[pRoot] <= treeSize[qRoot]) {
                ids[pRoot] = qRoot
                treeSize[qRoot] += treeSize[pRoot]
            } else {
                ids[qRoot] = pRoot
                treeSize[pRoot] += treeSize[qRoot]
            }
        }
    }
}

fun main() {
    unionFindTest{ WeightedQuickUnionUF(it) }
}