package chapter3.section5

import chapter3.section1.needToThrowSpecifiedException

/**
 * 测试对象是否正确实现了ST接口
 */
fun testSET(set: SET<Int>) {
    require(set.isEmpty())
    set.add(222)
    set.add(333)
    set.add(111)

    check(!set.isEmpty())
    check(set.size() == 3)
    check(set.contains(111))
    check(!set.contains(123))

    set.delete(222)
    check(set.size() == 2)
    check(!set.contains(222))

    set.add(444)
    set.add(111)
    check(set.contains(111))
    check(set.size() == 3)

    val keys = set.keys()
    var count = 0
    keys.forEach {
        when (it) {
            111, 333, 444 -> {
                check(set.contains(it))
                count++
            }
            else -> {
                throw IllegalStateException("Check failed.")
            }
        }
    }
    check(count == 3)

    set.delete(111)
    set.delete(333)
    set.delete(444)
    check(set.isEmpty())

    println("SET check succeed.")
}

/**
 * 测试对象是否正确实现了OrderedST接口
 * 会先调用testST方法测试ST接口，测试成功后不重复测试ST接口
 */
fun testOrderedSET(orderedSET: OrderedSET<Int>) {
    testSET(orderedSET)
    require(orderedSET.isEmpty())
    orderedSET.add(222)
    orderedSET.add(333)
    orderedSET.add(111)

    check(orderedSET.min() == 111)
    check(orderedSET.max() == 333)

    check(orderedSET.floor(123) == 111)
    check(orderedSET.floor(222) == 222)
    check(orderedSET.floor(555) == 333)
    needToThrowSpecifiedException<NoSuchElementException> { orderedSET.floor(100) }

    check(orderedSET.ceiling(123) == 222)
    check(orderedSET.ceiling(222) == 222)
    check(orderedSET.ceiling(100) == 111)
    needToThrowSpecifiedException<NoSuchElementException> { orderedSET.ceiling(555) }

    check(orderedSET.rank(100) == 0)
    check(orderedSET.rank(111) == 0)
    check(orderedSET.rank(123) == 1)
    check(orderedSET.rank(333) == 2)
    check(orderedSET.rank(555) == 3)

    check(orderedSET.select(0) == 111)
    check(orderedSET.select(2) == 333)
    needToThrowSpecifiedException<NoSuchElementException> { orderedSET.select(3) }

    orderedSET.deleteMin()
    check(orderedSET.size() == 2)
    check(!orderedSET.contains(111))

    orderedSET.deleteMax()
    check(orderedSET.size() == 1)
    check(!orderedSET.contains(333))

    orderedSET.add(444)
    orderedSET.add(555)
    check(orderedSET.size() == 3)
    check(orderedSET.max() == 555)
    check(orderedSET.min() == 222)

    check(orderedSET.size(100, 600) == 3)
    check(orderedSET.size(222, 444) == 2)
    check(orderedSET.size(222, 234) == 1)
    check(orderedSET.size(333, 222) == 0)

    var count = 0
    orderedSET.keys(200, 500).forEach {
        when (count) {
            0, 1 -> {
                orderedSET.contains(it)
                count++
            }
            else -> {
                throw IllegalStateException("Check failed.")
            }
        }
    }
    check(count == 2)

    orderedSET.deleteMax()
    orderedSET.deleteMin()
    orderedSET.deleteMax()
    check(orderedSET.isEmpty())

    println("OrderedSET check succeed.")
}
