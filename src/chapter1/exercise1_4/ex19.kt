package chapter1.exercise1_4

import extensions.random

/**
 * 给定一个含有N^2个不同整数的N*N数组，求矩阵的局部最小元素，且运行时间在最坏情况下与N成正比
 * 局部最小元素表示：a[i][j]<a[i+1][j] a[i][j]<a[i][j+1] a[i][j]<a[i-1][j] a[i][j]<a[i][j-1]
 * 边界上的点只需要和存在的点比较，所以肯定有局部最小元素
 */
/**
 * 这个方法简单，大多数情况下效率较高，但是在最坏不与N成正比，属于不稳定算法
 * 从左上角开始，依次与周围的点对比，值最小的点为下一个判断点，直到该点比周围所有点都小
 */
fun ex19a(array: Array<Array<Int>>): Triple<Int, Int, Int> {
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

/**
 * 这个方法不是很好理解，但是属于稳定算法，即使最坏情况下，也能保证和N成正比
 * 首先，在中间行搜索最小值，再将最小值与其上下两个元素比较，
 * 如果不满足题意，将取值范围限制在较小的一侧，矩阵被分为两半（上下两侧）。
 * 在较小的一侧，找到中间列的最小值，再将最小值与其左右两个元素比较，
 * 如果不满足题意，将取值范围限制在较小的一侧，矩阵被分为左右两侧
 * 查找范围缩小为原来矩阵的四分之一，递归操作，即可得到答案
 */
fun ex19b(array: Array<Array<Int>>): Triple<Int, Int, Int> {
    require(array.isNotEmpty() && array[0].isNotEmpty())
    var startX = 0
    var endX = array[0].size - 1
    var startY = 0
    var endY = array.size - 1

    var count = 0

    fun findMinInRow(row: Int): Int {
        var minIndex = startX
        for (i in startX + 1..endX) {
            count++
            if (array[row][i] < array[row][minIndex]) {
                minIndex = i
            }
        }
        return minIndex
    }

    fun findMinInColumn(column: Int): Int {
        var minIndex = startY
        for (i in startY + 1..endY) {
            count++
            if (array[i][column] < array[minIndex][column]) {
                minIndex = i
            }
        }
        return minIndex
    }

    fun isRowValid(row: Int) = row in startY..endY

    fun isColumnValid(column: Int) = column in startX..endX

    fun findLocalMinimum(): Pair<Int, Int> {
        val row = (startY + endY) / 2
        val rowMinColumn = findMinInRow(row)
        when {
            !isRowValid(row - 1) && !isRowValid(row + 1) -> return row to rowMinColumn
            !isRowValid(row - 1) -> {
                if (array[row][rowMinColumn] < array[row + 1][rowMinColumn]) {
                    return row to rowMinColumn
                } else {
                    startY = row + 1
                }
            }
            !isRowValid(row + 1) -> {
                if (array[row][rowMinColumn] < array[row - 1][rowMinColumn]) {
                    return row to rowMinColumn
                } else {
                    endY = row - 1
                }
            }
            else -> {
                if (array[row - 1][rowMinColumn] < array[row + 1][rowMinColumn]) {
                    endY = row
                } else {
                    startY = row
                }
            }
        }
        val column = (startX + endX) / 2
        val columnMinRow = findMinInColumn(column)
        when {
            !isColumnValid(column - 1) && !isColumnValid(column + 1) -> return columnMinRow to rowMinColumn
            !isColumnValid(column - 1) -> {
                if (array[columnMinRow][column] < array[columnMinRow][column + 1]) {
                    return columnMinRow to column
                } else {
                    startX = column + 1
                }
            }
            !isColumnValid(column + 1) -> {
                if (array[columnMinRow][column] < array[columnMinRow][column - 1]) {
                    return columnMinRow to column
                } else {
                    endX = column - 1
                }
            }
            else -> {
                if (array[columnMinRow][column - 1] < array[columnMinRow][column + 1]) {
                    endX = column
                } else {
                    startX = column
                }
            }
        }
        return findLocalMinimum()
    }

    val pair = findLocalMinimum()
    return Triple(pair.first, pair.second, count)
}

fun main() {
    val array1 = arrayOf(
            arrayOf(100, 99, 82, 81, 80),
            arrayOf(98, 97, 84, 83, 79),
            arrayOf(96, 95, 86, 85, 78),
            arrayOf(94, 93, 88, 87, 74),
            arrayOf(92, 91, 90, 89, 73))
    val array2 = arrayOf(
            arrayOf(119, 118, 117, 116, 115),
            arrayOf(114, 113, 104, 112, 111),
            arrayOf(100, 99, 98, 101, 102),
            arrayOf(110, 106, 97, 95, 107),
            arrayOf(109, 108, 105, 94, 93)
    )
    val array3 = Array(5) { Array(5) { random(100) } }
    val array = array1
    val tripleA = ex19a(array)
    val tripleB = ex19b(array)
    array.forEach {
        println(it.joinToString())
    }
    println()
    println("ex19a count = ${tripleA.third}  i = ${tripleA.first}  j = ${tripleA.second} array[i][j] = ${array[tripleA.first][tripleA.second]}")
    println("ex19b count = ${tripleB.third}  i = ${tripleB.first}  j = ${tripleB.second} array[i][j] = ${array[tripleB.first][tripleB.second]}")
}