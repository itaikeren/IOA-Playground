package playground.jpadal;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import playground.layout.logic.ElementEntity;

@RepositoryRestResource
public interface ElementDao extends PagingAndSortingRepository<ElementEntity, String>{
}
