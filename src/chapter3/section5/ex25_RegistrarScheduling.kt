package chapter3.section5

/**
 * 登记员的日程安排
 * 东北部某著名大学的注册主任最近作出的安排中有一位老师需要在同一时间为两个不同的班级授课
 * 请用一种方法来检查类似的冲突，帮助这位主任不要再犯相同的错误
 * 简单起见，假设每节课的时间为50分钟，分别从9:00、10:00、11:00、1:00、2:00和3:00开始
 *
 * 解：先将时间转换为24小时制，再转换成分钟数，每节课用区间表示
 * 将课程列表按开始时间排序，依次判断相邻的两个区间是否相交，相交则出现冲突
 */
fun ex25_RegistrarScheduling(rangeArray: Array<IntRange>): Boolean {
    rangeArray.sortBy { it.first }
    for (i in 0..rangeArray.size - 2) {
        val firstRange = rangeArray[i]
        val secondRange = rangeArray[i + 1]
        if (firstRange.first in secondRange
                || firstRange.last in secondRange
                || secondRange.first in firstRange
                || secondRange.last in firstRange) {
            return false
        }
    }
    return true
}

/**
 * 获取每节课程的区间，[startTime]为24小时制下的小时数，[duration]表示每节课的时长，单位为分钟
 */
private fun getTimeRange(startTime: Int, duration: Int = 50): IntRange {
    val start = startTime * 60
    return start..(start + duration)
}

fun main() {
    val array1 = arrayOf(getTimeRange(9), getTimeRange(10), getTimeRange(3), getTimeRange(1))
    println(ex25_RegistrarScheduling(array1))
    val array2 = arrayOf(getTimeRange(10), getTimeRange(2), getTimeRange(9), getTimeRange(10))
    println(ex25_RegistrarScheduling(array2))
}