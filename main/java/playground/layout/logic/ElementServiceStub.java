package playground.layout.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import playground.exception.ElementNotFoundException;
import playground.exception.NotAdminException;

//@Service
public class ElementServiceStub implements ElementService{
	private Map<String, ElementEntity> database;

	@PostConstruct
	public void init() {
		this.database = new HashMap<>();
	}

	@Override
	public ElementEntity addNewElement(String email, String userPlayground, ElementEntity elementEntity) {
		this.database.put(elementEntity.getId()+"#"+elementEntity.getPlayground(), elementEntity);
		return this.database.get(elementEntity.getId());
	}

	@Override
	public ElementEntity getElement(String userPlayground, String playground, String email, String id) throws ElementNotFoundException {
		ElementEntity rv = this.database.get(id+"#"+playground);
		if (rv == null) {
			throw new ElementNotFoundException("could not find element by id: " + id);
		}
		return rv;
	}

	@Override
	public void cleanup() {
		this.database.clear();
		
	}

	@Override
	public List<ElementEntity> getAllElements(String userPlayground, String email, int size, int page) {
		return new ArrayList<ElementEntity>(this.database.values());
	}
	
	@Override
	public List<ElementEntity> getAllElementsNearByXY(String userPlayground, String email, double x, double y, double distance, int size, int page) {
		ArrayList<ElementEntity> rv = new ArrayList<>();
		for(ElementEntity element : database.values()) {
			if(distance == distanceCheck(element, x, y)) {
				rv.add(element);
			}
		}
		return rv;
	}
	
	private double distanceCheck(ElementEntity element, double x, double y) {
		double rX = Math.pow(element.getX() - x, 2);
		double rY = Math.pow(element.getY() - y, 2);
		
		return Math.sqrt(rX+rY);
	}
	
	@Override
	public List<ElementEntity> getAllElementsWithAttribute(String userPlayground, String email, String attribute, String value, int page, int size) {
		ArrayList<ElementEntity> rv = new ArrayList<>();
		for(ElementEntity element : database.values()) {
			if(element.getAttributes().containsKey(attribute) && element.getAttributes().get(attribute).equals(value)) {
				rv.add(element);
			}
		}
		return rv;
	}

	@Override
	public void updateElement(String userPlayground, String email, String id, String playground, ElementEntity update) throws ElementNotFoundException, NotAdminException {
		ElementEntity existing = this.database.get(id+"#"+playground);
		boolean dirty = false;
		
		if (update.getX() != existing.getX()) {
			existing.setX(update.getX());
			dirty = true;
		}
		
		if (update.getY() != existing.getY()) {
			existing.setY(update.getY());
			dirty = true;
		}

		if (!update.getName().equals(existing.getName())) {
			existing.setName(update.getName());
			dirty = true;
		}
		
		if(update.getExpirationDate() != null) {
			if (!update.getExpirationDate().equals(existing.getExpirationDate())) {
				existing.setExpirationDate(update.getExpirationDate());
				dirty = true;
			}
		}

		
		if (!update.getType().equals(existing.getType())) {
			existing.setType(update.getType());
			dirty = true;
		}
		
		if (!update.getAttributes().equals(existing.getAttributes())) {
			existing.setAttributes(update.getAttributes());
			dirty = true;
		}
		
		if (dirty) {
			this.database.put(id, existing);
		}
		
	}

}
