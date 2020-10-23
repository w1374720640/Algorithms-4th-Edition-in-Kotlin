package chapter3.section3

/**
 * 向右图所示的红黑树（黑色加粗部分的链接为红色）中插入n并画出结果
 * （图中只显示了插入是的查找路径，你的解答中只需包含这些结点即可）
 *
 * 解：手动构造一颗不完整的红黑树，然后再插入
 */
fun main() {
    val bst = ShowChangeProcessRedBlackBST<Char, Int>()
    structureBST(bst)
    bst.showProcess = true
    bst.delay = 2000
    bst.showFlatView = false
    bst.draw()
    bst.put('n', 0)
    bst.drawText("end")
}

private fun structureBST(bst: RedBlackBST<Char, Int>) {
    bst.put('j', 0)
    var node = bst.root!!
    node.count = 11

    var nextNode = RedBlackBST.Node('u', 0, count = 10, color = RedBlackBST.BLACK)
    node.right = nextNode
    node = nextNode

    nextNode = RedBlackBST.Node('t', 0, count = 9, color = RedBlackBST.RED)
    node.left = nextNode
    node = nextNode

    nextNode = RedBlackBST.Node('s', 0, count = 8, color = RedBlackBST.BLACK)
    node.left = nextNode
    node = nextNode

    nextNode = RedBlackBST.Node('r', 0, count = 7, color = RedBlackBST.RED)
    node.left = nextNode
    node = nextNode

    nextNode = RedBlackBST.Node('q', 0, count = 6, color = RedBlackBST.BLACK)
    node.left = nextNode
    node = nextNode

    nextNode = RedBlackBST.Node('p', 0, count = 5, color = RedBlackBST.RED)
    node.left = nextNode
    node = nextNode

    nextNode = RedBlackBST.Node('l', 0, count = 4, color = RedBlackBST.BLACK)
    node.left = nextNode
    node = nextNode

    nextNode = RedBlackBST.Node('k', 0, count = 1, color = RedBlackBST.RED)
    node.left = nextNode
    node = nextNode

    nextNode = RedBlackBST.Node('o', 0, count = 2, color = RedBlackBST.BLACK)
    node.right = nextNode
    node = nextNode

    nextNode = RedBlackBST.Node('m', 0, count = 1, color = RedBlackBST.RED)
    node.left = nextNode
    node = nextNode

}