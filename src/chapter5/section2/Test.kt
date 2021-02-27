package chapter5.section2

import chapter2.section2.checkAscOrder
import chapter5.section1.getMSDData

fun testStringST(create: () -> StringST<Int>) {
    val st = create()
    check(st.isEmpty())
    check(st.size() == 0)

    val msdData = getMSDData()
    for (i in msdData.indices) {
        st.put(msdData[i], i)
    }
    check(!st.isEmpty())
    check(st.size() == 9)

    check(st.contains("by"))
    check(!st.contains("th"))
    check(!st.contains("shell"))
    check(st.get("by") == 3)
    check(st.get("seashells") == 12)

    check(st.keys().count() == st.size())
    // keys()方法返回的列表应该有序
    check(st.keys().checkAscOrder())

    st.delete("by")
    check(st.size() == 8)
    check(!st.contains("by"))
    check(st.keys().count() == st.size())

    check(st.longestPrefixOf("shellsccccc") == "shells")
    check(st.longestPrefixOf("shells") == "shells")
    check(st.longestPrefixOf("shell") == "she")
    check(st.longestPrefixOf("hello") == null)

    check(st.keysWithPrefix("sh").count() == 2)
    check(st.keysWithPrefix("se").count() == 3)
    check(st.keysWithPrefix("s").count() == 6)
    check(st.keysWithPrefix("a").count() == 1)
    check(st.keysWithPrefix("cell").count() == 0)
    check(st.keysWithPrefix("").count() == st.size())
    check(st.keysWithPrefix("s").checkAscOrder())

    check(st.keysThatMatch(".he").count() == 2)
    check(st.keysThatMatch("..e").count() == 3)
    check(st.keysThatMatch("sells").count() == 1)
    check(st.keysThatMatch("").count() == 0)
    check(st.keysThatMatch("ar").count() == 0)
    check(st.keysThatMatch("......").count() == 2)
    check(st.keysThatMatch("..e").checkAscOrder())

    // 符号表应该可以存放空字符串
    check(!st.contains(""))
    st.put("", 100)
    check(st.size() == 9)
    check(st.contains(""))
    check(st.get("") == 100)
    check(st.keys().count() == 9)
    check(st.keysWithPrefix("").count() == st.size())
    check(st.keysThatMatch("").count() == 1)

    println("StringST check succeed.")
}