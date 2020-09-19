package chapter3.section1

/**
 * 测试对象是否正确实现了ST接口
 */
fun testST(st: ST<Int, String>) {
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
    check(st.get(111) == "eee")
    check(st.size() == 3)

    val keys = st.keys()
    var count = 0
    keys.forEach {
        when (it) {
            111 -> {
                check(st.get(it) == "eee")
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
    st.delete(333)
    st.delete(444)
    check(st.isEmpty())

    println("ST check succeed.")
}

/**
 * 测试对象是否正确实现了OrderedST接口
 * 会先调用testST方法测试ST接口，测试成功后不重复测试ST接口
 */
fun testOrderedST(orderedST: OrderedST<Int, String>) {
    testST(orderedST)
    require(orderedST.isEmpty())
    orderedST.put(222, "aaa")
    orderedST.put(333, "bbb")
    orderedST.put(111, "ccc")

    check(orderedST.min() == 111)
    check(orderedST.max() == 333)

    check(orderedST.floor(123) == 111)
    check(orderedST.floor(222) == 222)
    check(orderedST.floor(555) == 333)
    needToThrowSpecifiedException<NoSuchElementException> { orderedST.floor(100) }

    check(orderedST.ceiling(123) == 222)
    check(orderedST.ceiling(222) == 222)
    check(orderedST.ceiling(100) == 111)
    needToThrowSpecifiedException<NoSuchElementException> { orderedST.ceiling(555) }

    check(orderedST.rank(100) == 0)
    check(orderedST.rank(111) == 0)
    check(orderedST.rank(123) == 1)
    check(orderedST.rank(333) == 2)
    check(orderedST.rank(555) == 3)

    check(orderedST.select(0) == 111)
    check(orderedST.select(2) == 333)
    needToThrowSpecifiedException<NoSuchElementException> { orderedST.select(3) }

    orderedST.deleteMin()
    check(orderedST.size() == 2)
    check(orderedST.get(111) == null)

    orderedST.deleteMax()
    check(orderedST.size() == 1)
    check(orderedST.get(333) == null)

    orderedST.put(444, "ddd")
    orderedST.put(555, "eee")
    check(orderedST.size() == 3)
    check(orderedST.max() == 555)
    check(orderedST.min() == 222)

    check(orderedST.size(100, 600) == 3)
    check(orderedST.size(222, 444) == 2)
    check(orderedST.size(222, 234) == 1)
    check(orderedST.size(333, 222) == 0)

    var count = 0
    orderedST.keys(200, 500).forEach {
        when (count) {
            0 -> {
                check(it == 222)
                count++
            }
            1 -> {
                check(it == 444)
                count++
            }
            else -> {
                throw IllegalStateException("Check failed.")
            }
        }
    }
    check(count == 2)

    orderedST.deleteMax()
    orderedST.deleteMin()
    orderedST.deleteMax()
    check(orderedST.isEmpty())

    println("OrderedST check succeed.")
}

/**
 * action表达式需要抛出指定异常，如果没抛出异常或抛出其他异常，整个函数反而会抛出IllegalStateException
 */
inline fun <reified T : Exception> needToThrowSpecifiedException(action: () -> Unit) {
    var checkSucceed = false
    try {
        action()
    } catch (e: Exception) { //泛型T不能用在catch参数中，即使用reified修饰也不行
        if (e is T) {
            checkSucceed = true
        }
    }
    if (!checkSucceed) {
        throw IllegalStateException("Check failed.")
    }
}
