package chapter1.section4

/**
 * 分数的二分查找。
 * 设计一个算法，使用对数级别的比较次数找出有理数p/q，其中0<p<q<N，比较形式为给定的数是否小于x？
 * 提示：两个分母均小于N的有理数之差不小于1/N^2
 *
 * 这题看了好多遍都没看懂什么意思
 * 网上的答案都是用二分法在一个数组中找到与给定数差值小于1/N^2的数
 * 但我觉得这题明显不是这个意思，N不是数组长度的意思，对数组中的数据也有要求
 * 具体什么意思弄不清，拒绝做这道题
 *
 * 最新的英文版似乎把这道题删了，换成了用二分法查找可重复数组中的key值对应的范围
 * 参考：https://github.com/reneargento/algorithms-sedgewick-wayne/blob/master/src/chapter1/section4/Exercise23_BinarySearchWithDuplicates.java
 * 这个仓库的作者在README中说：
 * The exercises answered on this repository are based on the seventh printing of the book (of September 2015).
 * Other printings of the book (especially older ones) may have exercises in a slightly different order.
 * 翻译过来意思是：
 * 在此存储库上回答的练习基于本书的第七次印刷（2015年9月）。 本书的其他印刷品（尤其是较旧的印刷品）的练习顺序可能略有不同。
 * （我使用的中文版是2012年翻译出版的）
 */