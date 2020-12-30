/* *****************************************************************************
 *  Name: Seam Carver
 *  Date: 26/08/20
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;


public class SeamCarver {

    private int[][] currentPic;
    private int currentH;
    private int currentW;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        currentH = picture.height();
        currentW = picture.width();

        currentPic = new int[currentW][currentH];
        for (int i = 0; i < currentW; i++)
            for (int j = 0; j < currentH; j++)
                currentPic[i][j] = picture.getRGB(i, j);
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(currentW, currentH);
        for (int i = 0; i < currentW; i++)
            for (int j = 0; j < currentH; j++) {
                pic.setRGB(i, j, currentPic[i][j]);
            }
        return pic;
    }

    // width of current picture
    public int width() {
        return currentW;
    }

    // height of current picture
    public int height() {
        return currentH;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (y >= height() || x >= width() || x < 0 || y < 0) throw new IllegalArgumentException();
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) return 1000;

        int dX = 0;
        int dY = 0;
        int[] leftRGB = getRGB(x - 1, y);
        int[] rightRGB = getRGB(x + 1, y);
        int[] upRGB = getRGB(x, y - 1);
        int[] downRGB = getRGB(x, y + 1);

        for (int i = 0; i < 3; i++) {
            dX += Math.pow((rightRGB[i] - leftRGB[i]), 2);
            dY += Math.pow((downRGB[i] - upRGB[i]), 2);
        }
        return Math.sqrt(dY + dX);
    }

    private int[] getRGB(int x, int y) {
        // int rgb = picture.getRGB(x, y);
        int rgb = currentPic[x][y];
        int[] aRGB = new int[3];
        aRGB[0] = (rgb >> 16) & 0xFF;
        aRGB[1] = (rgb >> 8) & 0xFF;
        aRGB[2] = (rgb >> 0) & 0xFF;
        return aRGB;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double champion = Double.POSITIVE_INFINITY;
        int tracker = 0;
        int[] horizontalSeam = new int[width()];
        double[][] sumTo = new double[height()][width()];
        int[][] edgeTo = new int[height()][width()];

        if (currentW == 1) {
            horizontalSeam[0] = tracker;
            return horizontalSeam;
        }

        double[][] horizontalEnergy = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                horizontalEnergy[i][j] = energy(j, i);
                if (j == 0) sumTo[i][j] = 0;
                else sumTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        int[] a = new int[] { 1, 0, -1 };
        for (int c = 0; c < width() - 1; c++) {
            for (int r = 0; r < height(); r++) {
                if (c == width() - 2) {
                    if (champion > sumTo[r][c]) {
                        champion = sumTo[r][c];
                        tracker = r;
                    }
                }
                for (int index : a) {
                    int w = r + index;
                    if (w >= 0 && w < height())
                        if (sumTo[w][c + 1] > sumTo[r][c] + horizontalEnergy[w][c + 1]) {
                            sumTo[w][c + 1] = sumTo[r][c] + horizontalEnergy[w][c + 1];
                            edgeTo[w][c + 1] = r;
                        }
                }
            }
        }

        horizontalSeam[width() - 1] = tracker;
        horizontalSeam[width() - 2] = tracker;
        for (int t = width() - 3; t > 0; t--) {
            horizontalSeam[t] = edgeTo[tracker][t + 1];
            tracker = horizontalSeam[t];
        }
        horizontalSeam[0] = tracker;
        return horizontalSeam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double champion = Double.POSITIVE_INFINITY;
        int tracker = 0;
        int[] verticalSeam = new int[height()];
        double[][] sumTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];

        if (currentH == 1) {
            verticalSeam[0] = tracker;
            return verticalSeam;
        }

        double[][] verticalEnergy = new double[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                verticalEnergy[i][j] = energy(i, j);
                if (j == 0) sumTo[i][j] = 0;
                else sumTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        int[] a = new int[] { 1, 0, -1 };
        for (int r = 0; r < height() - 1; r++) {
            for (int c = 0; c < width(); c++) {
                if (r == height() - 2) {
                    if (champion > sumTo[c][r]) {
                        champion = sumTo[c][r];
                        tracker = c;
                    }
                }
                for (int index : a) {
                    int w = c + index;
                    if (w >= 0 && w < width())
                        if (sumTo[w][r + 1] > sumTo[c][r] + verticalEnergy[w][r + 1]) {
                            sumTo[w][r + 1] = sumTo[c][r] + verticalEnergy[w][r + 1];
                            edgeTo[w][r + 1] = c;
                        }
                }
            }
        }

        verticalSeam[height() - 1] = tracker;
        verticalSeam[height() - 2] = tracker;
        for (int t = height() - 3; t > 0; t--) {
            verticalSeam[t] = edgeTo[tracker][t + 1];
            tracker = verticalSeam[t];
        }
        verticalSeam[0] = tracker;
        return verticalSeam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        else if (seam.length != width()) throw new IllegalArgumentException();
        else if (currentH <= 1)
            throw new IllegalArgumentException("Image is already as small as it can be");

        int prev = seam[0];

        currentH--;
        int[][] newPic = new int[currentW][currentH];
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] > height() || seam[i] < 0) throw new IllegalArgumentException();
            if (Math.abs(seam[i] - prev) > 1) throw new IllegalArgumentException();
            System.arraycopy(currentPic[i], 0, newPic[i], 0, seam[i]);
            System.arraycopy(currentPic[i], seam[i] + 1, newPic[i], seam[i],
                             currentH - seam[i]);
            prev = seam[i];
        }
        currentPic = newPic;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // currentW--;
        // int[][] newPic = new int[currentH][currentW];
        // for (int i = 0; i < seam.length; i++) {
        //     System.arraycopy(currentPicVertical[i], 0, newPic[i], 0, seam[i]);
        //     System.arraycopy(currentPicVertical[i], seam[i] + 1, newPic[i], seam[i],
        //                      currentW - seam[i]);
        // }
        // currentPicVertical = newPic;
        // vertical = true;

        if (seam == null) throw new IllegalArgumentException();
        else if (seam.length != height()) throw new IllegalArgumentException();
        else if (currentW <= 1)
            throw new IllegalArgumentException("Image is already as small as it can be");

        currentPic = transpose(currentPic);
        removeHorizontalSeam(seam);
        currentPic = transpose(currentPic);
    }

    private int[][] transpose(int[][] pic) {
        int[][] tMat = new int[currentH][currentW];
        for (int i = 0; i < currentW; i++) {
            for (int j = 0; j < currentH; j++) {
                tMat[j][i] = pic[i][j];
            }
        }

        int temp = currentH;
        currentH = currentW;
        currentW = temp;
        return tMat;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture pic = new Picture("6x5.png");
        SeamCarver sc = new SeamCarver(pic);
        StdOut.println(sc.energy(4, 5));
    }
}
