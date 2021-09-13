package nicecall.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="calls", path="calls")
public interface CallerRepository extends PagingAndSortingRepository<Caller, Long> {

}
