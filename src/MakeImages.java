import java.io.IOException;
import java.util.LinkedList;


public class MakeImages {
	public static final String DEFAULT_DIRNAME_T = "Test/";
	private static LinkedList<Face> set;
	
	/**
	 * Prints usage instructions.
	 */
	public static void helpPrinter() {
		System.out.println("  Command Line Parameters are as follows:");
		System.out.println("    \"--help\" : You're looking at it");
		System.out.println("    \"-d [directory]\" : Source directory (default: Test/");
		System.out.println("      Example: -d test_imgs");
		System.out.println("Note: Later command-line options override earlier ones if they are incompatable\n");
	}
	
	/**
	 * Main function. Checks parameters and initializes program.
	 * @param args
	 */
	public static void main(String[] args) {
		// Default folder names
		String dirname = DEFAULT_DIRNAME_T;
		
		// Parse through the command line arguments
		try
		{
			int i = 0;
			while(i < args.length)
			{
				 if(args[i].equalsIgnoreCase("-d")) {
					//override the default test directory name
					dirname = args[i + 1];
					if(Wormhole.getContents(dirname) == null) {
						throw new IllegalArgumentException(dirname + " is not a valid directory.");
					}
					i++;
				} else if(args[i].equalsIgnoreCase("--help")){
					helpPrinter();
					System.exit(0);
				} else {
					throw new IllegalArgumentException();
				}
				
				i++;
			}
		} catch(IllegalArgumentException ia) {
			System.err.println("Invalid Arguments: " + ia.getMessage());
			System.exit(4);
		}
		
		try {
			set = new LinkedList<Face>();
			Wormhole.read(dirname,Face.Facetype.test,set);
			for(int i = 0; i < set.size(); i++) {
				set.get(i).toImage();
			}
		} catch(IOException e) {
			System.err.println("Invalid Image Directory: " + e.getMessage());
			System.exit(4);
		}
	}
}
