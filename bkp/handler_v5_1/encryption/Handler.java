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
	
	public static final int WEIGHTING_A_CAP;
	public static final int WEIGHTING_B_CAP;
	
	static
	{
		key = new char[0];
		
		WEIGHTING_A_CAP = 10000;
		WEIGHTING_B_CAP = 10000;
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

			try
			{
				outputPath = reader.readLine();
			}
			catch (IOException e)
			{
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
			Encryptor.encrypt(inputPath, outputPath, key, true);
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
	
	public static boolean handleRaw(File input, File output, Boolean encrypt)
	{
		if (Boolean.TRUE.equals(encrypt) || (encrypt == null))
		{
			Encryptor.encrypt(input.getAbsolutePath(), output.getAbsolutePath(), key, encrypt);
			return true;
		}
		else
		{
			return Decryptor.decrypt(input.getAbsolutePath(), output.getAbsolutePath(), key);
		}
	}
	
	public static short checkSum(char[] chars)
	{
		short sum = 0;
		
		for (char x : chars)
		{
			sum += sum ^ (short) x;
		}
		
		return sum;
	}

	public static void main(String[] args)
	{
		try
		{
			reader = new BufferedReader(new FileReader(args[0].trim()));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		String line = "";

		do
		{
			try
			{
				line = reader.readLine();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} while (line != null && line.trim().length() == 0);
		
		if (line == null)
			return;
		
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
				try
				{
					line = reader.readLine();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			} while (line != null && line.trim().length() == 0);
			
			handle(line);
		}
	}
	
	public static int handleKey(char[] key)
	{
		int keyVal = 0;
		
		for (int i = 1; i < key.length; i++)
		{
			keyVal += ((((int) key[i] * 12) | ((int) key[i - 1] * 8)) * (key.length * 10 * (i > 0 ? i : 1)));
		}
		
		System.out.println(keyVal);
		
		return keyVal;
	}
	
	public static void setKey(char[] key)
	{
		Handler.key = key;
	}
}
