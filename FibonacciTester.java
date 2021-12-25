import java.util.*;

public class FibonacciTester {
    static FibonacciHeap heap;

    public static void main(String[] args) {
        testRandomHeaps(100, 3000, 1000, 1000);
        testKMin();
    }

    public static void testHeap(FibonacciHeap fibonacciHeap) {
        heap = fibonacciHeap;
        if (!heap.isEmpty()) {
            checkPrevNext(heap.firstNode);
            checkMin(heap.firstNode);
            checkRankings(heap.firstNode);
            checkRoots();
            checkSize();
            checkCountersRep();
        }
    }

    public static void testKMin() {
        for (int i = 1; i <= 15; i++) {
            int numOfNodes = (int) (Math.pow(2, i)) + 1;
            List<Integer> randomSort = new ArrayList<>();
            for (int j = 0; j < numOfNodes; j++) {
                randomSort.add(j);
            }
            Collections.shuffle(randomSort);
            FibonacciHeap fibonacciHeap = new FibonacciHeap();
            for (int number : randomSort) {
                fibonacciHeap.insert(number);
            }
            fibonacciHeap.deleteMin();
            checkKMin(fibonacciHeap);
        }
    }

    private static void testRandomHeaps(int numOfTests, int numOfNodes, int numOfMinDeletes, int numOfRandomDeletes) {
        if (numOfNodes < numOfMinDeletes + numOfRandomDeletes) {
            System.out.println("numOfNodes needs to be greater than or equal to numOfMinDeletes + numOfRandomDeletes");
            return;
        }
        int origNumOfMinDeletes = numOfMinDeletes;
        int origNumOfRandomDeletes = numOfRandomDeletes;
        FibonacciHeap curTotalHeap = new FibonacciHeap();
        for (int h = 1; h <= numOfTests; h++) {
            System.out.println("Test " + h);
            Random r = new Random();
            List<Integer> randomSort = new ArrayList<>();
            for (int j = 0; j < numOfNodes; j++) {
                randomSort.add(j);
            }
            Collections.shuffle(randomSort);

            FibonacciHeap fibonacciHeap = new FibonacciHeap();
            FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[numOfNodes];
            for (int number : randomSort) {
                nodes[number] = fibonacciHeap.insert(number);
                testHeap(fibonacciHeap);
            }
            while (numOfMinDeletes > 0 && numOfRandomDeletes > 0) {
                if (r.nextBoolean()) {
                    fibonacciHeap.deleteMin();
                    for (int i = 0; i < numOfNodes; i++) {
                        if (nodes[i] != null) {
                            nodes[i] = null;
                            break;
                        }
                    }
                    numOfMinDeletes--;
                } else {
                    int number;
                    do {
                        number = r.nextInt(numOfNodes);
                    } while (nodes[number] == null);
                    fibonacciHeap.delete(nodes[number]);
                    nodes[number] = null;
                    numOfRandomDeletes--;
                }
                testHeap(fibonacciHeap);
            }
            while (numOfMinDeletes > 0) {
                fibonacciHeap.deleteMin();
                for (int i = 0; i < numOfNodes; i++) {
                    if (nodes[i] != null) {
                        nodes[i] = null;
                        break;
                    }
                }
                numOfMinDeletes--;
                testHeap(fibonacciHeap);
            }
            while (numOfRandomDeletes > 0) {
                int number;
                do {
                    number = r.nextInt(numOfNodes);
                } while (nodes[number] == null);
                fibonacciHeap.delete(nodes[number]);
                nodes[number] = null;
                numOfRandomDeletes--;
                testHeap(fibonacciHeap);
            }
            numOfMinDeletes = origNumOfMinDeletes;
            numOfRandomDeletes = origNumOfRandomDeletes;
            if (h == 1) {
                curTotalHeap = fibonacciHeap;
            } else {
                for (int i = 0; i < numOfNodes; i++) {
                    if (nodes[i] != null) {
                        nodes[i].key += numOfNodes * h;
                    }
                }
                curTotalHeap.meld(fibonacciHeap);
                testHeap(curTotalHeap);
            }
        }
        System.out.println("DONE!");
    }

    private static void checkPrevNext(FibonacciHeap.HeapNode node) {
        FibonacciHeap.HeapNode firstNode = node;
        FibonacciHeap.HeapNode parentNode = node.parent;

        do {
            FibonacciHeap.HeapNode nextNode = node.next;
            if (nextNode.prev != node) {
                System.out.println("Error in next/prev: Node " + nextNode.key + "'s prev is incorrect, pointing at " + nextNode.prev.key + " and should be pointing at " + node.key);
            }
            if (node.parent != parentNode) {
                System.out.println("Error in parent: Node " + nextNode.key + "'s parent is incorrect, pointing at " + nextNode.parent.key + " and should be pointing at " + parentNode.key);
            }
            if (node.parent != null && node.key < parentNode.key) {
                System.out.println("Error in parent: Node " + nextNode.key + "'s key is " + node.key + " but its parent key is bigger, " + parentNode.key);
            }
            if (node.child != null) {
                checkPrevNext(node.child);
            }
            node = node.next;
        } while (node != firstNode);
    }

    private static void checkMin(FibonacciHeap.HeapNode node) {
        FibonacciHeap.HeapNode firstNode = node;
        do {
            if (node.key < heap.minNode.key) {
                System.out.println("Error in min: The node " + node.key + " is smaller than the min node " + heap.minNode.key);
            }
            if (node.child != null) {
                checkMin(node.child);
            }
            node = node.next;
        } while (node != firstNode);
    }

    private static void checkRankings(FibonacciHeap.HeapNode node) {
        FibonacciHeap.HeapNode firstNode = node;
        do {
            int childCnt = countChildren(node);
            if (childCnt != node.rank) {
                System.out.println("Error in rankings: The node " + node.key + " has " + childCnt + " children but is of rank " + node.rank);
            }
            if (node.child != null) {
                checkRankings(node.child);
            }
            node = node.next;
        } while (node != firstNode);
    }

    private static void checkSize() {
        int actualSize = checkSizeRec(heap.firstNode);
        if (actualSize != heap.size()) {
            System.out.println("Error in size: The heap's size value is " + heap.size() + " but the heap only has " + actualSize + " nodes");
        }
    }


    private static int checkSizeRec(FibonacciHeap.HeapNode node) {
        FibonacciHeap.HeapNode firstNode = node;
        int cnt = 0;
        do {
            cnt++;
            if (node.child != null) {
                cnt += checkSizeRec(node.child);
            }
            node = node.next;
        } while (node != firstNode);
        return cnt;
    }

    private static void checkCountersRep() {
        Map<Integer, Integer> ranksCnt = new HashMap<>();
        FibonacciHeap.HeapNode node = heap.firstNode;
        do {
            int rank = node.rank;
            int curRankCnt = ranksCnt.getOrDefault(rank, 0);
            ranksCnt.put(rank, curRankCnt + 1);
            node = node.next;
        } while (node != heap.firstNode);
        int[] arr = heap.countersRep();
        int maxRank = 0;
        for (int rank : ranksCnt.keySet()) {
            if (rank > maxRank) {
                maxRank = rank;
            }
        }
        if (arr.length != maxRank + 1) {
            System.out.println("Error in countersRep: The array is of size " + arr.length + " but the highest rank in the heap is " + maxRank);
        }
        for (int i = 0; i < arr.length; i++) {
            int heapCnt = ranksCnt.getOrDefault(i, 0);
            if (heapCnt != arr[i]){
                System.out.println("Error in countersRep: The heap contains " + heapCnt + " trees of rank " + i + " but the returned array's " + i + "th value is " + arr[i]);
            }
        }
    }

    private static void checkKMin(FibonacciHeap fibonacciHeap) {
        List<Integer> nodes = new ArrayList<>();
        getListOfAllNodesRec(fibonacciHeap.firstNode, nodes);
        nodes.sort(null);
        Random r = new Random();
        int k = r.nextInt(fibonacciHeap.size());
        int[] arr = FibonacciHeap.kMin(fibonacciHeap, k);
        for (int i = 0; i < k; i++) {
            if (arr[i] != nodes.get(i)) {
                System.out.println("Error in kMin: The " + i + "th minimum node in the heap is " + nodes.get(i) + " but the " + i + "th value in the array is " + arr[i]);
            }
        }
    }

    private static void getListOfAllNodesRec(FibonacciHeap.HeapNode node, List<Integer> lst) {
        FibonacciHeap.HeapNode firstNode = node;
        do {
            lst.add(node.key);
            if (node.child != null) {
                getListOfAllNodesRec(node.child, lst);
            }
            node = node.next;
        } while (node != firstNode);
    }

    private static void checkRoots() {
        FibonacciHeap.HeapNode node = heap.firstNode;
        do {
            if (!node.isRoot()) {
                System.out.println("Error in roots: The node " + node.key + " is a root but has a parent, " + node.parent.key);
            }
            if (node.isMarked) {
                System.out.println("Error in roots: The node " + node.key + " is a root but is marked");
            }
            node = node.next;
        } while (node != heap.firstNode);
    }

    private static int countChildren(FibonacciHeap.HeapNode node) {
        if (node.child == null) {
            return 0;
        } else {
            FibonacciHeap.HeapNode firstNode = node.child;
            node = node.child;
            int cnt = 0;
            do {
                cnt++;
                node = node.next;
            } while (node != firstNode);
            return cnt;
        }
    }
}
