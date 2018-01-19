package com.jgcomptech.tools.authenication;

import java.security.GeneralSecurityException;
import java.sql.SQLException;

/**
 * An object representing a user account.
 * @since 1.4.0
 */
public class UserAccount {
    private final UserManager userManager;
    private final String username;

    /**
     * Maps a user in the database to a object.
     * @param userManager the user manager to manage the user
     * @param username the username
     */
    public UserAccount(final UserManager userManager, final String username) {
        if(userManager == null) {
            throw new IllegalArgumentException("User Manager cannot be null!");
        }
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty!");
        }
        this.userManager = userManager;
        this.username = username;
    }

    /**
     * Returns the username.
     * @return the username
     */
    public String getUsername() { return username; }

    /**
     * Returns the user type.
     * @return the user type
     * @throws SQLException if an error occurs during lookup
     */
    public UserRole getUserRole() throws SQLException { return userManager.getUserRole(getUsername()); }

    /**
     * Sets a new password for the user using SHA-512 password hashing.
     * @param password the new password
     * @return true if password is changed successfully
     * @throws SQLException if an error occurs while changing password
     * @throws GeneralSecurityException if an error occurs while hashing the password
     */
    public boolean setPassword(final String password) throws GeneralSecurityException, SQLException {
        if(password == null) {
            throw new IllegalArgumentException("Password cannot be null!");
        }
        return userManager.setPassword(getUsername(), password);
    }

    /**
     * Sets the user role of the user.
     * @param userRole the system user role to change to
     * @return true if no errors occurred
     * @throws SQLException if an error occurs while changing user type
     */
    public boolean setUserRole(final UserRoleManager.SystemUserRoles userRole) throws SQLException {
        if(userRole == null) {
            throw new IllegalArgumentException("User role cannot be null!");
        }
        return userManager.setUserRole(getUsername(), userRole);
    }

    /**
     * Sets the user role of the user.
     * @param userRole the name of the user role to change to
     * @return true if no errors occurred
     * @throws SQLException if an error occurs while changing user type
     */
    public boolean setUserRole(final String userRole) throws SQLException {
        if(userRole == null || userRole.isEmpty()) {
            throw new IllegalArgumentException("User role cannot be empty!");
        }
        return userManager.setUserRole(getUsername(), userRole);
    }

    /**
     * Checks to see if the specified password matches the stored password in the database.
     * @param password the password to check against
     * @return true if the passwords match
     * @throws SQLException if an error occurs during lookup
     * @throws GeneralSecurityException if an error occurs while hashing the password
     */
    public boolean checkPasswordMatches(final String password) throws GeneralSecurityException, SQLException {
        if(password == null) {
            throw new IllegalArgumentException("Password cannot be null!");
        }
        return userManager.checkPasswordMatches(getUsername(), password);
    }
}
