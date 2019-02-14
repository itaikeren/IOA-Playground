package playground.jpadal;

public class GeneratedNumber {
	
	private static long ElementId = 0;
	private static long ActivityId = 0;
	public GeneratedNumber() {
	}

	public static long getNextElementValue() {
		ElementId++;
		return ElementId;
	}
	
	public static long getNextActivityValue() {
		ActivityId++;
		return ActivityId;
	}
	
	public static void setElementId(long num) {
		ElementId = num;
	}
	
	public static void setActivityId(long num) {
		ActivityId = num;
	}
	
	public static void resetId() {
		ElementId = 0;
		ActivityId = 0;
	}
	
}
