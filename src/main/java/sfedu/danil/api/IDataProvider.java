package sfedu.danil.api;

import sfedu.danil.models.User;

public interface IDataProvider<T> {
    void initDataSource();
    void saveRecord(T record);
    void deleteRecord(String  id);
    T getRecordById(String id);
    void updateRecord(T record);

}

