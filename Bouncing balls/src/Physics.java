import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Physics implements ActionListener {
	Cords[] cr = new Cords[50];
	Thread[] ta = new Thread[cr.length];
	long rate = 15;
	private boolean running = true;
	private boolean frozen = false;

	public boolean colliding(Cords cor) {  // TODO: move into same thread as movement
		for (int z = 0; z < cr.length; z++) { // for every entity in cr
			if (cr[z] != null && cor.id != z) { // make sure the entity isn't
												// null, avoid
				float xd = cor.getX() - cr[z].getX();
				float yd = cor.getY() - cr[z].getY();

				float sumRadius = 30;
				float sqrRadius = sumRadius * sumRadius;

				float distSqr = (xd * xd) + (yd * yd);

				if (distSqr <= sqrRadius) {
					//collided(cr[z], cor);
					return true;
				}
			}
		}

		return false;
	}

	public void collided(Cords cor, Cords cor2) {
		if (!cor.collided) {
			
		}
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
					cr[z].collided = false;
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
			if(timeLeft/1000000L < 11){
				System.out.println(timeLeft/1000000L);
				System.out.println(System.nanoTime() - beginTime);
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
