package chapter2.section5

/**
 * 编写一个Vector数据类型并将d维整型向量排序
 * 排序方法是先按照一维数字排序，一维数字相同的向量则按照二维数字排序，再相同的向量则按照三位数字排序...
 */
class Vector(val array: Array<Int>) : Comparable<Vector> {
    override fun compareTo(other: Vector): Int {
        for (i in array.indices) {
            val compareResult = array[i].compareTo(other.array[i])
            if (compareResult != 0) return compareResult
        }
        return 0
    }
}