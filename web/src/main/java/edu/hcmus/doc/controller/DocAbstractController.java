package edu.hcmus.doc.controller;

import edu.hcmus.doc.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DocAbstractController {

  @Autowired
  protected UserMapper userMapper;
}
