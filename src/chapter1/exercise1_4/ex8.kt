package chapter1.exercise1_4

//暴力计算数组中相等的整数对数量
fun ex8a(array: IntArray): Long {
    var count = 0L
    for (i in array.indices) {
        for (j in i + 1 until array.size) {
            if (array[i] == array[j]) {
                count++
            }
        }
    }
    return count
}

//优化后的方法
//先排序，再统计每个数字的重复次数，再用组合公式计算组合数
fun ex8b(array: IntArray): Long {
    //计算从total数量数据中取select个数据的组合数
    fun numOfCombinations(total: Int, select: Int): Long {
        require(total >= select)
        //分母
        var denominator = 1L
        //分子
        var molecule = 1L
        repeat(select) {
            denominator *= total - it
            molecule *= it + 1
        }
        return denominator / molecule
    }
    array.sort()
    var count = 0L
    var index = 0
    while (index < array.size) {
        val value = array[index]
        var equalNum = 1
        while (index + equalNum < array.size && array[index + equalNum] == value) {
            equalNum++
        }
        if (equalNum > 1) {
            count += numOfCombinations(equalNum, 2)
        }
        index += equalNum
    }
    return count
}

fun main() {
    println("fast method:")
    timeRatio(path = "./data/largeT.txt") { ex8b(it) }
    println("slow method:")
    timeRatio(path = "./data/largeT.txt") { ex8a(it) }
}
