package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;


public class Main extends Application {

    static FileInputStream inputStream ;
    static FileOutputStream outputStream;
    private static int currentByte;
    static ArrayList<Byte> bytes = new ArrayList<>();
    private static int numBitsInCurrentByte;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        launch(args);












//        Compress a = new Compress(file.getAbsolutePath());

        //Decompress b = new Decompress(file.getAbsolutePath());






    }


    public static Node<Comparable<?>>[] read(String file) throws IOException {

        inputStream = new FileInputStream(file);

        Node<Comparable<?>>[] treeNodes = new Node[256];

        for (int i = 0 ; i < treeNodes.length;i++){
            treeNodes[i] = new Node();
            treeNodes[i].ch = (char) i;
        }

        int byt;
        while ((byt = inputStream.read())!=-1)
        {
            treeNodes[byt].count++;

        }

        inputStream.close();

        return treeNodes;

    }

    public static int getBit(ArrayList<Byte> data, int pos) {
        int posByte = pos/8;
        int posBit = pos%8;
        byte valByte = data.get(posByte);
        int valInt = valByte>>(8-(posBit+1)) & 0x0001;
        return valInt;
    }

    public static int getBit2(byte[] data, int pos) {
        int posByte = pos/8;
        int posBit = pos%8;
        byte valByte = data[posByte];
        int valInt = valByte>>(8-(posBit+1)) & 0x0001;
        return valInt;
    }


    public static void write(int b) throws IOException {
        if (!(b == 0 || b == 1))
            throw new IllegalArgumentException("Argument must be 0 or 1");
        currentByte = currentByte << 1 | b;
        numBitsInCurrentByte++;
        if (numBitsInCurrentByte == 8) {
            bytes.add((byte) currentByte);
//            System.out.println("written : "+(++a) + " bytes");

            numBitsInCurrentByte = 0;
        }
    }


}
