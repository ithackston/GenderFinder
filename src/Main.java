
public class Main {
	/// Prints the commandline instructions.
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
	 * @param args
	 */
	public static void main(String[] args) {
		// Parse through the command line arguments
		try
		{
			int i = 0;
			while(i < args.length)
			{
				if(args[i].equalsIgnoreCase("-m")) {
					//args[i + 1];
				} else if(args[i].equalsIgnoreCase("-f")) {
					//args[i + 1];
				} else if(args[i].equalsIgnoreCase("-t")) {
					//args[i + 1];
				} else if(args[i].equalsIgnoreCase("-train")) {
					//train
				} else if(args[i].equalsIgnoreCase("-test")) {
					//test
				} else if(args[i].equalsIgnoreCase("--help")){
					helpPrinter();
					System.exit(0);
				} else
					throw new IllegalArgumentException();
				i += 2;
			}
		}
		catch(IllegalArgumentException ia)
		{
			System.err.println("Invalid Arguments: " + ia.getMessage());
			System.exit(4);
		}
	}

}
