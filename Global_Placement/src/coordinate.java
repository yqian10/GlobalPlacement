
public class coordinate {
		int x;
		int y;
		public coordinate(int x,int y) {
			this.x = x;
			this.y = y;
		}
		
	    public boolean equals(Object obj){
            coordinate another = (coordinate)obj;
            if(another.x==this.x &&another.y==this.y) return true;
            return false;
        }
        
  
        public int hashCode(){
                return 31*(x+y);
        }
}
