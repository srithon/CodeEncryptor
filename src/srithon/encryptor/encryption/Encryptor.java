package srithon.encryptor.encryption;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

public class Encryptor
{
	private static final int GCM_TAG_LENGTH = 128;
	
	public static void encrypt(String filePath, String outputPath, SecretKeySpec key, Boolean doCheckSum)
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
		
		OutputStream writer = null;
		
		boolean isImage = Handler.isImage(outputPath.substring(outputPath.lastIndexOf('.') + 1));
		
		if (!isImage)
		{
			try {
				writer = new FileOutputStream(outputPath);//, StandardCharsets.UTF_8));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
		{
			System.out.println("ImageWriter");
			writer = new EncryptedImageWriter(filePath, outputPath);
		}
		
		if (writer == null)
		{
			System.out.println("Writer is null!");
			return;
		}
		
		byte[] fileBytes = null;
		
		if (isImage)
		{
			try
			{
				fileBytes = ((DataBufferByte) ImageIO.read(new File(filePath)).getRaster().getDataBuffer()).getData();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try {
				fileBytes = Files.readAllBytes(Path.of(filePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Input Size: " + fileBytes.length + " bytes");
		
		byte[] iv = getIV();
		
		display("IV", iv);
		
		//char[] fullKey = getFullKey(key);
		
		//key = null;
		
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
        	cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
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
        
        System.out.println("Cipher Text Length: " + cipherText.length);
        
        if (isImage)
        {
        	// length and width
        	
        }
        
        if (isImage)
        {
        	int i = 0;
        	while (i < iv.length)
        	{
        		int integerToWrite = (iv[i++] >> 24) & 0xFF000000;
        		integerToWrite |= (iv[i++] >> 16) & 0x00FF0000;
        		integerToWrite |= (iv[i++] >> 8) & 0x0000FF00;
        		integerToWrite |= (iv[i++] & 0x000000FF);
        		try {
					writer.write(integerToWrite);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
        else
	        for (byte i : iv)
	        {
	        	try {
					writer.write((char) i);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
        
        display("ENCRYPTING TAG", Arrays.copyOfRange(cipherText, 0, 16));
        
        if (isImage)
        {
        	int i = cipherText.length - 16;
        	/*
        	 * TODO
        	 * replace this with call to 
        	 * EncryptedImageWriter.getARGB()
        	 */
        	while(i < cipherText.length)
	        {
        		int integerToWrite = (cipherText[i++] >> 24) & 0xFF000000;
	    		integerToWrite |= (cipherText[i++] >> 16) & 0x00FF0000;
	    		integerToWrite |= (cipherText[i++] >> 8) & 0x0000FF00;
	    		integerToWrite |= (cipherText[i++] & 0x000000FF);
	    		try {
					writer.write((byte) (integerToWrite) + 127);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	/*try {
					writer.write((char) (cipherText[i] + 127));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	        }
        }
        else
	        for (int i = cipherText.length - 16; i < cipherText.length; i++)
	        {
	        	try {
					writer.write((char) (cipherText[i] + 127));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
        
        if (isImage)
        {
        	/*
        	 * Do writing in blocks of 4
        	 * Get 4 integers and then write the result
        	 * of getARGB(...);
        	 * When at the end, fill in missing with 0s
        	 */
            int cipherTextInd;
            
            for (cipherTextInd = 0; cipherTextInd < cipherText.length - 16; cipherTextInd+=4)
            {
            	try {
    				writer.write(EncryptedImageWriter.getARGB(cipherText[cipherTextInd], cipherText[cipherTextInd+1], cipherText[cipherTextInd+2], cipherText[cipherTextInd+3]));
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
            }
            
            /*
             * 0 1 2 3 4 5 6 7 8 9 10 11 12 13 ... 45 46 47 48 49 50
             * 0 -> 34 (non-inc)
             * 0 -> 33 inclusive
             * 0 4 8 12 16 20 24 28 32; 36
             * remainder should be 1
             * 50 - 17 - 32 = 1
             */
            
            cipherTextInd -= 4;
            
            int remainder = cipherText.length - 17 - cipherTextInd;
            
            if (remainder > 0)
            {
            	int[] finalARGB = new int[3];
            	finalARGB[0] = cipherText[cipherTextInd + 1];
            	if (remainder > 1)
            	{
            		finalARGB[1] = cipherText[cipherTextInd + 2];
            		if (remainder > 2)
            		{
            			finalARGB[2] = cipherText[cipherTextInd + 3];
            		}
            	}
            	try
            	{
            		writer.write(EncryptedImageWriter.getARGB(finalARGB[0], finalARGB[1], finalARGB[2], 0));
            	}
            	catch (IOException e)
            	{
            		e.printStackTrace();
            	}
            }
        }
        else
        {
	        int currentIndexInCipherText;
	        
	        for (currentIndexInCipherText = 0; currentIndexInCipherText < cipherText.length - 16; currentIndexInCipherText++)
	        {
	        	/*
	        	 * TODO
	        	 * Do writing in blocks of 4
	        	 * Get 4 integers and then write the result
	        	 * of getARGB(...);
	        	 * When at the end, fill in missing with 0s
	        	 */
	        	
	        	try {
					writer.write((char) cipherText[currentIndexInCipherText]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	
	public static char[] getFullKey(char[] key)
	{
		char[] fullKey = new char[16];
		
		for (int i = 0; i < key.length; i++)
			fullKey[i] = key[i];
		for (int i = key.length; i < key.length; i++)
			fullKey[i] = (char) Math.pow(i, 2);
		
		return fullKey;
	}
	
    public static boolean decrypt(String encPath, String decPath, SecretKeySpec key)
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
        
        //char[] fullKey = getFullKey(key);
        
        //key = null;
        
        byte[] encrypted = null;
        
		boolean isImage = Handler.isImage(encPath.substring(encPath.lastIndexOf('.') + 1));
        
		if (isImage)
		{
			BufferedImage encryptedImage = null;
			try {
				encryptedImage = ImageIO.read(new File(encPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedImage encryptedImageCopy = new BufferedImage(encryptedImage.getWidth(), encryptedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			encryptedImageCopy.setData(encryptedImage.getData());
			int[] encryptedIntegers = ((DataBufferInt)(encryptedImageCopy.getRaster().getDataBuffer())).getData();
			encrypted = new byte[encryptedIntegers.length * 4];
			int indexByteEncrypted = 0;
			for (int i = 0; i < encryptedIntegers.length - 1;)
			{
				encrypted[indexByteEncrypted++] = (byte) encryptedIntegers[(i & 0xFF000000) >> 24];
				encrypted[indexByteEncrypted++] = (byte) encryptedIntegers[(i & 0x00FF0000) >> 16];
				encrypted[indexByteEncrypted++] = (byte) encryptedIntegers[(i & 0x0000FF00) >> 8];
				encrypted[indexByteEncrypted++] = (byte) encryptedIntegers[(i & 0x000000FF)];
			}
			System.out.println("Index Byte Encrypted: " + indexByteEncrypted);
			System.out.println("Length of Encrypted: " + encrypted.length);
			System.out.println("Encrypted[0]: " + encrypted[0]);
		}
		else
		{
			try {
				encrypted = Files.readAllBytes(Path.of(encPath));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}
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
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        /*byte[] encryptedWOIV = new byte[encrypted.length - 12];
        
        for (int i = 12; i < encrypted.length; i++)
        {
        	encryptedWOIV[i - 12] = encrypted[i];
        }*/

        byte[] decrypted = null;
        
        byte[] encryptedText = Arrays.copyOfRange(encrypted, 28, encrypted.length);
        
        //display("ENC", encryptedText);
        
        try
        {
        	//cipher.update(encrypted, 27, encrypted.length - 27);
        	cipher.update(encryptedText);
        	decrypted = cipher.doFinal(tag);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
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
        
        return true;
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
    
	private static SecretKeySpec getKey(char[] key)
    {
        return new SecretKeySpec(String.valueOf(key).getBytes(StandardCharsets.UTF_8), "AES");
    }
	
	public static void main(String[] args)
	{
		char[] key = new char[] { 'a', 'b', 'c', 'a', 'b', 'c', 'a', 'b', 'c', 'a', 'b', 'c', 'a', 'b', 'c', 'd' };
		
		String inputPath = "C:\\Development\\Java\\CodeEncryptor\\testIO\\testL\\input.txt";
		String outputPath = "C:\\Development\\Java\\CodeEncryptor\\testIO\\testL\\output.txt";
		encrypt(inputPath, outputPath, getKey(key), false);
		
		char[] altKey = new char[] { 'a', 'b' };
		String encPath = "C:\\\\Development\\\\Java\\\\CodeEncryptor\\\\testIO\\\\testL\\\\output.txt";
		String decPath = "C:\\\\Development\\\\Java\\\\CodeEncryptor\\\\testIO\\\\testL\\\\outputDec.txt";
		decrypt(encPath, decPath, getKey(key));
	}
}
