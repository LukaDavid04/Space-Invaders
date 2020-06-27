// GoodShip
// This is the player ship class, it is used for the player's location and health.

public class GoodShip{
	private int lives, playerx, maxlives = 10;
	private Bullet bullet;
	public GoodShip(int playerx){
		this.playerx = playerx;
		lives = 4;
	}
	public int getX(){
		return playerx;
	}
	public int getY(){
		int y = 480;
		return y;
	}
	public int getWidth(){
		int width = 60;
		return width;
	}
	public int getHeight(){
		int height = 60;
		return height;
	}
	public int getLives(){
		return lives;
	}
	public void loseLife(){
		lives -= 1;
	}
	public void addLife(int n){
		if(lives + n < maxlives){ // caps lives at 10
			lives += n;
		}
		else{
			lives = maxlives;
		}
	}
	public void move(int direction){
		if(direction == 0){
			if(playerx >= 10){
				playerx -= 5;
			}
		}
		if(direction == 1){
			if(playerx <= 925){
				playerx += 5;
			}
		}
	}
	public Bullet shoot(){
		bullet = new Bullet(playerx + 29, 480, 3, 0);
		return bullet;
	}
}