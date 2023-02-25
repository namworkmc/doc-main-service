package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class DocAbstractController {

  @Autowired
  protected UserMapper userDecoratorMapper;

  @Autowired
  @Qualifier("delegate")
  protected UserMapper userMapper;
}
