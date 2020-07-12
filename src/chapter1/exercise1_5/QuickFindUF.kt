package chapter1.exercise1_5

class QuickFindUF(N: Int) : UF {
    private val ids: IntArray = IntArray(N) { it }
    private var count = N

    override fun union(p: Int, q: Int) {
        val pRoot = find(p)
        val qRoot = find(q)
        if (pRoot != qRoot) {
            count--
            ids.forEachIndexed { index, value ->
                if (value == pRoot) {
                    ids[index] = qRoot
                }
            }
        }
    }

    override fun find(p: Int): Int {
        return ids[p]
    }

    override fun connected(p: Int, q: Int): Boolean {
        return ids[p] == ids[q]
    }

    override fun count(): Int {
        return count
    }
}

fun main() {
    val action: (Int) -> UF = { QuickFindUF(it) }
    unionFindTest(1, action)
    unionFindTest(2, action)
    //需要等很久很久很久...
    unionFindTest(3, action)
}