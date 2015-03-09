
public class count {
	private int value;
	private String substring;
	
	count(String a,int b){
		setSubstring(a);
		setValue(b);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getSubstring() {
		return substring;
	}

	public void setSubstring(String substring) {
		this.substring = substring;
	}
}
