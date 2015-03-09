import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class vigenere {
	
	public static char[] key;
	
	public static double[] frequent = {0.08167, 0.01492, 0.02782, 0.04253, 0.12702, 0.02228, 0.02015,
        0.06094, 0.06966, 0.00153, 0.00772, 0.04025, 0.02406, 0.06749,
        0.07507, 0.01929, 0.00095, 0.05987, 0.06327, 0.09056, 0.02758,
        0.00978, 0.02360, 0.00150, 0.01974, 0.00074};

    public static int NWD(int a, int b){
        if (b == 0) return a;  
        else return NWD(b, a%b);
    }
	
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
	
	private static int KeyLength(String kod){
		String WordA=null;
		ArrayList<count> countList = new ArrayList<count>();
		for(int i=4;i<60;i++){
			for(int j=0;j<kod.length()-i-i;j++){
				WordA= kod.substring(j, j+i);
				int lastIndex = 0;
				int ilosc=0;
				while(lastIndex != -1){
					lastIndex = kod.indexOf(WordA,lastIndex);
				       if( lastIndex != -1){
				    	   ilosc++;
				           lastIndex+=WordA.length();
				      }
				}
				countList.add(new count(WordA,ilosc));
			}
		}
		String maxKey=null;
		Integer maxValue = 0;
		
		for(count object: countList){
			if(object.getValue()>maxValue){
				maxValue = object.getValue();
				maxKey = object.getSubstring();
			}
		}
		
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		int lastIndex = 0;
		int ilosc=0;
		while(lastIndex != -1){
			lastIndex = kod.indexOf(maxKey,lastIndex);
		       if( lastIndex != -1){
		    	   ilosc++;
		           indexList.add(lastIndex);
		           lastIndex+=maxKey.length();
		      }
		}	
		
		ArrayList<Integer> indexListDiffs = new ArrayList<Integer>();	
		for(int i=0;i<indexList.size()-1;i++){
			indexListDiffs.add(indexList.get(i+1)-indexList.get(i));
		}
		
		return NWD(indexListDiffs.get(0),indexListDiffs.get(1));
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
		            	filePlainW.write(przetwarzanie(line));
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
		            	fileCryptoW.write(CryptLine(line));
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
				try {
					ReadKey();
					FileReader fileCryptoR = new FileReader("crypto.txt");
					FileWriter fileKeyCryptoW = new FileWriter("key-crypto.txt");						
					FileWriter fileDecryptW = new FileWriter("decrypt.txt");					
		            BufferedReader bufferedReader = new BufferedReader(fileCryptoR);
		            String line;
		            while((line = bufferedReader.readLine()) != null) {
		            	KeyLength(line);
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
			default:
				throw new IllegalArgumentException("Niepoprawny argument");
			}
		}
	}
}