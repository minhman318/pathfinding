package pathfinding.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Polygon {

    private final ArrayList<Point> mPoints;
    private final BoundingBox mBox;

    public Polygon() {
        mPoints = new ArrayList<>();
        mBox = new BoundingBox();
    }

    public Polygon(ArrayList<Point> points) {
        mPoints = new ArrayList<>(points);
        // Dinh nghia box
        Comparator<Point> Xcmp = new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return (int) (p1.getX() - p2.getX());
            }
        };
        Comparator<Point> Ycmp = new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return (int) (p1.getY() - p2.getY());
            }
        };
        float maxX = Collections.max(mPoints, Xcmp).getX();
        float maxY = Collections.max(mPoints, Ycmp).getY();
        float minX = Collections.min(mPoints, Xcmp).getX();
        float minY = Collections.min(mPoints, Ycmp).getY();
        mBox = new BoundingBox(maxX, maxY, minX, minY);
    }

    public double[] getXList(float scale) {
        double[] Xlist = new double[mPoints.size()];
        for (int i = 0; i < mPoints.size(); i++) {
            Xlist[i] = (double) mPoints.get(i).getX() * scale;
        }
        return Xlist;
    }

    public double[] getYList(float scale) {
        double[] Ylist = new double[mPoints.size()];
        for (int i = 0; i < mPoints.size(); i++) {
            Ylist[i] = (double) mPoints.get(i).getY() * scale;
        }
        return Ylist;
    }

    public int getNumPoint() {
        return mPoints.size();
    }

    // Kiểm tra xem một điểm có thuộc polygon
    public boolean isInside(Point out, Point p) {
        // Nếu nằm ngoài bounding box thì return 0
        if (mBox.isOutside(p) == true) {
            return false;
        }
        // Tạo danh sách các cạnh
        int n = mPoints.size();
        Line ed = null;
        ArrayList<Line> edges = new ArrayList<>();
        for (int i = 0; i < n - 1; i++) {
            ed = new Line(mPoints.get(i), mPoints.get(i + 1));
            if (ed.onLine(p)) {
                return true;
            }
            edges.add(ed);
        }
        ed = new Line(mPoints.get(n - 1), mPoints.get(0));
        if (ed.onLine(p)) {
            return true;
        }
        edges.add(ed);
        
        // Tìm điểm ngoài out thích hợp, tránh một số trường hợp đặc biệt
        boolean stop = false;
        next:
        while (stop == false) {
            Line draw = new Line(out, p);
            for (Point q : mPoints) {
                if (draw.onLine(q)) {
                    out = new Point(out.getX() + 1, out.getY());
                    continue next;
                }
            }
            stop = true;
        }
        // Đếm số lần đường kẻ qua p cắt các cạnh polygon
        int k = 0;        
        for (Line e : edges) {
            if (e.isMeetWith(out, p)) {
                k++;
            }
        }
        return k % 2 != 0;
    }
}
