package chapter1.section1

//课本中的二分查找原版
fun binarySearch(key: Int, array: IntArray): Int {
    var low = 0
    var high = array.size - 1
    while (low <= high) {
        val mid = (low + high) / 2
        when {
            key < array[mid] -> high = mid - 1
            key > array[mid] -> low = mid + 1
            else -> return mid
        }
    }
    return -1
}

inline fun <T> binarySearch(key: T, array: Array<T>, crossinline comparator: (T, T) -> Int): Int {
    var low = 0
    var high = array.size - 1
    while (low <= high) {
        val mid = (low + high) / 2
        val compareResult = comparator(key, array[mid])
        when {
            compareResult < 0 -> high = mid - 1
            compareResult > 0 -> low = mid + 1
            else -> return mid
        }
    }
    return -1
}