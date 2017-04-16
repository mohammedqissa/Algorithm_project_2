package sample;

import sun.misc.Queue;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Created by mohammedissa on 4/4/16.
 */
public class Decompress {



    Path pat ;

    byte[] data ;


    byte[] places = new byte[32];

    FileInputStream fileInputStream;
    FileOutputStream fileOutputStream;
    ObjectInputStream objectInputStream;

    ArrayList<Byte> freq = new ArrayList<>();

    Node<Comparable<?>>[] treeNodes =new Node[256];

    Node<Comparable<?>> tree;

    PriorityQueue<Node<Comparable<?>>> queue = new PriorityQueue<>();

    private int currentByteb;
    private int numBitsInCurrentByteb;


    public Decompress(String path) throws IOException {
//        fileInputStream = new FileInputStream(path);
//        fileOutputStream = new FileOutputStream(path.replace(".huff",""));

        read(path);


        buildTree();
        generateCode(tree,"");

//        BTreePrinter.printNode(tree);



        translate(path);

        System.exit(0);



    }

    private void translate(String path) throws IOException {


        String p = path.substring(0,path.lastIndexOf("."));

        fileOutputStream = new FileOutputStream(p);


        for (int i = 327 ; i < data.length*8-1 ; ){


            Node tmp = tree;

            while (true){


                int c = getBit(data,i);
                if(c == 1)
                    tmp = tmp.getLeft();
                else if (c == 0 )
                    tmp = tmp.getRight();
                i++;


                if((tmp.left == null )&& (tmp.right == null)){
                    fileOutputStream.write(tmp.ch);
                    break;
                }



            }

        }

        fileOutputStream.close();




    }


    public void read(String path) throws IOException {


        fileInputStream = new FileInputStream(path);
        objectInputStream = new ObjectInputStream(new FileInputStream(path));




        for (int i = 0 ; i < 32 ; i++)
            places[i] = objectInputStream.readByte();





        for (int i = 0 ; i < treeNodes.length;i++){
            treeNodes[i] = new Node<>();
            treeNodes[i].ch=(char)(i & 0xff);
        }


        Stack<Integer> stack = new Stack<>();


        //places
        for (int i = 0;i<256;i++){
            int n = getBit(places,i);
            if(n == 1){
                stack.add(i);
            }
        }





        //frequencies

        for (int i = 0 ; i <  256 ;i++){

            int a = objectInputStream.readInt();
            int p = stack.pop();
            treeNodes[p].count = a;


        }



        pat = Paths.get(path);

        data = Files.readAllBytes(pat);






    }

    public void buildTree(){

        for (int i = 0 ; i < treeNodes.length;i++)
            if (treeNodes[i].count>0)
                queue.add(treeNodes[i]);


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

    public int getBit(byte[] data, int pos) {
        int posByte = pos/8;
        int posBit = pos%8;
        byte valByte = data[posByte];
        int valInt = valByte>>(8-(posBit+1)) & 0x0001;
        return valInt;
    }

    public void writefreq(int b) throws IOException {
        if (!(b == 0 || b == 1))
            throw new IllegalArgumentException("Argument must be 0 or 1");
        currentByteb = currentByteb << 1 | b;
        numBitsInCurrentByteb++;
        if (numBitsInCurrentByteb == 8) {
            freq.add((byte) currentByteb);
//            System.out.println("written : "+(++a) + " bytes");

            numBitsInCurrentByteb = 0;
        }
    }

    public double binaryToInteger(String binary) {
        char[] numbers = binary.toCharArray();
        double result = 0;
        for(int i=numbers.length - 1; i>=0; i--)
            if(numbers[i]=='1')
                result += Math.pow(2, (numbers.length-i - 1));
        return result;
    }

    public  int readFullLengthInt(int index) throws IOException {
        int integer = 0;
        for(int i = 0; i < 4; ++i) {
            int byteRead = data[index];
            index++;
            for(int j = 0; j < 8; ++j) {
                int bit = (byteRead & 0x80);
                if(bit == 0x80) {
                    integer <<= 1;
                    integer += 1;
                }
                else {
                    integer <<= 1;
                }
                byteRead <<= 1;
            }
        }
        return integer;
    }






}
