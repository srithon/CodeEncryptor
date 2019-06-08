package srithon.encryptor.testing;

import java.util.Arrays;

public class WriteLengthWidthTest
{
	public static void main(String[] args)
	{
		System.out.println(Arrays.toString(splitNum(6492, 3593)));
	}
	
	/*
	 * Given 2 integers, return a byte array
	 * where each element is the next 8 bits of the
	 * integers
	 * 
	 * 1) Split integer 1 into bits
	 * 2) Group bits into 8s, put them into array
	 * 3) Repeat for integer 2
	 */
	public static byte[] splitNum(int length, int width) // returns [length, width]
	{
		//length and width are both allocated 3 bytes
		byte[] arr = new byte[6];
		String lengthBits = Integer.toBinaryString(length);
		System.out.println(lengthBits);
		for (int i = 0; i < 3; i++)
		{
			try
			{
				arr[i] = parseBitString(lengthBits.substring(i * 8, (i + 1) * 8));
			}
			catch (Exception e)
			{
				if (i > lengthBits.length() - 1)
					arr[i] = 0;
				else
					arr[i] = parseBitString(lengthBits.substring(i));
			}
		}
		String widthBits = Integer.toBinaryString(width);
		System.out.println(widthBits);
		for (int i = 0; i < 3; i++)
		{
			try
			{
				arr[i + 3] = parseBitString(widthBits.substring(i * 8, (i + 1) * 8));
			}
			catch (Exception e)
			{
				if (i > widthBits.length() - 1)
					arr[i] = 0;
				else
					arr[i] = parseBitString(widthBits.substring(i));
			}
		}
		return arr;
	}
	
	public static byte parseBitString(String bits)
	{
		byte b = 0;
		
		for (int i = bits.length() - 1; i > 1; i--)
		{
			if (bits.charAt(i) == '1')
			{
				b += Math.pow(2.0, i - 1);
			}
		}
		
		System.out.println(b);
		
		if (bits.charAt(0) == '1')
			b = (byte) (-b);
		
		System.out.println(b);
		
		return b;
	}
}
