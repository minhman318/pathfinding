package pathfinding.geometry;

// Lớp này lưu thông tin hình chữ nhật bao quanh Polygon
public class BoundingBox {

    public Point mTopRight;
    public Point mBotLeft;

    public BoundingBox() {
        mTopRight = new Point();
        mBotLeft = new Point();
    }

    public BoundingBox(Point a, Point b) {
        mTopRight = a;
        mBotLeft = b;
    }

    public BoundingBox(float maxX, float maxY, float minX, float minY) {
        mTopRight = new Point(maxX, maxY);
        mBotLeft = new Point(minX, minY);
    }

    public Point getTopLeft() {
        return mTopRight;
    }

    public Point getBotRight() {
        return mBotLeft;
    }

    public boolean isOutside(Point p) {
        boolean tl = p.getX() > mTopRight.getX() || p.getY() > mTopRight.getY();
        boolean br = p.getX() < mBotLeft.getX() || p.getY() < mBotLeft.getY();
        return tl || br;
    }
}
