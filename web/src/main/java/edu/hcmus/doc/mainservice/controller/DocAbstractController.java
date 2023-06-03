package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.util.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class DocAbstractController {

  @Autowired
  protected UserMapper userDecoratorMapper;

  @Autowired
  @Qualifier("delegate")
  protected UserMapper userMapper;

  @Autowired
  protected IncomingDocumentMapper incomingDecoratorDocumentMapper;

  @Autowired
  protected OutgoingDocumentMapper outgoingDecoratorDocumentMapper;

  @Autowired
  protected DocumentTypeMapper documentTypeMapper;

  @Autowired
  protected DepartmentMapper departmentMapper;

  @Autowired
  protected FolderMapper folderMapper;

  @Autowired
  protected DistributionOrganizationMapper distributionOrganizationMapper;

  @Autowired
  protected PaginationMapper paginationMapper;

  @Autowired
  protected TransferHistoryMapper transferHistoryMapper;
}
