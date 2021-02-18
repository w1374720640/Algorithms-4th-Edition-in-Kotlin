package chapter5.section1

import extensions.formatStringLength

class StudentGroup(val name: String, val group: Int) : Key {

    override fun key(): Int {
        return group
    }

    override fun toString(): String {
        return "[${formatStringLength(name, 10, alignLeft = true)} $group]"
    }
}

/**
 * 返回学生分组列表和最大的组号
 */
fun getStudentGroup(): Pair<Array<Key>, Int> {
    val array = arrayOf<Key>(
            StudentGroup("Anderson", 2),
            StudentGroup("Brown", 3),
            StudentGroup("Davis", 3),
            StudentGroup("Garcia", 4),
            StudentGroup("Harris", 1),
            StudentGroup("Jackson", 3),
            StudentGroup("Johnson", 4),
            StudentGroup("Jones", 3),
            StudentGroup("Martin", 1),
            StudentGroup("Martinez", 2),
            StudentGroup("Miller", 2),
            StudentGroup("Moore", 1),
            StudentGroup("Robinson", 2),
            StudentGroup("Smith", 4),
            StudentGroup("Taylor", 3),
            StudentGroup("Thomas", 4),
            StudentGroup("Thompson", 4),
            StudentGroup("White", 2),
            StudentGroup("Williams", 3),
            StudentGroup("Wilson", 4)
    )
    return array to 5
}