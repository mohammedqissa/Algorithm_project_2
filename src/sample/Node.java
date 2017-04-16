package sample;

/**
 * Created by mohammedissa on 4/3/16.
 */
public class Node<T extends Comparable<?>> implements Comparable<Node<Comparable<?>>>{
    int count;
    char ch;
    String huffCode;
    Node<Comparable<?>> left;
    Node<Comparable<?>> right;

    public Node() {
        ch = 0;
    }


    public Node(int count) {
        this.count = count;
        ch = 0;
        left = null;
        right = null;
    }
    public Node(int count, char ch) {
        this.count = count;
        this.ch = ch;
        left = null;
        right = null;
    }

    public int getCount() {
        return count;
    }
    public char getCh() {
        return ch;
    }
    public Node<Comparable<?>> getLeft() {
        return left;
    }
    public Node<Comparable<?>> getRight() {
        return right;
    }

    @Override
    public int compareTo(Node<Comparable<?>> t) {
        return count-t.count;
    }

    public String toString(){
        return count+"";
    }


}
