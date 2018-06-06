
public class Similarity {
	private int item;
	private float Cosine;
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public float getCosine() {
		return Cosine;
	}
	public void setCosine(float cosine) {
		Cosine = cosine;
	}
	public Similarity() {
		super();
	}
	public Similarity(int item, float cosine) {
		super();
		this.item = item;
		Cosine = cosine;
	}
}
