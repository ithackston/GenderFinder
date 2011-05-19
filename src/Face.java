import java.io.IOException;

/**
 * Stores a single face image.
 * @param args
 */
public class Face {
	public static enum Facetype {male,female,test};
	public static final int width = 128;
	public static final int height = 120;
	public Facetype type;
	private int[][] grid;
	private String name;
	
	public Face(String name, String raw, Facetype type) throws IOException {
		this.grid = new int[height][width]; //row major grid (grid[0][1] = row 0, col 1)
		this.name = name;
		this.type = type;
		
		//parse grid
		String[] rawgrid = raw.split("[ \n\r]+");
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				grid[i][j] = Integer.valueOf(rawgrid[width * i + j]);
			}
		}
	}
	
	public void printName() {System.out.println(name);} //used in debugging
}