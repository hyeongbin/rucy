package Team;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class SS_I extends Thread{


	private SampleProvider sampler;
	private SampleProvider sampler2;

	private float dis_pre=10; // 초기값
	private float dis_side=10; // 초기값
    private boolean stop =false; //stop sensing 

    public SS_I() {
		// TODO Auto-generated constructor stub
		
		EV3UltrasonicSensor ss_pre = new EV3UltrasonicSensor(SensorPort.S3);
		EV3UltrasonicSensor ss_side = new EV3UltrasonicSensor(SensorPort.S2);
		sampler= ss_pre.getMode("Distance");
		sampler2 =ss_side.getMode("Distance");

		this.setDaemon(true);
		
	}

	public synchronized float getDistance_pre(){
		return dis_pre;
	}
	
	public synchronized float getDistance_side(){
		return dis_side;
	}
	public void stop_ss(){
		stop = true;
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		float [] sample1 = new float[sampler.sampleSize()];
		float [] sample2 = new float[sampler2.sampleSize()];
		
		while(!stop){
			sampler.fetchSample(sample1, 0);
			dis_pre=(float)sample1[0];
			
			sampler2.fetchSample(sample2, 0);
	
			dis_side=(float)sample2[0];
		}
	}

}