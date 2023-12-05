package edu.hust.hespi.factorymanagement.util;

public class Constant {
    public interface DataType {
        int INTEGER = 1;
        int DOUBLE = 2;
        int STRING = 3;
        int TIME = 4;
        int BOOLEAN = 5;
    }
    public interface EntityType {
        int EMPLOYEE = 1;
    }
    public interface ColumnType {
        int DYNAMIC = 0;
        int STATIC = 1;
        int FIXED = 2;
    }
}
