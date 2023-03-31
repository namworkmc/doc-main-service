package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.Attachment;
import edu.hcmus.doc.mainservice.repository.custom.CustomAttachmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>,
    CustomAttachmentRepository {

}
