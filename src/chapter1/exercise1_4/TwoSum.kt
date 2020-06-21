package chapter1.exercise1_4

fun twoSum(array: IntArray): Long {
    var count = 0L
    for (i in array.indices) {
        for (j in i + 1 until array.size) {
            if (array[i] + array[j] == 0) {
                count++
            }
        }
    }
    return count
}

fun main() {
    timeRatio { twoSum(it) }
}