import java.util.*;

public class BinarySearchTree<E extends Comparable<E>> implements AbstractBinarySearchTree<E> {

    private Node<E> root;


    public BinarySearchTree() {
        this.root = null;
    }

    public BinarySearchTree(Node<E> root) {
        this.root = root;
    }

    // ===============================
    //             INSERT (AVL)
    // ===============================

    @Override
    public void insert(E element) {
        root = insertAVL(root, element);
    }

    private Node<E> insertAVL(Node<E> node, E key) {
        if (node == null) {
            return new Node<>(key);
        }

        int cmp = key.compareTo(node.value);

        if (cmp < 0)
            node.leftChild = insertAVL(node.leftChild, key);
        else if (cmp > 0)
            node.rightChild = insertAVL(node.rightChild, key);
        else
            return node; // дубликаты не вставляем

        // обновляем высоту
        updateHeight(node);

        // балансируем
        return balance(node);
    }

    // ===============================
    //       AVL — ВСПОМОГАТЕЛЬНЫЕ
    // ===============================

    private int height(Node<E> n) {
        return (n == null) ? 0 : n.height;
    }

    private void updateHeight(Node<E> n) {
        n.height = 1 + Math.max(height(n.leftChild), height(n.rightChild));
    }

    private int getBalance(Node<E> n) {
        return (n == null) ? 0 : height(n.leftChild) - height(n.rightChild);
    }

    private Node<E> rotateRight(Node<E> y) {
        Node<E> x = y.leftChild;
        Node<E> T2 = x.rightChild;

        x.rightChild = y;
        y.leftChild = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node<E> rotateLeft(Node<E> x) {
        Node<E> y = x.rightChild;
        Node<E> T2 = y.leftChild;

        y.leftChild = x;
        x.rightChild = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node<E> balance(Node<E> n) {
        int bf = getBalance(n);

        // левый перегруз
        if (bf > 1) {
            if (getBalance(n.leftChild) < 0) {
                n.leftChild = rotateLeft(n.leftChild);
            }
            return rotateRight(n);
        }

        // правый перегруз
        if (bf < -1) {
            if (getBalance(n.rightChild) > 0) {
                n.rightChild = rotateRight(n.rightChild);
            }
            return rotateLeft(n);
        }

        return n;
    }

    // ===============================
    // Проверка перед вставкой
    // ===============================
    public void checkAndInsert(E element) {
        if (contains(element)) {
            System.out.println("Элемент " + element + " уже существует в дереве.");
        } else {
            insert(element);
            System.out.println("Элемент " + element + " не найден и был добавлен в дерево.");
        }
    }

    // ===============================
    // CONTAINS / SEARCH
    // ===============================

    @Override
    public boolean contains(E element) {
        return containsRecursive(root, element);
    }

    private boolean containsRecursive(Node<E> node, E element) {
        if (node == null) return false;
        int cmp = element.compareTo(node.value);
        if (cmp == 0) return true;
        if (cmp < 0) return containsRecursive(node.leftChild, element);
        return containsRecursive(node.rightChild, element);
    }

    @Override
    public AbstractBinarySearchTree<E> search(E element) {
        Node<E> foundNode = searchRecursive(root, element);
        if (foundNode != null) return new BinarySearchTree<>(foundNode);
        return new BinarySearchTree<>();
    }

    private Node<E> searchRecursive(Node<E> current, E element) {
        if (current == null) return null;
        int cmp = element.compareTo(current.value);
        if (cmp == 0) return current;
        if (cmp < 0) return searchRecursive(current.leftChild, element);
        return searchRecursive(current.rightChild, element);
    }

    // ===============================
    // GETTERS
    // ===============================

    @Override
    public Node<E> getRoot() { return root; }

    @Override
    public Node<E> getLeft() { return root != null ? root.leftChild : null; }

    @Override
    public Node<E> getRight() { return root != null ? root.rightChild : null; }

    @Override
    public E getValue() { return root != null ? root.value : null; }

    // ===============================
    // PRINT METHODS (ваши, без изменений)
    // ===============================

    public void printTree() {
        if (root == null) {
            System.out.println("Дерево пустое");
            return;
        }
        printTreeRecursive(root, 0, "Корень:");
    }

    private void printTreeRecursive(Node<E> node, int level, String prefix) {
        if (node != null) {
            String indent = "  ".repeat(level);
            System.out.println(indent + prefix + node.value);

            if (node.leftChild != null || node.rightChild != null) {
                if (node.leftChild != null)
                    printTreeRecursive(node.leftChild, level + 1, "Левый: ");
                else
                    System.out.println("  ".repeat(level + 1) + "Левый: null");

                if (node.rightChild != null)
                    printTreeRecursive(node.rightChild, level + 1, "Правый: ");
                else
                    System.out.println("  ".repeat(level + 1) + "Правый: null");
            }
        }
    }

    public void printTreeVisual() {
        // полностью оставлен ваш код
        if (root == null) {
            System.out.println("Дерево пустое");
            return;
        }
        List<List<String>> lines = new ArrayList<>();
        List<Node<E>> level = new ArrayList<>();
        List<Node<E>> next = new ArrayList<>();

        level.add(root);
        int nn = 1;
        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<>();
            nn = 0;
            for (Node<E> n : level) {
                if (n == null) {
                    line.add(null);
                    next.add(null);
                    next.add(null);
                } else {
                    String aa = n.value.toString();
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.leftChild);
                    next.add(n.rightChild);

                    if (n.leftChild != null) nn++;
                    if (n.rightChild != null) nn++;
                }
            }

            if (widest % 2 == 1) widest++;
            lines.add(line);
            List<Node<E>> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }
                    System.out.print(c);

                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) System.out.print(" ");
                    } else {
                        for (int k = 0; k < hpw; k++) System.out.print(j % 2 == 0 ? " " : "─");
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) System.out.print(j % 2 == 0 ? "─" : " ");
                    }
                }
                System.out.println();
            }

            for (int j = 0; j < line.size(); j++) {
                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                for (int k = 0; k < gap1; k++) System.out.print(" ");
                System.out.print(f);
                for (int k = 0; k < gap2; k++) System.out.print(" ");
            }
            System.out.println();

            perpiece /= 2;
        }
    }

    // ===============================
    // Traversals
    // ===============================

    public List<E> inOrder() {
        List<E> r = new ArrayList<>();
        inOrderRecursive(root, r);
        return r;
    }

    private void inOrderRecursive(Node<E> n, List<E> r) {
        if (n != null) {
            inOrderRecursive(n.leftChild, r);
            r.add(n.value);
            inOrderRecursive(n.rightChild, r);
        }
    }

    public List<E> preOrder() {
        List<E> r = new ArrayList<>();
        preOrderRecursive(root, r);
        return r;
    }

    private void preOrderRecursive(Node<E> n, List<E> r) {
        if (n != null) {
            r.add(n.value);
            preOrderRecursive(n.leftChild, r);
            preOrderRecursive(n.rightChild, r);
        }
    }

    public List<E> postOrder() {
        List<E> r = new ArrayList<>();
        postOrderRecursive(root, r);
        return r;
    }

    private void postOrderRecursive(Node<E> n, List<E> r) {
        if (n != null) {
            postOrderRecursive(n.leftChild, r);
            postOrderRecursive(n.rightChild, r);
            r.add(n.value);
        }
    }

    public List<E> bfs() {
        List<E> r = new ArrayList<>();
        if (root == null) return r;

        Queue<Node<E>> q = new LinkedList<>();
        q.add(root);

        while (!q.isEmpty()) {
            Node<E> n = q.poll();
            r.add(n.value);

            if (n.leftChild != null) q.add(n.leftChild);
            if (n.rightChild != null) q.add(n.rightChild);
        }
        return r;
    }

    public List<E> dfs() {
        List<E> r = new ArrayList<>();
        if (root == null) return r;

        Stack<Node<E>> st = new Stack<>();
        st.push(root);

        while (!st.isEmpty()) {
            Node<E> n = st.pop();
            r.add(n.value);

            if (n.rightChild != null) st.push(n.rightChild);
            if (n.leftChild != null) st.push(n.leftChild);
        }
        return r;
    }

    @Override
    public String toString() {
        return inOrder().toString();
    }
}
