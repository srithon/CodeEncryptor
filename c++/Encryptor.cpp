#include "Encryptor.h"
#include <fstream>
#include <iostream>
#include <cstring>
#include <cctype>
#include <codecvt>
#include <locale>

int main()
{
	/*cout << (int) 'Ç' << endl;
	cout << ((char) 36483) << endl;
	cout << (int) 'â' << endl;*/
	
	//cout << (int) 'a' << endl;
	
	
	ifstream istream;
	ofstream ostream;
	
	istream.open("C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testI\\source2.txt");
	ostream.open("C:\\Data\\Development\\Workspaces\\General\\CodeEncryptor\\testIO\\testI\\output2.txt");
	
	if (!istream || !ostream)
	{
		cout << "FAILED" << endl;
	}
	else
	{
		cout << "SUCCESS" << endl;
	}
	
	//char x;
	string j = "";
	
	/*while (stream >> x)
	{
		j += x;
	}*/
	
	int numLine = 1;
	
	std::wstring_convert<std::codecvt<char16_t,char,std::mbstate_t>,char16_t> convert;
	
	for (string line; getline(istream, line); )
	{
		//cout << line << endl;

		std::u16string u16 = convert.from_bytes(line);
		
		char char_array[line.length()+1];
		strcpy(char_array, line.c_str());
		
		for (int i = 0; i < sizeof(char_array) / sizeof(char_array[0]); i++)
		{
			if (!isspace(char_array[i]))
			{
				char_array[i] = (char)
								((36480 - (numLine * 10)) -
									(((int) char_array[i])
										+ i
										+ (sizeof(char_array) / sizeof(char_array[0]) * 2)
										+ numLine
										* (1 + (numLine % 2))));
			}
			
			ostream << char_array[i];
		}
		
		ostream << endl;
		
		//ostream << line << endl;
		numLine++;
	}
	
	//cout << j << endl;

	return 0;
}