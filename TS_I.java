package Team;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.hardware.*;
import lejos.hardware.sensor.*;



public class TS_I extends Thread{
	  private SampleProvider sampler;
	  private int touched =0;
	  private boolean stop=false;
	  public TS_I() {
		// TODO Auto-generated constructor stub
		  EV3TouchSensor ts= new EV3TouchSensor(SensorPort.S1);
		  sampler = ts.getMode("Touch");
		  this.setDaemon(true); // main이 죽으면 죽는다는 거인듯?
		  
	}
	 
	  public synchronized int getTouched(){
		  return touched;
	  }
	  
	  public void stop_ts(){
		  stop = true;
	  }
	  
	  @Override
	public void run() {
		// TODO Auto-generated method stub
	//	super.run();
		  float [] sample = new float[sampler.sampleSize()];
		  while(!stop){
			  sampler.fetchSample(sample, 0);
			  touched =(int)sample[0];
		  }
	}
}
