package design.kfu.sunrise.service.access;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.util.model.Permission;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Daniyar Zakiev
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    public List<Permission> generateDefault(List<String> userPermissionNames,
                                                   List<String> partnerPermissionNames,
                                                   List<String> adminPermissionNames,
                                                   Account account,
                                                   AccountAccessService accountAccessService) {
        List<Permission> permissions = new ArrayList<>();
        Set<String> set = new HashSet<>(userPermissionNames);
        set.addAll(partnerPermissionNames);
        set.addAll(adminPermissionNames);
        if (account == null) {
            for (String permission : set) {
                permissions.add(new Permission(permission, false));
            }
        } else {
            for (String permission : set) {
                if (accountAccessService.isAdmin(account)) {
                    if (adminPermissionNames.contains(permission)) {
                        permissions.add(new Permission(permission, true));
                    } else {
                        permissions.add(new Permission(permission, false));
                    }
                } else {
                    if (accountAccessService.isPartner(account)) {
                        if (partnerPermissionNames.contains(permission)) {
                            permissions.add(new Permission(permission, true));
                        } else {
                            permissions.add(new Permission(permission, false));
                        }
                    } else {
                        if (userPermissionNames.contains(permission)) {
                            permissions.add(new Permission(permission, true));
                        } else {
                            permissions.add(new Permission(permission, false));
                        }
                    }
                }
            }
        }
        return permissions;
    }
}
