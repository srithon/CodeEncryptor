package srithon.encryptor.encryption;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class EncryptedImageWriter extends OutputStream
{
	private BufferedImage image;
	
	private File outputFile;
	
	private int[] raster;
	
	private int currentPixel;
	
	public EncryptedImageWriter(String imageFilePath, String outputFilePath)
	{
		outputFile = new File(outputFilePath);
		
		BufferedImage decryptedImage = null;
		
		try {
			decryptedImage = ImageIO.read(new File(imageFilePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (decryptedImage != null)
		{
			int h = decryptedImage.getHeight();
			int w = decryptedImage.getWidth();
			
			w = (int) Math.ceil((double) ((h * w) + 28) / h);
			//28 bytes of AES-256 data
			//12 bytes IV, 16 bytes GCM tag
			
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		}
		
		currentPixel = 0;
		
		raster = ((DataBufferInt) (image.getRaster().getDataBuffer())).getData();
	}
	
	@Override
	public void write(int b) throws IOException
	{
		raster[currentPixel++] = (0b11111111 & b);
		
		/*
		 * 1 0 0 1 0 0 1 0 0 1 0 0 1 0 0 1 1 1 1 0 0 1
			    _ _ _ _ _ _ _ _
		   &
		   0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1 1 1
		   yields 8 lowest bits
		 */
	}
	
	@Override
	public void close() throws IOException
	{
		ImageIO.write(image, outputFile.getName().substring(outputFile.getName().lastIndexOf('.') + 1), outputFile);
	}
}
