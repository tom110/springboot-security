package sdgm.tom.security.rbac.authentication;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sdgm.tom.security.rbac.domain.Admin;
import sdgm.tom.security.rbac.repository.AdminRepository;

/**
 * @author zhailiang
 *
 */
@Component
@Transactional
public class RbacUserDetailsService implements UserDetailsService, SocialUserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AdminRepository adminRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("表单登录用户名:" + username);
		Admin admin = adminRepository.findByUsername(username);
		admin.getUrls();
		return admin;
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		logger.info("表单登录social用户名:" + userId);
		Admin admin = adminRepository.findByUsername(userId);
		admin.getUrls();
		return (SocialUserDetails) admin;
	}
}
