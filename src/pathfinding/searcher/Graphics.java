package pathfinding.searcher;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pathfinding.geometry.Point;
import pathfinding.geometry.Polygon;

// Lớp này cung cấp các hàm static để vẽ lên canvas
public class Graphics {

    // Thuộc tính scale dùng để tăng giảm tỷ lệ vẽ cho phù hợp với kích thước map thật
    public static float scale = 1.0f;
    public static GraphicsContext gc;

    // Vẽ điểm start
    public static void drawStartPoint(Point start) {
        int size = 10;
        float x = start.getX() * scale - size / 2;
        float y = start.getY() * scale - size / 2;
        gc.setFill(Color.GREEN);
        gc.fillOval(x, y, size, size);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeOval(x, y, size, size);
    }

    // Vẽ điểm goal
    public static void drawGoalPoint(Point goal) {
        int size = 10;
        float x = goal.getX() * scale - size / 2;
        float y = goal.getY() * scale - size / 2;
        gc.setFill(Color.RED);
        gc.fillOval(x, y, size, size);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeOval(x, y, size, size);
    }

    // Vẽ điểm
    public static void drawDot(Point p) {
        gc.setFill(Color.RED);
        gc.fillOval(p.getX()* scale, p.getY()* scale, 3.0, 3.0);
    }

    // Vẽ điểm mờ
    public static void drawFuzzyDot(Point p) {
        gc.setFill(Color.rgb(0, 0, 0, 0.1d));
        gc.fillOval(p.getX() * scale, p.getY() * scale, 2.0, 2.0);

    }

    // Vẽ polygon
    public static void drawPolygon(Polygon p, Color cl, int strokeWidth) {
        gc.setFill(cl);
        gc.fillPolygon(p.getXList(scale), p.getYList(scale), p.getNumPoint());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(strokeWidth);
        gc.strokePolygon(p.getXList(scale), p.getYList(scale), p.getNumPoint());
    }
}
