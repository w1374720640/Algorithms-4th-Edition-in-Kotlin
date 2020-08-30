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

fun <T : Comparable<T>> binarySearch(key: T, array: Array<T>): Int {
    var low = 0
    var high = array.size - 1
    while (low <= high) {
        val mid = (low + high) / 2
        val compareResult = key.compareTo(array[mid])
        when {
            compareResult < 0 -> high = mid - 1
            compareResult > 0 -> low = mid + 1
            else -> return mid
        }
    }
    return -1
}

/**
 * 自定义比较规则进行二分查找，要求数组排序时也使用相同的比较规则
 * 通常用不可比较对象中的某个可比较属性比较大小
 * 例如对于不同学生，用学号作为比较方式，学生可以不实现Comparable接口，学号可以比较就行，属于委托
 */
fun <T, R : Comparable<R>> binarySearchBy(key: T, array: Array<T>, selector: (T) -> R?): Int {
    return binarySearchWith(key, array, compareBy(selector))
}

/**
 * 自定义比较规则进行二分查找，要求数组排序时也使用相同的比较规则
 * 直接定义如何比较两个对象
 */
fun <T> binarySearchWith(key: T, array: Array<T>, comparator: Comparator<in T>): Int {
    var low = 0
    var high = array.size - 1
    while (low <= high) {
        val mid = (low + high) / 2
        val compareResult = comparator.compare(key, array[mid])
        when {
            compareResult < 0 -> high = mid - 1
            compareResult > 0 -> low = mid + 1
            else -> return mid
        }
    }
    return -1
}