package CS245_Project02;

public class Words {
	private String word;
	private int count;
	public Words(String word) {
		this.word = word.toLowerCase();
		this.count = 0; 
	}
	public String getWord() {
		return word;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int cnt) {
		this.count = cnt;
	}
	public int compareTo(Words other) {
		Words w = other;
		if(w.getCount() > this.count) {
			return 1;
		}else if(w.getCount() < this.count) {
			return -1;
		}else {
			return 0;
		}
	}
}
