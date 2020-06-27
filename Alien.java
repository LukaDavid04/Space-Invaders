// Alien
// This is the alien class, it is used for the alien's movement and health.

public class Alien{
	private int Ay, score, direction, Ax, width, height, hp;
	private Bullet bullet;
	public Alien(int Ax, int Ay, int score){
		this.Ax = Ax;
		this.Ay = Ay;
		this.score = score;
		this.direction = 0;
		this.width = 30;
		this.height = 57;
		this.hp = 3;
	}
	public int getX(){
		return Ax;
	}
	public int getScore(){
		return score;
	}
	public int getY(){
		return Ay;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public int getHp(){
		return hp;
	}
	public void damage(){
		hp -= 1;
	}
	public boolean move(int speed){
		if(direction == 0){
			Ax -= 1 + speed * 0.03;
			if(Ax < 50){
				return true;
			}
			return false;
		}
		if(direction == 1){
			Ax += 1 + speed * 0.03;
			if(Ax > 950){
				return true;
			}
		}
		return false;
	}
	public void shift(){
		if(direction == 0){
			direction = 1;
			Ay += 40;
			Ax += 5;
		}
		else{
			direction = 0;
			Ay += 40;
			Ax -= 5;
		}
	}
	public Bullet shoot(){
		bullet = new Bullet(Ax + 10, Ay, 3, 1);
		return bullet;
	}
}