
package sdgm.tom.security.rbac.repository;

import org.springframework.stereotype.Repository;
import sdgm.tom.security.rbac.domain.Resource;

/**
 * @author zhailiang
 *
 */
@Repository
public interface ResourceRepository extends ImoocRepository<Resource> {

	Resource findByName(String name);

}
