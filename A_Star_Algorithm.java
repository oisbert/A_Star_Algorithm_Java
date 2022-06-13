/*

Team Details:
    Arnas Juravicius 18257305
    Cyiaph McCann 17233453
    Dylan Kearney 18227023
    Oisin McNamara 18237398

MODULE CODE: CS4006
For an analysis of our program see "CS4006 Project Analysis.pdf"

*/


import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class A_Star_Algorithm{
    private char[][] mat;
    private Point start;
    private Point end;
    private Point current;
    Scanner scan;
    ArrayList<Point> open;
    ArrayList<Point> closed;
    ArrayList<Point> neighbours;

    final char obs = 'X';

    public is18227023() {
        mat = new char[8][8];
        scan = new Scanner(System.in);
        open = new ArrayList<>(); // Step 4
        closed = new ArrayList<>(); // Step 4
        neighbours = new ArrayList<>();
    }

    private int randomPos() {
        return (int) (Math.random() * 8);
    }

    private void fillI() {
        int tlX = randomPos(), tlY = randomPos(), sizeX = 1, sizeY = 4;
        if (shapeFits(tlX, tlY, sizeX, sizeY)) {
            for (int i = tlY; i < tlY + sizeY; i++) {
                setPoints(new Point(i, tlX), obs);
            }
        } else {
            fillI();
        }
    }

    private void fillT() {
        int tlX = randomPos(), tlY = randomPos(), sizeX = 3, sizeY = 4;
        if (shapeFits(tlX, tlY, sizeX, sizeY)) {
            for (int i = tlY; i < tlY + sizeY; i++) {
                setPoints(new Point(i, tlX + 1), obs);
            }
            for (int j = tlX; j < tlX + sizeX; j++) {
                setPoints(new Point(tlY, j), obs);
            }
        } else {
            fillT();
        }
    }

    private void fillJ() {
        int tlX = randomPos(), tlY = randomPos(), sizeX = 3, sizeY = 5;
        if(shapeFits(tlX, tlY, sizeX, sizeY)) {
             
            for(int i = tlY; i < tlY+sizeY-1; i++) {
                setPoints(new Point(i, tlX + sizeX), obs);
            }
            for(int j = tlX; j < tlX+sizeX+1; j++) {
                setPoints(new Point(tlY+sizeY-1, j), obs);
            }
        }
        else {
            fillJ();
        }
    }

    private void fillL() {
        int tlX = randomPos(), tlY = randomPos(), sizeX = 3, sizeY = 4;
        if (shapeFits(tlX, tlY, sizeX, sizeY)) {
            for (int i = tlY; i < tlY + sizeY; i++) {
                setPoints(new Point(i, tlX), obs);
            }
            for (int j = tlX; j < tlX + sizeX; j++) {
                setPoints(new Point(tlY+sizeY, j), obs);
            }
        } else {
            fillL();
        }
    }

    private void search() {

        open.clear();
        closed.clear();
        open.add(start);

        start.setGn(0);
        start.setHn(end);
        start.setFn();

        current = start;

        int searched = 0;
        int steps;

        while (!open.isEmpty()) {
            for (Point p : open) {

                p.setHn(end);
                p.setFn();
            }
            int nextGn = current.getGn() + 1;
            Collections.sort(open);
            current = open.get(0);
            searched++;

            current.setGn(nextGn);
            current.setHn(end);
            current.setFn();

            open.remove(current);
            closed.add(current);

            if (current.equals(end)) {
                steps = printPath(current);  //prints the path starting at current (which is now the end point)
                System.out.printf("End goal reached...\nPath took %d steps!\nPoints searched: %d\n", steps, searched);
                break;
            }

            neighbours = neighbours(current);
            for (Point n : neighbours) {
                n.setGn(current.getGn() + 1);      //neighbours gn is one more steps than currents gn
                if (closed.contains(n)) {          //if this neighbour is in closed, skip this loop 
                    continue;
                }

                if (n.getGn() < current.getGn() || !open.contains(n)) {
                    n.setHn(end);
                    n.setFn();
                    n.setParent(current);      //current's parent is this neighbour. 
                    if (!open.contains(n)) {
                        open.add(n);
                    }
                }
            }
        }
        setPoints(start, 'S');
        setPoints(end, 'E');
    }

    public int printPath(Point p) {

        int steps = 0;
        char ch = '\u25A0';

        while (p.getParent() != null) { // print the parent of each point given by p

            p = p.getParent();
            setPoints(p, ch);
            steps++;
        }

        return steps;
    }

    public static void main(String args[]) {
        is18227023 i = new is18227023();
        i.run();

    }

    public ArrayList<Point> neighbours(Point p) {
        ArrayList<Point> neighbours = new ArrayList<>();

        int x = p.getX();
        int y = p.getY();

        if (inGrid(x - 1, y))
            if (checkOpenPos(x - 1, y))
                neighbours.add(new Point(x - 1, y));
        if (inGrid(x, y - 1))
            if (checkOpenPos(x, y - 1))
                neighbours.add(new Point(x, y - 1));
        if (inGrid(x + 1, y))
            if (checkOpenPos(x + 1, y))
                neighbours.add(new Point(x + 1, y));
        if (inGrid(x, y + 1))
            if (checkOpenPos(x, y + 1))
                neighbours.add(new Point(x, y + 1));

        return neighbours;
    }

    private boolean checkOpenPos(int x, int y) {
        if (isEqual(x, y, obs))
            return false;
        if (isEqual(x, y, '-'))
            return false;
        if (isEqual(x, y, 'S'))
            return false;
        return true;
    }

    private boolean shapeFits(int tlX, int tlY, int sizeX, int sizeY) {
        return ((tlX > 0 && tlX + sizeX < 7) && (tlY > 0 && tlY + sizeY < 7));
    }

    private boolean isEqual(int x, int y, char value) {
        return mat[x][y] == value;
    }

    private boolean inGrid(int x, int y) {
        return ((x >= 0 && x <= 7) && (y >= 0 && y <= 7));
    }

    private void setStartAndEndIndexes(int sx, int sy, int ex, int ey) throws Exception {
        if (inGrid(sx, sy)) {
            if (!isEqual(sx, sy, obs)) {
                start = new Point(sx, sy);
            } else
                throw new Exception("Shape exists here. Try again.");
        } else
            throw new Exception("Incorrect index positions.");

        if (inGrid(ex, ey)) {
            if (!isEqual(ex, ey, obs)) {
                Point p = new Point(ex, ey);
                if (!p.isEqual(start)) {
                    end = new Point(ex, ey);
                    setPoints(end, 'E');
                    setPoints(start, 'S');
                } else
                    throw new Exception("Your start cant be the same as your end");
            } else
                throw new Exception("Shape exists here. Try again.");
        } else
            throw new Exception("Incorrect index positions.");

    }

    private void fillArray(char value) {
        int j;
        for (int i = 0; i < mat.length; i++) {
            for (j = 0; j < mat[i].length; j++) {
                mat[i][j] = value;
            }
        }
    }

    private void setPoints(Point p, char value) {
        mat[p.getX()][p.getY()] = value;
    }

    private void display() {
        int j;
        System.out.println("  0 1 2 3 4 5 6 7");
        for (int i = 0; i < mat.length; i++) {
            System.out.print(i + " ");
            for (j = 0; j < mat[i].length; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println("|");
        }
    }

    public void run() {

        boolean run = true;
        int a = 0, b = 0, c = 0, d = 0;

        fillArray(' ');

        int r = (int) (Math.random() * 4);

        switch (r) {
            case 0:
                fillI();
                break;
            case 1:
                fillT();
                break;
            case 2:
                fillJ();
                break;
            case 3:
                fillL();
                break;
            default:
                fillI();      //default case
        }

        while (run) {
            try {
                display();
                System.out.print("Enter start x: ");
                a = scan.nextInt();
                System.out.print("Enter start y: ");
                b = scan.nextInt();

                System.out.print("Enter end x: ");
                c = scan.nextInt();
                System.out.print("Enter end y: ");
                d = scan.nextInt();
                System.out.println();

                setStartAndEndIndexes(b, a, d, c);
                display();
                System.out.print("Would you like to commence the A Star search? (y/n)  ");
                String command = scan.next();
                if (command.equals("y")) {
                    search();
                    display();
                    run = false;
                } else {
                    run = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Incorrect information entered.");
                run = false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}

class Point implements Comparable<Point> {
    private int x;
    private int y;
    private int gn;
    private int hn;
    private int fn;
    Point parent = null;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isEqual(Point p) {
        return this.x == p.x && this.y == p.y;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")" + " f(n) = " + fn + " ,h(n) = " + hn + " ,g(n) = " + gn;
    }

    @Override
    public int compareTo(Point p) {
        if (this.fn == p.fn)
            return Integer.compare(this.fn, p.fn);
        else
            return Integer.compare(this.hn, p.hn);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point p = (Point) o;
            return this.x == p.x && this.y == p.y;
        }
        return false;
    }

    public void setHn(Point p) { 
        hn = Math.abs(this.x - p.x) + Math.abs(this.y - p.y); 
    }

    //just a load of getters and setters
    public void setGn(int gn) { this.gn = gn; }

    public Point getParent() { return parent; }

    public void setParent(Point p) { parent = p; }

    public int getGn() { return gn; }

    public int getHn() { return hn; }

    public int getFn() { return fn; }

    public void setFn() { fn = gn + hn; }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    public int getY() { return y; }

    public int getX() { return x; }

}
