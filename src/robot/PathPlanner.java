package robot;

public class PathPlanner {
	public static void main(String [] args) {
		// open the map 
		Map map = new Map(args[0]);
		
		// grow obstacles
		// for(Polygon p : map.polygons) {
		//    ? = p.grow();
		// }	
		
		// create visibility graph
		
		// use dijkstra's to get shortest path
	} 
}
