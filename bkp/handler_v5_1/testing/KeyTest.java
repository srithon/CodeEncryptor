package srithon.encryptor.testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import srithon.encryptor.encryption.Handler;

public class KeyTest
{
	private static BufferedWriter writer;
	
	static
	{
		File output = new File((System.getProperty("user.dir") + File.separatorChar), "output.txt");
		
		if (!output.exists())
		{
			try {
				output.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try
		{
			writer = new BufferedWriter(new FileWriter(output));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		char[] validChars = new char[47];
		
		for (int i = 0; i < 10; i++)
		{
			validChars[i] = (char) (48 + i);
		}
		
		for (int i = 0; i < 26; i++)
		{
			validChars[i + 10] = (char) (97 + i);
		}
		
		validChars[36] = '\'';
		
		for (int i = 0; i < 4; i++)
		{
			validChars[i + 37] = (char) (44 + i);
		}
		
		validChars[41] = ';';
		
		for (int i = 0; i < 3; i++)
		{
			validChars[i + 42] = (char) (91 + i);
		}
		
		validChars[45] = '`';
		validChars[46] = '=';
		
		System.out.println(Arrays.toString(validChars));
		
		//printPermutation(validChars, 0, 10);
		
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void printPermutation(char[] a, int startIndex, int endIndex) {
		if (startIndex == endIndex)
		{
			try
			{
				writer.write(String.valueOf(Handler.handleKey(a)));
				writer.newLine();
				writer.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
	    }
		else {
	        //try to move the swap window from start index to end index
	        //i.e 0 to a.length-1
	        for (int x = startIndex; x < endIndex; x++) {
	            swap(a, startIndex, x);
	            printPermutation(a, startIndex + 1, endIndex);
	            swap(a, startIndex, x);
	        }
	    }
	}
	
	private static void swap(char[] a, int i, int x) {
	    char t = a[i];
	    a[i] = a[x];
	    a[x] = t;
	}
}