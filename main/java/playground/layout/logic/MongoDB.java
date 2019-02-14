package playground.layout.logic;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import playground.jpadal.GeneratedNumber;

/** Enable if you want the DB will be remove every time you start the application */

//@Component
public class MongoDB {
	private MongoTemplate mongoTemplate;
	
	@Autowired
	public MongoDB(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@PostConstruct
	public void cleanUp() {
		mongoTemplate.getDb().drop();
		GeneratedNumber.resetId();
		System.err.println("DB is clear now");
	}
	
}
