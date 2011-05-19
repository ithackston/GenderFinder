import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;


public class Wormhole {
	public static final String DEFAULT_DIRNAME_M = "Male/";		//default directory names
	public static final String DEFAULT_DIRNAME_F = "Female/";
	public static final String DEFAULT_DIRNAME_T = "Test/";
	
	public static enum Facetype {male,female,test};
	
	/**
	 * Prints usage instructions.
	 */
	public static void helpPrinter()
	{
		System.out.println("  Command Line Parameters are as follows:");
		System.out.println("    \"--help\" : You're looking at it");
		System.out.println("    \"-train\" : Train the network with data");
		System.out.println("      Example: -train");
		System.out.println("    \"-test\" : Test on an set of unknown data");
		System.out.println("      Example: -test");
		System.out.println("    \"-m [directory]\" : Set male image directory (default: Male/)");
		System.out.println("      Example: -m male_imgs");
		System.out.println("    \"-f [directory]\" : Set female image directory (default: Female/)");
		System.out.println("      Example: -f female_imgs");
		System.out.println("    \"-t [directory]\" : Set test image directory (default Test/)");
		System.out.println("      Example: -t test_imgs");
		System.out.println("Note: Later command-line options override earlier ones if they are incompatable\n");
	}
	
	/**
	 * Main function. Checks parameters and initializes program.
	 * @param args
	 */
	public static void main(String[] args) {
		// Default folder names
		String dirNameMale = DEFAULT_DIRNAME_M;
		String dirNameFemale = DEFAULT_DIRNAME_F;
		String dirNameTest = DEFAULT_DIRNAME_T;
		boolean train = false;
		boolean test = false;
		
		// Parse through the command line arguments
		try
		{
			int i = 0;
			while(i < args.length)
			{
				if(args[i].equalsIgnoreCase("-m")) {
					//override the default male directory name
					dirNameMale = args[i + 1];
					if(getContents(dirNameMale) == null) {
						throw new IllegalArgumentException(dirNameMale + " is not a valid directory.");
					}
					i++;
				} else if(args[i].equalsIgnoreCase("-f")) {
					//override the default female directory name
					dirNameFemale = args[i + 1];
					if(getContents(dirNameFemale) == null) {
						throw new IllegalArgumentException(dirNameFemale + " is not a valid directory.");
					}
					i++;
				} else if(args[i].equalsIgnoreCase("-t")) {
					//override the default test directory name
					dirNameTest = args[i + 1];
					if(getContents(dirNameTest) == null) {
						throw new IllegalArgumentException(dirNameTest + " is not a valid directory.");
					}
					i++;
				} else if(args[i].equalsIgnoreCase("-train")) {
					train = true;
				} else if(args[i].equalsIgnoreCase("-test")) {
					test = true;
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
			if(train) {
				read(dirNameMale,Facetype.male);
				read(dirNameFemale,Facetype.female);
			}
			
			if(test) {
				read(dirNameTest,Facetype.test);
			}
		} catch(IOException e) {
			System.err.println("Invalid Image Directory: " + e.getMessage());
			System.exit(4);
		}
	}
	
	/**
	 * Gets and returns the valid face files of the given directory name.
	 * @param dirName
	 */
	public static String[] getContents(String dirName) {
		File dir = new File(dirName);

		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		    	//only list non-hidden .txt files
		        return !name.startsWith(".") && name.endsWith(".txt");
		    }
		};
		
		return dir.list(filter);
	}
	
	/**
	 * Reads and stores the valid face files of the given directory name.
	 * @param dirName
	 * @throws IOException
	 */
	public static void read(String dirName, Facetype type) throws IOException {
		String[] images = getContents(dirName);
		
		if(images == null) {
			throw new IOException(dirName);
		}
		
		for(String img : images) {
			img = dirName + "/" + img;
			try {
				// TODO Store the images. This simply reads and discards them.
				new Face(img,readFile(img),type);
			} catch(IOException e) {
				System.err.println("Invalid Image File: " + e.getMessage());
				System.exit(4);
			}
		}
	}
	
	/**
	 * Efficiently read file and return string.
	 * http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
	 * @param path
	 * @throws IOException
	 */
	private static String readFile(String path) throws IOException {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		}
		finally {
			stream.close();
		}
	}
	
}
