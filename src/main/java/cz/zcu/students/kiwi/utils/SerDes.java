package cz.zcu.students.kiwi.utils;

public abstract class SerDes<T> {
    public abstract String serialize(T object);

    public abstract T deserialize(String text) throws SerDesException;
}
