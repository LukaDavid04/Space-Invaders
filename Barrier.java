// Barrier
// This is the barrier class, it is used for the barriers stage and location.

public class Barrier{
	private int x, y, stage;
	public Barrier(int x, int y, int stage){
		this.stage = 1;
		this.x = x;
		this.y = y;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getStage(){
		return stage;
	}
	public void barrierHit(){
		stage += 1;		
	}
}