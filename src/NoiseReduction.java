/**
 * The NoiseReduction class uses the ideas in "Cellular Automata and Image
 * Processing" by Popovici to implement methods to reduce the noise in a
 * grey-scaled image.
 * 
 * @author Liam Foxcroft
 *
 */
public class NoiseReduction {

  /**
   * Applies noise reduction to a single pixel using the Von Neumann
   * neighborhood and method outlined in "Cellular Automata and Image
   * Processing" by Popovici.
   * 
   * @param arr the array of grey-scaled colors representing the image
   * @param row the row of the central pixel
   * @param col the column of the central pixel
   * @return the color of the pixel after noise reduction
   */
  public static int reduce(int[][] arr, int row, int col) {
    int[] cnt = new int[256];
    int[] dx = {0, 1, -1, 0, 0};
    int[] dy = {0, 0, 0, 1, -1};
    int maxOccurence = 0;
    int maxColor = 0;
    for (int k = 0; k < 5; ++k) {
      if (maxOccurence < ++cnt[arr[row + dx[k]][col + dy[k]]]) {
        maxColor = arr[row + dx[k]][col + dy[k]];
        maxOccurence = cnt[maxColor];
      }
    }
    if (cnt[arr[row][col]] == maxOccurence) { // weight centre more
      return arr[row][col];
    } else {
      return maxColor;
    }
  }

  /**
   * Iterates over all the pixels in the input picture and creates a new picture
   * with noise reduction applied to it. Border pixels are not checked.
   * 
   * @param pic the picture which noise reduction must be applied to
   * @return the picture produced by applying noise reduction
   */
  public static Picture getNoiseReduction(Picture pic) {
    int width = pic.width();
    int height = pic.height();
    int[][] oldArr = Utility.getArrFromPic(pic);
    int[][] newArr = new int[width][height];

    for (int i = 0; i < width; ++i) {
      for (int j = 0; j < height; ++j) {
        if (i == 0 || j == 0 || i == width - 1 || j == height - 1) {
          newArr[i][j] = oldArr[i][j]; // don't include borders
        } else {
          newArr[i][j] = reduce(oldArr, i, j);
        }
      }
    }

    return Utility.getPicFromArr(newArr);
  }

  /**
   * Unit tests the noise reduction methods by applying noise reduction to the
   * input image and showing the results.
   * 
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    getNoiseReduction(new Picture(args[0])).show();
  }

}
