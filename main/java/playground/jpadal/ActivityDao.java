package playground.jpadal;

import org.springframework.data.repository.PagingAndSortingRepository;

import playground.layout.logic.ActivityEntity;

public interface ActivityDao extends PagingAndSortingRepository<ActivityEntity, String>{

}
