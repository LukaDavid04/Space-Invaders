// SpaceInvaders
// By: Jack Li and Luka David
// This program is designed to create a space battle for the user. The user will have to defend his/her planet from the attack of aliens
// with their battleship and various upgrades. The user can collect lives, upgrade their equipment and hide behind barriers. This file 
// contains the main code used to run the program.

// Various Imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.*;
import java.io.*;

public class SpaceInvaders extends JFrame implements ActionListener,KeyListener{
	javax.swing.Timer myTimer;
	GamePanel game;

    public SpaceInvaders() {
		super("SpaceInvaders");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,600);

		myTimer = new javax.swing.Timer(20, this); // A timer is used to count time spent with upgrades and to increase difficulty
		myTimer.start();

		game = new GamePanel();
		add(game);
		addKeyListener(this);
		setResizable(false);
		
		new MyMenu(this); // menu screen created for the user
		
    }
    public void start(){
		myTimer.start();
		setVisible(true);
    }

	public void actionPerformed(ActionEvent evt){
		if(game != null){
			game.refresh();
			game.repaint();
		}
	}

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
    	game.setKey(e.getKeyCode(),true);
    }

    public void keyReleased(KeyEvent e) {
    	game.setKey(e.getKeyCode(),false);
    }

    public static void main(String[] arguments) { // starts the game
		SpaceInvaders frame = new SpaceInvaders();
    }
}

class MyMenu extends JFrame implements ActionListener{ // This is the menu class where all the images and buttons are created and added
	private SpaceInvaders space;
	JButton play = new JButton("Play");
	
	public MyMenu(SpaceInvaders s){
		super("Menu");
		setSize(1000, 600);
		space = s;
		play.addActionListener(this);
		ImageIcon menu = new ImageIcon("images/background.jpg");
		JLabel backLabel = new JLabel(menu);
		JLayeredPane mPage = new JLayeredPane();
		mPage.setLayout(null);
		backLabel.setSize(1000, 600);
		backLabel.setLocation(0, 0);
		mPage.add(backLabel,1);		
		play.setSize(200, 60);
		play.setLocation(400, 360);
		play.setBackground(Color.BLUE);  // I got this bit of code from stack overflow
    	play.setForeground(Color.WHITE);
		mPage.add(play, 3);
		
		add(mPage); // Adds the images and buttons to the screen
		setVisible(true);
	}
	
    public void actionPerformed(ActionEvent evt) {
    	space.start();
    	setVisible(false);
	}
}

class GamePanel extends JPanel{ // This is the gamepanel class where the images are added and game methods are
	private boolean []keys;
	private Image mainBack, back1, back2, dbshot, back3, bossShip, playerShip, alienShip, life, GameOver, laserbeam, doubleScore, BIGdoubleScore, extraLife, boss;
	private Image b1, b2, b3, b4, b5;
	private String highScore = "";
	private GoodShip player;
	private PowerUp powerup;
	private boolean limit = false, madeBarrier = false, down = false, laser = false, gameOver = false, doubleShot = false, twoScore = false, tagged2 = false;
	private int delay = 100, time = 0, score = 0, by = 0, by2 = 0, by3 = 0, maxWave, level = 0, laserCount = 0, doubleCount = 0, doubleTimer = 0, powerupCount = 2000, minCount = 1000;
	ArrayList<Bullet>bullets = new ArrayList<Bullet>();
	ArrayList<Bullet>badBullets = new ArrayList<Bullet>();
	ArrayList<Alien>aliens = new ArrayList<Alien>();
	ArrayList<Alien>dead = new ArrayList<Alien>();
	ArrayList<PowerUp>powerups = new ArrayList<PowerUp>();
	ArrayList<Boss>bosses = new ArrayList<Boss>();
	ArrayList<Barrier>barriersList = new ArrayList<Barrier>();

	public GamePanel(){
		keys = new boolean[KeyEvent.KEY_LAST+1];
		player = new GoodShip(500);
		//Load Images
		b1 = new ImageIcon("Images/b1.png").getImage();
		b2 = new ImageIcon("Images/b2.png").getImage();
		b3 = new ImageIcon("Images/b3.png").getImage();
		b4 = new ImageIcon("Images/b4.png").getImage();
		b5 = new ImageIcon("Images/b5.png").getImage();
		back1 = new ImageIcon("Images/OuterSpace.png").getImage();
		back2 = new ImageIcon("Images/OuterSpace1.png").getImage();
		back3 = new ImageIcon("Images/OuterSpace2.png").getImage();
		playerShip= new ImageIcon("Images/playerShip.png").getImage();
		alienShip = new ImageIcon("Images/alienShip.png").getImage();
		bossShip = new ImageIcon("Images/BOSS.png").getImage();
		GameOver = new ImageIcon("Images/GameOver.png").getImage();
		laserbeam = new ImageIcon("Images/laserbeam.png").getImage();
		doubleScore = new ImageIcon("Images/doubleScore.png").getImage();
		dbshot = new ImageIcon("Images/2guns.png").getImage();
		extraLife = new ImageIcon("Images/extraLife.png").getImage();
		//Scale Images
		playerShip = playerShip.getScaledInstance(60, 60, playerShip.SCALE_SMOOTH);
		life = playerShip.getScaledInstance(30, 30, playerShip.SCALE_SMOOTH);
		back1 = back1.getScaledInstance(1000, 600, back1.SCALE_SMOOTH);
		back2 = back2.getScaledInstance(1000, 600, back2.SCALE_SMOOTH);
		back3 = back3.getScaledInstance(1000, 600, back3.SCALE_SMOOTH);
		alienShip = alienShip.getScaledInstance(30, 57, alienShip.SCALE_SMOOTH);
		boss = bossShip.getScaledInstance(100, 100, bossShip.SCALE_SMOOTH);
		GameOver = GameOver.getScaledInstance(1000, 600, GameOver.SCALE_SMOOTH);
		laserbeam = laserbeam.getScaledInstance(20, 20, laserbeam.SCALE_SMOOTH);
		doubleScore = doubleScore.getScaledInstance(20, 20, doubleScore.SCALE_SMOOTH);
		BIGdoubleScore = doubleScore.getScaledInstance(40, 40, doubleScore.SCALE_SMOOTH);
		extraLife = extraLife.getScaledInstance(20, 20, extraLife.SCALE_SMOOTH);
		dbshot = dbshot.getScaledInstance(20, 20, dbshot.SCALE_SMOOTH);
		
	
		setSize(1000, 600);
	}
	public String GetHighScore(){ // Retrieves the high score from the scoreboard file
		FileReader readFile = null; // I got this file import code from a coder on Stack Over Flow (Brandonio)
		BufferedReader reader = null;
		try{
			readFile = new FileReader("scoreboard.txt"); // where the highest score is kept
			reader = new BufferedReader(readFile);
			return reader.readLine(); // there's only one line for the highest score
		}
		catch(Exception e){ // incase the file is empty
			return "0";
		}
		finally{
			try{
				reader.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public void CheckScore(){ // Checks if the current score is higher than the high score
		if(score>Integer.parseInt(highScore)){
			highScore = ""+score;
		}
	}
    public void setKey(int k, boolean v) {
    	keys[k] = v;
    }
	public void refresh(){
		if(madeBarrier == false){ // checks if the barriers were created at the start of the game
			spawnBarrier();
			madeBarrier = true;
		}
		Random random = new Random();
		if(keys[KeyEvent.VK_LEFT] ){ // allows player to move using arrow keys
			player.move(0);
		}
		if(keys[KeyEvent.VK_RIGHT] ){
			player.move(1);
		}
		if(keys[KeyEvent.VK_SPACE]){ // allows player to shoot using space bar
			if(doubleShot){
				if(time < 0){ 
					bullets.add(new Bullet(player.getX(), 480, 10, 0));
					bullets.add(new Bullet(player.getX() + 58, 480, 10, 0));
					doubleCount -= 1;
					time = delay;
				}
			}
			else{
				if(time < 0){
					bullets.add(player.shoot());
					time = delay; // ensures the user's bullets are not fired continously
				}	
			}
			if(doubleCount <= 0){
				doubleShot = false;
			}
		}
		if(keys[KeyEvent.VK_Z]){
			if(time < 0){
				if(laser){ // When a laser power up is equiped
					for(int j = 0; j < 30; j++){
						bullets.add(new Bullet(player.getX() + 29, 480 + j * 10, 10, 0));
					}
					laserCount -= 1;
				}
				if(laserCount <= 0){
					laser = false;
				}
				time = delay;
			}
		}
		if(true){
			powerupCount = powerupCount - level * 200;
			if(powerupCount < minCount){
				powerupCount = minCount;
			}
			int x = random.nextInt(powerupCount); // ensures random power ups are dropped
			if(x <= 2){
				powerups.add(new PowerUp("laser", random.nextInt(800), 0));
			}
			if(x > 2 && x <= 4){
				powerups.add(new PowerUp("life", random.nextInt(800), 0));
			}
			if(x > 4 && x <= 6){
				powerups.add(new PowerUp("double", random.nextInt(800), 0));
			}
			if(x == 7){
				powerups.add(new PowerUp("doubleScore", random.nextInt(800), 0));
			}
		}
		if(aliens.isEmpty() && bosses.isEmpty()){ // when all the enemies in a wave are killed
			level += 1;
			if(level < 6){
				spawnWave();
			}
			else{ // after level 6, there are only boss waves
				spawnBoss();
			}
		}
		for(int i = powerups.size() - 1; i >= 0; i--){ // for loop for letting power ups descend to the player
			powerups.get(i).move();
			if(powerups.get(i).collide(player) == true){
				if(powerups.get(i).getName().equals("laser")){
					laser = true;
					laserCount += 1;
				}
				if(powerups.get(i).getName().equals("life")){
					player.addLife(1);
				}
				if(powerups.get(i).getName().equals("double")){
					doubleShot = true;
					doubleCount += 10;
				}
				if(powerups.get(i).getName().equals("doubleScore")){
					twoScore = true;
					doubleTimer = 500;
				}
				powerups.remove(i);
			}
		}
		for(int i = 0; i < bullets.size(); i++){ // for-loop for when a bullet collides with something
			boolean killbullet = false; // boolean allowing the bullet the freedom to hit anything that has collison
			bullets.get(i).move(); // after the bullets are moved, they check for collisions
			for(int j = 0; j < aliens.size(); j++){ // checks for alien collisons
				boolean tagged = false;
				tagged = bullets.get(i).hit(aliens.get(j));
				if(tagged){
					score += aliens.get(j).getScore();
					if(twoScore){
						score += aliens.get(j).getScore();
					}
					aliens.get(j).damage();
					if(aliens.get(j).getHp() == 0){
						dead.add(aliens.get(j));
						aliens.remove(j);
					}
					killbullet = true;
					j = aliens.size();
					CheckScore();
				}
			}
			for(int x = 0; x < barriersList.size(); x++){ // checks for barrier collisions
				boolean Btagged = false;
				Btagged = bullets.get(i).hit(barriersList.get(x));
				if(Btagged){
					barriersList.get(x).barrierHit();
					if(barriersList.get(x).getStage() == 6){
						barriersList.remove(x);
					}
					x = barriersList.size();
					killbullet = true;
				}
			}
			for(int j = 0; j < bosses.size(); j++){ // checks for boss collisions
				boolean tagged2 = false;
				tagged2 = bullets.get(i).hit(bosses.get(j));
				if(tagged2){
					score += bosses.get(j).getScore();
					if(twoScore){
						score += bosses.get(j).getScore();
					}
					bosses.get(j).damage();
					if(bosses.get(j).getHp() == 0){
						bosses.remove(j);
					}
					j = bosses.size();
					killbullet = true;
					CheckScore();
				}
			}
			if(killbullet){
				bullets.remove(i); // removes the bullet if anything contacted it
			}
		}
		for(int i = 0; i < badBullets.size(); i++){ // same for-loop structure, just for bullets shot by aliens
			badBullets.get(i).move();
			boolean killbadbullet = false;
			boolean tagged = false;
			tagged = badBullets.get(i).hit(player);
			if(tagged){
				player.loseLife();
				killbadbullet = true;
			}
			for(int k = 0; k < barriersList.size(); k++){
				boolean BBtagged = false;
				BBtagged = badBullets.get(i).hit(barriersList.get(k));
				if(BBtagged){
					barriersList.get(k).barrierHit();
					if(barriersList.get(k).getStage() == 6){
						barriersList.remove(k);
					}
					killbadbullet = true;
					k = barriersList.size();
				}
			}
			if(killbadbullet){
				badBullets.remove(i);
			}
		}
		for(int i = 0; i < aliens.size(); i++){ // this for-loop creates random bullets to be shot by the aliens
			int n = random.nextInt(1000 - dead.size()); // the more dead aliens the more bullets
			if(n == 0){
				badBullets.add(aliens.get(i).shoot());
			}
			limit = aliens.get(i).move(dead.size());
			if(aliens.get(i).getY() > 500){
				down = true;
			}
			if(limit == true){
				for(int j = 0; j < aliens.size(); j++){
					aliens.get(j).shift();
				}
				limit = false;
			}	
		}
		for(int i = 0; i < bosses.size(); i++){ // chooses random boss bullets
			int n = random.nextInt(10);
			if(n == 0){
				badBullets.add(bosses.get(i).shoot());
			}
			limit = bosses.get(i).move();
			if(bosses.get(i).getY() > 500){
				down = true;
			}
			if(limit == true){
				for(int j = 0; j < bosses.size(); j++){
					bosses.get(j).shift();
				}
				limit = false;
			}
		}
		if(player.getLives() <= 0){ // if the player dies
			gameOver = true;
		}
		time -= 8;
		if(doubleTimer > 0){
			doubleTimer -= 1;
		}
		if(doubleTimer <= 0){
			twoScore = false;
		}
	}
	public void spawnWave(){ // new wave is created here
		player.addLife(level); // the player recieves lives for defeating a wave
		for(int i = 0; i < dead.size(); i++){
			dead.remove(i);
		}
    	for(int i = 0; i < level; i++){ // as the game continues the levels get harder
    		for(int j = 0; j < 10; j++){
    			aliens.add(new Alien(300 + 50 * j, 100 + i * 50, 50));
    		}
    	}
    }
    public void spawnBarrier(){ // the barriers are created here
    	for(int i=0; i < 4; i++){
    		barriersList.add(new Barrier(90+250*i,425,1));
    		barriersList.add(new Barrier(90+250*i,400,1));
    		barriersList.add(new Barrier(90+250*i,375,1));
    		barriersList.add(new Barrier(115+250*i,375,1));
    		barriersList.add(new Barrier(140+250*i,375,1));
    		barriersList.add(new Barrier(140+250*i,400,1));
    		barriersList.add(new Barrier(140+250*i,425,1));
    	}
    }
    public void spawnBoss(){ // bosses are created here
    	int num = 0;
    	player.addLife(level); // based on the level, more bosses appear
    	for(int i = 0; i < dead.size(); i++){
			dead.remove(i);
		}
		if(level < 9){
			num = level;
		}
		else{
			num = 9;
		}
		for(int i = 0; i < num - 5; i++){
			bosses.add(new Boss(200 + i * 150, 50, 100));
		}
    }

    public void paintComponent(Graphics g){ // everything is drawn here
  		// Background
    	g.fillRect(0, 0, 1000, 600); // moving background is created here
    	g.drawImage(back1, 0, by, this);
    	g.drawImage(back1, 0, by - 600, this);
    	g.drawImage(back2, 0, by2, this);
    	g.drawImage(back2, 0, by2 - 600, this);
  		g.drawImage(back3, 0, by3, this);
  		g.drawImage(back3, 0, by3 - 600, this);
  		g.drawImage(playerShip, player.getX(), 480, this);
  		// Text on Screen
		g.setColor(new Color(255, 255, 255));
		g.setFont(new Font("Earth Orbiter", Font.PLAIN, 33));
		g.drawString("LIVES: ", 10, 35);
		g.drawString(Integer.toString(player.getLives()), 130, 35);
		g.drawString("SCORE: ", 700, 35);
		g.drawString(Integer.toString(score), 825, 35);
		g.drawString("LASERS: ", 10, 85);
		g.drawString(Integer.toString(laserCount), 150, 85);
		g.drawString("DOUBLESHOT: ", 400, 35);
		g.drawString(Integer.toString(doubleCount), 640, 35);
		g.drawString("HIGHSCORE: ", 610, 85);
		g.drawString(highScore, 830, 85);
		if(twoScore){
			g.drawImage(BIGdoubleScore, 940, 95, this);
		}
		if(highScore.equals("")){
			highScore = this.GetHighScore();
		}
		g.drawString("LEVEL: ", 400, 85);
		g.drawString(Integer.toString(level), 515, 85);
		// Bullets
		for(int i = 0; i < bullets.size(); i++){
			g.setColor(new Color(222, 0, 0));
			g.fillRect(bullets.get(i).getX(), bullets.get(i).getY(), 5, 10);	
		}
		// Alien Bullets
		for(int i = 0; i < badBullets.size(); i++){
			g.setColor(new Color(0, 222, 0));
			g.fillRect(badBullets.get(i).getX(), badBullets.get(i).getY(), 5, 10);
		}
		// Power ups
		for(int i = 0; i < powerups.size(); i++){
			PowerUp powerup = powerups.get(i);
			if(powerup.getName().equals("laser")){
				g.drawImage(laserbeam, powerup.getX(), powerup.getY(), this);
			}
			if(powerup.getName().equals("life")){
				g.drawImage(extraLife, powerup.getX(), powerup.getY(), this);
			}
			if(powerup.getName().equals("double")){
				g.drawImage(dbshot, powerup.getX(), powerup.getY(), this);
			}
			if(powerup.getName().equals("doubleScore")){
				g.drawImage(doubleScore, powerup.getX(), powerup.getY(), this);
			}
		}
		// Aliens
		for(int i = 0; i < aliens.size(); i++){
			g.drawImage(alienShip, aliens.get(i).getX(), aliens.get(i).getY(), this);
			g.setColor(new Color(255, 255, 255));
			g.fillRect(aliens.get(i).getX(), aliens.get(i).getY() - 4, aliens.get(i).getWidth(), 5);
			g.setColor(new Color(255, 0, 0));
			g.fillRect(aliens.get(i).getX() + 3, aliens.get(i).getY() - 3, aliens.get(i).getHp() * 8, 3);
		}
		// Bosses
		for(int i = 0; i < bosses.size(); i++){
			g.drawImage(boss, bosses.get(i).getX() - 2, bosses.get(i).getY(), this);
			g.setColor(new Color(255, 255, 255));
			g.fillRect(bosses.get(i).getX() - 3, bosses.get(i).getY() - 8, bosses.get(i).getWidth(), 10);
			g.setColor(new Color(255, 0, 0));
			g.fillRect(bosses.get(i).getX(), bosses.get(i).getY() - 4, bosses.get(i).getHp() * 3, 3);
		}
		for(int i = 0; i < player.getLives(); i++){
			g.drawImage(extraLife, 175 + i * 20, 15, this);
		}
		// Barriers
		for(int i = 0; i < barriersList.size(); i++){
    		if(barriersList.get(i).getStage() == 1){
    			g.drawImage(b1, barriersList.get(i).getX(), barriersList.get(i).getY(), this);
    		}
    		if(barriersList.get(i).getStage() == 2){
    			g.drawImage(b2, barriersList.get(i).getX(), barriersList.get(i).getY(), this);
    		}
    		if(barriersList.get(i).getStage() == 3){
    			g.drawImage(b3, barriersList.get(i).getX(), barriersList.get(i).getY(), this);
    		}
    		if(barriersList.get(i).getStage() == 4){
    			g.drawImage(b4, barriersList.get(i).getX(), barriersList.get(i).getY(), this);
    		}
    		if(barriersList.get(i).getStage() == 5){
    			g.drawImage(b5, barriersList.get(i).getX(), barriersList.get(i).getY(), this);
    		}
		}
		if(down){
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, 1000, 600);
			g.drawImage(GameOver, 0, 0, this);
		}
		if(gameOver == true){
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, 1000, 600);
			g.drawImage(GameOver, 0, 0, this);
		}
		by += 5;
		by2 += 4;
		by3 += 3;
		if(by > 600){
			by = 0;
		}
		if(by2 > 600){
			by2 = 0;
		}
		if(by3 > 600){
			by3 = 0;
		}
    }
}





