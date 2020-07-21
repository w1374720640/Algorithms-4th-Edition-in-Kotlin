package chapter1.section5

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
    //unionNum=512000之前只有较少的连接操作，速度很快，后面有大量连接操作，耗时急剧上升
    unionFindTest{ QuickUnionUF(it) }
}