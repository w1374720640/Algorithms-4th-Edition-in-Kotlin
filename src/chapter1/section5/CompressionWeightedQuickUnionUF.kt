package chapter1.section5

/**
 * 使用路径压缩的加权quick-union算法
 */
class CompressionWeightedQuickUnionUF(N: Int) : WeightedQuickUnionUF(N) {

    override fun find(p: Int): Int {
        var pId = p
        while (pId != ids[pId]) {
            pId = ids[pId]
        }
        var toBeCompressedId = p
        while (ids[toBeCompressedId] != pId) {
            toBeCompressedId = ids[toBeCompressedId]
            ids[toBeCompressedId] = pId
        }
        return pId
    }
}

fun main() {
    unionFindTest{ CompressionWeightedQuickUnionUF(it) }
}