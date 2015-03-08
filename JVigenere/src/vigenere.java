import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class vigenere {
	
	public static int key;
	public static int key2;

	
	
	@SuppressWarnings("resource")
	public static void main(String[] args){
		if(args.length==0){
			throw new IllegalArgumentException("Brak Argumentu");
		}
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("orig.txt"));
			if(br.readLine() == null){
				throw new IllegalArgumentException("Najpierw nale¿y prztworzyæ tekst komend¹ '-p'");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}