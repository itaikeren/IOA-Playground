package playground.layout.logic;

import java.util.List;

import playground.exception.ElementNotFoundException;
import playground.exception.NotAdminException;

public interface ElementService {
	public ElementEntity addNewElement(String email, String userPlayground, ElementEntity elementEntity) throws NotAdminException;

	public ElementEntity getElement(String userPlayground, String playground, String email, String id) throws ElementNotFoundException;

	public void cleanup();

	public List<ElementEntity> getAllElements(String userPlayground, String email, int size, int page);
	
	public List<ElementEntity> getAllElementsNearByXY(String userPlayground, String email, double x, double y, double distance, int size, int page);

	public List<ElementEntity> getAllElementsWithAttribute(String userPlayground, String email, String attribute, String value, int page, int size);

	public void updateElement(String userPlayground, String email, String id, String playground, ElementEntity update) throws ElementNotFoundException, NotAdminException;

}
