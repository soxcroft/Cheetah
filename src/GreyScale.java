/**
 * The GreyScale class implements methods for grey-scaling a color picture.
 * 
 * @author Liam Foxcroft
 *
 */
public class GreyScale {
  /**
   * Returns the grey scaled version of a color based on the weighted grey-scale
   * method given in the project specification.
   * 
   * @param rgb the RGB value represented as a single integer
   * @return the grey scaled version of the RGB value
   */
  public static int getGrey(int rgb) {
    int red = (rgb >> 16) & 0xFF; // 11111111
    int green = (rgb >> 8) & 0xFF;
    int blue = rgb & 0xFF;
    return (int) (0.299 * red + 0.587 * green + 0.114 * blue);
  }

  /**
   * Returns a grey scaled version of the input picture.
   * 
   * @param pic the picture to be grey-scaled
   * @return grey scaled version of input
   */
  public static Picture getGreyScale(Picture pic) {
    int width = pic.width();
    int height = pic.height();
    int[][] newPic = new int[width][height];
    for (int i = 0; i < width; ++i) {
      for (int j = 0; j < height; ++j) {
        newPic[i][j] = getGrey(pic.getRGB(i, j)); // TODO
      }
    }
    return Utility.getPicFromArr(newPic);
  }

  /**
   * Unit tests the methods in GreyScale class by creating and showing a picture
   * after noise reduction.
   * 
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    getGreyScale(new Picture(args[0])).show();
  }

}
