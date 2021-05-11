/**
 * The EdgeDetection class uses the ideas in "Cellular Automata and Image
 * Processing" by Popovici to implement methods to detect edges in an image. The
 * image must be grey-scaled and have noise reduction applied to it before edges
 * can be detected.
 * 
 * @author Liam Foxcroft
 *
 */
public class EdgeDetection {

  /**
   * Uses the Von Neumann neighborhood and ideas in Popovici's article to detect
   * if a single pixel is an edge. It does this by checking if the difference
   * between the central pixel and its neighbour's colors is less than a
   * threshold value, and if they all are the central pixel is set to black,
   * otherwise white.
   * 
   * @param arr the 2D array which represents the grey-scaled colors of the
   *        image
   * @param row the row of the pixel
   * @param col the column of the pixel
   * @param epsilon the threshold value
   * @return 255 (white) if central pixel is an edge, or 0 (black) if it is not
   *         an edge
   */
  public static int detect(int[][] arr, int row, int col, int epsilon) {
    int[] dx = {1, -1, 0, 0};
    int[] dy = {0, 0, 1, -1};
    boolean edge = false;
    for (int k = 0; k < 4 && !edge; ++k) {
      edge |=
          (Math.abs(arr[row][col] - arr[row + dx[k]][col + dy[k]]) > epsilon);
    }
    if (edge) {
      return 255; // white
    } else {
      return 0; // black
    }
  }

  /**
   * Iterates over all the pixels in the input picture and creates a new picture
   * of the edges by calling detect(). This picture of the edges is then
   * returned. Border pixels are not checked.
   * 
   * @param pic the grey-scaled, noise reduced input picture
   * @param epsilon the threshold value for determining if central pixels are
   *        edges
   * @return the new picture of the edges in the input picture
   */
  public static Picture getEdgeDetected(Picture pic, int epsilon) {
    int width = pic.width();
    int height = pic.height();
    int[][] oldArr = Utility.getArrFromPic(pic);
    int[][] newArr = new int[width][height];
    for (int i = 0; i < width; ++i) {
      for (int j = 0; j < height; ++j) {
        if (i == 0 || j == 0 || i == width - 1 || j == height - 1) {
          newArr[i][j] = 0; // don't include borders
        } else {
          newArr[i][j] = detect(oldArr, i, j, epsilon);
        }
      }
    }
    return Utility.getPicFromArr(newArr);
  }

  /**
   * Unit tests the methods in EdgeDetection by creating and showing the picture
   * created based on the input image file path and epsilon value.
   * 
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    getEdgeDetected(new Picture(args[0]), Integer.parseInt(args[1])).show();
  }

}
