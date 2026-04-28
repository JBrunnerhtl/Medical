package at.htlleonding.medical.model;

import java.io.*;

public class SerializeTool<T extends Serializable> {

    public void write(T object, String fileName) {
        try (var oos = new ObjectOutputStream(new FileOutputStream(fileName))){
            oos.writeObject(object);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T read(String fileName) {
        if(!new File(fileName).exists()) return null;
        try (var ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
