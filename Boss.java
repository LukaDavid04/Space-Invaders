// Boss
// This is the Boss ship class, it is used for te bosses movement, location, and health.

import java.util.*;
public class Boss{
	private int By, score, direction, Bx, width, height, hp;
	private Bullet bullet;
	public Boss(int Bx, int By, int score){
		this.Bx = Bx;
		this.By = By;
		this.score = score;
		this.direction = 0;
		this.width = 100;
		this.height = 100;
		this.hp = 30;
	}
	public int getX(){
		return Bx;
	}
	public int getScore(){
		return score;
	}
	public int getY(){
		return By;
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
	public boolean move(){
		if(direction == 0){
			Bx -= 2;
			if(Bx < 50){
				return true;
			}
			return false;
		}
		if(direction == 1){
			Bx += 2;
			if(Bx > 900){
				return true;
			}
		}
		return false;
	}
	public void shift(){
		if(direction == 0){
			direction = 1;
			By += 40;
			Bx += 5;
		}
		else{
			direction = 0;
			By += 40;
			Bx -= 5;
		}
	}
	public Bullet shoot(){
		Random random = new Random();
		bullet = new Bullet(Bx + random.nextInt(100), By, 3, 1);
		return bullet;
	}
}