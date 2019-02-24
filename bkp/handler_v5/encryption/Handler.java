package srithon.encryptor.encryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Handler
{
	private static BufferedReader reader = null;

	private static String lastType;

	private static String path = null;
	
	private static char[] key;
	
	public static final int weightingACap, weightingBCap;
	
	static
	{
		key = new char[0];
		
		weightingACap = 10000;
		weightingBCap = 10000;
	}
	
	public static void handle(String type)
	{
		if (type == null)
			return;
		
		String inputPath = "";
		String outputPath = "";

		if (type.startsWith("--"))
		{
			try
			{
				inputPath = reader.readLine();
				outputPath = reader.readLine();
			}
			catch (IOException e)
			{

			}
		}
		else
		{
			inputPath = type;
			type = lastType;

			try {
				outputPath = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (path != null)
		{
			if (!inputPath.trim().substring(0, 3).contains(":"))
			{
				inputPath = path + inputPath;
			}
			
			if (!outputPath.trim().substring(0, 3).contains(":"))
			{
				outputPath = path + outputPath;
			}
		}

		if (type.contains("encrypt"))
		{
			Encryptor.encrypt(inputPath, outputPath, key);
		}
		else if (type.contains("decrypt"))
		{
			Decryptor.decrypt(inputPath, outputPath, key);
		}
		else
		{
			return;
		}

		lastType = type;
	}
	
	public static void handleRaw(File input, File output, boolean encrypt)
	{
		if (encrypt)
		{
			Encryptor.encrypt(input.getAbsolutePath(), output.getAbsolutePath(), key);
		}
		else
		{
			Decryptor.decrypt(input.getAbsolutePath(), output.getAbsolutePath(), key);
		}
	}

	public static void main(String[] args)
	{
		//args = new String[] {"C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testD\\instructions.txt"};

		//System.out.println(Arrays.toString(new File("C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testF\\").listFiles()));
		
		try {
			reader = new BufferedReader(new FileReader(args[0].trim()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String line = "";

		do
		{
			try {
				line = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (line != null && line.trim().length() == 0);
		
		if (line.startsWith("path"))
		{
			String[] temp = line.split("=");
			
			if (temp.length > 1)
			{
				path = temp[1];
				
				if (!path.endsWith("\\"))
				{
					path += "\\";
				}
			}
			else
			{
				handle(line);
			}
		}
		
		while (line != null)
		{
			do
			{
				try {
					line = reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (line != null && line.trim().length() == 0);

			/*while (line != null && line.trim().length() == 0)
			{
				try {
					reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			
			handle(line);
		}
	}
	
	public static int handleKey(char[] key)
	{
		int keyVal = 0;
		
		for (int i = 1; i < key.length; i++)
		{
			keyVal += ((((int) key[i] * 12) | (int) (key[i - 1] * 8)) * (key.length * 10 * (i > 0 ? i : 1)));
		}
		
		System.out.println(keyVal);
		
		return keyVal;
	}
	
	public static void setKey(char[] key)
	{
		Handler.key = key;
	}
}
