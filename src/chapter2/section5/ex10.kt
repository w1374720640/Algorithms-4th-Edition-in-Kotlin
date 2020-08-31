package chapter2.section5

/**
 * 创建一个数据类型Version来表示软件的版本，例如115.1.1、115.10.1、115.10.2
 */
class Version(val version: String) : Comparable<Version> {
    init {
        //检查输入的version参数是否符合格式，格式不对抛出异常
        val list = version.split(".")
        require(list.size == 3)
        list.forEach {
            //不是Int格式会抛出异常
            it.toInt()
        }
    }

    override fun compareTo(other: Version): Int {
        val numList = version.split(".").map { it.toInt() }
        val otherNumList = other.version.split(".").map { it.toInt() }
        return when {
            numList[0] != otherNumList[0] -> numList[0].compareTo(otherNumList[0])
            numList[1] != otherNumList[1] -> numList[1].compareTo(otherNumList[1])
            else -> numList[2].compareTo(otherNumList[2])
        }
    }

    override fun toString(): String {
        return "version:$version"
    }
}

fun main() {
    val array = arrayOf(Version("115.1.1"), Version("115.10.1"), Version("115.10.2"))
    array.sort()
    array.forEach {
        println(it)
    }
}