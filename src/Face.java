import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * Stores a single face image.
 * @param args
 */
public class Face {
	public static enum Facetype {male,female,test};
	public static final int width = 128;
	public static final int height = 120;
	public static final int area = width*height;
	public Facetype type;
	public int[] grid;
	public String name;
	
	/**
	 * Face constructor.
	 * @param name
	 * @param raw
	 * @param type
	 * @throws Exception
	 */
	public Face(String name, String raw, Facetype type) throws Exception {
		this.grid = new int[area];
		this.name = name;
		this.type = type;
		
		//parse grid
		String[] rawgrid = raw.split("[ \n\r]+");
		
		//copy as integers
		for(int i = 0; i < area; i++) {
			if(i > rawgrid.length - 1) {
				throw new Exception("Incomplete input data. Not enough pixels.");
			}
			grid[i] = Integer.valueOf(rawgrid[i]);
		}
	}
	
	/**
	 * Face constructor.
	 * @param x
	 * @param y
	 * @throws Exception
	 */
	public int getPixel(int x, int y) throws Exception {
		if(x > width || y > height || x < 1 || y < 1) {
			throw new Exception("Coordinates out of bounds.");
		}
		
		//1,1 is the top left pixel; width,height bottom right
		return grid[width * (y - 1) + (x - 1)]; 
	}
	
	public String toString() {
		return name;
	}
	
	public void toImage() {
		BufferedImage img = new BufferedImage(width+1,height+1,BufferedImage.TYPE_INT_RGB );
		File imgfile = new File(name.replace(".txt", ".png"));
		for(int x = 1; x <= width; x++) {
			for(int y = 1; y <= height; y++) {
				try {
					int val = getPixel(x,y);
					int rgb = (255 << 24) | (val << 16) | (val << 8) | val; 
					img.setRGB(x, y, rgb);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("x:"+x+" y:"+y);
				}
			}
		}
		try {
			ImageIO.write(img, "png", imgfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}