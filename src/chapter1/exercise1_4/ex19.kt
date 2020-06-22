package chapter1.exercise1_4

/**
 * 给定一个含有N^2个不同整数的N*N数组，求数组的局部最小元素，且运行时间在最坏情况下与N成正比
 * 局部最小元素表示：a[i][j]<a[i+1][j] a[i][j]<a[i][j+1] a[i][j]<a[i-1][j] a[i][j]<a[i][j-1]
 * FIXME 在最坏情况下结果不与N成正比
 */
fun ex19(array: Array<Array<Int>>): Triple<Int, Int, Int> {
    require(array.isNotEmpty() && array[0].isNotEmpty())
    var x = 0
    var y = 0
    var count = 0
    while (true) {
        var minX = x
        var minY = y
        count++
        if (x > 0 && array[x - 1][y] < array[minX][minY]) {
            minX = x - 1
            minY = y
        }
        if (y > 0 && array[x][y - 1] < array[minX][minY]) {
            minX = x
            minY = y - 1
        }
        if (x < array.size - 1 && array[x + 1][y] < array[minX][minY]) {
            minX = x + 1
            minY = y
        }
        if (y < array.size - 1 && array[x][y + 1] < array[minX][minY]) {
            minX = x
            minY = y + 1
        }
        if (minX == x && minY == y) {
            return Triple(x, y, count)
        } else {
            x = minX
            y = minY
        }
    }
}

fun main() {
    val array = arrayOf(
            arrayOf(100, 99, 82, 81, 80),
            arrayOf(98, 97, 84, 83, 79),
            arrayOf(96, 95, 86, 85, 78),
            arrayOf(94, 93, 88, 87, 74),
            arrayOf(92, 91, 90, 89, 73))
    val triple = ex19(array)
    println("N = 5  count = ${triple.third}  i = ${triple.first}  j = ${triple.second}")
}