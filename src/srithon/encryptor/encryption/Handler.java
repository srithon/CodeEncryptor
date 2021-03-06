package srithon.encryptor.encryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.crypto.spec.SecretKeySpec;

public class Handler
{
	private static BufferedReader reader = null;

	private static String lastType;

	private static String path = null;
	
	private static SecretKeySpec key;
	
	private static List<String> imageExtensions;
	
	static
	{
		key = null;
		
		imageExtensions = new ArrayList<>(Arrays.asList("jpg", "jpeg", "png", "bmp", "tiff"));
	}
	
	public static boolean isImage(String ext)
	{
		return imageExtensions.contains(ext.toLowerCase());
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
			Encryptor.decrypt(inputPath, outputPath, key);
		}
		else
		{
			return;
		}

		lastType = type;
	}
	
	public static boolean keyIsValid()
	{
		return key != null;
	}
	
	public static Boolean handleRaw(File input, File output, Boolean encrypt)
	{
		if (Boolean.TRUE.equals(encrypt) || (encrypt == null))
		{
			Encryptor.encrypt(input.getAbsolutePath(), output.getAbsolutePath(), key, encrypt);
			return true;
		}
		else
		{
			return Encryptor.decrypt(input.getAbsolutePath(), output.getAbsolutePath(), key);
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
	
	public static void setKey(SecretKeySpec key)
	{
		Handler.key = key;
	}
}
