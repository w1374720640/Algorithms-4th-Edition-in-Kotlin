package chapter5.section2

import chapter5.section1.Alphabet
import extensions.inputPrompt
import extensions.random
import extensions.readInt

/**
 * 随机电话号码
 * 编写一个TrieST的用例（R=10），从命令行接受一个int值N并打印出N个形如 (xxx) xxx-xxxx的随机电话号码。
 * 使用符号表避免出现重复的号码。使用本书网站上的AreaCodes.txt来避免打印出不存在的区号。
 */
fun ex19_RandomPhoneNumbers(N: Int): Array<String> {
    val st = TrieST<Int>(Alphabet.DECIMAL)
    while (st.size() < N) {
        val stringBuilder = StringBuilder()
        repeat(10) {
            stringBuilder.append(random(10))
        }
        val phoneNum = stringBuilder.toString()
        // 这里没有检查区号，如果需要检查区号，将区号加入白名单，判断前三位是否在白名单中，不是则直接返回
        st.put(phoneNum, 0)
    }
    val array = Array(N) { "" }
    var i = 0
    st.keys().forEach {
        array[i++] = "(${it.substring(0, 3)}) ${it.substring(3, 6)}-${it.substring(6, 10)}"
    }
    return array
}

fun main() {
    inputPrompt()
    val N = readInt("N=")
    println(ex19_RandomPhoneNumbers(N).joinToString(separator = "\n"))
}