# FibonacciTester
Tester for the FibonacciHeap project.

To use the tester make sure that the FibonacciHeap fields firstNode and minNode are public, and that the HeapNode fields are all public.

Use testRandomHeaps to test randomally generated trees. numOfTests is the total number of randomally generated tests, numOfNodes is the number of nodes to insert in each heap, numOfMinDeletes is the number of deleteMin method calls each test will make, and numOfRandomDeletes is the number of delete method calls with a random input each test will make.

Use testHeap to check a specific FibonacciHeap.

Use testKMin to test the static method kMin for a few randomally generated heaps with a single tree in them.

The tester checks if every node chains is connected correctly (node.next.prev == node and node.prev.next == node and if every node in a node chain has the same parent), if the minimum node in the tree is in fact the node that is pointed by the minimum field, if the rankings of the nodes are correct (if the rank of each node is the number of children it has), if the roots are correct (no marked roots and every root's parent is null), if the size of the heap is correct, and if the method countersRep is correct.

The tester also checks the method meld by combining 2 randomally generated trees. To test this method properly, make numOfNodes bigger than numOfMinDeletes + numOfRandomDeletes.
