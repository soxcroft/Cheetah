import java.awt.Color;

/**
 * The Utility class contains methods for converting pictures to 2D arrays, and
 * 2D arrays back to pictures. These are used by many of the classes in this
 * project so it is easiest to put them here rather than copying them to each
 * class.
 * 
 * @author Liam Foxcroft
 *
 */
public class Utility {

  /**
   * Converts a grey-scaled picture to a 2D array of integer values.
   * 
   * @param pic the grey-scaled picture which should be converted to a 2D array
   * @return a 2D array of integer values which represents the input picture
   */
  public static int[][] getArrFromPic(Picture pic) {
    int[][] arr = new int[pic.width()][pic.height()];
    for (int i = 0; i < pic.width(); ++i) {
      for (int j = 0; j < pic.height(); ++j) {
        arr[i][j] = pic.get(i, j).getRed(); // since all values are the same
      }
    }
    return arr;
  }

  /**
   * Converts a 2D array of integer values to a grey-scaled picture.
   * 
   * @param arr a 2D array containing colors of pixels in a grey-scaled picture
   * @return the picture represented by the input array
   */
  public static Picture getPicFromArr(int[][] arr) {
    // TODO check arr[0] isnt length 0
    Picture pic = new Picture(arr.length, arr[0].length);
    for (int i = 0; i < arr.length; ++i) {
      for (int j = 0; j < arr[i].length; ++j) {
        pic.set(i, j, new Color(arr[i][j], arr[i][j], arr[i][j]));
      }
    }
    return pic;
  }

  /**
   * Unit tests the methods in the Utility class
   * 
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    int[][] arr = getArrFromPic(new Picture(args[0]));
    getPicFromArr(arr).show();
  }

}
