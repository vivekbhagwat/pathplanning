import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class PathPlanner {
	
	private MapComponent comp;
	
	public LinkedList<Point> dijkstra(Map map)
	{
		LinkedList<Point> path = new LinkedList<Point>();
		ArrayList<Point> points = map.nodes;
		map.start.dist = 0.0;
		// path.add(map.start);		
		
		PriorityQueue<Point> pq = new PriorityQueue<Point>(points.size());
		
		for(int i = 0; i < points.size(); i++)
		{
			pq.add(points.get(i));
		}
		
		Point current;
		while((current = pq.poll()) != null)
		{
			current.known = true;
			ArrayList<Point> next = PathPlanner.possibleNextPoints(current, map);
			Point adj = null;
			
			// finds index of the point
			// so dumb, but no one cares
			int j = 0;
			while(!points.get(j).equals(current))
				j++;
			for(int i = 0; i < next.size(); i++)
			{
				adj = next.get(i);
				
				// finds index of the point
				int ind = points.indexOf(adj); //built-in
				// int ind = 0;
				// while(!points.get(ind).equals(adj))
					// ind++;
				
				if(!adj.known)
				{
					if(current.dist + map.adjacencyMatrix[j][ind] < adj.dist)
					{
						adj.dist = current.dist + map.adjacencyMatrix[j][ind];
						adj.path = current;
						// comp.drawPossiblePaths(adj, )
					}
					pq.add(adj);
				}
			}
		}
		Point end = map.goal;
		// double pathLength = end.dist;
		while(end.path != null)
		{
			path.add(end);
			end = end.path;
		}
		path.add(map.start);
		java.util.Collections.reverse(path);
		return path;
	}
	
	public static ArrayList<Point> possibleNextPoints(Point current, Map map)
	{
		// gets all the points, except the immediate predecessor
		ArrayList<Point> next = new ArrayList<Point>();
		for(int i = 0; i < map.nodes.size(); i++)
		{
			if(map.nodes.get(i) != current.path)
				next.add(map.nodes.get(i));
		}
		return next;
	}
	
	public void createGUI(Map robotMap)
	{
		JFrame frame = new JFrame();
		comp = new MapComponent(robotMap);
		frame.add(comp);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
	
	public static void main(String [] args) {
		// open the map 
		Map map = new Map(args[0], args[1]);
		// Map map = new Map(args[0], new Point(-1.0,-1.0), new Point(2.0,2.0));
		PathPlanner planner = new PathPlanner();
		planner.createGUI(map);
		LinkedList<Point> path = planner.dijkstra(map);
		System.out.println(path);
		planner.comp.setPath(path);
	} 
}
