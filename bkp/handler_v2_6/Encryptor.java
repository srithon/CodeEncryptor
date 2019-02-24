package srithon.encryptor;

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
	
	public Encryptor(String filePath, String outputPath)
	{
		filePath = filePath.trim();
		outputPath = outputPath.trim();
		
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
		
		for (int i = 0; i < tempDir.length - 1; i++)
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
				
				goneChars.add(chars[i]);
				chars[i] = randomChar();
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
		
		try {
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try
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
		}
		
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
		new Encryptor(args[0], args[1]);
	}
}
