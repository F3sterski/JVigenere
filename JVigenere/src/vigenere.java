import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class vigenere {
	
	public static char[] key;

	private static String przetwarzanie(String line){
		
		StringBuilder outLine = new StringBuilder();
		
		for(char ch : line.toCharArray()){
			if ((ch >= 'a' && ch <= 'z')){
				outLine.append(ch);
			}
			else if ((ch >= 'A' && ch <= 'Z')){
				outLine.append(Character.toLowerCase(ch));
			}
		}
		return outLine.toString();
	}
	
	private static void ReadKey(){
        try {
    		
            FileReader fileReader = new FileReader("key.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            key = line.toCharArray();
            
            if(key.length>25){
	        	System.err.println("Niepoprawny klucz");
	        	System.exit(0);
            }
            
            for(int i=0;i<key.length;i++){
    			if ((key[i] < 'a' || key[i] > 'z')){
    				throw new IllegalArgumentException("Niepoprawny znak w kluczu");
    			}
            }
            
            bufferedReader.close();
            
    	}
	    catch(FileNotFoundException ex) {
	        System.err.println("Unable to open file 'key.txt'");   
	        ex.printStackTrace();
	        System.exit(0);
	    }
	    catch(IOException ex) {
	        System.err.println("Error reading file 'key.txt'");  
	        ex.printStackTrace();
	        System.exit(0);
	    }
	    catch(NullPointerException ex){
	    	ex.printStackTrace();
	    	System.exit(0);
	    }
	}
	private static boolean IsOrigEmpty(){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("orig.txt"));
			if(br.readLine() == null){
				br.close();
				return true;
			}
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static String CryptLine(String BeforeLine){
		StringBuilder AfterLine = new StringBuilder();
		int i=0;
		for(char x : BeforeLine.toCharArray()){
			AfterLine.append((char)((((int)x-97)+((int)key[i]-97))%26+97));
			i++;
			i%=key.length;
		}
		return AfterLine.toString();
	}
	
	private static String DecryptLine(String BeforeLine){
		StringBuilder AfterLine = new StringBuilder();
		int i=0;
		for(char x : BeforeLine.toCharArray()){
			AfterLine.append((char)((((int)x-97)+26-((int)key[i]-97))%26+97));
			i++;
			i%=key.length;
		}
		return AfterLine.toString();
	}
	
	
	public static void main(String[] args){
		if(args.length==0){
			throw new IllegalArgumentException("Brak Argumentu");
		}
		else if(args[0]!="-p" && IsOrigEmpty()){
			throw new IllegalArgumentException("Najpierw nale¿y prztworzyæ tekst komend¹ '-p'");
		}
		else{
			switch(args[0]){
			case "-p":
				try {
					FileReader fileOrigR = new FileReader("orig.txt");
					FileWriter filePlainW = new FileWriter("plain.txt");					
		            BufferedReader bufferedReader = new BufferedReader(fileOrigR);
		            String line;
		            while((line = bufferedReader.readLine()) != null) {
		            	filePlainW.write(przetwarzanie(line)+'\n');
		            }
		            fileOrigR.close();
		            filePlainW.close();
		            
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case "-e":
				try {
					ReadKey();
					FileReader filePlainR = new FileReader("plain.txt");
					FileWriter fileCryptoW = new FileWriter("crypto.txt");					
		            BufferedReader bufferedReader = new BufferedReader(filePlainR);
		            String line;
		            while((line = bufferedReader.readLine()) != null) {
		            	fileCryptoW.write(CryptLine(line)+'\n');
		            }
		            filePlainR.close();
		            fileCryptoW.close();
		            
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}				
				break;
			case "-d":
				try {
					ReadKey();
					FileReader fileCryptoR = new FileReader("crypto.txt");
					FileWriter fileDecryptW = new FileWriter("decrypt.txt");					
		            BufferedReader bufferedReader = new BufferedReader(fileCryptoR);
		            String line;
		            while((line = bufferedReader.readLine()) != null) {
		            	fileDecryptW.write(DecryptLine(line)+'\n');
		            }
		            fileCryptoR.close();
		            fileDecryptW.close();
		            
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}		
				break;
			case "-k":
				break;
			default:
				throw new IllegalArgumentException("Niepoprawny argument");
			}
		}
	}
}