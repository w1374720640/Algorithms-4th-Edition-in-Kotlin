package chapter5.section2

import chapter5.section1.Alphabet
import extensions.readInt
import extensions.readString
import extensions.safeCall

class TrieStringSET(alphabet: Alphabet = Alphabet.EXTENDED_ASCII) : StringSET {
    private val st = TrieST<String>(alphabet)

    companion object {
        private const val DEFAULT_VALUE = ""
    }

    override fun add(key: String) {
        st.put(key, DEFAULT_VALUE)
    }

    override fun delete(key: String) {
        st.delete(key)
    }

    override fun contains(key: String): Boolean {
        return st.contains(key)
    }

    override fun isEmpty(): Boolean {
        return st.isEmpty()
    }

    override fun size(): Int {
        return st.size()
    }

    override fun iterator(): Iterator<String> {
        return st.keys().iterator()
    }

    override fun containsPrefix(s: String): Boolean {
        val iterator = st.keysWithPrefix(s).iterator()
        return iterator.hasNext()
    }

    override fun toString(): String {
        return st.keys().joinToString()
    }
}

class ThreeWayStringSET : StringSET {
    private val st = TST<String>()

    companion object {
        private const val DEFAULT_VALUE = ""
    }

    override fun add(key: String) {
        st.put(key, DEFAULT_VALUE)
    }

    override fun delete(key: String) {
        st.delete(key)
    }

    override fun contains(key: String): Boolean {
        return st.contains(key)
    }

    override fun isEmpty(): Boolean {
        return st.isEmpty()
    }

    override fun size(): Int {
        return st.size()
    }

    override fun iterator(): Iterator<String> {
        return st.keys().iterator()
    }

    override fun containsPrefix(s: String): Boolean {
        val iterator = st.keysWithPrefix(s).iterator()
        return iterator.hasNext()
    }

    override fun toString(): String {
        return st.keys().joinToString()
    }
}

fun main() {
    println("Please input command:")
    println("0: exit, 1: add, 2: delete, 3: contains, 4: isEmpty, 5: size, 6: containPrefix, 7: toString")
    val set = TrieStringSET()
//    val set = ThreeWayStringSET()
    while (true) {
        safeCall {
            when (readInt("command: ")) {
                0 -> return
                1 -> {
                    val key = readString("add key: ")
                    set.add(key)
                    println(set)
                }
                2 -> {
                    val key = readString("delete key: ")
                    set.delete(key)
                    println(set)
                }
                3 -> {
                    val key = readString("contain key: ")
                    println(set.contains(key))
                }
                4 -> println("isEmpty: ${set.isEmpty()}")
                5 -> println("size=${set.size()}")
                6 -> println(set.containsPrefix(readString("containPrefix: ")))
                7 -> println(set)
            }
        }
    }

}