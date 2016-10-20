import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Physics implements ActionListener {
	Cords[] cr = new Cords[50];
	Thread[] ta = new Thread[cr.length];
	long rate = 7;
	private boolean running = true;
	private boolean frozen = false;

	public boolean colliding(Cords cor) {  // TODO: move into same thread as movement
		for (int z = 0; z < cr.length; z++) { // for every entity in cr
			if (cr[z] != null && cor.id != z) { // make sure the entity isn't
												// null, avoid
				float xd = cor.getX() - cr[z].getX();
				float yd = cor.getY() - cr[z].getY();

				float sumRadius = 40;
				float sqrRadius = sumRadius * sumRadius;

				float distSqr = (xd * xd) + (yd * yd);
				double dist = (double) Math.sqrt(distSqr);

				if (distSqr < sqrRadius) {
					double sin = (cor.getY()-cr[z].getY())/dist;
					double cos = (cor.getX()-cr[z].getX())/dist;
					double newVelX1 =-1*cor.getRise()*cos*sin + cor.getRun()*cos*cos + cr[z].getRise()*cos*sin + cr[z].getRun()*cos*cos;
					double newVelY1 =cor.getRise()*cos*cos - cor.getRun()*cos*sin + cr[z].getRise()*cos*cos + cr[z].getRun()*cos*sin;
					double newVelX2 =-1*cr[z].getRise()*cos*sin + cr[z].getRun()*sin*sin + cor.getRise()*cos*sin + cor.getRun()*sin*sin;
					double newVelY2 =cr[z].getRise()*sin*sin - cr[z].getRun()*cos*sin + cor.getRise()*sin*sin + cor.getRun()*cos*sin;
					int centerx1 = (int)(cor.getX() + sumRadius/2);
					int centery1 = (int)(cor.getY() + sumRadius/2);
					int centerx2 = (int)(cr[z].getX() + sumRadius/2);
					int centery2 = (int)(cr[z].getY() + sumRadius/2);
					int midpointx = (centerx1 + centerx2) / 2;
					int midpointy = (centery1 + centery2) / 2;
					cor.setX((int) (midpointx + (sumRadius/2 +3) * (centerx1 - centerx2) / dist - sumRadius/2)); 
					cor.setY((int) (midpointy + (sumRadius/2 +3) * (centery1 - centery2) / dist - sumRadius/2)); 
					cr[z].setX((int) (midpointx + (sumRadius/2 +3) * (centerx2 - centerx1) / dist - sumRadius/2)); 
					cr[z].setY((int) (midpointy + (sumRadius/2 +3) * (centery2 - centery1) / dist - sumRadius/2));
					cr[z].setRun(newVelX2);
					cr[z].setRise(newVelY2);
					cor.setRun(newVelX1);
					cor.setRise(newVelY1);
					//cor.run();
					//cr[z].run();
					return true;
				}
			}
		}

		return false;
	}

	synchronized void start() {
		long beginTime, timeTaken;
		while (running) {
			beginTime = System.nanoTime();
			if(!frozen){
			for (int z = 0; z < cr.length; z++) { // for every entity in cr
				if (cr[z] != null) { // make sure the entity isn't null, avoid
					ta[z] = new Thread(cr[z]);
					ta[z].start();
				}
			}
			for (int z = 0; z < cr.length; z++) { // for every entity in cr
				if (cr[z] != null) { // make sure the entity isn't null, avoid
					if (colliding(cr[z])) {
						// cr[z].c = Color.BLACK;
					} else {
						// cr[z].c = cr[z].oc;
					}
				}
			}
			timeTaken = System.nanoTime() - beginTime;
			long timeLeft= ((rate*1000000L) - timeTaken);
			if(timeLeft<0) timeLeft = 0;
			try {
				Thread.sleep(timeLeft/1000000L,(int) (timeLeft%1000000L));
			} catch (InterruptedException e) {

				e.printStackTrace();
			}	
		}else{
			try {
				Thread.sleep(rate);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Clear")){
			for (int z = 0; z < cr.length; z++) { // for every entity in cr
				ta[z] = null;
				cr[z] = null;
			}
		}else{
			frozen = !frozen;
		}
	}
}
