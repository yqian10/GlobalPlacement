
public class net {
	public double width;  //the width of the block/terminal 
	public double height;  //the height of the block/terminal 
	public String id;	// the name of the block/terminal
	public double x;		// the x coordinate of block/terminal 
	public double y;		// the y coordinate of block/terminal 
	public double area;	// the area of block 
	public boolean is_block;		// true means it's block, otherwise it's terminal 
	public net(String id,double x,double y,boolean is_block) {
		this.id = id;
		this.is_block = is_block;
		if(is_block) {  //means it is block
			this.width = x;
			this.height = y;
			this.area = x * y; 
			x = 0;
			y = 0;
		}
		else {   //it is terminal 
			this.x = x;
			this.y = y; 
			this.width = 0;
			this.height = 0;
			this.area = 0; 
		}
	}
}
