package edu.hcmus.doc.mainservice.common;

import lombok.Data;

@Data
public class PostgresContainerProps {

  public static final String IMAGE = "postgres:15.1-alpine3.17";
  public static final String DATABASE_NAME = "DOC_MAIN_TEST";
  public static final String USERNAME = "test";
  public static final String PASSWORD = "test";
  public static final int PORT = 5432;
  public static final String DB_SCRIPTS_PATH = "../db/src/test";
  public static final String ENTRYPOINT_INIT_DB_PATH = "/docker-entrypoint-initdb.d";
}
