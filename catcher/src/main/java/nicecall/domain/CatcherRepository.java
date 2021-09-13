package nicecall.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel="catches", path="catches")
public interface CatcherRepository extends PagingAndSortingRepository<Catcher, Long> {

    public Optional<Catcher> findByCallId(Long callId);
}
