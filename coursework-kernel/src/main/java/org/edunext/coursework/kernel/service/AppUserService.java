package org.edunext.coursework.kernel.service;

import org.edunext.coursework.kernel.BaseAppUser;

import java.util.Set;

/**
 * @author xulixin
 */
public interface AppUserService {

    BaseAppUser getByUsername(String username);

    Set<String> findRolesByUsername(String username);

    Set<String> findPermissionsByUsername(String username);
}
