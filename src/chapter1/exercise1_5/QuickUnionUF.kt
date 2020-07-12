package chapter1.exercise1_5

open class QuickUnionUF(N: Int) : UF {
    protected val ids = IntArray(N) { it }
    protected var count = N

    override fun union(p: Int, q: Int) {
        val pRoot = find(p)
        val qRoot = find(q)
        if (pRoot != qRoot) {
            count--
            ids[pRoot] = qRoot
        }
    }

    override fun find(p: Int): Int {
        var pIndex = p
        while (pIndex != ids[pIndex]) {
            pIndex = ids[pIndex]
        }
        return pIndex
    }

    override fun connected(p: Int, q: Int): Boolean {
        return find(p) == find(q)
    }

    override fun count(): Int {
        return count
    }
}

fun main() {
    val action: (Int) -> UF = { QuickUnionUF(it) }
    unionFindTest(1, action)
    unionFindTest(2, action)
    //同样需要等很久很久很久...
    unionFindTest(3, action)
}