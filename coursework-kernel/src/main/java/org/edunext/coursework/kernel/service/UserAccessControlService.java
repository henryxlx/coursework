package org.edunext.coursework.kernel.service;

import org.edunext.coursework.kernel.exception.ActionGraspException;

/**
 * @author xulixin
 */
public interface UserAccessControlService {

    void doLoginCheck(String username, String password) throws ActionGraspException;

    boolean isLoggedIn();

    Object getCurrentUser();

    boolean hasRole(String roleName);
}
