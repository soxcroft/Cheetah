import java.io.File;

/**
 * The Animal class takes in an image of a cheetah and counts the number of
 * spots. It does this by using the GreyScale class to get the grey scaled
 * version of the image, NoiseReduction class to reduces the 'noise' in this
 * image (based on Popovici article), EdgeDetection class to detect the edges in
 * the image and finally SpotDetection class to detect and count the spots in
 * the image.
 * 
 * @author Liam Foxcroft
 *
 */
public class Animal {

  /**
   * Prints error message to standard error and terminates the program
   * 
   * @param err error message to be printed to standard error
   */
  public static void throwError(String err) {
    System.err.println(err);
    System.exit(1);
  }

  /**
   * Error handling for input parameters. Looks for invalid number of arguments,
   * invalid argument types, invalid mode, invalid epsilon and invalid file
   * 
   * @param args input parameters
   */
  public static void handleErrors(String[] args) {
    // Invalid number of arguments
    String[] validModes = {"0", "1", "2", "3"};
    int[] numArgs = {2, 2, 3, 5};
    for (int i = 0; i < validModes.length; ++i) {
      if (validModes[i].equals(args[0]) && args.length != numArgs[i]) {
        throwError("ERROR: invalid number of arguments");
      }
    }
    // Invalid argument type
    for (int i = 0; i < args.length; ++i) {
      if (i != 1 && !args[i].matches("-?\\d+$")) { // regex for integer, don't
                                                   // check filename (args[1])
        throwError("ERROR: invalid argument type");
      }
    }
    // Invalid mode
    if (!(args[0].equals("0") || args[0].equals("1") || args[0].equals("2")
        || args[0].equals("3"))) {
      throwError("ERROR: invalid mode");
    }
    // Invalid epsilon TODO How to check for overflow ?
    if (args.length >= 3
        && (Integer.parseInt(args[2]) < 0 || 255 < Integer.parseInt(args[2]))) {
      throwError("ERROR: invalid epsilon");
    }
    // Invalid file
    File file = new File(args[1]);
    if (!(file.exists() && file.canRead() && file.isFile())) {
      throwError("ERROR: invalid or missing file");
    }
  }

  /**
   * Returns the picture produced based on the cheetah image and mode that the
   * program is run with
   * 
   * @param file filepath for image
   * @param mode the mode that the program is run with
   * @param epsilon the value of epsilon to use when detecting edges
   * @param r1 the minimum radius of the mask to use in spot detection
   * @param r2 the maximum radius of the mask to use in spot detection
   * @return updated picture of cheetah based on the mode given
   */
  public static Picture getPic(File file, int mode, int epsilon, int r1,
      int r2) {
    Picture pic = new Picture(file);
    if (mode >= 0) {
      pic = GreyScale.getGreyScale(pic);
    }
    if (mode >= 1) {
      pic = NoiseReduction.getNoiseReduction(pic);
    }
    if (mode >= 2) {
      pic = EdgeDetection.getEdgeDetected(pic, epsilon);
    }
    if (mode >= 3) {
      pic = SpotDetection.detectSpots(pic, r1, r2);
    }
    return pic;
  }

  /**
   * Saves the picture produced to directory '../out' and creates the name based
   * on the original filename as well as the mode
   * 
   * @param pic the picture to be saved
   * @param file the file path of the original picture of the cheetah
   * @param mode the mode that the program was run with
   */
  public static void savePic(Picture pic, File file, int mode) {
    String fileName =
        "../out/" + file.getName().substring(0, file.getName().indexOf('.'));
    switch (mode) {
      case 0:
        fileName += "_GS.png";
        break;
      case 1:
        fileName += "_NR.png";
        break;
      case 2:
        fileName += "_ED.png";
        break;
      case 3:
        fileName += "_SD.png";
        break;
      default:
    }
    pic.save(fileName);
  }

  /**
   * Reads in the command line arguments and calls the necessary methods to
   * check that the arguments are valid, before creating and saving the new
   * image based on the given input image and mode.
   * 
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // Check that the input is valid
    handleErrors(args);
    // Read the input into the various variables
    int mode = Integer.parseInt(args[0]);
    File file = new File(args[1]);
    int epsilon = 0;
    if (mode == 2 || mode == 3) {
      epsilon = Integer.parseInt(args[2]);
    }
    int r1 = 0;
    int r2 = 0;
    if (mode == 3) {
      r1 = Integer.parseInt(args[3]);
      r2 = Integer.parseInt(args[4]);
    }
    // Create and save the new picture based on the mode
    Picture pic = getPic(file, mode, epsilon, r1, r2);
    savePic(pic, file, mode);
  }

}
