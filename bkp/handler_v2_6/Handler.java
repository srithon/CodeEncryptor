package srithon.encryptor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Handler
{
	private static BufferedReader reader = null;

	private static String lastType;

	private static String path = null;
	
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
			new Encryptor(inputPath, outputPath);
		}
		else if (type.contains("decrypt"))
		{
			new Decryptor(inputPath, outputPath);
		}

		lastType = type;
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
}
