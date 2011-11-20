package robot;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Map
{
	public final double ROBOT_SIZE = 1.0; //random value?
	public ArrayList<Point> map;
	public double[][] adjacencyMatrix;
	
	public Map(String inputFile, Point start, Point goal)
	{
		//read file here
		map = new ArrayList<Point>();
		map.add(start);
		map.add(goal);
			
		processFile(inputFile);
	
		adjacencyMatrix = new double[map.size()][map.size()];
		fillAdjacencyMatrix();
	}
	
	private void fillAdjacencyMatrix()
	{
		for(int i = 0; i < adjacencyMatrix.length; i++)
		{
			for(int j = 0; j < adjacencyMatrix[i].length; j++)
			{
				adjacencyMatrix[i][j] = map.get(i).distFrom(map.get(j));
			}
		}
	}
	
	//fills in map with points from inputFile
	private void processFile(String inputFile)
	{
		BufferedReader br;
		
		try {
			br = new BufferedReader(new FileReader(inputFile));
		}catch(FileNotFoundException e)
		{
			System.out.println(e);
			e.printStackTrace();
			return;
		}
		try {
			int numPolygons = Integer.parseInt(br.readLine());
			Polygon[] polygons = new Polygon[numPolygons];
			
			for(int i = 0; i < polygons.length; i++)
			{
				int numVertices = Integer.parseInt(br.readLine());
				for(int j = 0; j < numVertices; j++)
				{
					polygons[i].add(new Point(br.readLine()));
					// String[] nums = br.readLine().split("\\s");
					// polygons[i].add(new Point(Double.parseDouble(nums[0]), 
												// Double.parseDouble(nums[1])));
				}
				polygons[i].grow(ROBOT_SIZE/2);
				addToMap(polygons[i]);
			}			
		}catch(IOException e)
		{
			System.out.println(e);
			e.printStackTrace();
			return;
		}finally {
			try {
				if(br != null)
					br.close();
			}catch(IOException e)
			{
				System.out.println(e);
				e.printStackTrace();
				return;
			}
		}
	}
	
	private void addToMap(Polygon p)
	{
		Point[] vertices = p.setOfPoints();
		
		for(int j = 0; j < vertices.length; j++)
		{
			map.add(vertices[j]);
		}
	}
	
}