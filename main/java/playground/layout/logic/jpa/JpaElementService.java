package playground.layout.logic.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import playground.aop.AdminCheck;
import playground.aop.MyLogger;
import playground.aop.MyPerformanceCheck;
import playground.exception.ElementNotFoundException;
import playground.exception.NotAdminException;
import playground.jpadal.ElementDao;
import playground.jpadal.GeneratedNumber;
import playground.jpadal.UserDao;
import playground.layout.UserTO;
import playground.layout.logic.ElementEntity;
import playground.layout.logic.ElementService;

@Service
public class JpaElementService implements ElementService {
	
	@Value("${playground}")
	private String playground;
	
	private ElementDao elements;
	private UserDao users;

	@Autowired
	public JpaElementService(ElementDao elements,  UserDao users) {
		super();
		this.elements = elements;
		this.users = users;
		if(this.elements.count() > 0) {
			List<ElementEntity> allElements= this.elements.findAll(PageRequest.of(0, 5, Direction.DESC, "creationDate")).getContent();
			GeneratedNumber.setElementId(Long.parseLong(allElements.get(0).getId()));
		}
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	@AdminCheck
	public ElementEntity addNewElement(String email, String userPlayground, ElementEntity elementEntity) throws NotAdminException {
			long number = GeneratedNumber.getNextElementValue();
			elementEntity.setId("" + number);
			elementEntity.setPlayground(playground);
			elementEntity.setIdentifier(elementEntity.getId()+"#"+elementEntity.getPlayground());
			elementEntity.setCreatorEmail(email);
			elementEntity.setCreatorPlayground(userPlayground);
			return this.elements.save(elementEntity);
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	public ElementEntity getElement(String userPlayground, String playground, String email, String id) throws ElementNotFoundException {
		ElementEntity ee = this.elements.findById(id+"#"+playground).orElseThrow(() -> new ElementNotFoundException("No Element with ID: " + id));
		
		if(users.findById(email+"#"+userPlayground).get().getRole().equals(UserTO.adminRole)) {
			return ee;
		} else {
			if(ee.getExpirationDate() != null) {
				if(ee.getExpirationDate().after(new Date())) {
					return ee;
				} else {
					throw new ElementNotFoundException("Sorry, your element is expired");
				}				
			} else {
				return ee;				
			}
		}
	}

	@Override
	public void cleanup() {
		this.elements.deleteAll();
		this.users.deleteAll();
		GeneratedNumber.resetId();
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	public List<ElementEntity> getAllElements(String userPlayground, String email, int size, int page) {
		List<ElementEntity> allElements= this.elements.findAll(PageRequest.of(page, size, Direction.DESC, "creationDate")).getContent();
		if(users.findById(email+"#"+userPlayground).get().getRole().equals(UserTO.adminRole)) {
			return allElements;
		} else {
			List<ElementEntity> filteredElements = new ArrayList<>();
			for(ElementEntity element : allElements) {
				if(element.getExpirationDate() != null) {
					if(element.getExpirationDate().after(new Date())) {
						filteredElements.add(element);
					}					
				} else {
					filteredElements.add(element);
				}
			}
			return filteredElements;
		}
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	public List<ElementEntity> getAllElementsNearByXY(String userPlayground, String email, double x, double y, double targetDistance, int size, int page) {
		List<ElementEntity> allElements = this.elements.findAll(PageRequest.of(page, size, Direction.DESC, "creationDate"))
				.getContent();
		for (ElementEntity e : allElements) {
			e.setDistance(this.distanceCheck(e, x, y));
		}

		List<ElementEntity> allNearByElements = new ArrayList<>();
		for (ElementEntity elementEntity : allElements) {
			if (elementEntity.getDistance() <= targetDistance) {
				allNearByElements.add(elementEntity);
			}
		}
		
		if(users.findById(email+"#"+userPlayground).get().getRole().equals(UserTO.adminRole)) {
			return allNearByElements;
		} else {
			List<ElementEntity> filteredElements = new ArrayList<>();
			for(ElementEntity element : allNearByElements) {
				if(element.getExpirationDate() != null) {
					if(element.getExpirationDate().after(new Date())) {
						filteredElements.add(element);
					}					
				} else {
					filteredElements.add(element);
				}
			}
			return filteredElements;
		}

	}

	private double distanceCheck(ElementEntity element, double x, double y) {
		double rX = Math.pow(element.getX() - x, 2);
		double rY = Math.pow(element.getY() - y, 2);

		return Math.sqrt(rX + rY);
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	public List<ElementEntity> getAllElementsWithAttribute(String userPlayground, String email, String attribute, String value, int page, int size) {

		List<ElementEntity> allElements = this.elements.findAll(PageRequest.of(page, size, Direction.DESC, "creationDate"))
				.getContent();
		List<ElementEntity> allElementsWithAtt = new ArrayList<>();
		for (ElementEntity elementEntity : allElements) {

			// search by name
			if (elementEntity.getName().equals(value)) {
				allElementsWithAtt.add(elementEntity);
			}
			// search by type
			else if (elementEntity.getType().equals(value)) {
				allElementsWithAtt.add(elementEntity);
			}
			// search by attribute
			else if (elementEntity.getAttributes().containsKey(attribute)
					&& elementEntity.getAttributes().get(attribute).equals(value)) {
				allElementsWithAtt.add(elementEntity);
			}
		}

		if(users.findById(email+"#"+userPlayground).get().getRole().equals(UserTO.adminRole)) {
			return allElementsWithAtt;
		} else {
			List<ElementEntity> filteredElements = new ArrayList<>();
			for(ElementEntity element : allElementsWithAtt) {
				if(element.getExpirationDate() != null) {
					if(element.getExpirationDate().after(new Date())) {
						filteredElements.add(element);
					}					
				} else {
					filteredElements.add(element);
				}
			}
			return filteredElements;
		}		
	}

	@Override
	@MyLogger
	@MyPerformanceCheck
	public void updateElement(String userPlayground, String email, String id, String playground, ElementEntity update) throws ElementNotFoundException, NotAdminException {
		
		if (!users.findById(email+"#"+userPlayground).get().getRole().equals(UserTO.adminRole)) {
			throw new NotAdminException("Only admin can preform that action!");
		} else {
			ElementEntity existing = getElement(userPlayground, playground, email, id);

			if (update.getX() != existing.getX()) {
				existing.setX(update.getX());
			}

			if (update.getY() != existing.getY()) {
				existing.setY(update.getY());
			}

			if (!update.getName().equals(existing.getName())) {
				existing.setName(update.getName());
			}

			if (update.getExpirationDate() != null) {
				if (!update.getExpirationDate().equals(existing.getExpirationDate())) {
					existing.setExpirationDate(update.getExpirationDate());
				}
			} else {
				existing.setExpirationDate(null);
			}

			if (!update.getType().equals(existing.getType())) {
				existing.setType(update.getType());
			}

			if (!update.getAttributes().equals(existing.getAttributes())) {
				existing.setAttributes(update.getAttributes());
			}

			this.elements.save(existing);
		}
	}
}
