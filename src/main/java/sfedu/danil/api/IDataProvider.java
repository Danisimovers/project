package sfedu.danil.api;

public interface IDataProvider<T> {
    boolean initDataSource();
    boolean saveRecord(T record);
    boolean deleteRecord(String  id);
    T getRecordById(String id);
    boolean updateRecord(T record);

}

