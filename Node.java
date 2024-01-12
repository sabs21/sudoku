public class Node
{
    private int data;
    private Node next;
    private Node prev;

    public Node () {
        data = -1;
        next = null;
        prev = null;
    }

    public Node (int d) {
        data = d;   
        next = null;
        prev = null;
    }

    public Node (int d, Node n, Node p) {
        data = d;
        next = n;   
        prev = p;
    }

    public void setData (int newData) {
        data = newData;   
    }

    public void setNext (Node newNext) {
        next = newNext;   
    }
    
    public void setPrev (Node newPrev) {
        prev = newPrev;
    }

    public int getData () {
        return data;   
    }

    public Node getNext () {
        return next;   
    }
    
    public Node getPrev () {
        return prev;
    }

    public void displayNode () {
        System.out.print (data);   
    }
}
