package capter1.exercise1_1

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
