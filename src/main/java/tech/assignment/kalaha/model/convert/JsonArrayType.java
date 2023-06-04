package tech.assignment.kalaha.model.convert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class JsonArrayType implements UserType {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

    @Override
    public Class<?> returnedClass() {
        return List.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        String json = resultSet.getString(names[0]);
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, List.class);
        } catch (IOException e) {
            throw new HibernateException("Failed to convert JSON to object: " + e.getMessage(), e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement statement, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            statement.setNull(index, Types.VARCHAR);
            return;
        }
        try {
            String json = objectMapper.writeValueAsString(value);
            statement.setObject(index, json, Types.VARCHAR);
        } catch (JsonProcessingException e) {
            throw new HibernateException("Failed to convert object to JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }
        try {
            String json = objectMapper.writeValueAsString(value);
            return objectMapper.readValue(json, List.class);
        } catch (IOException e) {
            throw new HibernateException("Failed to deep copy object: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        Object deepCopy = deepCopy(value);
        if (!(deepCopy instanceof Serializable)) {
            throw new HibernateException("Failed to deep copy object, it's not serializable: " + deepCopy);
        }
        return (Serializable) deepCopy;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }
}
