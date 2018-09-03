
public class block {
	public double width;
	public double height;
	public String id;
	public double x;
	public double y;
	public double area;
	public block(String id,double width,double height) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.area = width * height; 
		x = 0;
		y = 0;
	}
}
