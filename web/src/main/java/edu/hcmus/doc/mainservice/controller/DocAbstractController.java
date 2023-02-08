package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DocAbstractController {

  @Autowired
  protected UserMapper userMapper;
}
