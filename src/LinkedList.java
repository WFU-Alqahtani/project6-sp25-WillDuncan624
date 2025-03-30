import java.util.Random;

class LinkedList {
    public Node head;
    public Node tail;
    public int size = 0;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void shuffle(int shuffle_count) {
        Random rand = new Random();
        for (int i = 0; i < shuffle_count; i++) {
            int r1 = rand.nextInt(size);
            int r2 = rand.nextInt(size);
            swap(r1, r2);
        }
    }

    public Card remove_from_index(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds in remove_from_index");
        }
        if (index == 0) {
            return remove_from_head();
        }
        if (index == size - 1) {
            Card card = tail.data;
            tail = tail.prev;
            if (tail != null) {
                tail.next = null;
            } else {
                head = null;
            }
            size--;
            return card;
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        current.prev.next = current.next;
        current.next.prev = current.prev;
        size--;
        return current.data;
    }

    public void insert_at_index(Card x, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds in insert_at_index");
        }
        if (index == 0) {
            Node newNode = new Node(x);
            newNode.next = head;
            if (head != null) {
                head.prev = newNode;
            } else {
                tail = newNode;
            }
            head = newNode;
            size++;
            return;
        }
        if (index == size) {
            add_at_tail(x);
            return;
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        Node newNode = new Node(x);
        newNode.prev = current.prev;
        newNode.next = current;
        current.prev.next = newNode;
        current.prev = newNode;
        size++;
    }

    public void swap(int index1, int index2) {
        if (index1 < 0 || index1 >= size || index2 < 0 || index2 >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds in swap");
        }
        if (index1 == index2) return;
        if (index1 > index2) {
            int temp = index1;
            index1 = index2;
            index2 = temp;
        }
        Card card1 = remove_from_index(index1);
        Card card2 = remove_from_index(index2 - 1);
        insert_at_index(card1, index2 - 1);
        insert_at_index(card2, index1);
    }

    public void add_at_tail(Card data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public Card remove_from_head() {
        if (head == null) {
            throw new IllegalStateException("List is empty, cannot remove from head");
        }
        Card card = head.data;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else { // list became empty
            tail = null;
        }
        size--;
        return card;
    }

    public void sanity_check() {
        Node curr = head;
        int count_forward = 0;
        while (curr != null) {
            curr = curr.next;
            count_forward++;
        }

        curr = tail;
        int count_backward = 0;
        while (curr != null) {
            curr = curr.prev;
            count_backward++;
        }

        if (count_forward == count_backward && count_forward == size) {
            System.out.println("Basic sanity Checks passed");
        } else {
            System.out.println("Count forward:  " + count_forward);
            System.out.println("Count backward: " + count_backward);
            System.out.println("Size of LL:     " + size);
            System.out.println("Sanity checks failed");
            System.exit(-1);
        }
    }

    public void print() {
        Node curr = head;
        int i = 1;
        while (curr != null) {
            curr.data.print_card();
            if (curr.next != null)
                System.out.print(" -->  ");
            else
                System.out.println(" X");

            if (i % 7 == 0) System.out.println("");
            i++;
            curr = curr.next;
        }
        System.out.println("");
    }

    public void addAll(LinkedList other) {
        Node curr = other.head;
        while (curr != null) {
            this.add_at_tail(curr.data);
            curr = curr.next;
        }
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
}