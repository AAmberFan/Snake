package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent; 
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Yard extends Frame {

	PrintThread printThread = new PrintThread();
	private boolean gameOver = false;
	public static final int ROWS = 40;
	public static final int COLS = 40;
	public static final int BLOCK_SIZE = 20;
	private int score = 0;
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	Snake snake = new Snake(this);
	Egg egg = new Egg();
	Image offScreenImage = null;
	public void launch() {
		this.setLocation(200,200);
		this.setSize(COLS*BLOCK_SIZE, COLS*BLOCK_SIZE );
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setVisible(true);
		this.addKeyListener(new KeyMonitor());
		new Thread(printThread).start();
	}
	@Override
	public void paint(Graphics g) {
		Color color = g.getColor();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, ROWS*BLOCK_SIZE, COLS*BLOCK_SIZE );
		g.setColor(Color.DARK_GRAY);
		for(int i=1; i<ROWS;i++) {
			g.drawLine(0, BLOCK_SIZE*i, COLS*BLOCK_SIZE, BLOCK_SIZE*i);
		}
		for(int i=1; i<COLS;i++) {
			g.drawLine(BLOCK_SIZE*i, 0, BLOCK_SIZE*i, ROWS*BLOCK_SIZE);
		}
		g.setColor(color);
	
		snake.eat(egg);
		egg.draw(g);
		snake.draw(g);
		g.setColor(Color.YELLOW);
		g.drawString("Score:"+ score, 10, 60);
		if(gameOver) {
			Font font = new Font("Verdana", 10 , 30);
			g.setFont(font);
			g.drawString("GAME OVER  your scrore is: "+ score, 200 ,300);
			printThread.gameOver();
		}
		
	}
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(ROWS*BLOCK_SIZE, COLS*BLOCK_SIZE );
		}
		Graphics gOff = offScreenImage.getGraphics();
		print(gOff);
		g.drawImage(offScreenImage,0,0,null);
	}
	private class PrintThread implements Runnable{
		private boolean running = true;
		public void run() {
			while(running) {
				repaint();
				try {
					Thread.sleep(200);
					
				}catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		public void gameOver() {
			running = false;
		}
	}
	public static void main(String[] args) {
		new Yard().launch();
	}
	public void stop() {
		gameOver = true;
	}
	private class KeyMonitor extends KeyAdapter{
		
		public void keyPressed(KeyEvent e) {
			snake.keyPressed(e);
		}
	}
}
