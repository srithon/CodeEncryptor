package srithon.encryptor.encryption;

import java.io.File;

public class Instruction
{
	private File in;
	private File out;
	private Boolean type;
	/*
	 * true = encrypting
	 * null = encrypting + checksum
	 * false = decrypting
	 */
	
	public Instruction(File in, File out, Boolean type)
	{
		this.in = in;
		this.out = out;
		this.type = type;
	}
	
	public Boolean execute()
	{
		return Handler.handleRaw(in, out, type);
	}
	
	public Boolean getType()
	{
		return type;
	}
	
	public void setType(Boolean type)
	{
		this.type = type;
		//System.out.println("Type set to " + type);
	}
	
	public String toString()
	{
		String[] split = in.getAbsolutePath().split("\\\\");
		String inStr = split[split.length - 2] + File.separatorChar + split[split.length - 1];
		
		split = out.getAbsolutePath().split("\\\\");
		String outStr = split[split.length - 2] + File.separatorChar + split[split.length - 1];
		
		return ((!(!type)) ? "ENCRYPTING" : "DECRYPTING") + "\n"
				+ inStr + "\n"
				+ outStr + "\n";
	}
}