package srithon.encryptor.encryption;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor
{
	private static final byte[] iv;
	private static final int GCM_TAG_LENGTH = 128;
	
	static
	{
		iv = new byte[16];
		for (byte i = 0; i < iv.length; i++)
		{
			iv[i] = (byte)(i + 1);
		}
	}
	
	public static void encrypt(String filePath, String outputPath, char[] key, Boolean doCheckSum)
	{
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
			try
			{
				output.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		FileOutputStream writer = null;
		
		try {
			writer = new FileOutputStream(outputPath);//, StandardCharsets.UTF_8));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (writer == null)
		{
			System.out.println("Writer is null!");
			return;
		}
		
		byte[] fileBytes = null;
		
		try {
			fileBytes = Files.readAllBytes(Path.of(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] iv = getIV();
		
		display("IV", iv);
		
		char[] fullKey = getFullKey(key);
		
		key = null;
		
		Cipher cipher = null;

        try
        {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        if (cipher == null)
        	return;
        
        try
        {
        	cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(String.valueOf(fullKey).getBytes(StandardCharsets.UTF_8), "AES"), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            //cipher.init(Cipher.ENCRYPT_MODE, getKey(fullKey));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        byte[] cipherText = null;
        
        try
        {
            cipherText = cipher.doFinal(fileBytes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        for (byte i : iv)
        {
        	try {
				writer.write((char) i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        display("TAG", Arrays.copyOfRange(cipherText, 0, 16));
        
        for (int i = cipherText.length - 16; i < cipherText.length; i++)
        {
        	try {
				writer.write((char) (cipherText[i] + 127));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        for (int i = 0; i < cipherText.length - 16; i++)
        {
        	try {
				writer.write((char) cipherText[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //return cipherText;
    }
	
	private static byte[] getIV()
	{
		SecureRandom rand = new SecureRandom();
		byte[] iv = new byte[12];
		
		for (int i = 0; i < iv.length; i++)
		{
			iv[i] = (byte) rand.nextInt(128);
			System.out.println(iv[i]);
		}
		
		return iv;
	}
	
	private static char[] getFullKey(char[] key)
	{
		char[] fullKey = new char[16];
		
		for (int i = 0; i < key.length; i++)
			fullKey[i] = key[i];
		for (int i = key.length; i < key.length; i++)
			fullKey[i] = (char) Math.pow(i, 2);
		
		return fullKey;
	}

    public static void decrypt(String encPath, String decPath, char[] key)
    {
        Cipher cipher = null;
        try
        {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        char[] fullKey = getFullKey(key);
        
        key = null;
        
        byte[] encrypted = null;
		try {
			encrypted = Files.readAllBytes(Path.of(encPath));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		byte[] iv = Arrays.copyOfRange(encrypted, 0, 12);
	    byte[] tag = Arrays.copyOfRange(encrypted, 12, 28);
	    
	    display("TAG", tag);
	    
	    for (int i = 0; i < tag.length; i++)
	    {
	    	if (tag[i] < 0)
	    	{
				tag[i] = (byte) (256 + (tag[i] - 127));
	    	}
			else
			{
				tag[i] = (byte) (tag[i] - 127);
			}
	    	
	    	encrypted[i] = tag[i];
	    }
	    
	    display("IV", iv);
	    display("TAG", tag);
	    
	    //byte[] text = Arrays.copyOfRange(encrypted, 27, encrypted.length);

        try
        {
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(String.copyValueOf(fullKey).getBytes(StandardCharsets.UTF_8), "AES"), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        /*byte[] encryptedWOIV = new byte[encrypted.length - 12];
        
        for (int i = 12; i < encrypted.length; i++)
        {
        	encryptedWOIV[i - 12] = encrypted[i];
        }*/

        byte[] decrypted = null;
        
        byte[] encryptedText = Arrays.copyOfRange(encrypted, 28, encrypted.length);
        
        display("ENC", encryptedText);
        
        try
        {
        	//cipher.update(encrypted, 27, encrypted.length - 27);
        	cipher.update(encryptedText);
        	decrypted = cipher.doFinal(tag);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        File outputFile = new File(decPath);
        
        BufferedWriter writer = null;
        
        try {
			writer = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        for (byte b : decrypted)
        {
        	try {
				writer.write((char) b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    private static void display(String name, byte[] ar)
	{
    	System.out.print(name + " (" + ar.length + "}: ");
		for (byte j : ar)
		{
			System.out.print(j);
			System.out.print(" ");
		}
		
		System.out.println();
	}
    
	private static SecretKey getKey(char[] key)
    {
        byte[] keyBytes = new byte[key.length];
        
        for (int i = 0; i < keyBytes.length; i++)
        	keyBytes[i] = (byte) key[i];

        return new SecretKeySpec(keyBytes, "AES");
    }
	
	public static String encryptedCheckSum(long checkSum, int numLine)
	{
		char[] chars = String.valueOf(checkSum).toCharArray();
		
		for (int i = 0; i < chars.length; i++)
		{
			chars[i] = (char)
			((28653 - (numLine * 6)) -
				(((int) chars[i])
					+ i
					+ (chars.length * 2)
					+ numLine
					+ (i * (14327 / (int) (Math.pow(10, 3))) & (numLine * (60593 + (numLine * 6)))) % 10000
					+ ((4500 / (i + 1)) - (Math.abs(~numLine) + (i * i)) % 10000)
					* (2 + (numLine % 2))));
		}
		
		return String.valueOf(chars);
	}
	
	/*public static void main(String[] args)
	{
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
	}*/
	
	public static void main(String[] args)
	{
		char[] key = new char[] { 'a', 'b', 'c' };
		
		String inputPath = "C:\\Development\\Java\\CodeEncryptor\\testIO\\testL\\input.txt";
		String outputPath = "C:\\Development\\Java\\CodeEncryptor\\testIO\\testL\\output.txt";
		encrypt(inputPath, outputPath, key, false);
		
		String encPath = "C:\\\\Development\\\\Java\\\\CodeEncryptor\\\\testIO\\\\testL\\\\output.txt";
		String decPath = "C:\\\\Development\\\\Java\\\\CodeEncryptor\\\\testIO\\\\testL\\\\outputDec.txt";
		decrypt(encPath, decPath, key);
	}
}
