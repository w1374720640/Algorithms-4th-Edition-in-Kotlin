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

/**
 * 测试可重复键符号表
 */
fun testDuplicateKeysST(st: DuplicateKeysST<Int, String>) {
    require(st.isEmpty())
    st.put(222, "aaa")
    st.put(333, "bbb")
    st.put(111, "ccc")

    check(!st.isEmpty())
    check(st.size() == 3)
    check(st.contains(111))
    check(st.get(222) == "aaa")
    check(st.get(123) == null)

    st.delete(222)
    check(st.size() == 2)
    check(st.get(222) == null)

    st.put(444, "ddd")
    st.put(111, "eee")
    check(st.size() == 3)

    // 键111对应多个值
    check(st.get(111) == "ccc" || st.get(111) == "eee")
    val iterator = st.getAllValues(111)?.iterator()
    check(iterator != null)
    var valueNum = 0
    while (iterator.hasNext()) {
        when (iterator.next()) {
            "ccc", "eee" -> valueNum++
            else -> throw IllegalStateException()
        }
    }
    check(valueNum == 2)

    val keys = st.keys()
    var count = 0
    keys.forEach {
        when (it) {
            111 -> {
                count++
            }
            333 -> {
                check(st.get(it) == "bbb")
                count++
            }
            444 -> {
                check(st.get(it) == "ddd")
                count++
            }
            else -> {
                throw IllegalStateException("Check failed.")
            }
        }
    }
    check(count == 3)

    st.delete(111)
    check(st.get(111) == null && st.getAllValues(111) == null)
    st.delete(333)
    st.delete(444)
    check(st.isEmpty())

    println("DuplicateKeysST check succeed.")
}


fun testMathSET(createSET: () -> MathSET<Int>) {
    val set = createSET()
    testSET(set)

    val a = intArrayOf(1, 3, 5, 7, 9)
    val b = intArrayOf(0, 2, 4, 6, 8)
    val c = IntArray(10) { it }
    val setB = createSET()
    val setC = createSET()
    a.forEach { set.add(it) }
    b.forEach { setB.add(it) }
    c.forEach { setC.add(it) }

    check(set.complement(setB).sameAs(setB))
    check(set.complement(setC).sameAs(setB))
    check(setB.complement(setC).sameAs(set))

    check(set.union(setB).sameAs(setC))
    check(set.union(setC).sameAs(setC))
    check(setB.union(setC).sameAs(setC))

    check(set.intersection(setB).isEmpty())
    check(set.intersection(setC).sameAs(set))
    check(setB.intersection(setC).sameAs(setB))

    println("MathSET check succeed.")
}

fun <K : Any> SET<K>.sameAs(other: SET<K>): Boolean {
    if (this.size() != other.size()) return false
    this.keys().forEach {
        if (!other.contains(it)) {
            return false
        }
    }
    return true
}
