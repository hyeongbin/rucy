package Team;

import lejos.hardware.Button;
import java.io.IOException;
import java.io.InputStream;

import lejos.hardware.Bluetooth;
import lejos.hardware.Button;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.NXTConnection;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;

import Team.TS_I;
import Team.SS_I;
import Team.LS_I;
import lejos.hardware.Sound;
public class MovingControl {


	
	private static TS_I ts; //touch sensor 
	private static SS_I uss; //two ultrasonarsensors  - 1 for forward , 1 for sides
	private static LS_I ls;
	
	
	//
	static public final int TRUE = 1;
	static public final int FALSE = 0;
	
	static public int Mode;
	
	static boolean stop_flag=false;
	static boolean back_flag=false;
	
	static int start_tick=0;
	static int end_tick = 0;
	
	static int outline_start=0;
	static int outline_end=0;

	
		public static void direction_attack()			//공격 모드
		{
			
			stop_flag=false;
			start_tick =(int)java.lang.System.currentTimeMillis() ;
		
			System.out.println("attack Start!");

			boolean df=true;
			while(!stop_flag){
				
				df = true;				//df 이게 몰까
				// touch sensor값 들 처리 
				 if(ls.getLight() >= 0.3)		//흰줄일 경우
					{ 
					 Sound.beep();	
					 System.out.println("White Line");
					 WheelControl.backward(15);
				 	try{
						int i=1;
						int j=1;
						while(true){
							
								if( ls.getLight() <0.3){	//다시 검정을 찾았을 경우
									break;
								}
					
								Sound.beep();
								ts.sleep(200);
								uss.sleep(200);
								
								WheelControl.rotate(90);	//90도 회전
								WheelControl.forward(5*i);
								
								if( ls.getLight() <0.3){	//90도 돌아서 다시 찾았을 경우 백플래그 초기화
									back_flag = false;
									WheelControl.forward(15);
									break;
								}
								WheelControl.backward(5*i++);
								if(i==5){
									if(back_flag){
										WheelControl.backward(30);
										if( ls.getLight() <0.3){
											back_flag = false;
											WheelControl.forward(15);
											break;
										}
										else{			//검정이 아닐 경우
											for(;;){
												Sound.twoBeeps();
												WheelControl.rotate(75);
												WheelControl.forward(10*j);
												if( ls.getLight() <0.3){	//검정 찾았을 경우
													back_flag = false;
													WheelControl.forward(15);
													break;
												}
												WheelControl.backward(10*j++);			//j가 뭘까
												if(j>8){
													if(!back_flag){
														break;
													}
													j = 1;
													back_flag = false;
												}
											}
										}
									}
									else{
										back_flag = true;
									}
									i=1;
								}
							}
						}catch(Exception e){
							
						}
						df=false;
						
				
					}
/*				else if(ts.getTouched()==1){
					WheelControl.fast_forward(10);
					System.out.println( "touch > forward");
					df=false;
				}*/
		
				//sonar sensor 값 들 처리 
			//	System.out.println("side sonar: "+ uss.getDistance_side()+"pre"+uss.getDistance_pre());
				
				//light sensor 값처리
				else if(uss.getDistance_pre()< 0.45){		//정면 센서에 거리가 40이하로 감지되면
					WheelControl.fast_forward(10);
					df=false;
				}
				

				else if(uss.getDistance_side()< 0.45){		//측면 센서 거리가 40이하일 경우
					System.out.println("SS in direction_attack-- side");
					WheelControl.rotateRight();
					WheelControl.fast_forward(10);
					df=false;
				}
				
				if(df){
				  WheelControl.rotate(30);
				}
				
				end_tick =(int)java.lang.System.currentTimeMillis() ;		//이건 또 모시여 검정 나갔을때 시간초 세는건가?
				//System.out.println("start:"+ start_tick +"\n end_tick :"+end_tick);
				if( (end_tick-start_tick) > (180*1000)){				
					stop_flag=true;
					System.out.println("Game Over");
				}
				
	
			}
		}
	
		
		
		public static void direction_defence()			//수비 모드
		{
			stop_flag=false;
	
			start_tick =(int)java.lang.System.currentTimeMillis() ;
			
			while(!stop_flag){
				if(ls.getLight() >= 0.3)					//흰색 선일 경우			0.3이하일 경우 검정, 이상일 경우 흰색
				{ 
					 Sound.beep();	
					 System.out.println("White Line");
					 
					 WheelControl.forward(15);
					 
					if( ls.getLight() >=0.3){			//나갔을 때 후퇴
						WheelControl.backward(30);
					}
				 	
					try{
						int i=1;
						int j=1;
						
						while(true){
							
								//경기장 안에 들어오면
								if( ls.getLight() <0.3){		//무사히 들어오면 브렉
									break;
								}
								ts.sleep(200);
								uss.sleep(200);
								
								Sound.beep();			//잠시 멈췄다가 90도 회전
								
								WheelControl.rotate(90);
								
								WheelControl.forward(5*i);
								if( ls.getLight() <0.3){
									WheelControl.forward(15);
									break;
								}
								WheelControl.backward(5*i++);
								if(i==5){
									i=1;
									
									if(back_flag){
										//back flag
										WheelControl.backward(30);
										
										//내부에 들어왔으면
										if(ls.getLight()<0.3){
											back_flag=false;
											boolean escape_flag=false;
		
											
											for(int k=0; k<5;k++){
												WheelControl.rotate(75);
												WheelControl.forward(30);
											
												if(ls.getLight()<0.3){
													escape_flag=true;
													break;
												}
												WheelControl.backward(25);
											}
											if(escape_flag){
												break;
											}
											
										}
									}
									else{
										back_flag=true;
									}
							/*		if(back_flag){
										WheelControl.forward(30);
										if( ls.getLight() <0.3){
											back_flag = false;
											WheelControl.forward(15);
											break;
										}
										else{
											for(;;){
												Sound.twoBeeps();
												WheelControl.rotate(75);
												WheelControl.backward(10*j);
												if( ls.getLight() <0.3){
													back_flag = false;
													WheelControl.forward(15);
													break;
												}
												WheelControl.forward(10*j++);
												if(j>8){
													if(!back_flag){
														break;
													}
													j = 1;
													back_flag = false;
												}
											}
										}
									}
									else{
										back_flag = true;
									}*/
								}
							}
						}catch(Exception e){
							
						}
						
				
					}
				// touch sensor값 들 처리 
/*				else if(ts.getTouched()==1){
					WheelControl.rotate(30);
					WheelControl.fast_backward(2); //2cm 
			//		System.out.println("TS in direction_defense-- touched ");
				}*/
				
				else if(uss.getDistance_side()< 0.45 ){
					System.out.println("defense-- side");
					WheelControl.rotateLeft();
				//	WheelControl.fast_backward(10);
				}
				
				//light sensor 값처리
				else if(uss.getDistance_pre()< 0.35){
					System.out.println("direction_defence"+uss.getDistance_pre());
					WheelControl.backward(10);
					WheelControl.rotate(45);				
				}
					
				else{
					WheelControl.rotate(30);
				}
				
				
				end_tick =(int)java.lang.System.currentTimeMillis() ;
				if( (end_tick-start_tick)>180*1000){
					stop_flag=true;	
					System.out.println("Time Out Game over");
				}
			
			}
				return;
					
				

		}
		
		
		public static void main (String[] args) throws Exception {

		boolean isPause = true;  // 긴급 정지
		//init sensor
		 ts = new TS_I();
		 uss= new SS_I();
		 ls = new LS_I();
		 
		 ts.start();
		 uss.start();
		 ls.start();
		 ///
		int mode;
	//	SonarSensor_Interface sonar = new SonarSensor_Interface();		
		WheelControl.init();
		
		while(true){
			
		mode=0;
		LCD.clear();
		System.out.println("MODE SELECT LEFT: ATTACK, RIGHT: DEFENCE");
	
		while(true){
		mode=Button.waitForAnyPress(6000);
		System.out.println(mode);
		if(mode != 0 )
			break;
		}
		Mode = mode;

		switch (Mode) {
		case 8:
			System.out.println("right");
			try{
			
				direction_attack ();
			
			}catch (Exception e){
				System.out.println(e);
			}
			break;
		case 16:
			System.out.println("LEFT");
			try{
			direction_defence();				
			}catch (Exception e){
				System.out.println(e);
			}
		
			break;							
		case Button.ID_UP:
			System.out.println("Bye");
			return ;
		default:
			break;
		}
		Mode =0;
       
		}
	}



}