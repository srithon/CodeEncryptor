package srithon.encryptor.testing.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/*
 * Write data to an image and read it back
 * See if the data can be recovered from the output
 */
public class CircularReadWriteTest
{
	public static void main(String[] args)
	{
		argbTest();
	}
	
	// originally intended to be for argb, but decided on type byte gray
	private static void argbTest()
	{
		//fill byte array with random bytes
		byte[] data = new byte[400];
		new Random().nextBytes(data);
		
		/*
		// 4 bytes per int
		int[] convertedData = new int[100];
		int currentIndexInData = 0;
		for (int i = 0; i < convertedData.length; i++)
		{
			convertedData[i] = (data[currentIndexInData++] << 24);
			convertedData[i] |= (data[currentIndexInData++] << 16);
			convertedData[i] |= (data[currentIndexInData++] << 8);
			convertedData[i] |= (data[currentIndexInData++]);
		}
		*/
		
		BufferedImage encodedImage = new BufferedImage(25, 16, BufferedImage.TYPE_BYTE_GRAY);
		/*
		// transfer data integers to image raster
		
		int[] encodedImageRaster = ((DataBufferInt) encodedImage.getRaster().getDataBuffer()).getData();
		for (int i = 0; i < convertedData.length; i++)
			encodedImageRaster[i] = convertedData[i];
		*/
		
		byte[] encodedImageRaster = ((DataBufferByte) encodedImage.getRaster().getDataBuffer()).getData();
		for (int i = 0; i < data.length; i++)
			encodedImageRaster[i] = data[i];
		
		// write image to file
		File encodedFile = new File("CircularReadWriteARGBTest.png");
		
		try
		{
			ImageIO.write(encodedImage, "png", encodedFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		// read back the image
		BufferedImage readImage = null;
		try
		{
			readImage = ImageIO.read(encodedFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		// print out the read raster and compare to original data
		byte[] readRaster = ((DataBufferByte) readImage.getRaster().getDataBuffer()).getData();
		
		System.out.println(readRaster.length);
		
		display("Original Data", data);
		display("Read Data", readRaster);
		
		for (int i = 0; i < readRaster.length; i++)
		{
			if (readRaster[i] != data[i])
				System.out.println("!! (" + i + ")");
		}
	}
	
	// Successful
	private static void byteTest()
	{
		byte[] data = {1, 3, 5, 7, 9, 1, 2, 3, 5, 7, 9, 1};
		BufferedImage encodedImage = new BufferedImage(3, 4, BufferedImage.TYPE_BYTE_GRAY);
		byte[] encodedImageBuffer  = ((DataBufferByte) encodedImage.getRaster().getDataBuffer()).getData();
		for (int i = 0; i < data.length; i++)
			encodedImageBuffer[i] = data[i];
		
		File encodedFile = new File("CircularReadWriteTestEncoded.png");
		
		try {
			ImageIO.write(encodedImage, "png", encodedFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		BufferedImage readImage = null;
		
		try
		{
			readImage = ImageIO.read(encodedFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		byte[] readData = ((DataBufferByte) readImage.getRaster().getDataBuffer()).getData();
		
		display("Original Data", data);
		display("Read Data", readData);
	}
	
	/*
	 * Prints out an array preceded by an identifier
	 */
	private static void display(String tag, byte[] arr)
	{
		System.out.print(tag + ": ");
		for (int i = 0; i < arr.length; i++)
		{
			System.out.print(arr[i]);
			if (i != arr.length - 1)
				System.out.print(", ");
			else
				System.out.println();
		}
	}
	
	private static void display(String tag, int[] arr)
	{
		System.out.print(tag + ": ");
		for (int i = 0; i < arr.length; i++)
		{
			System.out.print(arr[i]);
			if (i != arr.length - 1)
				System.out.print(", ");
			else
				System.out.println();
		}
	}
}
