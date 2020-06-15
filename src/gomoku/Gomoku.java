package gomoku;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Move;
import utils.Stack;

@SuppressWarnings("serial")
public class Gomoku extends JPanel implements Runnable, MouseListener{
	public static final String PLAYER = "BLACK"; // MINIMIZER
	public static final String AI = "WHITE"; // MAXIMIZER
	public static final String EMPTY = "";
	
	public static final int NUM_OF_ROW = 15;
	public static final int NUM_OF_COL = 15;
	public static final int WIDTH = 500;
	public static final int HEIGHT = 500;
	
	public static final int[] MOVEX = {-1, 0, 1, -1, 1, -1, 0, 1};
	public static final int[] MOVEY = {-1, -1, -1, 0, 0, 1, 1, 1};
	
	public static final Move MARKER = new Move(-10, -10);
	
	private Stack<Move> suitableMoves;
	private Stack<Move> occupiedMoves;
	
	private JLabel[][] board;
	private Random rand;
	private Thread thread;
	Graphics graphic;
	private boolean isRunning;
	
	private boolean isPlayerMove;
	private boolean end;
	
	
	public Gomoku() {
		super();
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setFocusable(true);
		this.requestFocus();
		this.setLayout(null);
		this.setBackground(Color.BLACK);
	}
	
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addMouseListener(this);
			thread.start();
		}
	}
	
	public void init() {
		isRunning = true;
		
		suitableMoves = new Stack<>();
		occupiedMoves = new Stack<>();
		board = new JLabel[NUM_OF_ROW][NUM_OF_COL];
		drawBoard();
		
		rand = new Random();
//		isPlayerMove = (rand.nextInt(5) < 2);
		graphic = this.getGraphics();
	}
	
	public void drawBoard() {
		for(int i = 0; i < NUM_OF_ROW; i++) {
			for(int j = 0; j < NUM_OF_COL; j++) {
				board[i][j] = new JLabel(String.valueOf((i * 15) + j));
				board[i][j].setText(EMPTY);
				board[i][j].setBounds(100 + i * 20, 50 + j * 20, 20, 20);
				board[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				board[i][j].setBackground(Color.GRAY);
				board[i][j].setOpaque(true);
				board[i][j].setVisible(true);
				this.add(board[i][j]);
			}
		}
	}
	
	public void drawAxis() {
		
		graphic.setColor(Color.WHITE);
		for(int i = 0; i < NUM_OF_ROW; i++) {
			graphic.drawString(i + "", 105 + i * 20, 40);
		}
		for(int i = 0; i < NUM_OF_ROW; i++) {
			graphic.drawString(i + "", 80,  65 + i * 20);
		}
	}
	
	public void run() {
		init();
		drawAxis();
		// Game Loops
		while (isRunning && !end) {
			
			update();
		}

		stop();
	}

	private void update() {
		for(int i = 0; i < NUM_OF_ROW; i++) {
			for(int j = 0; j < NUM_OF_COL; j++) {
				board[i][j].setBounds(100 + i * 20, 50 + j * 20, 20, 20);
				board[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			}
		}
		
		if(!isPlayerMove && !end) {
			if(occupiedMoves.isEmpty()) setMove(rand.nextInt(5) + 5, rand.nextInt(5) + 5, "White", AI);
			else getBestMove();
		}
	}
	
	public boolean isMovesLeft() {
		for(int i = 0; i < NUM_OF_ROW; i++) {
			for(int j = 0; j < NUM_OF_COL; j++) {
				if(board[i][j].getText() == EMPTY) return true;
			}
		}
		return false;
	}
	
	public int evaluation() {
		int maxAIMoves = 0;
		int maxPlayerMoves = 0;
		
		for(int i = 0; i < NUM_OF_ROW; i++) {
			int AIMoves_0 = 0;
			int PlayerMoves_0 = 0;
			
			for(int j = 0; j < NUM_OF_COL; j++) {
				// COLs
				
				if(board[i][j].getText() == AI) {AIMoves_0++; PlayerMoves_0 = 0;}
				
				if(board[i][j].getText() == EMPTY) {
					if(AIMoves_0 > maxAIMoves) maxAIMoves = AIMoves_0; AIMoves_0 = 0;
					if(maxAIMoves == 5) return 10;
				}
				
				if(board[i][j].getText() == PLAYER) {PlayerMoves_0 ++; AIMoves_0 = 0;}
				
				if(board[i][j].getText().isEmpty()) {
					if(PlayerMoves_0 > maxPlayerMoves) maxPlayerMoves = PlayerMoves_0; PlayerMoves_0 = 0;
					if(maxPlayerMoves == 5) return -10;
				}
			}
			
			int AIMoves_1 = 0;
			int PlayerMoves_1 = 0;
			
			for(int j = NUM_OF_COL-1; j >= 0; j--) {
				// COLs
				
				if(board[i][j].getText() == AI) {AIMoves_1++; PlayerMoves_1 = 0;}
				
				if(board[i][j].getText() == EMPTY) {
					if(AIMoves_1 > maxAIMoves) maxAIMoves = AIMoves_1; AIMoves_1 = 0;
					if(maxAIMoves == 5) return 10;
				}
				
				if(board[i][j].getText() == PLAYER) {PlayerMoves_1 ++; AIMoves_1 = 0;}
				
				if(board[i][j].getText().isEmpty()) {
					if(PlayerMoves_1 > maxPlayerMoves) maxPlayerMoves = PlayerMoves_1; PlayerMoves_1 = 0;
					if(maxPlayerMoves == 5) return -10;
				}
			}
		}

		for(int i = 0; i < NUM_OF_COL; i++) {
			int AIMoves_0 = 0;
			int PlayerMoves_0 = 0;
			
			for(int j = 0; j < NUM_OF_ROW; j++) {
				// ROWs
				
				if(board[j][i].getText() == AI) {AIMoves_0++; PlayerMoves_0 = 0;}
				
				if(board[j][i].getText() == EMPTY) {
					if(AIMoves_0 > maxAIMoves) maxAIMoves = AIMoves_0; AIMoves_0 = 0;
					if(maxAIMoves == 5) return 10;
				}
					
				if(board[j][i].getText() == PLAYER) {PlayerMoves_0 ++; AIMoves_0 = 0;}
				
				if(board[j][i].getText().isEmpty()) {
					if(PlayerMoves_0 > maxPlayerMoves) maxPlayerMoves = PlayerMoves_0; PlayerMoves_0 = 0;
					if(maxPlayerMoves == 5) return -10;
				}
			}
			
			int AIMoves_1 = 0;
			int PlayerMoves_1 = 0;
			
			for(int j = NUM_OF_ROW-1; j >= 0; j--) {
				// ROWs
				
				if(board[j][i].getText() == AI) {AIMoves_1++; PlayerMoves_1 = 0;}
				
				if(board[j][i].getText() == EMPTY) {
					if(AIMoves_1 > maxAIMoves) maxAIMoves = AIMoves_1; AIMoves_1 = 0;
					if(maxAIMoves == 5) return 10;
				}
					
				if(board[j][i].getText() == PLAYER) {PlayerMoves_1 ++; AIMoves_1 = 0;}
				
				if(board[j][i].getText().isEmpty()) {
					if(PlayerMoves_1 > maxPlayerMoves) maxPlayerMoves = PlayerMoves_1; PlayerMoves_1 = 0;
					if(maxPlayerMoves == 5) return -10;
				}
			}
		}
		
		for(int y = NUM_OF_COL-1; y >= 0; y--) {
			int i = NUM_OF_ROW-1;
			int j = y;
			int AIMoves = 0;
			int PlayerMoves = 0;
			
			while(i < NUM_OF_ROW && j < NUM_OF_COL) {
				if(board[i][j].getText() == AI) {AIMoves++; PlayerMoves = 0;}
				
				if(board[i][j].getText() == EMPTY) {
					if(AIMoves > maxAIMoves) maxAIMoves = AIMoves; AIMoves = 0;
					if(maxAIMoves == 5) return 10;
				}
					
				if(board[i][j].getText() == PLAYER) {PlayerMoves ++; AIMoves = 0;}
				
				if(board[i][j].getText().isEmpty()) {
					if(PlayerMoves > maxPlayerMoves) maxPlayerMoves = PlayerMoves; PlayerMoves = 0;
					if(maxPlayerMoves == 5) return -10;
				}
				
				i--;
				j++;
			}
			
			i = NUM_OF_ROW-1;
			j = y;
			AIMoves = 0;
			PlayerMoves = 0;
			
			while(i < NUM_OF_ROW && j < NUM_OF_COL) {
				if(board[j][i].getText() == AI) {AIMoves++; PlayerMoves = 0;}
				
				if(board[j][i].getText() == EMPTY) {
					if(AIMoves > maxAIMoves) maxAIMoves = AIMoves; AIMoves = 0;
					if(maxAIMoves == 5) return 10;
				}
					
				if(board[j][i].getText() == PLAYER) {PlayerMoves ++; AIMoves = 0;}
				
				if(board[j][i].getText().isEmpty()) {
					if(PlayerMoves > maxPlayerMoves) maxPlayerMoves = PlayerMoves; PlayerMoves = 0;
					if(maxPlayerMoves == 5) return -10;
				}
				
				i--;
				j++;
			}
		}
		
		for(int x = 0; x < NUM_OF_ROW; x++) {
			int i = x;
			int j = 0;
			int AIMoves = 0;
			int PlayerMoves = 0;
			
			while(i >= 0 && j < NUM_OF_COL) {
				if(board[i][j].getText() == AI) {AIMoves++; PlayerMoves = 0;}
				
				if(board[i][j].getText() == EMPTY) {
					if(AIMoves > maxAIMoves) maxAIMoves = AIMoves; AIMoves = 0;
					if(maxAIMoves == 5) return 10;
				}
					
				if(board[i][j].getText() == PLAYER) {PlayerMoves ++; AIMoves = 0;}
				
				if(board[i][j].getText().isEmpty()) {
					if(PlayerMoves > maxPlayerMoves) maxPlayerMoves = PlayerMoves; PlayerMoves = 0;
					if(maxPlayerMoves == 5) return -10;
				}
				
				i--;
				j++;
			}
			
			i = x;
			j = 0;
			AIMoves = 0;
			PlayerMoves = 0;
			
			while(i >= 0 && j < NUM_OF_COL) {
				if(board[j][i].getText() == AI) {AIMoves++; PlayerMoves = 0;}
				
				if(board[j][i].getText() == EMPTY) {
					if(AIMoves > maxAIMoves) maxAIMoves = AIMoves; AIMoves = 0;
					if(maxAIMoves == 5) return 10;
				}
					
				if(board[j][i].getText() == PLAYER) {PlayerMoves ++; AIMoves = 0;}
				
				if(board[j][i].getText().isEmpty()) {
					if(PlayerMoves > maxPlayerMoves) maxPlayerMoves = PlayerMoves; PlayerMoves = 0;
					if(maxPlayerMoves == 5) return -10;
				}
				
				i--;
				j++;
			}
		}
		
		for(int y = NUM_OF_COL-1; y >= 0; y--) {
			int i = 0;
			int j = y;
			int AIMoves = 0;
			int PlayerMoves = 0;
			
			while(i < NUM_OF_ROW && j < NUM_OF_COL) {
				if(board[i][j].getText() == AI) {AIMoves++; PlayerMoves = 0;}
				
				if(board[i][j].getText() == EMPTY) {
					if(AIMoves > maxAIMoves) maxAIMoves = AIMoves; AIMoves = 0;
					if(maxAIMoves == 5) return 10;
				}
					
				if(board[i][j].getText() == PLAYER) {PlayerMoves ++; AIMoves = 0;}
				
				if(board[i][j].getText().isEmpty()) {
					if(PlayerMoves > maxPlayerMoves) maxPlayerMoves = PlayerMoves; PlayerMoves = 0;
					if(maxPlayerMoves == 5) return -10;
				}
				
				i++;
				j++;
			}
		}
		
		for(int x = 0; x < NUM_OF_ROW; x++) {
			int i = x;
			int j = NUM_OF_COL-1;
			int AIMoves = 0;
			int PlayerMoves = 0;
			
			while(i >= 0 && j >= 0) {
				if(board[i][j].getText() == AI) {AIMoves++; PlayerMoves = 0;}
				
				if(board[i][j].getText() == EMPTY) {
					if(AIMoves > maxAIMoves) maxAIMoves = AIMoves; AIMoves = 0;
					if(maxAIMoves == 5) return 10;
				}
					
				if(board[i][j].getText() == PLAYER) {PlayerMoves ++; AIMoves = 0;}
				
				if(board[i][j].getText().isEmpty()) {
					if(PlayerMoves > maxPlayerMoves) maxPlayerMoves = PlayerMoves; PlayerMoves = 0;
					if(maxPlayerMoves == 5) return -10;
				}
				
				i--;
				j--;
			}
		}
		
		for(int x = NUM_OF_COL-1; x >= 0; x--) {
			int i = x;
			int j = 0;
			int AIMoves = 0;
			int PlayerMoves = 0;
			
			// Bottom Right to Top left diagonal
			while(i < NUM_OF_ROW && j >= 0) {
				if(board[i][j].getText() == AI) {AIMoves++; PlayerMoves = 0;}
				
				if(board[i][j].getText() == EMPTY) {
					if(AIMoves > maxAIMoves) maxAIMoves = AIMoves; AIMoves = 0;
					if(maxAIMoves == 5) return 10;
				}
					
				if(board[i][j].getText() == PLAYER) {PlayerMoves ++; AIMoves = 0;}
				
				if(board[i][j].getText().isEmpty()) {
					if(PlayerMoves > maxPlayerMoves) maxPlayerMoves = PlayerMoves; PlayerMoves = 0;
					if(maxPlayerMoves == 5) return -10;
				}
				
				i++;
				j++;
			}
		}
		
		for(int y = 0; y < 15; y++) {
			int i = 15-1;
			int j = y;
			int AIMoves = 0;
			int PlayerMoves = 0;
			
			// Bottom Right to Top left diagonal
			while(i >= 0 && j >= 0) {
				if(board[i][j].getText() == AI) {AIMoves++; PlayerMoves = 0;}
				
				if(board[i][j].getText() == EMPTY) {
					if(AIMoves > maxAIMoves) maxAIMoves = AIMoves; AIMoves = 0;
					if(maxAIMoves == 5) return 10;
				}
					
				if(board[i][j].getText() == PLAYER) {PlayerMoves ++; AIMoves = 0;}
				
				if(board[i][j].getText().isEmpty()) {
					if(PlayerMoves > maxPlayerMoves) maxPlayerMoves = PlayerMoves; PlayerMoves = 0;
					if(maxPlayerMoves == 5) return -10;
				}
				
				i--;
				j--;
			}
		}
		
//		System.out.println("Max Player: " + maxPlayerMoves + " Max AI: " + maxAIMoves);
		if(maxAIMoves == maxPlayerMoves) return 0;
		return (maxAIMoves > maxPlayerMoves) ? (maxAIMoves * 2) : (maxPlayerMoves * -2);
	}
	
	public int evaluation_2() {
		int maxPlayerMoves = 1;
		int maxAIMoves = 1;
		int tempX = 0, tempY = 0;
		
		for(int i = 0; i < occupiedMoves.size(); i++) {
			Move moveCheck = occupiedMoves.get(i);
			int x = moveCheck.getX();
			int y = moveCheck.getY();
			String currentPlayer = board[x][y].getText();
			
			// Check left
			tempX = x-1;
			if(x+1 < NUM_OF_ROW && tempX >= 0) {
				if(board[x+1][y].getText().equals(EMPTY)) {
					int cnt = 1;
					while(board[tempX][y].getText().equals(currentPlayer)) {cnt++; tempX--; if(tempX < 0) break;}
					if(currentPlayer.equals(AI) && cnt > maxAIMoves) maxAIMoves = cnt;
					if(currentPlayer.equals(PLAYER) && cnt > maxPlayerMoves) maxPlayerMoves = cnt;
				}
			}
			
			// Check right
			tempX = x+1;
			if(x-1 >= 0 && tempX < NUM_OF_ROW) {
				if(board[x-1][y].getText().equals(EMPTY)) {
					int cnt = 1;
					while(board[tempX][y].getText().equals(currentPlayer)) {cnt++; tempX++; if(tempX == NUM_OF_ROW) break;}
					if(currentPlayer.equals(AI) && cnt > maxAIMoves) maxAIMoves = cnt;
					if(currentPlayer.equals(PLAYER) && cnt > maxPlayerMoves) maxPlayerMoves = cnt;
				}
			}
			
			// Check up
			tempY = y-1;
			if(y+1 < NUM_OF_COL && tempY >= 0) {
				if(board[x][y+1].getText().equals(EMPTY)) {
					int cnt = 1;
					while(board[x][tempY].getText().equals(currentPlayer)) {cnt++; tempY--; if(tempY < 0) break;}
					if(currentPlayer.equals(AI) && cnt > maxAIMoves) maxAIMoves = cnt;
					if(currentPlayer.equals(PLAYER) && cnt > maxPlayerMoves) maxPlayerMoves = cnt;
				}
			}
			
			// Check down
			tempY = y+1;
			if(y-1 >= 0 && tempY < NUM_OF_COL) {
				if(board[x][y-1].getText().equals(EMPTY)) {
					int cnt = 1;
					while(board[x][tempY].getText().equals(currentPlayer)) {cnt++; tempY++; if(tempY == NUM_OF_COL) break;}
					if(currentPlayer.equals(AI) && cnt > maxAIMoves) maxAIMoves = cnt;
					if(currentPlayer.equals(PLAYER) && cnt > maxPlayerMoves) maxPlayerMoves = cnt;
				}
			}
			
			// Check upper left
			tempX = x-1;
			tempY = y-1;
			if(x+1 < NUM_OF_ROW && y+1 < NUM_OF_COL && tempX >= 0 && tempY >=0) {
				if(board[x+1][y+1].getText().equals(EMPTY)) {
					int cnt = 1;
					while(board[tempX][tempY].getText().equals(currentPlayer)) {cnt++; tempX--; tempY--; if(tempX < 0 || tempY < 0) break;}
					if(currentPlayer.equals(AI) && cnt > maxAIMoves) maxAIMoves = cnt;
					if(currentPlayer.equals(PLAYER) && cnt > maxPlayerMoves) maxPlayerMoves = cnt;
				}
			}
			
			// Check upper right
			tempX = x+1;
			tempY = y-1;
			if(x-1 >= 0 && y+1 < NUM_OF_COL && tempX < NUM_OF_ROW && tempY >= 0) {
				if(board[x-1][y+1].getText().equals(EMPTY)) {
					int cnt = 1;
					while(board[tempX][tempY].getText().equals(currentPlayer)) {cnt++; tempX++; tempY--; if(tempX == NUM_OF_ROW || tempY < 0) break;}
					if(currentPlayer.equals(AI) && cnt > maxAIMoves) maxAIMoves = cnt;
					if(currentPlayer.equals(PLAYER) && cnt > maxPlayerMoves) maxPlayerMoves = cnt;
				}
			}
			
			// Check bottom left
			tempX = x-1;
			tempY = y+1;
			if(x+1 < NUM_OF_ROW && y-1 >= 0 && tempX >= 0 && tempY < NUM_OF_COL) {
				if(board[x+1][y-1].getText().equals(EMPTY)) {
					int cnt = 1;
					while(board[tempX][tempY].getText().equals(currentPlayer)) {cnt++; tempX--; tempY++; if(tempX < 0 || tempY == NUM_OF_COL) break;}
					if(currentPlayer.equals(AI) && cnt > maxAIMoves) maxAIMoves = cnt;
					if(currentPlayer.equals(PLAYER) && cnt > maxPlayerMoves) maxPlayerMoves = cnt;
				}
			}
			
			// Check bottom right
			tempX = x+1;
			tempY = y+1;
			if(x-1 >= 0 && y-1 >= 0 && tempX < NUM_OF_ROW && tempY < NUM_OF_COL) {
				if(board[x-1][y-1].getText().equals(EMPTY)) {
					int cnt = 1;
					while(board[tempX][tempY].getText().equals(currentPlayer)) {cnt++; tempX++; tempY++; if(tempX == NUM_OF_ROW || tempY == NUM_OF_COL) break;}
					if(currentPlayer.equals(AI) && cnt > maxAIMoves) maxAIMoves = cnt;
					if(currentPlayer.equals(PLAYER) && cnt > maxPlayerMoves) maxPlayerMoves = cnt;
				}
			}
			
			if(maxAIMoves == 5) return 10;
			if(maxPlayerMoves == 5) return -10;
		}
		
//		System.out.println("Max Player: " + maxPlayerMoves + " Max AI: " + maxAIMoves);
		if(maxAIMoves == maxPlayerMoves) return 0;
		return (maxAIMoves > maxPlayerMoves) ? (maxAIMoves * 2) : (maxPlayerMoves * -2);
	}
	
	public int minimax(int depth, boolean isAIMove, int alpha, int beta) {
		int score = evaluation_2();
		
		if(score == 10) return (score + depth);
		
		if(score == -10) return (score - depth);
		
		if(depth == 0) return (score < 0) ? score - depth : score + depth;
		
		if(occupiedMoves.size() == 225) return 0;
		
		if(isAIMove) {
			int bestValue = -Integer.MAX_VALUE;
			for(int i = 0; i < suitableMoves.size(); i++) {
				Move move = suitableMoves.get(i);
				if(move.compareTo(MARKER) == 0) continue;
				
				int x = move.getX();
				int y = move.getY();
				
//				System.out.println("added AI x: " + x + " y: " + y);
				setMove(x, y, "White", AI);
				
				int value = minimax(depth-1, false, alpha, beta);
				bestValue = Math.max(bestValue, value);
				alpha = Math.max(alpha, bestValue);
//				System.out.println("deleted x: " + x + "y: " + y);
				removeMove(x, y, i);
				
				if(beta <= alpha) {break; }
			}
			return bestValue;
		}else {
			int bestValue = +Integer.MAX_VALUE;
			for(int i = 0; i < suitableMoves.size(); i++) {
				Move move = suitableMoves.get(i);
				if(move.compareTo(MARKER) == 0) continue;
				
				int x = move.getX();
				int y = move.getY();
				
//				System.out.println("added P1 x: " + x + " y: " + y);
				setMove(x, y, "Black", PLAYER);
				
				int value = minimax(depth-1, true, alpha, beta);
				bestValue = Math.min(bestValue, value);
				beta = Math.min(bestValue, beta);
//				System.out.println("deleted x: " + x + "y: " + y);
				removeMove(x, y, i);

				if(beta <= alpha) break; 
			}
			return bestValue;
		}
	}
	
	public void getBestMove() {
		int bestValue = -Integer.MAX_VALUE;
		Move bestMove = new Move(-1,-1);
		
		for(int i = 0; i < suitableMoves.size(); i++) {
			Move move = suitableMoves.get(i);
			if(move.compareTo(MARKER) == 0) continue;
			
			int x = move.getX();
			int y = move.getY();
			
			setMove(x, y, "White", AI);
			int value = minimax(3, false, -Integer.MAX_VALUE, Integer.MAX_VALUE);
			removeMove(x, y, i);
			
			if(value > bestValue) {
				bestMove.setX(x);
				bestMove.setY(y);
				bestValue = value;
			}
		}

		setMove(bestMove.getX(), bestMove.getY(), "White", AI);
		checkCurrentStatus();
	}
	
	public void setMove(int i, int j, String src, String txt) {
		if(board[i][j].getIcon() == null) {
			board[i][j].setCursor(Cursor.getDefaultCursor());
			board[i][j].setIcon(new ImageIcon(Gomoku.class.getResource("/resources/" + src + ".png"))); 
			board[i][j].setText(txt);
			
			if(txt == PLAYER) isPlayerMove = false;
			if(txt == AI) isPlayerMove = true;
			Move move = new Move(i, j);
			
			occupiedMoves.push(move);
			
			if(!suitableMoves.isEmpty()) {suitableMoves.delNode(move); suitableMoves.push(MARKER);}
			for(int m = 0; m < 8; m++) {
				int x = i + MOVEX[m];
				int y = j + MOVEY[m];
				Move temp = new Move(x, y);
				if(x < 0 || y < 0 || x >= NUM_OF_ROW || y >= NUM_OF_COL || board[x][y].getIcon() != null) continue;
				if(!suitableMoves.contains(temp)) suitableMoves.push(temp);
			}
		}
//		displayArr();
	}
	
	public void removeMove(int i, int j, int index) {
		if(board[i][j].getIcon() != null) {
			board[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			board[i][j].setIcon(null); 
			board[i][j].setText("");
			
			occupiedMoves.pop();
			while(suitableMoves.pop().compareTo(MARKER) != 0) {
				// pop until marker
			}
			
			suitableMoves.addNodeByPosition(new Move(i, j), index);
		}
//		displayArr();
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getX() < 100 || e.getX() > 400 || e.getY() < 50 || e.getY() > 350) return;
		if(isPlayerMove) setMove((e.getX() - 100) / 20, (e.getY() - 50) / 20, "Black", PLAYER);
//		else setMove((e.getX() - 100) / 20, (e.getY() - 50) / 20, "White", AI);
		checkCurrentStatus();
	}
	
	public void checkCurrentStatus() {
		int score = evaluation_2();
		if(Math.abs(score) == 10) {
			end = true;
			graphic.setColor(Color.RED);
			graphic.fillRect(WIDTH/2 - 100, HEIGHT - 125, 200, 100);
			graphic.setColor(Color.white);
			Font font = new Font("Verdana", Font.BOLD, 20);
			graphic.setFont(font);
			graphic.drawString(((score == 10)? "AI wins !!!" : "You wins !!!!"), WIDTH/2 -60, HEIGHT - 65);
			if(score == 10) System.out.println("AI wins !!!"); 
			else System.out.println("You wins !!!");
		}
	}
	
	public void displayArr() {
		System.out.print("Occupied moves: [size = " + occupiedMoves.size() + "] ");
		occupiedMoves.display();
		System.out.print("Suitable moves: [size = " + suitableMoves.size() + "] ");
		suitableMoves.display();
		System.out.println();
	}
	
	public synchronized void start() {
		if (isRunning) return;
			
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!isRunning) return;
			
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException ex) {
			Logger.getLogger(Gomoku.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}
}



