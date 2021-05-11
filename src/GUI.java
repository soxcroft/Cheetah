import java.io.File;

/**
 * The GUI class provides a simple graphical interface for viewing the images
 * produced by the various modes as described in the project specification.
 * 
 * @author Liam Foxcroft
 *
 */
public class GUI {
  /**
   * Creates the images produced by the various modes and saves them to
   * "fileName" plus the necessary extensions
   * 
   * @param file the file containing the picture of a cheetah
   * @param fileName the name of the file where the images should be saved,
   *        without the extension (_GS.png etc.)
   * @param epsilon the value of epsilon to use when detecting edges
   * @param r1 the minimum radius of the mask to use in spot detection
   * @param r2 the maximum radius of the mask to use in spot detection
   */
  public static void createPictures(File file, String fileName, int epsilon,
      int r1, int r2) {
    Picture pic = new Picture(file);
    pic = GreyScale.getGreyScale(pic);
    pic.save(fileName + "_GS.png");
    pic = NoiseReduction.getNoiseReduction(pic);
    pic.save(fileName + "_NR.png");
    pic = EdgeDetection.getEdgeDetected(pic, epsilon);
    pic.save(fileName + "_ED.png");
    pic = SpotDetection.detectSpots(pic, r1, r2);
    pic.save(fileName + "_SD.png");
  }

  /**
   * Initializes the GUI and draws the picture of the cheetah to the canvas.
   * 
   * @param filePath the name of the file containing the picture of the cheetah
   */
  public static void initializeGUI(String filePath) {
    // Initialize the GUI window
    Picture pic = new Picture(filePath);
    int cols = pic.width();
    int rows = pic.height();
    StdDraw.setCanvasSize(cols, rows);
    StdDraw.setXscale(-cols / 2, cols / 2);
    StdDraw.setYscale(-rows / 2, rows / 2);
    StdDraw.enableDoubleBuffering();
    StdDraw.picture(0, 0, filePath);
    StdDraw.show();
  }

  /**
   * Draws the specified image on the canvas.
   * 
   * @param filePath the name of the file containing the image which should be
   *        drawn
   */
  public static void updateGUI(String filePath) {
    if (filePath.equals("")) {
      return;
    }
    StdDraw.clear();
    StdDraw.picture(0, 0, filePath); // Picture is cached so can't draw new
                                     // picture
    StdDraw.show();
  }

  /**
   * Reads in the arguments needed for producing the various images, calls the
   * necessary methods for initializing the GUI, detects when keys are pressed
   * and updates the GUI accordingly. 1 - original picture, 2 - grey-scaled
   * picture, 3 - image after noise reduction, 4 - image after edge detection, 5
   * - image after spot detection
   * 
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    File file = new File(args[0]);
    String fileName =
        "../out/" + file.getName().substring(0, file.getName().indexOf('.'));
    int epsilon = Integer.parseInt(args[1]);
    int r1 = Integer.parseInt(args[2]);
    int r2 = Integer.parseInt(args[3]);
    createPictures(file, fileName, epsilon, r1, r2);
    initializeGUI(args[0]);
    boolean running = true;
    while (running) {
      while (!StdDraw.hasNextKeyTyped()) {
        continue; // wait
      }
      char move = StdDraw.nextKeyTyped();
      if (move == 'q') {
        // QUIT
        running = false;
      }
      String filePath = "";
      switch (move) {
        case '1':
          filePath = args[0];
          break;
        case '2':
          filePath = fileName + "_GS.png";
          break;
        case '3':
          filePath = fileName + "_NR.png";
          break;
        case '4':
          filePath = fileName + "_ED.png";
          break;
        case '5':
          filePath = fileName + "_SD.png";
          break;
        default:
      }
      updateGUI(filePath);
    }
  }

}
