package com.project.backend.Configurations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class ToByteArray {
    public static <T extends Serializable> byte[] toByteArray  (T t) {
        byte[] bytes =null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            oos.flush();
            oos.close();
            bos.close();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            //TODO: handle exception
        }

        return bytes;
    }  
    public static <T extends Serializable> ByteBuffer ToByteBuffer(T t) {
        return ByteBuffer.wrap(toByteArray(t));
    }

    public static <T extends Serializable> Object ToObject(byte[] bytes) {
        Object obj = null;
        try {
          ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
          ObjectInputStream ois = new ObjectInputStream (bis);
          obj = ois.readObject();
        }
        catch (IOException ex) {
          //TODO: Handle the exception
        }
        catch (ClassNotFoundException ex) {
          //TODO: Handle the exception
        }
        return obj;

    }


}