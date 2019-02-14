package playground.layout;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import playground.exception.ElementNotFoundException;
import playground.exception.NotAdminException;
import playground.layout.logic.ElementEntity;
import playground.layout.logic.ElementService;

@RestController
public class ElementUI {

	private ElementService elements;

	@Autowired
	public void setElements(ElementService elements) {
		this.elements = elements;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/playground/elements/{userPlayground}/{email}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO createElement(@RequestBody ElementTO newElement, @PathVariable("email") String email, @PathVariable("userPlayground") String userPlayground) {
		ElementTO rv = new ElementTO();
		try {
			rv = new ElementTO(this.elements.addNewElement(email, userPlayground, newElement.toEntity()));
		} catch (NotAdminException e) {
			System.out.println(e.getMessage());
		}
		return rv;
	}

	@RequestMapping(method = RequestMethod.PUT, path = "playground/elements/{userPlayground}/{email}/{playground}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@PathVariable("userPlayground") String userPlayground, @PathVariable("email") String email, @PathVariable("playground") String playground,
			@PathVariable("id") String id, @RequestBody ElementTO updatedElement) throws Exception {
		this.elements.updateElement(userPlayground, email, id, playground, updatedElement.toEntity());
	}

	@RequestMapping(method = RequestMethod.GET, path = "/playground/elements/{userPlayground}/{email}/{playground}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO getElement(@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, @PathVariable("playground") String playground,
			@PathVariable("id") String id) throws ElementNotFoundException {

		return new ElementTO(elements.getElement(userPlayground, playground, email, id));
	}

	@RequestMapping(method = RequestMethod.GET, path = "/playground/elements/{userPlayground}/{email}/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<ElementTO> getAllElements(@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		ArrayList<ElementTO> allElements = new ArrayList<>();
		ArrayList<ElementEntity> rv = new ArrayList<>(this.elements.getAllElements(userPlayground, email, size, page));
		for (ElementEntity element : rv) {
			allElements.add(new ElementTO(element));
		}
		return allElements;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<ElementTO> getAllElementsNearLocation(@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, @PathVariable("x") double x, @PathVariable("y") double y,
			@PathVariable("distance") double distance) {
		ArrayList<ElementTO> allElements = new ArrayList<>();
		ArrayList<ElementEntity> rv = new ArrayList<>(this.elements.getAllElementsNearByXY(userPlayground, email, x, y, distance,10,0));
		for (ElementEntity element : rv) {
			allElements.add(new ElementTO(element));
		}
		return allElements;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<ElementTO> searchElements(@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, @PathVariable("attributeName") String attributeName,
			@PathVariable("value") String value) {
		ArrayList<ElementTO> allElements = new ArrayList<>();
		ArrayList<ElementEntity> rv = new ArrayList<>(this.elements.getAllElementsWithAttribute(userPlayground, email, attributeName, value, 0, 3));
		for (ElementEntity element : rv) {
			allElements.add(new ElementTO(element));
		}
		return allElements;
	}

}