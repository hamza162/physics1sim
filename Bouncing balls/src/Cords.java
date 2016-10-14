import java.awt.Color;
//These are the ball objects
class Cords implements Runnable {
	//Basic info, self explanatory, keep in mind y is upside down, so rise is actually fall in our view
	double x = 0;
	double y = 0;
	double rise;
	double run;
	//used these for changing colors when testing out collisions, doesnt really matter
	Color c;
	Color oc;
	// Thread t = new Thread(this);
	//setting boundaries
	private static int maxX = 500;
	private static int maxY = 500;
	boolean freeze = false;
	// used to prevent double collisions
	boolean collided;

	public int getX() {
		return (int) Math.round(x);
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int) Math.round(y);
	}

	public void setY(int y) {
		this.y = y;
	}

	int[] tile = new int[2];
	int id;

	Cords(int nx, int ny, Color cr, int ri, int ru, int ID) {
		this.id = ID;
		x = nx;
		y = ny;
		c = cr;
		oc = c;
		rise = ri;
		run = ru;
		//old code i just noticed, dk why i did all this
//		if (ri == 0 || ru == 0) {
//			rise = ri;
//			run = ru;
//		} else {
//			//rise = ri / gcd(ri, ru);
//			//run = ru / gcd(ri, ru);
//		}
	}

//	static int gcd(int a, int b) {
//		while (b > 0) {
//			int temp = b;
//			b = a % b; // % is remainder
//			a = temp;
//		}
//		return a;
//	}

	public void setCords(int nx, int ny) {
		x = nx;
		y = ny;
	}
//this method is run for every ball on every tick
	public synchronized void run() {
		if (freeze) // dont do anything if frozen
			return;
		if (y <= 0) {
			rise = rise*-1; // reflect velocity up
			if (y > 0) {//teleport back into bounds
				y = 0;
			}
			//rise += 1;
		} else if (y >= getMaxY()) { // this is when ball hits the floor
			rise = Math.abs(rise) * -1; // reflect velocity down
			// all this stuff is reactions to floor
			
			if (rise <= -4) { //changing rise so it loses velocity when it bounces
				rise = rise / 1.3;
			} else if (rise > -4 && rise > .5) { //prevent bouncing forever
				rise = rise + .5;
			} else {
				rise = 0;
			}
			if (run <= -0.03) { //friction
				run = run + 0.03;
			} else if (run > -0.03 && run < 0.03) { // preventing screwy math
				run = 0;
			} else {
				run = run - 0.03;
			}
			if (y > getMaxY()) {//teleport back into bounds
				y = getMaxY();
			}
		} else
			rise += 1; // gravity 
		y = y + rise; //move y

		if (x <= 0) {
			run = Math.abs(run);
		} else if (x >= getMaxX()) {
			run = Math.abs(run) * -1;
			if (x > getMaxX()) {
				x = getMaxX();
			}
		}
		x = x + run;//move x

	}

	public static int getMaxX() {
		return maxX;
	}

	public static void setMaxX(int maxX) {
		Cords.maxX = maxX;
	}

	public static int getMaxY() {
		return maxY;
	}

	public static void setMaxY(int maxY) {
		Cords.maxY = maxY;
	}

}