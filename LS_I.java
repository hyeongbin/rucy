package Team;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Stopwatch;

/**
 * Example leJOS EV3 Project with an ant build file
 *
 */
public class LS_I extends Thread {
	  private float light=0;
	  private boolean stop =false;
	  
	  private SampleProvider sampler;
	  public LS_I() {
		// TODO Auto-generated constructor stub
		  NXTLightSensor ls = new NXTLightSensor(SensorPort.S4);
		  sampler =ls.getMode("Ambient");
		  this.setDaemon(true);
	  }
	  
	  public synchronized float getLight(){
		  return light; 
	  }
	  
	  public void stop_light(){
		  stop=true;
		 
	  }
	  
	  @Override
	public void run() {
		// TODO Auto-generated method stub
	  
		  float [] sample = new float[sampler.sampleSize()];
		  
		  while(!stop){
			  sampler.fetchSample(sample, 0);
			  light=(float)sample[0];
		  }
	}

}
