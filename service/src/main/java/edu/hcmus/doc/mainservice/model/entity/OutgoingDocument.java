package edu.hcmus.doc.mainservice.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "outgoing_document", schema = "doc_main", catalog = "doc")
public class OutgoingDocument extends DocAbstractEntity {

}
