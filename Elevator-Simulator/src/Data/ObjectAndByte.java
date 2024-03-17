//package Data;
//
//import java.io.*;
//
///**
// * This class contains two public functions. One converts object to bytes, the other one coverts bytes to object
// */
//public class ObjectAndByte {
//
//    public static byte[] objectToByte(Object o){
//        try{
//            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//            ObjectOutputStream output = new ObjectOutputStream(byteOut);
//            output.writeObject(o);
//            return byteOut.toByteArray();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static Object byteToObject(byte[] bytes){
//        try {
//            ByteArrayInputStream input = new ByteArrayInputStream(bytes);
//            ObjectInputStream inputObject = new ObjectInputStream(input);
//            return inputObject.readObject();
//        }catch (IOException | ClassNotFoundException e ){
//            System.out.println(e);
//        }
//        return None;
//    }
//}
