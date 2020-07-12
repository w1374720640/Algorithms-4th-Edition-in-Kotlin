package chapter1.exercise1_5

class CompressionWeightedQuickUnionUF(N: Int) : WeightedQuickUnionUF(N) {

    override fun find(p: Int): Int {
        var pId = p
        while (pId != ids[pId]) {
            pId = ids[pId]
        }
        if (ids[p] != pId) {
            ids[p] = pId
        }
        return pId
    }
}

fun main() {
    val action: (Int) -> UF = { CompressionWeightedQuickUnionUF(it) }
    unionFindTest(1, action)
    unionFindTest(2, action)
    unionFindTest(3, action)
}