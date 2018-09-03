import java.util.*;

public class Execution {
	public static int total_terminals;
	public static int total_nodes;
	public static int total_pins;
	public static int total_nets;
	public static Map<Integer,String> id_map = new HashMap();
	public static Map<String,net> res;
	public static Map<String,String> best_res;
	public static Map<String,String> pq_info;
	public static Map<String,terminal> terminals;
	public static Map<String,block> blocks;
	public static List<List<net>> nets;
	public static List<List<net>> nets_terminal;  // the netlists contain terminals 
	public static List<List<net>> nets_noterminal;  // the netlists don't contain terminals 		
	public static double terminal_left;
	public static double terminal_right;
	public static double terminal_up;
	public static double terminal_down;
	public static double Chip_Area;
	public static double width;
	public static double height;
	public static int block_capacity;   //the maximum blocks/terminals can be stored in a single cell
	public static int block_nums;   //the maximum blocks/terminals can be stored in a single cell
	public static double block_width;
	public static double block_height;
	public static double avg_block_width;
	public static double avg_block_height;
	public static double cell_area_width;
	public static double cell_area_height;
	public static double cell_area;
	public static int block_row_nums; // # of blocks that can be set in the row
	public static int block_col_nums;  // # of blocks that can be set in the col
	public static double swap_rate = 0.5;
	
	public static double calculate_OLx(double xi,double xj,double wi,double wj) {
		return Math.max(0.0, 0.5*((wi+wj)-Math.abs((xj-wj/2)-(xi-wi/2))-Math.abs((xj+wj/2)-(xi+wi/2))));	
	}
	
	public static double calculate_OLy(double yi,double yj,double hi,double hj) {
		return Math.max(0.0, 0.5*((hi+hj)-Math.abs((yj-hj/2)-(yi-hi/2))-Math.abs((yj+hj/2)-(yi+hi/2))));	
	}
	
	public static double calculate_Chip_Area(double xi,double xj,double yi,double yj) {
	    width = xj - xi;
	    height = yj - yi;
		return width*height;
	}
	
	public static double calculate_Cost(double cost_alpha, double Area,double WireLength) {
			return (cost_alpha)*(Area) + (1-cost_alpha)*(WireLength);
	}
	
	public static double calculate_WL(){
		double wl = 0.0;
		for(int i=0;i<nets.size();i++) {
				List<net> netlist = nets.get(i);
				double left = Double.MAX_VALUE;
				double right  = Double.MIN_VALUE;
				double up = Double.MIN_VALUE;
				double down  = Double.MAX_VALUE;
				for(net n:netlist) {
					left = Math.min(left,n.x);
					right = Math.max(right,n.x);
					down = Math.min(down,n.y);
					up = Math.max(up,n.y);
				}
				wl+= (right-left) + (up-down);
		}
		return wl;
	}
	
	public static double calculate_WL_accurate(){
		double wl = 0.0;
		for(int i=0;i<nets.size();i++) {
				List<net> netlist = nets.get(i);
				double left = Double.MAX_VALUE;
				double right  = Double.MIN_VALUE;
				double up = Double.MIN_VALUE;
				double down  = Double.MAX_VALUE;
				for(net n:netlist) {
					if(res.containsKey(n.id)) {
						net cur = res.get(n.id);
						left = Math.min(left,cur.x);
						right = Math.max(right,cur.x);
						down = Math.min(down,cur.y);
						up = Math.max(up,cur.y);
					}
					else {
						left = Math.min(left,n.x);
						right = Math.max(right,n.x);
						down = Math.min(down,n.y);
						up = Math.max(up,n.y);
					}
				}
				wl+= (right-left) + (up-down);
		}
		return wl;
	}
	

	
	public static double calculate_overlap_area(){
			double area = 0.0;
			for(String s:res.keySet()) {
				net n = res.get(s);
				if(!n.is_block) continue;
				for(String s1:res.keySet()) {
					if(s1.compareTo(s)==0) continue;
					net n1 = res.get(s1);
					if(!n1.is_block) continue;
					area+=  calculate_OLx(n.x,n1.x,n.width,n1.width)* calculate_OLy(n.y,n1.y,n.height,n1.height);
				}
			}
			return (area/Chip_Area)*100/2;
	}
	
	

	public static int generate_int_range(int Min,int Max) {
		return Min + (int)(Math.random() * ((Max - Min) + 1));
	}

	
	
  
	public static double generate_random_number(double rangeMin,double rangeMax) {
	  			Random r = new Random();
	  			double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
	  			return randomValue;
	}
  

   
	
	public static void initilization() {
		 total_terminals = 0;
		 total_nodes = 0;
		 total_pins = 0;
		 total_nets = 0;
		 terminal_left = Double.MAX_VALUE;
		 terminal_right = Double.MIN_VALUE;
		 terminal_up = Double.MIN_VALUE;
		 terminal_down = Double.MAX_VALUE;
		 res = new HashMap();
		 terminals = new HashMap();
		 blocks = new HashMap();
		 nets = new ArrayList();
		 nets_terminal = new ArrayList();  // the netlists contain terminals 
		 nets_noterminal = new ArrayList();  // the netlists don't contain terminals 	
		 Chip_Area = 0.0;
		 width = 0.0;
		 height = 0.0;
		 avg_block_width = 0.0;
		 avg_block_height = 0.0;
		 block_nums = 0;
		 block_capacity = 0;
		 block_row_nums = 0;
		 block_col_nums = 0;
		 cell_area = 0.0;
		 cell_area_width = 0.0;
		 cell_area_height =0.0;
		 best_res = new HashMap();
		 pq_info = new HashMap();
	}
	
	public static void readfiles(String pl_location,String nodes_location,String nets_location) {
		terminal_reader tr = new terminal_reader();
		tr.read(pl_location);
		Chip_Area = calculate_Chip_Area(terminal_left ,terminal_right,terminal_down,terminal_up);
		nodes_reader nr = new nodes_reader();
		nr.read(nodes_location);
		nets_reader nrd = new nets_reader();
		nrd.read(nets_location);
		avg_block_width = avg_block_width/(total_nodes - total_terminals);
		avg_block_height = avg_block_height/(total_nodes - total_terminals);
}
	
	
	
	public static double rand_01(){
		  Random rand = new Random();
		  return rand.nextDouble();
	}
	
	

	public static void initial_random_generation() {
		int count = 0;
		for(int i=0;i<nets.size();i++) {
			List<net> netlist = nets.get(i);
			for(net n:netlist) {
				if(!n.is_block) res.put(n.id, n);
				else{
						id_map.put(count,n.id);
						double x = generate_random_number(terminal_left,terminal_right);
						while(x+n.width/2>=terminal_right || x-n.width/2<=terminal_left) {
							 x = generate_random_number(terminal_left,terminal_right);
						}
						double y = generate_random_number(terminal_down,terminal_up);
						while(y+n.height/2>=terminal_up || y-n.height/2<=terminal_down) {
							y = generate_random_number(terminal_down,terminal_up);
						}
						n.x = x;
						n.y = y;
					    res.put(n.id, n);
					    count++;
				}
			}
		}
	}
	
	public static double pertubation(double area,String id,double new_x,double new_y,Map<String,String> cur_res,Map<String,String> neighbor_res) throws IllegalAccessException {
				if(!res.containsKey(id))  throw new IllegalAccessException("the update overlap area cannot fetch the id");
				area = area/50*Chip_Area;
				double old_area = 0;
				double new_area = 0;
				net n = res.get(id);
				cur_res.clear();
				neighbor_res.clear();
				for(String key:res.keySet()) {
						cur_res.put(key,res.get(key).x+" "+res.get(key).y);
						neighbor_res.put(key,res.get(key).x+" "+res.get(key).y);
						if(key.compareTo((id))==0 || !res.get(key).is_block) continue;
						net n1 = res.get(key);
						old_area+=calculate_OLx(n.x,n1.x,n.width,n1.width)* calculate_OLy(n.y,n1.y,n.height,n1.height);
				}
				old_area = old_area * 2;
				neighbor_res.put(n.id, new_x+" "+new_y);
				for(String key:res.keySet()) {
					if(key.compareTo((id))==0 || !res.get(key).is_block) continue;
					net n1 = res.get(key);
					new_area+=calculate_OLx(new_x,n1.x,n.width,n1.width)* calculate_OLy(new_y,n1.y,n.height,n1.height);
				}
				new_area = new_area * 2;
				return ((area - old_area + new_area)/Chip_Area)*100/2;
		
	}
	
	  public static double acceptanceProbability(double energy, double newEnergy, double temperature) {
	        // If the new solution is better, accept it
	        if (newEnergy < energy) {
	            return 1.0;
	        }
	        // If the new solution is worse, calculate an acceptance probability
	        return Math.exp((energy - newEnergy) / temperature);
	    }
	  
	  public static double calculate_neighbor_wl(String id,double new_x,double new_y) {
		  	double wl = 0.0;
			for(int i=0;i<nets.size();i++) {
					List<net> netlist = nets.get(i);
					double left = Double.MAX_VALUE;
					double right  = Double.MIN_VALUE;
					double up = Double.MIN_VALUE;
					double down  = Double.MAX_VALUE;
					for(net n:netlist) {
						if(n.id.compareTo(id)==0) {
							left = Math.min(left,new_x);
							right = Math.max(right,new_x);
							down = Math.min(down,new_y);
							up = Math.max(up,new_y);
						}
						else if(res.containsKey(n.id)) {
							net cur = res.get(n.id);
							left = Math.min(left,cur.x);
							right = Math.max(right,cur.x);
							down = Math.min(down,cur.y);
							up = Math.max(up,cur.y);
						}
						else {
							left = Math.min(left,n.x);
							right = Math.max(right,n.x);
							down = Math.min(down,n.y);
							up = Math.max(up,n.y);
						}
					}
					wl+= (right-left) + (up-down);
			}
			return wl;
	    }
	
	
	public static void Simulated_Annealing(double WL,double AREA,String output_path) throws IllegalAccessException {
			double temp = 100;
			double area = AREA;
			double wl = WL;
			double best = Double.MAX_VALUE;
        		Map<String,String> neighbor_res = new HashMap();
        		Map<String,String> cur_res = new HashMap();
        		System.out.println("enter");
        		int count = 0;
        		double alpha = 0.95;
        		for(int i=0;i<4500000;i++) {
         		   
        			 double currentEnergy  = calculate_Cost(alpha,area,wl/100000);	//very good for 25 % 
        				int index = generate_int_range(0,id_map.size()-1);
        				net n = res.get(id_map.get(index));
        				String id = n.id;
        				double new_x = generate_random_number(terminal_left,terminal_right);
					while(new_x+n.width/2>=terminal_right || new_x-n.width/2<=terminal_left) {
							 new_x = generate_random_number(terminal_left,terminal_right);
					}
					double new_y = generate_random_number(terminal_down,terminal_up);
					while(new_y+n.height/2>=terminal_up || new_y-n.height/2<=terminal_down) {
						new_y = generate_random_number(terminal_down,terminal_up);
					}
        	            double new_area = pertubation(area,id,new_x,new_y,cur_res,neighbor_res);
        	            double new_wl = calculate_neighbor_wl(id,new_x,new_y);
        	            double neighbourEnergy = calculate_Cost(alpha,new_area,new_wl/100000);
        	            double value = Math.exp(-(neighbourEnergy - currentEnergy) / temp);
        	     
        	           
        	            	if(neighbourEnergy - currentEnergy<0 || value > Math.random()) {
        	            				for(String key:neighbor_res.keySet()) {
        	            					   double x = Double.parseDouble((neighbor_res.get(key).split(" ")[0]));
        	            					   double y = Double.parseDouble((neighbor_res.get(key).split(" ")[1]));
        	            					   res.get(key).x = x;
        	            					   res.get(key).y = y;
        	            				}
        	            				if(neighbourEnergy< best) {
        	            					best = neighbourEnergy;
        	            					best_res.clear();
        	            					for(String key:neighbor_res.keySet()) {
        	            						best_res.put(key, neighbor_res.get(key));
         	            				}
        	            					 writer w = new writer();
        	            					 writer.write(output_path);
        	            					 System.out.println(count+"      "+new_area);
        	            				}
        	            				area = new_area;
        	            				wl = new_wl;
        	            }
        	            	temp = temp * Math.pow(0.999,i);
        	            count++;
        	        }
			
	}
	
	
	 public static void main(String [] args) throws IllegalAccessException {
		String pl_path = "/Users/yiming/Desktop/UMD/ENEE640/Exam2/Benchmarks and Script/ibm02/ibm02.pl";  // put the address of .pl address
		String nodes_path = "/Users/yiming/Desktop/UMD/ENEE640/Exam2/Benchmarks and Script/ibm02/ibm02.nodes"; // put the address of .nodes address
		String nets_path = "/Users/yiming/Desktop/UMD/ENEE640/Exam2/Benchmarks and Script/ibm02/ibm02.nets"; // put the address of .nets address
		String output_path = "/Users/yiming/Desktop/UMD/ENEE640/Exam2/ibm02_output.pl";  //put the addess of output path 
		initilization();
		readfiles(pl_path,nodes_path,nets_path);
		initial_random_generation();
		double wl = calculate_WL_accurate();
		double area = calculate_overlap_area();
      	System.out.println(area);
		Simulated_Annealing(wl,area,output_path);
	 }
}
