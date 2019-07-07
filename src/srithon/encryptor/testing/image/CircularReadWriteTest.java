package srithon.encryptor.testing.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
		actualARGBTest();
	}
	
	/*
	 * Write bytes to an ARGB image
	 * and attempt to retrieve the same
	 * bytes from the output
	 */
	private static void actualARGBTest()
	{
		byte[] originalData = new byte[20000];
		new Random().nextBytes(originalData);
		
		int[] convertedData = new int[originalData.length / 4];
		int currentIndexInData = 0;
		for (int i = 0; i < convertedData.length; i++)
		{
			convertedData[i] =  (originalData[currentIndexInData++] & 0xFF) << 24;
			convertedData[i] |= (originalData[currentIndexInData++] & 0xFF) << 16;
			convertedData[i] |= (originalData[currentIndexInData++] & 0xFF) << 8;
			convertedData[i] |=  originalData[currentIndexInData++] & 0xFF;
			//why the hell does & 0xFF fix problems?
			//bytes are supposed to be 8 bits anyway so this shouldn't do anything??
		}
		System.out.println(Arrays.toString(Arrays.copyOf(originalData, 20)));
		byte[] recoveredData = new byte[convertedData.length * 4];
		int recDatInd = 0;
		for (int i = 0; i < convertedData.length; i++)
		{
			recoveredData[recDatInd++] = (byte) ((convertedData[i] & 0xFF000000) >> 24);
			recoveredData[recDatInd++] = (byte) ((convertedData[i] & 0x00FF0000) >> 16);
			recoveredData[recDatInd++] = (byte) ((convertedData[i] & 0x0000FF00) >> 8);
			recoveredData[recDatInd++] = (byte)  (convertedData[i] & 0x000000FF);
		}
		
		/*
		 * x = FF99FF55
		 * 1111_1111	1001_1001	1111_1111	0101_0101
		 * a)	(x & 0xFF000000) >> 24
		 * b) 	(x & 0x00FF0000) >> 16
		 * c)	(x & 0x0000FF00) >> 8
		 * d)	(x & 0x000000FF)
		 */
		//this is proof that recovering works
		System.out.println(Arrays.toString(Arrays.copyOf(recoveredData, 20)));
		
		BufferedImage encodedImage = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
		int[] encodedRaster = ((DataBufferInt) encodedImage.getRaster().getDataBuffer()).getData();
		
		System.out.println("Encoded raster length: " + encodedRaster.length);
		System.out.println("Converted data length: " + convertedData.length);
		
		for (int i = 0; i < convertedData.length; i++)
			encodedRaster[i] = convertedData[i];
		//write the encodedImage to file
		File encodedFile = new File("testIO\\testR\\CircularReadWrite.png");
		try
		{
			ImageIO.write(encodedImage, "png", encodedFile);
		}
		catch (IOException e)
		{
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
		
		byte[] readRaster = ((DataBufferByte) readImage.getRaster().getDataBuffer()).getData();
		for (int i = 1; i < readRaster.length - 2; i+=4)
		{
			byte t = readRaster[i];
			readRaster[i] = readRaster[i + 2];
			readRaster[i + 2] = t;
		}
		// every other byte is wrong?
		/*
		 * For some wonky reason, have to swap every pair of even indices
		 */
		
		System.out.println(Arrays.toString(Arrays.copyOf(originalData, 30)));
		System.out.println(Arrays.toString(Arrays.copyOf(readRaster, 30)));
		
		for (int i = 0; i < originalData.length; i++)
		{
			if (originalData[i] != readRaster[i])
			{
				int start = i - 4;
				int end = i + 4;
				if (end > originalData.length - 1)
					end = originalData.length - 1;
				
				System.out.println("Discrepancy at i = " + i);
				System.out.println(Arrays.toString(Arrays.copyOfRange(originalData, start, end)));
				System.out.println(Arrays.toString(Arrays.copyOfRange(readRaster, start, end)));
				System.out.println();
			}
		}
		
	}
	
	// originally intended to be for argb, but decided on type byte gray
	private static void largerScaleByteTest()
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
