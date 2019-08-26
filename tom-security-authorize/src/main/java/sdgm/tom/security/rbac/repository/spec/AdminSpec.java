package sdgm.tom.security.rbac.repository.spec;


import sdgm.tom.security.rbac.domain.Admin;
import sdgm.tom.security.rbac.dto.AdminCondition;
import sdgm.tom.security.rbac.repository.support.ImoocSpecification;
import sdgm.tom.security.rbac.repository.support.QueryWraper;

/**
 * @author zhailiang
 *
 */
public class AdminSpec extends ImoocSpecification<Admin, AdminCondition> {

	public AdminSpec(AdminCondition condition) {
		super(condition);
	}

	@Override
	protected void addCondition(QueryWraper<Admin> queryWraper) {
		addLikeCondition(queryWraper, "username");
	}

}
