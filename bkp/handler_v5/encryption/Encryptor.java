package srithon.encryptor.encryption;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Encryptor
{
	ArrayList<Character> goneChars = new ArrayList<Character>();
	
	public static void encrypt(String filePath, String outputPath, char[] key)
	{
		int k = Handler.handleKey(key);
		
		filePath = filePath.trim();
		outputPath = outputPath.trim();
		
		//File output = new File(outputPath);
		String[] tempDir = outputPath.split("\\\\");
		String dir = "";
		
		for (short i = 0; i < tempDir.length - 1; i++)
		{
			dir += tempDir[i];
			dir += "\\";
		}

		/*System.out.println(filePath);
		System.out.println(outputPath);

		System.out.println(dir);*/
		
		new File(dir).mkdirs();
		
		File output = new File(dir, tempDir[tempDir.length - 1]);

		/*System.out.println(dir);
		System.out.println(tempDir[tempDir.length - 1]);*/
		
		if (!output.exists())
		{
			try {
				output.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		tempDir = filePath.split("\\\\");
		dir = "";
		
		for (short i = 0; i < tempDir.length - 1; i++)
		{
			dir += tempDir[i];
			dir += "\\";
		}
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		dir = dir.trim();
		
		File inputDirectory = new File(dir);
		/*System.out.println(dir);
		System.out.println(Arrays.toString(inputDirectory.listFiles()));*/
		
		try {
			reader = new BufferedReader(new FileReader(new File(inputDirectory, tempDir[tempDir.length - 1])));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			writer = new BufferedWriter(new FileWriter(outputPath));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String currentLine = "";
		short numLine = 1;
		char[] chars = new char[0];
		
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
			{
				break;
			}
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
					chars[i] = (char)
							((36480 - (numLine * 10)) -
								(((int) chars[i])
									+ i
									+ (chars.length * 2)
									+ numLine
									+ (i * (k / (int) (Math.pow(10, 3))) & (numLine * (k + (numLine * 6)))) % Handler.weightingACap
									+ ((k / 9000) - ((int) Math.abs(~numLine) + (i * i)) % Handler.weightingBCap) //fix the parentheses or no?
									//+ Math.abs((int) (((4 * k) / numLine)) * (~k | (numLine * i) - ~numLine << (i % 4)))
									* (1 + (numLine % 2))));
				}
			}
			
			/*String encryptedLine = "";
			
			for (char x : chars)
			{
				encryptedLine += x;
			}*/
			
			/*for (char x : chars)
			{
				try {
					writer.append(x);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			
			numLine++;
		}
		
		/*try {
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*try
		{
			writer.append((char) 59);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		for (int i = 0; i < goneChars.size(); i++)
		{
			if (i % 14 == 0)
			{
				try
				{
					writer.newLine();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			try
			{
				writer.append(goneChars.get(i));
				
				for (int j = 0; j < i % 14; j++)
					writer.append(randomChar());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}*/
		
		/*for (char x : goneChars)
		{
			try {
				writer.append(x);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		try {
			writer.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static char randomChar()
	{
		char ret;
		
		do
		{
			ret = (char)(Math.random() * 256 + 60);
		} while (Character.isWhitespace(ret));
		
		return ret;
	}
	
	public static void main(String[] args)
	{
		//new Encryptor("C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testC\\source.txt",
			//	"C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testC\\encrypted.txt");
		//System.out.println((char) 317);
		//new Encryptor(args[0], args[1]);
		//new Encryptor("C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testH\\CodeEncryptor\\en.txt", "C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testH\\CodeEncryptor\\enen.txt", new char[] {'a', 'b', 'c', 'd'});
		
		int k = 2927120;
		int numLine = 179;
		
		k =0;
		
		for (int i = 0; i < 10; i++)
		{
			//System.out.println(Math.abs((int) (((4 * k) / numLine)) * (~k | (numLine * i) - ~numLine << (i % 4))));
		
			System.out.println((i * (k / (int) (Math.pow(10, 3))) & (numLine * (k + (numLine * 6)))) % 10000
			+ ((k / 9000) - ((int) Math.abs(~numLine) + (i * i)) % 10000));//Math.abs((int) (((4 * k) / numLine)) * (~k | (numLine * i) - ~numLine << (i % 4)))
			
		}
		
		k = Handler.handleKey(new char[] { 'h', 'c', 'x', 'y' });
		
		System.out.println("NEW\n\n");
		
		for (int i = 0; i < 10; i++)
		{
			//System.out.println(Math.abs((int) (((4 * k) / numLine)) * (~k | (numLine * i) - ~numLine << (i % 4))));
		
			System.out.println((i * (k / (int) (Math.pow(10, 3))) & (numLine * (k + (numLine * 6))))
			+ (k / 9000) - ((int) Math.abs(~numLine) + (i * i)));//Math.abs((int) (((4 * k) / numLine)) * (~k | (numLine * i) - ~numLine << (i % 4)))
			
		}
	}
}
