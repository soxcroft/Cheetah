/**
 * The SpotDetection class implements methods to count the number of spots on a
 * picture of a cheetah. It does this by creating small images of spots, called
 * masks. These masks are then moved over the image and the similarity between
 * them and the area they are covering is checked.
 * 
 * @author Liam Foxcroft
 *
 */
public class SpotDetection {

  // DEBUGGERS
  /**
   * Prints a 2D array of integers to standard out
   * 
   * @param arr the 2D array which should be printed to standard out
   */
  public static void print2d(int[][] arr) {
    for (int i = 0; i < arr.length; ++i) {
      for (int j = 0; j < arr[i].length; ++j) {
        StdOut.printf("%3d ", arr[i][j]);
      }
      StdOut.println();
    }
  }

  // PROJECT CODE
  /**
   * Creates a mask by using the procedure described in the project
   * specification.
   * 
   * @param picRadius the radius of the biggest circle that could fit in the
   *        image
   * @param donutRadius the radius of the center line through the ring
   * @param width the width of the edge of the spot TODO
   * @param delta the TODO
   * @return the 2D array representation of the mask
   */
  public static int[][] createMask(int picRadius, int donutRadius, int width,
      int delta) {
    int[][] mask = new int[2 * picRadius + 1][2 * picRadius + 1];
    for (int i = 0; i < 2 * picRadius + 1; ++i) {
      for (int j = 0; j < 2 * picRadius + 1; ++j) {
        int circle = (i - picRadius) * (i - picRadius)
            + (j - picRadius) * (j - picRadius);
        boolean donut =
            (circle < ((donutRadius - delta) * (donutRadius - delta) + width)
                && circle > ((donutRadius - delta) * (donutRadius - delta)
                    - width));
        if (donut) {
          mask[i][j] = 255; // white
        } else {
          mask[i][j] = 0; // black
        }
      }
    }
    return mask;
  }

  /**
   * Gets the block of pixels covered by the mask.
   * 
   * @param edges the 2D array representation of the picture of edges
   * @param width the width of the block
   * @param col the column of the bottom left pixel in the block
   * @param row the row of the bottom left pixel in the block
   * @return the block of pixels covered by the mask
   */
  public static int[][] getBlock(int[][] edges, int width, int col, int row) {
    int[][] block = new int[width][width];
    for (int i = 0; i < width; ++i) {
      for (int j = 0; j < width; ++j) {
        block[i][j] = edges[col + i][row + j];
      }
    }
    return block;
  }

  /**
   * Determines the maximum value (color) of a pixel in a block. This value will
   * always be 0 or 255 because the block contains the colors of black and white
   * pixels.
   * 
   * @param block the block of pixels
   * @param width the width of the block of pixels
   * @return the maximum value of a pixel in the block
   */
  public static int getMax(int[][] block, int width) {
    int max = 0;
    for (int i = 0; i < width; ++i) {
      for (int j = 0; j < width; ++j) {
        if (max < block[i][j]) {
          max = block[i][j];
        }
      }
    }
    return max;
  }

  /**
   * Not needed because the pixels are black and white and are thus already
   * normalized?
   * 
   * @param block the image block
   * @param width the width of the image block
   * @param max the maximum value of a pixel in the block
   */
  public static void normalise(int[][] block, int width, double max) {
    for (int i = 0; i < width; ++i) {
      for (int j = 0; j < width; ++j) {
        block[i][j] *= 255.0 / max;
      }
    }
  }

  /**
   * Adds the differences between values in the spot mask and the pixels they
   * cover in the picture of edges.
   * 
   * @param block the block of pixels covered by the mask
   * @param mask the image of a spot
   * @param width the width of the block and mask
   * @return the sum of differences between the mask and block values
   */
  public static int sumOfDifferences(int[][] block, int[][] mask, int width) {
    int sum = 0;
    for (int i = 0; i < width; ++i) {
      for (int j = 0; j < width; ++j) {
        sum += Math.abs(block[i][j] - mask[i][j]);
      }
    }
    return sum;
  }

  /**
   * TODO this works well. Use boolean array to remember which spots have been
   * counted. Try make it use the width of the image block or radius
   * 
   * @param counted the 2D array which stores the centers of spots which have
   *        been counted
   * @param spotCounter the spot mask of the center of the spot
   * @param width the width of the spot mask
   * @param row the row of the top left pixel covered by the mask
   * @param col the column of the top left pixel covered by the mask
   * @return true if the spot hasn't been counted before, else false
   */
  public static boolean countSpot(boolean[][] counted, int[][] spotCounter,
      int width, int row, int col) {
    boolean isSpot = false;
    for (int i = 0; i < width; ++i) {
      for (int j = 0; j < width; ++j) {
        if (spotCounter[i][j] == 255) {
          int nx = row + i;
          int ny = col + j;
          isSpot |= counted[nx][ny];
          counted[nx][ny] = true;
        }
      }
    }
    return isSpot;
  }

  /**
   * Changes the colors of pixels stored in a 2D array which are covered by the
   * image block from 'color' to 'set'
   * 
   * @param arr the 2D array of pixel colors
   * @param block the image block
   * @param col the row of the top left pixel
   * @param row the column of the top left pixel
   * @param colour the color which should be changed
   * @param set the color to change to
   */
  public static void setInts(int[][] arr, int[][] block, int col, int row,
      int colour, int set) {
    int width = block.length;
    for (int i = 0; i < width; ++i) {
      for (int j = 0; j < width; ++j) {
        if (block[i][j] == colour) {
          arr[col + i][row + j] = set;
        }
      }
    }
  }

  /**
   * Counts the number of spots in a picture of edges and adds them to a new
   * picture using the mask given as an argument.
   * 
   * @param edges the 2D array representing the picture of edges
   * @param spots the 2D array representing the image of spots
   * @param counted the 2D array used to avoid recounting spots
   * @param mask the mask which should be used to check if image blocks contain
   *        spots
   * @param spotCounter the
   * @param diff the maximum difference between pixel values in the spot mask
   *        and the pixels which it covers in edges for a spot
   * @return the number of spots counted with the spot mask
   */
  public static int findSpots(int[][] edges, int[][] spots, boolean[][] counted,
      int[][] mask, int[][] spotCounter, int diff) {
    int counter = 0;
    int picWidth = edges.length;
    int picHeight = edges[0].length;
    int maskWidth = mask.length; // only width because its a square
    for (int i = 0; i < picWidth - maskWidth; ++i) {
      for (int j = 0; j < picHeight - maskWidth; ++j) {
        int[][] imageBlock = getBlock(edges, maskWidth, i, j);
        int max = getMax(imageBlock, maskWidth);
        if (max == 0) {
          continue; // continue because every pixel in image block is black
        }
        // normalise(imageBlock, maskWidth, max); // TODO is this necessary?
        int sum = sumOfDifferences(imageBlock, mask, maskWidth);
        if (sum < diff) {
          // if pixel in image block is white, make it white in spots
          setInts(spots, imageBlock, i, j, 255, 255);
          if (!countSpot(counted, spotCounter, maskWidth, i, j)) {
            counter++;
          }
        }
      }
    }
    return counter;
  }

  /**
   * Creates and returns a new picture of spots found in the given picture of
   * edges.
   * 
   * @param pic the picture containing edges
   * @param r1 the minimum radius for the spot masks
   * @param r2 the maximum radius for the spot masks
   * @return the new picture of spots found in the input picture
   */
  public static Picture detectSpots(Picture pic, int r1, int r2) {
    int[][] edges = Utility.getArrFromPic(pic);
    int[][] spots = new int[pic.width()][pic.height()];
    boolean[][] counted = new boolean[pic.width()][pic.height()];
    int counter = 0;
    int[] width = {6, 9, 12, 15, 18, 21, 24, 27};
    int[] delta = {0, 1, 1, 1, 1, 1, 2, 2};
    int[] difference = {4800, 6625, 11000, 15000, 19000, 23000, 28000, 35000};
    for (int i = 0; i <= r2 - r1; ++i) {
      int[][] mask = createMask(r1 + i, r1 + i, width[i], delta[i]);
      int[][] spotCounter =
          createMask(r1 + i, (r1 + i) / 2, width[i], delta[i]);
      counter +=
          findSpots(edges, spots, counted, mask, spotCounter, difference[i]);
    }
    StdOut.println(counter);
    return Utility.getPicFromArr(spots);
  }

  /**
   * Unit tests the methods in SpotDetection.
   * 
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    int[] radius = {4, 5, 6, 7, 8, 9, 10, 11};
    int[] width = {6, 9, 12, 15, 18, 21, 24, 27};
    int[] delta = {0, 1, 1, 1, 1, 1, 2, 2};
    // int[] difference = {4800, 6625, 11000, 15000, 19000, 23000, 28000,
    // 35000};
    int n = 7;
    int[][] mask1 = createMask(radius[n], (radius[n]) / 2, width[n], delta[n]);
    print2d(mask1);
    int[][] mask2 =
        createMask(radius[n], (radius[n] - 2) / 2, width[n], delta[n]);
    print2d(mask2);
    // Picture out = detectSpots(new Picture(args[0]), Integer.valueOf(args[1]),
    // Integer.valueOf(args[2]));
    // out.show();
  }

}
