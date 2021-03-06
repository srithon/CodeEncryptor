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

	public static void decrypt(String filePath, String outputPath, char[] key)
	{
		int k = Handler.handleKey(key);
		
		filePath = filePath.trim();
		outputPath = outputPath.trim();
		
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String currentLine = "";
		/*char firstChar = '0';

		while (firstChar != (char) 59)
		{
			try {
				currentLine = reader.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (currentLine.length() == 0)
				continue;
			else
				firstChar = currentLine.charAt(0);
		}*/

		/*while (true)
		{
			try {
				currentLine = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (currentLine == null)
				break;

			char[] characters = currentLine.toCharArray();

			int x = 0;
			
			for (int i = 0; i < characters.length; i += x)
			{
				replacedChars.add(characters[i]);
				
				x++;
			}
		}*/

		/*try {
			reader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		/*File output = new File(filePath);
		
		if (!output.exists())
		{
			try {
				output.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		//File output = new File(outputPath);
		String[] tempDir = outputPath.split("\\\\");
		String dir = "";
		
		for (int i = 0; i < tempDir.length - 1; i++)
		{
			dir += tempDir[i];
			dir += "\\";
		}

		/*System.out.println(filePath);
		System.out.println(outputPath);

		System.out.println(dir);*/
		
		new File(dir).mkdirs();

		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(outputPath));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//Iterator iter = new Iterator();
		
		int numLine = 1;
		char[] chars = new char[0];

		all:
		{
			while (true)
			{
				try {
					currentLine = reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for (char x : chars)
				{
					try {
						writer.append(x);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (currentLine == null)
					break all;
				else
				{
					if (numLine != 1)
					{
						try
						{
							writer.newLine();
							writer.flush();
						}
						catch (IOException e)
						{
							
						}
					}
				}

				chars = currentLine.toCharArray();

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
										+ ((k / 9000) - ((int) Math.abs(~numLine) + (i * i)) % 10000)
										//+ Math.abs((int) (((4 * k) / numLine)) * (~k | (numLine * i) - ~numLine << (i % 4)))
										* (1 + (numLine % 2))));
						
						if (charValue < 0)
						{
							/*System.out.println(numLine);
							System.out.println(currentLine);
							System.out.println(chars[i]);
							System.out.println((int) chars[i]);
							
							try
							{
								Thread.sleep(5000);
							}
							catch (InterruptedException e)
							{
								
							}*/
							
							charValue = 0 - charValue;
						}
						
						//System.out.println(charValue);
						chars[i] = (char) charValue;
								;
						/*chars[i] = (char)
								(-(((int) chars[i]) - numLine) /
								((1 + (numLine % 2) * (1280 - 10*numLine))
								- i - chars.length*2));*/
					}

					/*if (iter.hasNext())
						chars[i] = iter.next();
					else
					{
						break all;
					}*/
				}

				/*String encryptedLine = "";

				for (char x : chars)
				{
					encryptedLine += x;
				}*/

				/*try {
					writer.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				numLine++;
			}
		}
		
		try {
			writer.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*private class Iterator
	{
		private int index = 0;

		public char next()
		{
			return replacedChars.get(index++);
		}

		public boolean hasNext()
		{
			return replacedChars.size() > index;
		}
	}*/

	/*public static void main(String[] args)
	{
		//new Decryptor("C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\");
		//new Decryptor(args[0], args[1]);
		//new Decryptor("C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testH\\CodeEncryptor\\enen.txt", "C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testH\\CodeEncryptor\\ende.txt", new char[] {'a', 'b', 'c', 'd'});
		//new Decryptor("C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testC\\encrypted.txt",
				//"C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testC\\unencryptedOutput.txt");
	}*/
}
