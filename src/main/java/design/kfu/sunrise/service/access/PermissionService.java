package design.kfu.sunrise.service.access;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.util.model.Permission;

import java.util.List;

/**
 * @author Daniyar Zakiev
 */
public interface PermissionService {
    List<Permission> generateDefault(List<String> userPermissionNames,
                                     List<String> partnerPermissionNames,
                                     List<String> adminPermissionNames,
                                     Account account,
                                     AccountAccessService accountAccessService);
}
