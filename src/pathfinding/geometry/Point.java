package pathfinding.geometry;

import static java.lang.Math.sqrt;

// Lớp này định nghĩa một điểm
public class Point {

    // X, Y mang kiểu float để dễ tính toán toán học nhưng bản chất vẫn là lưu số nguyên
    private float mX;
    private float mY;

    public Point() {
        mX = 0;
        mY = 0;
    }

    public Point(Point p) {
        mX = p.mX;
        mY = p.mY;
    }

    public Point(float x, float y) {
        mX = x;
        mY = y;
    }

    public void move(float x, float y) {
        mX = x;
        mY = y;
    }

    // Trả về một trong 8 Point xung quanh
    public Point move(int k) {
        Point t = new Point(this);
        switch (k) {
            case 0:
                t.mY++;
                break;
            case 1:
                t.mX++;
                t.mY++;
                break;
            case 2:
                t.mX++;
                break;
            case 3:
                t.mX++;
                t.mY--;
                break;
            case 4:
                t.mY--;
                break;
            case 5:
                t.mX--;
                t.mY--;
                break;
            case 6:
                t.mX--;
                break;
            case 7:
                t.mY++;
                t.mX--;
                break;
            default:
        }
        return t;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public boolean equals(Point p) {
        return mX == p.getX() && mY == p.getY();
    }

    // Tính bình phương khoảng cách giữa 2 điểm (ko lấy căn để tăng chính xác)
    public static float computeDistance2(Point from, Point to) {
        float dx = from.getX() - to.getX();
        float dy = from.getY() - to.getY();
        return (float) sqrt(dx * dx + dy * dy);
    }
}
