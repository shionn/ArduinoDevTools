package generator;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Rgb565 {

	public static void main(String[] args) throws IOException {
		convert("mimic.bmp");
		convert("wifi.bmp");
	}

	private static void convert(String filename) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(new File(filename));
		try (FileWriter fw = new FileWriter(filename.split("\\.")[0] + ".h");
				BufferedWriter bf = new BufferedWriter(fw)) {
			bf.write("#include <Arduino.h>");
			bf.newLine();
			bf.newLine();
			bf.write("static uint16_t PROGMEM " + filename.split("\\.")[0] + "[] = { ");
			bf.newLine();
			bf
					.write(Integer.toString(bufferedImage.getWidth()) + ", "
							+ Integer.toString(bufferedImage.getHeight()) + ", ");
			bf.newLine();

			int c = 0;
			for (int y = 0; y < bufferedImage.getHeight(); y++) {
				for (int x = 0; x < bufferedImage.getWidth(); x++) {
					int rgb = bufferedImage.getRGB(x, y);
					bf.write("0x" + Integer.toHexString(rgbToRgb565(rgb)) + ", ");
					if (++c >= 8) {
						bf.newLine();
						c = 0;
					}
				}
			}
			bf.write("};");
			bf.newLine();
		}
	}

	public static int rgbToRgb565(int rgb) {
		// Extract the red, green, and blue components
		int r = (rgb >> 16) & 0xFF; // Red component
		int g = (rgb >> 8) & 0xFF; // Green component
		int b = rgb & 0xFF; // Blue component

		// Convert to RGB565
		int r565 = (r >> 3) & 0x1F; // 5 bits for red
		int g565 = (g >> 2) & 0x3F; // 6 bits for green
		int b565 = (b >> 3) & 0x1F; // 5 bits for blue

		// Combine into a single 16-bit value
		return (r565 << 11) | (g565 << 5) | b565;
	}

}
