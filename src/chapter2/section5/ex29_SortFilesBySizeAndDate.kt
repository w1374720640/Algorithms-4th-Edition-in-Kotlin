package chapter2.section5

import chapter1.section3.SinglyLinkedList
import chapter1.section3.add
import chapter1.section3.find
import extensions.*
import java.io.File

/**
 * 按大小和最后修改日期将文件排序
 * 为File数据类型编写比较器，使之能够将文件按照大小、文件名或最后修改日期将文件升序或降序排列
 * 在程序LS中使用这些比较器，它接受一个命令行参数并根据指定的顺序列出目录的内容
 * 例如，"-t"指按照时间戳排序
 * 支持多个选项以消除排序位次相同者，同时必须确保排序的稳定性
 *
 * 解：排序可选参数如下
 * -s 按大小排序
 * -t 按最后修改日期时间戳排序
 * -n 按名称排序
 * -d 降序排序
 * 是-d参数用单独变量保存，其余值按出现顺序依次添加进单向链表中，忽略非法参数及超长参数
 * 因为需要保证排序的稳定性，Arrays.sort()方法默认用归并排序实现，可以直接使用
 */
fun ex29_SortFilesBySizeAndDate(array: Array<File>, vararg args: String) {
    val sizeOrder = "-s"
    val timeOrder = "-t"
    val nameOrder = "-n"
    val descOrder = "-d"

    var isDesc = false
    val argList = SinglyLinkedList<String>()
    for (i in args.indices) {
        when (args[i]) {
            sizeOrder, timeOrder, nameOrder -> {
                //参数列表最长三个，添加前需要判断是否已添加
                if (argList.size < 3 && argList.find(args[i]) == -1) {
                    argList.add(args[i])
                }
            }
            descOrder -> isDesc = true
            //忽略无效字符
        }
    }
    //如果没有任何参数，默认按名称排序
    if (argList.size == 0) {
        argList.add(nameOrder)
    }

    val comparator = Comparator<File> { o1, o2 ->
        var result = 0
        val iterator = argList.iterator()
        while (iterator.hasNext()) {
            val arg = iterator.next()
            when (arg) {
                //如果File是目录，则length()方法返回值未定义，不代表实际大小
                //如果需要获取实际大小，需要递归所有子目录，暂时忽略
                sizeOrder -> result = o1.length().compareTo(o2.length())
                timeOrder -> result = o1.lastModified().compareTo(o2.lastModified())
                nameOrder -> result = o1.name.compareTo(o2.name)
            }
            //如果前一个条件已经判断出差异，则无需后续判断
            if (result != 0) break
        }
        //默认升序排列，降序排列直接将比较结果乘-1
        if (isDesc) {
            result *= -1
        }
        result
    }

    array.sortWith(comparator)
}

fun main() {
    inputPrompt()
    val path = readString("directory: ")
    val args = readLine("args: ")
    val file = File(path)
    if (file.isDirectory) {
        val fileList = file.listFiles()
        if (fileList == null || fileList.isEmpty()) {
            println("File list is empty!")
        } else {
            val argsArray = args.split(" ").toTypedArray()
            ex29_SortFilesBySizeAndDate(fileList, *argsArray)
            fileList.forEach {
                //格式化打印文件信息
                println("${formatStringLength(it.name, 50, true)} size=${formatLong(it.length(), 15, true)} " +
                        "lastModified=${formatLong(it.lastModified(), 15, true)}")
            }
        }
    } else {
        println("Not a directory")
    }
}