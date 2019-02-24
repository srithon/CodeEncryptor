package srithon.encryptor.encryption;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Decryptor
{
	ArrayList<Character> replacedChars = new ArrayList<Character>();

	public static boolean decrypt(String filePath, String outputPath, char[] key)
	{
		int k = Handler.handleKey(key);
		boolean success = false;
		
		filePath = filePath.trim();
		outputPath = outputPath.trim();
		
		BufferedReader reader = null;

		try
		{
			reader = new BufferedReader(new FileReader(filePath));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		if (reader == null)
		{
			System.out.println("Reader is null!");
			return false;
		}

		String currentLine = "";
		
		String[] tempDir = outputPath.split("\\\\");
		String dir = "";
		
		for (int i = 0; i < tempDir.length - 1; i++)
		{
			dir += tempDir[i];
			dir += "\\";
		}

		new File(dir).mkdirs();

		BufferedWriter writer = null;

		try
		{
			writer = new BufferedWriter(new FileWriter(outputPath));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		if (writer == null)
		{
			System.out.println("Writer is null!");
			return false;
		}

		int numLine = 1;
		char[] chars = new char[0];
		long realCheckSum = 0;
		long experimentalCheckSum = 0;

		while (true)
		{
			try
			{
				currentLine = reader.readLine();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			if (currentLine != null)
			{
				for (char x : chars)
				{
					try
					{
						writer.append(x);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}

				if (numLine != 1)
				{
					try
					{
						writer.newLine();
						writer.flush();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			else
			{
				if (realCheckSum != 0)
				{
					experimentalCheckSum -= Handler.checkSum(chars);

					success = realCheckSum == experimentalCheckSum;
				}
				else
				{
					success = true;
				}

				break;
			}

			chars = currentLine.toCharArray();

			if (Character.isDigit(chars[0]) || chars[0] == '-')
			{
				try
				{
					realCheckSum = Long.parseLong(currentLine);
				}
				catch (NumberFormatException e)
				{
					realCheckSum = 0;
				}
			}

			for (int i = 0; i < chars.length; i++)
			{
				if (!Character.isWhitespace(chars[i]))
				{
					int charValue = ((36480 - (numLine * 10))
							-
							(((int) chars[i])
									+ i
									+ (chars.length * 2)
									+ numLine
									+ (i * (k / (int) (Math.pow(10, 3))) & (numLine * (k + (numLine * 6)))) % 10000
									+ ((k / 9000) - (Math.abs(~numLine) + (i * i)) % 10000)
									//+ Math.abs((int) (((4 * k) / numLine)) * (~k | (numLine * i) - ~numLine << (i % 4)))
									* (1 + (numLine % 2))));

					if (charValue < 0)
					{
						charValue = 0 - charValue;
					}

					chars[i] = (char) charValue;
				}
			}

			experimentalCheckSum += Handler.checkSum(chars);

			numLine++;
		}
		
		try
		{
			if (writer != null)
				writer.close();
			if (reader != null)
				reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return success;
	}
}
