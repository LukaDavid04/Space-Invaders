// PowerUp
// This is the power up class, it is used for the power up's type, location and collision.

public class PowerUp{
	private int x, y, width = 20, height = 20;
	private String name;
	public PowerUp(String name, int x, int y){
		this.name = name;
		this.x = x;
		this.y = y;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public String getName(){
		return name;
	}
	public void move(){
		y += 1;
	}
	public boolean collide(GoodShip player){ // checks for collisions with the player
		if(player.getX() < x && player.getX() + player.getWidth() > x){
			if(player.getY() < y && player.getY() + player.getHeight() > y){
				return true;
			}
			if(player.getY() > y && player.getY() < y + height){
				return true;
			}
		}
		if(player.getX() > x && player.getX() < x + width){
			if(player.getY() < y && player.getY() + player.getHeight() > y){
				return true;
			}
			if(player.getY() > y && player.getY() < y + height){
				return true;
			}
		}
		return false;
	}
}