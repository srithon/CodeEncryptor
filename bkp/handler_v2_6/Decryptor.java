package srithon.encryptor;

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

	public Decryptor(String filePath, String outputPath)
	{
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
		char firstChar = '0';

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
		}

		while (true)
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
		}

		try {
			reader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

		Iterator iter = new Iterator();

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

				if (currentLine == null)
					break;

				char[] chars = currentLine.toCharArray();

				for (int i = 0; i < chars.length; i+=2)
				{
					if (Character.isWhitespace(chars[i]))
						continue;

					if (iter.hasNext())
						chars[i] = iter.next();
					else
					{
						break all;
					}
				}

				/*String encryptedLine = "";

				for (char x : chars)
				{
					encryptedLine += x;
				}*/

				for (char x : chars)
				{
					try {
						writer.append(x);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				try {
					writer.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

	private class Iterator
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
	}

	public static void main(String[] args)
	{
		//new Decryptor("C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\");
		new Decryptor(args[0], args[1]);
		//new Decryptor("C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testC\\encrypted.txt",
				//"C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testC\\unencryptedOutput.txt");
	}
}
