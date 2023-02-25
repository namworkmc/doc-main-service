package edu.hcmus.doc.mainservice.model.custom;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import lombok.SneakyThrows;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

public class CustomArrayType<T extends Serializable> implements UserType {

  protected static final int[] SQL_TYPES = {Types.ARRAY};

  private Class<T> typeParameterClass;

  @Override
  public Object assemble(Serializable cached, Object owner) {
    return this.deepCopy(cached);
  }

  @Override
  public Object deepCopy(Object value) {
    return value;
  }

  @Override
  public Serializable disassemble(Object value) {
    return (T) this.deepCopy(value);
  }

  @Override
  public boolean equals(Object x, Object y) {

    if (x == null) {
      return y == null;
    }
    return x.equals(y);
  }

  @Override
  public int hashCode(Object x) {
    return x.hashCode();
  }

  @Override
  @SneakyThrows
  public Object nullSafeGet(ResultSet resultSet, String[] names,
      SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
    if (resultSet.wasNull()) {
      return null;
    }
    if (resultSet.getArray(names[0]) == null) {
      return new Integer[0];
    }

    Array array = resultSet.getArray(names[0]);
    T javaArray = (T) array.getArray();
    return javaArray;
  }

  @Override
  public void nullSafeSet(PreparedStatement statement, Object value, int index,
      SharedSessionContractImplementor sharedSessionContractImplementor)
      throws HibernateException, SQLException {
    Connection connection = statement.getConnection();
    if (value == null) {
      statement.setNull(index, SQL_TYPES[0]);
    } else {
      T castObject = (T) value;
      Array array = connection.createArrayOf("integer", (Object[]) castObject);
      statement.setArray(index, array);
    }
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return original;
  }

  @Override
  public Class<T> returnedClass() {
    return typeParameterClass;
  }

  @Override
  public int[] sqlTypes() {
    return new int[]{Types.ARRAY};
  }
}
