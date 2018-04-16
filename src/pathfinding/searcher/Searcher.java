package pathfinding.searcher;

import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;
import static pathfinding.App.messageBox;
import pathfinding.geometry.Point;

// Lớp này định nghĩa thuật toán tìm kiếm
public class Searcher {

    public static int algorithm = 0;
    private Map mMap; // Bản đồ
    private Point mFrom; // Điểm start
    private Point mTo; // Điểm goal
    private float mDistance; // Khoảng cách đường đi

    // Nested class này dùng để định nghĩa một Node trong đồ thị
    class Node {

        public Point mPoint; // Tọa độ node
        public Node mPrev; // Node trước đó, dùng để quay lui
        public float mCost; // Chi phí 1 bước để đến nút, dùng để tính quãng đường
        public float mSCost; // Chi phí từ điểm Start đến nút
        public float mHeuristic; // Giá trị heuristic tại Node

        public Node() {
            mPoint = null;
            mPrev = null;
            mCost = 0;
            mSCost = 0;
            mHeuristic = 0;
        }

        public Node(Point p, Node prev) {
            mPoint = p;
            mPrev = prev;
            mCost = 0;
            mSCost = 0;
            mHeuristic = Point.computeDistance2(p, mTo);
        }

        public boolean equals(Node e) {
            return this.mPoint.equals(e);
        }
    }

    public Searcher(Map map, Point from, Point to) {
        mMap = map;
        mFrom = new Point(from);
        mTo = new Point(to);
    }

    public Searcher(String input, Point from, Point to) {
        mMap = new Map(input);
        mFrom = new Point(from);
        mTo = new Point(to);
    }

    public void clear() {
        mDistance = 0;
    }

    public float getDistance() {
        return mDistance;
    }

    public void run() {
        if (algorithm == 0) {
            astarSearch();
        } else if (algorithm == 1) {
            greedySearch();
        }
    }

    // Kiểm tra xem điểm p có nằm trong arr hay không
    private boolean contains(ArrayList<Point> arr, Point p) {
        for (Point e : arr) {
            if (e.getX() == p.getX() && e.getY() == p.getY()) {
                return true;
            }
        }
        return false;
    }

    // Hàm tính hashCode
    private String hash(Node p) {
        return String.valueOf(p.mPoint.getX()) + String.valueOf(p.mPoint.getY());
    }

    // Hàm tính hashCode
    private String hash(Point p) {
        return String.valueOf(p.getX()) + String.valueOf(p.getY());
    }

    // Kiểm tra một Node đã có trong hashtable hay chưa
    private boolean exist(Node p, Hashtable<String, Node> h) {
        String hashCode = hash(p);
        Node tmp = h.get(hashCode);
        return (tmp != null);
    }

    // Đưa một Node vào hashtable
    private boolean putIntoTable(Node p, Hashtable<String, Node> h) {
        String hashCode = hash(p);
        Node tmp = h.get(hashCode);
        if (tmp == null) {
            h.put(hashCode, p);
            return true;
        }
        return false;
    }

    // Đưa một Point vào hashtable
    private boolean putIntoTable(Point p, Hashtable<String, Point> h) {
        String hashCode = hash(p);
        Point tmp = h.get(hashCode);
        if (tmp == null) {
            h.put(hashCode, p);
            return true;
        }
        return false;
    }

    // Xóa một phần tử có key là <code> trong bảng băm
    private void removeFromTable(String code, Hashtable<String, Node> h) {
        Node tmp = h.get(code);
        if (tmp != null) {
            tmp = null;
            h.put(code, tmp);
        }
    }

    // Lấy một phần tử từ bảng băm
    private Node getFromTable(String code, Hashtable<String, Node> h) {
        Node tmp = h.get(code);
        return tmp;
    }

    // Kiểm tra xem robot có thể đi qua p không
    private boolean canGo(Point p, Point out) {
        for (int j = 0; j < mMap.numObj(); j++) {
            if (mMap.getObject(j).isInside(out, p)) { // Nếu thuộc một vật cản
                return false;
            }
        }
        return true;
    }

    // Hàm quay lui để vẽ đường đi
    private void drawPath(Node p) {
        while (p != null) {
            Graphics.drawDot(p.mPoint);
            mDistance += p.mCost;
            p = p.mPrev;
        }
    }

    // Hàm search đường đi dùng thuật toán dùng Greedy
    public void greedySearch() {
        Hashtable<String, Point> bucket = new Hashtable<>();
        if (!mMap.isInside(mTo)) {
            return;
        }
        // Định nghĩa đối tượng comparator để so sánh độ ưu tiên trong priority queue
        Comparator<Node> cmp = new Comparator<Node>() {
            @Override
            public int compare(Node a, Node b) {
                float fa = (float) a.mHeuristic;
                float fb = (float) b.mHeuristic;
                if (fa - fb > 0) {
                    return 1;
                } else if (fa - fb < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }

        };
        Point root = new Point(0, -1);
        // Dùng comparator để tạo PriorityQueue
        PriorityQueue<Node> queue = new PriorityQueue<>(cmp);
        Node start = new Node(mFrom, null);
        Node current = null;
        queue.offer(start);
        final float s2 = (float) sqrt(2);

        while (!queue.isEmpty()) {
            current = queue.poll();
            if (current.mPoint.equals(mTo)) { // Nếu tìm được goal thì vẽ ra đường đi
                drawPath(current);
                return;
            }
            // Đưa điểm hiện tại vào bẳng băm, xem như đã xử lý
            putIntoTable(current.mPoint, bucket);

            // Lần lượt duyệt 8 hướng đi
            move:
            for (int i = 0; i < 8; i++) {
                float cost = 0;
                Point nextPoint = current.mPoint.move(i); // Xét bước tiếp theo
                if (i == 1 || i == 3 || i == 5 || i == 7) { // Các hướng 1 3 5 7 có chi phí căn 2
                    cost = s2;
                } else {
                    cost = 1.0f;
                }
                if (putIntoTable(nextPoint, bucket) == true) { // Nếu chưa tồn tại trong hashtable
                    if (mMap.isInside(nextPoint) == true) { // Nếu thuộc Map
                        for (int j = 0; j < mMap.numObj(); j++) {
                            if (mMap.getObject(j).isInside(root, nextPoint)) { // Nếu thuộc một vật cản
                                continue move; // thì bỏ qua điểm này
                            }
                        }
                        Node nextNode = new Node(nextPoint, current);
                        nextNode.mCost = cost;
                        nextNode.mSCost = current.mSCost + cost;
                        queue.offer(nextNode); // Đưa Node này vào Queue
                        Graphics.drawFuzzyDot(nextPoint); // Vẽ điểm duyệt
                    }
                }
            }
        }
        // Không tìm được đường đi
        messageBox("Searching failed",
                "Cannot find any path from Start to Goal.",
                "Check input.txt file and try again.");
    }

    // Thuật toán tìm kiếm A*
    public void astarSearch() {
        Hashtable<String, Node> closedTable = new Hashtable<>();
        Hashtable<String, Node> openTable = new Hashtable<>();
        if (!mMap.isInside(mTo)) {
            return;
        }
        // Định nghĩa đối tượng comparator để so sánh độ ưu tiên hai đỉnh trong priority queue
        Comparator<Node> cmp = new Comparator<Node>() {
            @Override
            public int compare(Node a, Node b) {
                double fa = a.mHeuristic + a.mSCost; // Hàm f = g + h
                double fb = b.mHeuristic + b.mSCost; // Hàm f = g + h
                if (fa - fb > 0) {
                    return 1;
                } else if (fa - fb < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }

        };
        Point root = new Point(0, -1);
        // Dùng comparator trên để tạo PriorityQueue
        PriorityQueue<Node> queue = new PriorityQueue<>(cmp);
        Node start = new Node(mFrom, null);
        Node current = null;
        queue.offer(start);
        final float s2 = (float) sqrt(2);

        while (!queue.isEmpty()) {
            current = queue.poll();
            if (current == null) {
                return;
            }
            if (current.mPoint.equals(mTo)) { // Nếu tìm được goal thì vẽ ra đường đi
                drawPath(current);
                return;
            }
            // Đưa điểm hiện tại vào bẳng băm, xem như đã xử lý
            putIntoTable(current, closedTable);
            
            Graphics.drawFuzzyDot(current.mPoint); // Vẽ điểm đã duyệt

            // Lần lượt duyệt 8 hướng đi
            for (int i = 0; i < 8; i++) {
                float cost = 0;
                Point nextPoint = current.mPoint.move(i); // Xét bước tiếp theo
                if (i == 1 || i == 3 || i == 5 || i == 7) { // Các hướng 1 3 5 7 có chi phí căn 2
                    cost = s2;
                } else {
                    cost = 1.0f; // Các bước 1 2 4 6 có chi phí 1
                }
                // Nếu bước tiếp theo khả thi
                if (mMap.isInside(nextPoint) == true && canGo(nextPoint, root)) {
                    Node openNode = getFromTable(hash(nextPoint), openTable);
                    Node closedNode = getFromTable(hash(nextPoint), closedTable);
                    if (openNode != null) { // Nếu đã có trong open
                        if (openNode.mSCost > current.mSCost + cost) {
                            openNode.mSCost = current.mSCost + cost;
                            openNode.mPrev = current;
                        }
                    } else {
                        if (closedNode != null) { // Nếu đã có trong closed
                            if (closedNode.mSCost > current.mSCost + cost) {
                                Node tmp = getFromTable(hash(nextPoint), closedTable);
                                putIntoTable(tmp, openTable);
                                //tmp.mSCost = current.mSCost + cost;
                                removeFromTable(hash(nextPoint), closedTable);
                            }
                        } else { // Nếu chưa có trong closed và open
                            Node nextNode = new Node(nextPoint, current);
                            nextNode.mSCost = current.mSCost + cost;
                            nextNode.mCost = cost;
                            putIntoTable(nextNode, openTable);
                            queue.offer(nextNode);
                        }
                    }
                }
            }
        }
        messageBox("Searching failed",
                "Cannot find any path from Start to Goal.",
                "Check input.txt file and try again.");
    }
}
