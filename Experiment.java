package hw2;



import java.util.Scanner;

public class Experiment {
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		System.out.println("enter n");
		int n = in.nextInt();
		System.out.println("enter m");
		int m = in.nextInt();
		double finCount = 0;
		double finMax = 0;
		for(int j = 0; j < 10000; j++){
			int[] res = trail(m, n);
			int max = 0;
			int count = 0;
			for(int i = 0; i < res.length; i++){
				if(res[i] > max){
					max = res[i];
					
				}
				if(res[i] == 0){
					count++;
				}
			}
			finMax += max;
			finCount += count;
		}
		finMax = finMax / 10000;
		finCount = finCount / 10000;
		System.out.println("the number of empty list is " + finCount + "  and the longest list length is " + finMax);
		
	}
	
	public static int[] trail(int m, int n){
		int[] vertex = new int[m];
		
		for(int i = 0; i < n; i++){
			int test = (int) (Math.random() * n);
			//System.out.println(test);
			int pos = test % m;
			//System.out.println(pos);
			vertex[pos]++;
		}
		/*
		int[] temp = new int[m];
		for(int j = 0; j < m; j++){
			temp[j] = vertex[j].size();
		}
		
		
		return temp;
		*/
		return vertex;
	}
	
}
