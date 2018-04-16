package pathfinding.geometry;

public class Line {

    // Thông tin phương trình đường thẳng
    private float mA;
    private float mB;
    private float mC;

    // Thông tin hai điểm tạo nên đoạn thẳng
    private Point mFrom;
    private Point mTo;

    public Line() {
        mA = 0;
        mB = 0;
        mFrom = null;
        mTo = null;
    }

    public Line(Point from, Point to) {
        mA = from.getY() - to.getY();
        mB = to.getX() - from.getX();
        mC = -(mA * from.getX() + mB * from.getY());
        mFrom = new Point(from);
        mTo = new Point(to);
    }

    public Line(Line d) {
        mA = d.mA;
        mB = d.mB;
        mC = d.mC;
        mFrom = new Point(d.mFrom);
        mTo = new Point(d.mTo);
    }

    // Kiểm tra đoạn thẳng có chứa điểm p hai không
    public boolean onLine(Point p) {
        boolean c1 = (mA * p.getX() + mB * p.getY() + mC) == 0;
        boolean c2 = (p.getX() - mFrom.getX()) * (p.getX() - mTo.getX()) <= 0;
        boolean c3 = (p.getY() - mFrom.getY()) * (p.getY() - mTo.getY()) <= 0;
        return c1 && c2 && c3;
    }

    // Kiểm tra đoạn thẳng this có cắt đoạn (out, p) hay ko
    public boolean isMeetWith(Point out, Point p) {
        Line c = new Line(out, p);
        float t1 = mA * out.getX() + mB * out.getY() + mC;
        float t2 = mA * p.getX() + mB * p.getY() + mC;

        float t3 = c.mA * mFrom.getX() + c.mB * mFrom.getY() + c.mC;
        float t4 = c.mA * mTo.getX() + c.mB * mTo.getY() + c.mC;

        // Hai điểm đầu mút của đoạn thẳng này khác phía với đoạn còn lại
        return t1 * t2 < 0 && t3 * t4 < 0;
    }
}
