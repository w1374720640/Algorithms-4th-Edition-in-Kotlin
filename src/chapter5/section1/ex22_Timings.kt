package chapter5.section1

import chapter2.section2.topDownMergeSort
import chapter2.section3.quickSort
import extensions.shuffle
import extensions.spendTimeMillis

/**
 * 运行时间
 * 使用多种键生成器比较高位优先的字符串排序与三向字符串快速排序的运行时间。
 * 对于定长的键，在比较中加入低位优先的字符串排序算法。
 *
 * 解：低位优先字符串排序使用练习5.1.9实现的算法
 * 还额外加入自顶向下的归并排序和标准快速排序做对比
 */
fun ex22_Timings(array: Array<String>) {
    array.shuffle()
    val lsdTime = spendTimeMillis {
        ex9.sort(array)
    }
    println("lsdTime:    $lsdTime ms")
    array.shuffle()
    val msdTime = spendTimeMillis {
        MSD.sort(array)
    }
    println("msdTime:    $msdTime ms")
    array.shuffle()
    val quick3Time = spendTimeMillis {
        Quick3String.sort(array)
    }
    println("quick3Time: $quick3Time ms")
    array.shuffle()
    val mergeSortTime = spendTimeMillis {
        topDownMergeSort(array)
    }
    println("mergeSortTime: $mergeSortTime ms")
    array.shuffle()
    val quickSortTime = spendTimeMillis {
        quickSort(array)
    }
    println("quickSortTime: $quickSortTime ms")
}

fun main() {
    var N = 10000
    val W = 10
    repeat(3) {
        println("N=$N")
        println("decimalArray:")
        val decimalArray = ex18_RandomDecimalKeys(N, W)
        ex22_Timings(decimalArray)

        println("platesArray:")
        val platesArray = ex19_RandomCALicensePlates(N)
        ex22_Timings(platesArray)

        println("wordsArray:")
        val wordsArray = ex20_RandomFixedLengthWords(N, W)
        ex22_Timings(wordsArray)

        println("itemArray:")
        val itemArray = ex21_RandomItems(N)
        ex22_Timings(itemArray)
        println()
        N *= 10
    }
}