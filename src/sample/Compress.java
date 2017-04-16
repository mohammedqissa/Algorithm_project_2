package sample;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Created by mohammedissa on 4/3/16.
 */
public class Compress {



    Path pat ;
    byte[] data ;

    ArrayList<Byte> bytes = new ArrayList<>();

    int currentByte ;
    int numBitsInCurrentByte ;

    String path;
    int a =0;


    int numOfNodes;

    Node<Comparable<?>>[] treeNodes ;

    Node<Comparable<?>> tree;

    PriorityQueue<Node<Comparable<?>>> queue = new PriorityQueue<>();

    FileOutputStream fileOutputStream;
    //    FileInputStream fileInputStream;
    int numberOfBits =0;

    byte max ;


    public Compress(String path) throws IOException {

        this.path = path;

        System.out.println("reading");

        pat = Paths.get(path);

        data = Files.readAllBytes(pat);

//        fileInputStream = new FileInputStream(path);
        treeNodes = new Node[256];

        for (int i = 0 ; i < treeNodes.length;i++){
            treeNodes[i] = new Node();
            treeNodes[i].ch = (char) i;
        }

        for (int i =0;i<data.length;i++)
        {
            treeNodes[data[i]&0xff].count++;

        }

//        fileInputStream.close();


//        treeNodes = Main.read(path);
        System.out.println("build tree");

        buildTree();
        System.out.println("generating code");

//        BTreePrinter.printNode(tree);

        generateCode(tree,new String());
//        printLeafNodes(tree);
        getMax();

//        int c = 1;
//        for (int i = 0 ; i < treeNodes.length;i++)
//            if (treeNodes[i].count>0)
//                System.out.println(treeNodes[i].count + "    "+ c++);

        for (int i = 0 ; i < treeNodes.length;i++)
            System.out.println(treeNodes[i].count);

//        System.out.println("writing to file");

        writeToFile();




    }

    public void buildTree(){

        for (int i = 0 ; i < treeNodes.length;i++)
            if (treeNodes[i].count>0)
                queue.add(treeNodes[i]);

        numOfNodes = queue.size();

        while (queue.size()>1){

            Node<Comparable<?>> a = queue.remove();
            Node<Comparable<?>> b = queue.remove();

            Node<Comparable<?>> c = new Node<Comparable<?>>(a.count+b.count);

            c.right = a;
            c.left = b;
            queue.add(c);

        }

        tree = queue.remove();

        queue = null;

//        BTreePrinter.printNode(tree);

    }



    private void generateCode(Node node, String code) {

        if(node != null) {
            if(node.left == null && node.right == null){
                node.huffCode = code;


            }
            else {
                generateCode(node.getLeft(), code + "0");
                generateCode(node.getRight(), code + "1");
            }
        }
    }

    public static void printLeafNodes(Node t)
    {
        if(t == null)
            return;
        if(t.left == null && t.right==null)
            System.out.println(t.huffCode +"    " + (int)t.ch);
        printLeafNodes(t.left);
        printLeafNodes(t.right);
    }

    public void getMax(){

        int max = Integer.MIN_VALUE;

        for(int i = 0 ; i<treeNodes.length;i++)
            if (treeNodes[i].count> max)
                max = treeNodes[i].count;

        byte[] bytes = ByteBuffer.allocate(4).putInt(max).array();
        String a ="";

        for (int i = 0 ; i<bytes.length*8;i++)
            a+=getBit(bytes,i);
//            System.out.print(Integer.toBinaryString(bytes[i] & 255 | 256).substring(1));

        int b = a.indexOf("1");
        this.max = (byte)(bytes.length*8 - b);

//        System.out.println(Integer.toBinaryString(this.max & 255 | 256).substring(1));
//        System.out.println(this.max);



    }

    public int getBit(byte[] data, int pos) {
        int posByte = pos/8;
        int posBit = pos%8;
        byte valByte = data[posByte];
        int valInt = valByte>>(8-(posBit+1)) & 0x0001;
        return valInt;
    }


    private void writeToFile() throws IOException {



        fileOutputStream = new FileOutputStream(path+".huff");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        //write number of bits for every frequence

//        int m = this.max;
//        byte[] mm = ByteBuffer.allocate(4).putInt(m).array();
//
//        for (int i = 8 ; i >0;i--)
//            write(getBit(mm,mm.length*8-i));
//
//        System.out.println("max max  "+(max));



         ;
        //this 32 bytes to detect frequenceis places

        for (int i = 0 ; i < 256;i++){
            if (treeNodes[i].count>0)
                write(1);
            else
                write(0);
        }






        //write frquenceis

        for (int j = 0 ; j<treeNodes.length;j++){

            int freq = treeNodes[255-j].count;


            if (freq>0){

                byte[] a = ByteBuffer.allocate(4).putInt(freq).array();
                bytes.add(a[0]);
                bytes.add(a[1]);
                bytes.add(a[2]);
                bytes.add(a[3]);
            }

        }





//        write translated code

        for (int j =0 ; j<data.length ; j++)
        {
//            int b =  (data[j] + 128);
//
//            byte[] bytes = ByteBuffer.allocate(4).putInt(Integer.parseInt(treeNodes[data[j]+128].huffCode)).array();
//              System.out.println(data[j]&0xff);


            byte[] cc = ByteBuffer.allocate(4).putInt(Integer.parseInt(treeNodes[data[j]&0xff].huffCode)).array();

            for (int i = 0;i < cc.length*8;i++){
                write(getBit(cc,cc.length*8-i-1));
            }

//            byte[] cc = ByteBuffer.allocate(4).putInt(Integer.parseInt(c)).array();
//
//            for (int i = 8 ; i >0;i--)
//                write(getBit(cc,cc.length*8-i));


//            System.out.println(treeNodes[data[j]+128].huffCode);

//            byte[] bytes = fileInputStream.read(byte);
//            System.out.println("\n");



        }

        byte[] d = new byte[bytes.size()];
        for (int i =0;i<d.length;i++)
        {
            d[i] = bytes.get(i);

        }

//        for (int i = 0 ; i < d.length;i++){
//            System.out.println(d[i]);
//        }

//        System.out.println(d[50] + " is");

        objectOutputStream.write(d);
        objectOutputStream.close();

//        fileOutputStream.write(d);
//
//        fileOutputStream.close();

//        for (int i = 0 ; i < treeNodes.length;i++)
//            if(treeNodes[i].count>0)
//                System.out.println(i);

        System.exit(0);




    }









    // Writes a bit to the stream. The specified bit must be 0 or 1.
//    public void write(int b) throws IOException {
//        if (!(b == 0 || b == 1))
//            throw new IllegalArgumentException("Argument must be 0 or 1");
//        currentByte = currentByte << 1 | b;
//        numBitsInCurrentByte++;
//        if (numBitsInCurrentByte == 8) {
//            fileOutputStream.write(currentByte);
//            numBitsInCurrentByte = 0;
//        }
//    }

    public void write(int b) throws IOException {
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