package playground.activities;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Message {

	private String title;
	private String content;
	
	public Message(String contect) {
		this.title = "Message@" + LocalDateTime.now().getHour()+ ":" +LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond();
		this.content = contect;
	}
	
	public Map<String, String> getMessage(){
		Map<String,String> m = new HashMap<>();
		m.put(title, content);
		return m;
	}
}
