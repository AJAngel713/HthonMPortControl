import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;
import com.thalmic.myo.*;
import com.thalmic.myo.example.DataCollector;

public class Main {

	public static void main(String[] args) {
		try {
			///// Serial
			SerialPort[] ports = SerialPort.getCommPorts();

			int numPort = 0;
			
			for (SerialPort p : ports){
				System.out.println(numPort++ + ". " + p.getSystemPortName());
			}
			System.out.println("Enter choice: ");
			Scanner userChoice = new Scanner(System.in);
			int i = userChoice.nextInt();
			
			SerialPort openPort = ports[i];
			
			if (openPort.openPort()){
				System.out.println("port open");
			} else {
				System.out.println("open Failed");
				return;
			}
			
			
			//////////
			Hub hub = new Hub("com.example.MyoStick");

			System.out.println("Attempting to find a Myo...");
			Myo myo = hub.waitForMyo(10000);

			if (myo == null) {
				throw new RuntimeException("Unable to find a Myo!");
			}

			System.out.println("Connected to a Myo armband!");
			
			GestureDetector dataCollector = new GestureDetector(openPort);
			hub.addListener(dataCollector);

			while (true) {
				hub.run(1000 / 20);
//				System.out.print(dataCollector);
			}
		} catch (Exception e) {
			System.err.println("Error: ");
			e.printStackTrace();
			System.exit(1);
		}
	}

}
