package com.springpracticesdemo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springpracticesdemo.enums.LdapGroup;
import com.springpracticesdemo.model.User;
import com.springpracticesdemo.repository.custom.UserRepositoryCriteria;

public interface UserRepository extends CrudRepository<User, Long>, UserRepositoryCriteria {

    @Query("SELECT DISTINCT u FROM User u JOIN u.ldapGroups lg"
            + " WHERE lg in (:groups)"
            + " AND (lower(u.username) LIKE lower(CONCAT('%',:searchParameter, '%'))"
            + " OR lower(CONCAT(u.firstName,' ',u.lastName)) LIKE lower(CONCAT('%',:searchParameter, '%'))"
            + " OR lower(u.email) LIKE lower(CONCAT('%',:searchParameter, '%')))"
            + " ORDER BY u.firstName, u.lastName, u.username, u.email")
    List<User> findUsersByUsernameOrEmailOrNamesAndGroups(String searchParameter, List<LdapGroup> groups);

    @Query("SELECT DISTINCT u FROM User u JOIN u.ldapGroups lg"
            + " WHERE u.username NOT IN :excludeUsernames"
            + " AND lg in (:groups)"
            + " AND (lower(u.username) LIKE lower(CONCAT('%',:searchParameter, '%'))"
            + " OR lower(CONCAT(u.firstName,' ',u.lastName)) LIKE lower(CONCAT('%',:searchParameter, '%'))"
            + " OR lower(u.email) LIKE lower(CONCAT('%',:searchParameter, '%')))"
            + " ORDER BY u.firstName, u.lastName, u.username, u.email")
    List<User> findUsersByUsernameOrEmailOrNamesAndGroups(String searchParameter, List<String> excludeUsernames,
            List<LdapGroup> groups);

    @Query("SELECT u FROM User u"
            + " WHERE u.username NOT IN :excludeUsernames"
            + " AND (lower(u.username) LIKE lower(CONCAT('%',:searchParameter, '%'))"
            + " OR lower(CONCAT(u.firstName,' ',u.lastName)) LIKE lower(CONCAT('%',:searchParameter, '%'))"
            + " OR lower(u.email) LIKE lower(CONCAT('%',:searchParameter, '%')))"
            + " ORDER BY u.firstName, u.lastName, u.username, u.email")
    List<User> findUsersByUsernameOrEmailOrNames(String searchParameter, List<String> excludeUsernames);

    @Query("SELECT u FROM User u"
            + " WHERE lower(u.username) LIKE lower(CONCAT('%',:searchParameter, '%'))"
            + " OR lower(CONCAT(u.firstName,' ',u.lastName)) LIKE lower(CONCAT('%',:searchParameter, '%'))"
            + " OR lower(u.email) LIKE lower(CONCAT('%',:searchParameter, '%'))"
            + " ORDER BY u.firstName, u.lastName, u.username, u.email")
    List<User> findUsersByUsernameOrEmailOrNames(String searchParameter);

    @Query("SELECT u FROM User u"
            + " WHERE u.username IN :searchParameters"
            + " OR u.email IN :searchParameters")
    List<User> findUsersByUsernamesOrEmails(List<String> searchParameters);

    Optional<User> findByUsernameIgnoreCase(String username);
}
