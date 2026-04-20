package com.phoenixsan.identity.identity_service.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phoenixsan.identity.identity_service.master.entity.RoleLinkMapping;
import com.phoenixsan.identity.identity_service.master.enums.Action;


@Repository
public interface RoleLinkMappingRepository extends JpaRepository<RoleLinkMapping, Long> {

    List<RoleLinkMapping> findByRoleId(Long roleId);

    boolean existsByRoleIdAndLinkIdAndAction(Long roleId, Long linkId, Action action);

    void deleteByRoleId(Long roleId);

    @Query("""
    SELECT COUNT(r) > 0
    FROM RoleLinkMapping r
    JOIN r.link l
    WHERE r.role.id = :roleId
      AND l.name LIKE CONCAT(:path, '%')
      AND r.action = :action
""")
boolean hasAccess(Long roleId, String path, Action action);
}
