package chapter3.section1

import extensions.inputPrompt
import extensions.readString

fun main() {
    inputPrompt()
    val st = SequentialSearchST<String, String>()
    st.put("A+", "4.33")
    st.put("A", "4.00")
    st.put("A-", "3.67")
    st.put("B+", "3.33")
    st.put("B", "3.00")
    st.put("B-", "2.67")
    st.put("C+", "2.33")
    st.put("C", "2.00")
    st.put("C-", "1.67")
    st.put("D", "1.00")
    st.put("F", "0.00")
    while (true) {
        try {
            val key = readString("key: ").toUpperCase()
            val value = st.get(key)
            if (value == null) {
                println("Invalid key!")
            } else {
                println("value: $value")
            }
        } catch (e: Exception) {
            break
        }
    }
}