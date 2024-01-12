public class RankSeq
{
    //private int rank;
    private int size;
    private Node header;
    private Node trailer;

    public RankSeq()
    {
        // Define the header and trailer Nodes
        header = new Node(-1);
        trailer = new Node(-1);

        // Link the header and trailer to each other
        header.setNext(trailer);
        trailer.setPrev(header);

        // Set the initial size of the sequence.
        size = 0;  
    }

    public int size() 
    {
        return size;
    }

    public boolean isEmpty() 
    {
        return size < 1;
    }

    private void checkRank (int rank) throws BoundaryViolationException 
    {
        if (rank < 1 || rank > 9)//size()+1) 
        {
            throw new BoundaryViolationException ("Invalid rank");
        }
    }

    public Node nodeAtRank (int rank) throws BoundaryViolationException 
    {
        Node node; 
        //System.out.println("CALL: nodeAtRank(" + rank + "),\t Size: " + size() + "\tisEmpty: " + isEmpty());

        if (!isEmpty())
        {
            checkRank(rank);
        }


        if (rank <= size()/2) {
            node = header.getNext();
            for (int i=0; i < rank; i++)
            {
                node = node.getNext();  
            }
        }
        else  
        {
            node = trailer.getPrev();
            for (int i=0; i < size()-rank-1; i++)
            {
                node = node.getPrev();    
            }
        }
        return node;
    }

    public void replaceItem(int rank, int data) throws BoundaryViolationException 
    {
        Node node = nodeAtRank(rank); 
        node.setData(data);
    }

    public void insertItem (int rank, int data) throws BoundaryViolationException 
    {
        Node prev = nodeAtRank(rank);
        Node next = prev.getNext();
        Node node = new Node(data, next, prev);
        next.setPrev(node);
        prev.setNext(node);
        size++;
    }

    public int deleteItem (int rank) throws BoundaryViolationException 
    {
        Node node = nodeAtRank(rank);
        Node next = node.getNext();
        Node prev = node.getPrev();
        prev.setNext(next);
        next.setPrev(prev);
        size--;
        return node.getData();
    }

}
