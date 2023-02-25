package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.DocSystemRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocSystemRoleRepository extends JpaRepository<DocSystemRole, Long> {

}
