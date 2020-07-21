package chapter1.section4

/**
 * 计算从total数量数据中取select个数据的排列数
 * 从n个数中取m个数排列的公式为 P(n,m)=n*(n-1)*(n-2)*...*(n-m+1)=n!/(n-m)!     (规定0!=1)
 * n个数的全排列公式(m=n) P(n)=n*(n-1)*(n-2)*...*1=n!
 */
fun permutation(total: Int, select: Int): Long {
    require(select in 0..total)
    var count = 1L
    repeat(select) {
        count *= total - it
    }
    return count
}

/**
 * 计算从total数量数据中取select个数据的组合数
 * 从n个数中取m个数组合的公式为 C(n,m)=P(n,m)/m!
 */
fun combination(total: Int, select: Int): Long {
    require(select in 0..total)
    return permutation(total, select) / factorial(select)
}

/**
 * 计算n的阶乘
 */
fun factorial(n: Int): Long {
    require(n >= 0)
    return permutation(n, n)
}
