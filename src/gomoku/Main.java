package gomoku;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Gomoku");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setContentPane(new Gomoku());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void evaluation() {
		for(int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				System.out.print((i * 15) + j + " ");
			}
			System.out.println();
			
			for(int j = 15-1; j >= 0; j--) {
				System.out.print((i * 15) + j + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		
		for(int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				System.out.print((j * 15) + i + " ");
			}
			System.out.println();

			for(int j = 15-1; j >= 0; j--) {
				System.out.print((j * 15) + i + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		
		for(int y = 15-1; y >= 0; y--) {
			int i = 15-1;
			int j = y;
			
			while(i < 15 && j < 15) {
				System.out.print((i * 15) + j + " ");
				i--;
				j++;
			}
			System.out.println();
			
			i = 15-1;
			j = y;
			
			while(i < 15 && j < 15) {
				System.out.print((j * 15) + i + " ");
				i--;
				j++;
			}
			System.out.println();
		}
		
		System.out.println();
		
		for(int x = 0; x < 15; x++) {
			int i = x;
			int j = 0;
			
			while(i >= 0 && j < 15) {
				System.out.print((i * 15) + j + " ");
				i--;
				j++;
			}
			System.out.println();
			
			i = x;
			j = 0;
			
			while(i >= 0 && j < 15) {
				System.out.print((j * 15) + i + " ");
				i--;
				j++;
			}
			System.out.println();
		}
		
		System.out.println();
		
		for(int y = 15-1; y >= 0; y--) {
			int i = 0;
			int j = y;
			
			while(i < 15 && j < 15) {
				System.out.print((i * 15) + j + " ");
				i++;
				j++;
			}
			System.out.println();
		}
		
		for(int x = 0; x < 15; x++) {
			int i = x;
			int j = 15-1;
			
			while(i >= 0 && j >= 0) {
				System.out.print((i * 15) + j + " ");
				i--;
				j--;
			}
			System.out.println();
		}
		
		System.out.println();
		
		for(int x = 15-1; x >= 0; x--) {
			int i = x;
			int j = 0;
			
			// Bottom Right to Top left diagonal
			while(i < 15 && j >= 0) {
				System.out.print((i * 15) + j + " ");
				i++;
				j++;
			}
			System.out.println();
		}
		
		for(int y = 0; y < 15; y++) {
			int i = 15-1;
			int j = y;
			
			// Bottom Right to Top left diagonal
			while(i >= 0 && j >= 0) {
				System.out.print((i * 15) + j + " ");
				i--;
				j--;
			}
			System.out.println();
		}
	}
}
