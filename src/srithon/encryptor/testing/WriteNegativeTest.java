package srithon.encryptor.testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class WriteNegativeTest {
	public static void main(String[] args) throws IOException
	{
		File output = new File("WriteNegativeTest.txt");
		
		if (!output.exists())
			output.createNewFile();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		
		byte[] init = {-127, -125, -123, -121, 100};
		
		for (byte i : init)
		{
			writer.write((char) i + 127);
		}
		
		writer.close();
		
		byte[] post = Files.readAllBytes(output.toPath());
		
		display("init", init);
		displayB("post", post);
	}
	
	private static final void display(String tag, byte[] arr)
	{
		System.out.print(tag + " (" + arr.length + "): ");
		
		for (byte i : arr)
		{
			System.out.print(i);
			System.out.print(" ");
		}
		
		System.out.println();
	}
	
	private static final void displayB(String tag, byte[] arr)
	{
		System.out.print(tag + " (" + arr.length + "): ");
		
		for (byte i : arr)
		{
			if (i < 0)
				System.out.print(256 + (i - 127));
			else
				System.out.print(i - 127);
			System.out.print(" ");
		}
		
		System.out.println();
	}
}
