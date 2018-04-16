package pathfinding.searcher;

import pathfinding.geometry.Point;
import pathfinding.geometry.Polygon;
import java.util.ArrayList;
import java.io.*;
import static pathfinding.App.messageBox;

// Lớp này định nghĩa bản đồ 
public final class Map {

    private int mWidth;
    private int mHeight;
    private ArrayList<Polygon> mObjects; // Danh sách vật cản
    private Point mStart; // Điểm đầu
    private Point mGoal; // Điểm cuối
    private float mEuclide;

    public Map() {
        mWidth = 0;
        mHeight = 0;
        mObjects = new ArrayList<>();
        mStart = new Point();
        mGoal = new Point();
        mEuclide = -1;
    }

    public Map(String input) {
        this();
        fetch(input);
        checkStartGoal();
    }

    // Kiểm tra tính hợp lệ của hai điểm S và G
    public boolean checkStartGoal() {
        Point out = new Point(0, -1);
        if (mStart.equals(mGoal)) {
            return false;
        }
        for (Polygon p : mObjects) {
            if (!this.isInside(mStart) || !this.isInside(mGoal)) {
                return false;
            }
            if (p.isInside(out, mStart) || p.isInside(out, mGoal)) {
                return false;
            }
        }
        mEuclide = (float) Point.computeDistance2(mStart, mGoal);
        return true;
    }

    // Hàm này nạp dữ liệu từ file input
    public void fetch(String fileName) {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);

            String line;
            line = br.readLine();

            String[] size = line.split(" ");
            mWidth = Integer.parseInt(size[0]);
            mHeight = Integer.parseInt(size[1]);

            line = br.readLine();
            String[] fromTo = line.split(" ");

            mStart.move(Float.parseFloat(fromTo[0]), Float.parseFloat(fromTo[1]));
            mGoal.move(Float.parseFloat(fromTo[2]), Float.parseFloat(fromTo[3]));

            line = br.readLine();
            int objCount = Integer.parseInt(line);

            for (int i = 0; i < objCount; i++) {
                line = br.readLine();
                String[] poly = line.split(" ");
                int n = 0;
                for (String e : poly) {
                    n++;
                }
                ArrayList<Point> tmp = new ArrayList<>();
                for (int j = 0; j < n; j = j + 2) {
                    float x = Float.parseFloat(poly[j]);
                    float y = Float.parseFloat(poly[j + 1]);
                    tmp.add(new Point(x, y));
                }
                Polygon obj = new Polygon(tmp);
                mObjects.add(obj);
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            messageBox("IO Exception",
                    "Cannot open input.txt.",
                    "Make sure input.txt exists in program folder.");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    public Point getStart() {
        return mStart;
    }

    public Point getGoal() {
        return mGoal;
    }

    public Polygon getObject(int i) {
        return mObjects.get(i);
    }

    public int numObj() {
        return mObjects.size();
    }

    public float getEuclide() {
        return mEuclide;
    }

    // Kiểm tra một điểm có nằm trong Map hay không
    public boolean isInside(Point p) {
        return p.getX() >= 0 && p.getX() <= mWidth && p.getY() >= 0 && p.getY() <= mHeight;
    }
}
