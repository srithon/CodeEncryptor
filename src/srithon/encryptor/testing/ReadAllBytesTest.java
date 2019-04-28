package srithon.encryptor.testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadAllBytesTest {
	public static void main(String[] args)
	{
		File outputFile = new File("readAllBytesTest.txt");
		
		if (!outputFile.exists())
		{
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		byte[] init = { 1, 2, 3, 4, 5, 6, 7, 8 };
		
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (byte i : init)
		{
			try
			{
				writer.write((char) i);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] fileBytes = null;
		
		try {
			fileBytes = Files.readAllBytes(outputFile.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		display(init);
		display(fileBytes);
	}
	
	private static void display(byte[] ar)
	{
		for (byte j : ar)
		{
			System.out.print(j);
			System.out.print(" ");
		}
		
		System.out.println();
	}
}
