package playground.jpadal;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import playground.layout.logic.UserEntity;

@RepositoryRestResource
public interface UserDao extends PagingAndSortingRepository<UserEntity, String> {

}
