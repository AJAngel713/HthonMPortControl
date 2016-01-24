import com.fazecast.jSerialComm.SerialPort;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.enums.Arm;
import com.thalmic.myo.enums.PoseType;
import com.thalmic.myo.enums.VibrationType;
import com.thalmic.myo.enums.WarmupState;
import com.thalmic.myo.enums.XDirection;

/*
 * 
 * public enum PoseType {
    REST,
    FIST, // backward
    WAVE_IN, // LEFT
    WAVE_OUT, // RIGHT
    FINGERS_SPREAD, // stop
    DOUBLE_TAP, // forward
    UNKNOWN
}
forward
backward
stop
 */

public class GestureDetector extends AbstractDeviceListener {
	private static final int SCALE = 18;
	private double rollW;
	private double pitchW;
	private double yawW;
	private Arm whichArm;
	private SerialPort portToWrite;
	private byte[] commands;

	public GestureDetector(SerialPort portToWrite) {
		rollW = 0;
		pitchW = 0;
		yawW = 0;
		this.portToWrite = portToWrite;
		commands = new byte[1];
		commands[0] = 'x';
	}

	@Override
	public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
		Quaternion normalized = rotation.normalized();

		double roll = Math.atan2(2.0f * (normalized.getW() * normalized.getX() + normalized.getY() * normalized.getZ()), 1.0f - 2.0f * (normalized.getX() * normalized.getX() + normalized.getY() * normalized.getY()));
		double pitch = Math.asin(2.0f * (normalized.getW() * normalized.getY() - normalized.getZ() * normalized.getX()));
		double yaw = Math.atan2(2.0f * (normalized.getW() * normalized.getZ() + normalized.getX() * normalized.getY()), 1.0f - 2.0f * (normalized.getY() * normalized.getY() + normalized.getZ() * normalized.getZ()));

		rollW = ((roll + Math.PI) / (Math.PI * 2.0) * SCALE);
		pitchW = ((pitch + Math.PI / 2.0) / Math.PI * SCALE);
		yawW = ((yaw + Math.PI) / (Math.PI * 2.0) * SCALE);
	}

	@Override
	public void onPose(Myo myo, long timestamp, Pose pose) {
		PoseType t = pose.getType();
		if (t == PoseType.FIST) {
			//myo.vibrate(VibrationType.VIBRATION_SHORT);
			System.out.println("In FIST");
			commands[0] = 'C';
			portToWrite.writeBytes(commands, 1);
		}
		if (t == PoseType.WAVE_OUT) {
			//myo.vibrate(VibrationType.VIBRATION_SHORT);
			System.out.println("In Wave out");
			commands[0] = 'R';
			portToWrite.writeBytes(commands, 1);
		}
		if (t == PoseType.WAVE_IN) {
			//myo.vibrate(VibrationType.VIBRATION_SHORT);
			System.out.println("In Wave in");
			commands[0] = 'L';
			portToWrite.writeBytes(commands, 1);
		}
		if (t == PoseType.FINGERS_SPREAD) {
			//myo.vibrate(VibrationType.VIBRATION_SHORT);
			System.out.println("In FINGERS_SPREAD");
			commands[0] = 'U';
			portToWrite.writeBytes(commands, 1);
		}
		if (t == PoseType.DOUBLE_TAP) {
			myo.vibrate(VibrationType.VIBRATION_MEDIUM);
			System.out.println("In DOUBLE_TAP");
			commands[0] = 'C';
			portToWrite.writeBytes(commands, 1);
		}
		if (t == PoseType.UNKNOWN) {
			myo.vibrate(VibrationType.VIBRATION_SHORT);
			System.out.println("In UNKNOWN");
		}
	}

	@Override
	public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection, float rotation, WarmupState warmupState) {
		whichArm = arm;
		System.out.println(whichArm.toString() + "Connected arm.");
	}

	@Override
	public void onArmUnsync(Myo myo, long timestamp) {
		whichArm = null;
		System.out.println(whichArm.toString() + "Disconnected arm.");
	}

	@Override
	public String toString() {
		return "";
	}

	private String repeatCharacter(char character, int numOfTimes) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < numOfTimes; i++) {
			builder.append(character);
		}
		return builder.toString();
	}
}