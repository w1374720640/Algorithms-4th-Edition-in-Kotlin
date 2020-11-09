package chapter3.section1

import edu.princeton.cs.algs4.In
import extensions.inputPrompt
import extensions.readInt

fun ex9(input: In, minLength: Int, st: ST<String, Int>) {
    var lastWord = ""
    var lastCount = 0
    while (!input.isEmpty) {
        val word = input.readString()
        if (word.length >= minLength) {
            val count = st.get(word)
            if (count == null) {
                st.put(word, 1)
            } else {
                st.put(word, count + 1)
            }
            lastWord = word
            lastCount = if (count == null) 1 else count + 1
        }
    }
    println("lastWord=$lastWord lastCount=$lastCount")
}

fun main() {
    inputPrompt()
    val minLength = readInt("minLength=")
    val path = "./data/tale.txt"
    ex9(In(path), minLength, BinarySearchST())
}