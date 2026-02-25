
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;


interface AbstractBinarySearchTree<E extends Comparable<E>> {
    class Node<E> {
        public E value;
        public Node<E> leftChild;
        public Node<E> rightChild;
        public int height = 1;


        public Node(E value) {
            this.value = value;
        }

        public Node(E value, Node<E> leftChild, Node<E> rightChild) {
            this.value = value;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }
    }

    void insert(E element);
    boolean contains(E element);
    AbstractBinarySearchTree<E> search(E element);
    Node<E> getRoot();
    Node<E> getLeft();
    Node<E> getRight();
    E getValue();
}


class BinaryTree<E> implements AbstractBinaryTree<E> {
    private E key;
    private BinaryTree<E> left;
    private BinaryTree<E> right;

    public BinaryTree(E key) {
        this.key = key;
    }

    public BinaryTree(E key, BinaryTree<E> left, BinaryTree<E> right) {
        this.key = key;
        this.left = left;
        this.right = right;
    }

    @Override
    public E getKey() {
        return key;
    }

    @Override
    public BinaryTree<E> getLeft() {
        return left;
    }

    @Override
    public BinaryTree<E> getRight() {
        return right;
    }

    @Override
    public void setKey(E key) {
        this.key = key;
    }

    //задание 2 dsf + bfs
    public void dfs() {
        dfs(this);
        System.out.println();
    }

    private void dfs(BinaryTree<E> node) {
        if (node == null) return;
        dfs(node.left);
        dfs(node.right);
        System.out.print(node.key + " ");
    }

    public void bfs() {
        if (this == null) return;
        Queue<BinaryTree<E>> queue = new LinkedList<>();
        queue.add(this);
        while (!queue.isEmpty()) {
            BinaryTree<E> node = queue.poll();
            System.out.print(node.key + " ");
            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }
        System.out.println();
    }

    @Override
    public String asIndentedPreOrder(int indent) {
        StringBuilder sb = new StringBuilder(" ".repeat(indent) + key + "\n");
        if (left != null) sb.append(left.asIndentedPreOrder(indent + 2));
        if (right != null) sb.append(right.asIndentedPreOrder(indent + 2));
        return sb.toString();
    }

    @Override
    public List<AbstractBinaryTree<E>> preOrder() {
        List<AbstractBinaryTree<E>> list = new ArrayList<>();
        list.add(this);
        if (left != null) list.addAll(left.preOrder());
        if (right != null) list.addAll(right.preOrder());
        return list;
    }

    @Override
    public List<AbstractBinaryTree<E>> inOrder() {
        List<AbstractBinaryTree<E>> list = new ArrayList<>();
        if (left != null) list.addAll(left.inOrder());
        list.add(this);
        if (right != null) list.addAll(right.inOrder());
        return list;
    }

    @Override
    public List<AbstractBinaryTree<E>> postOrder() {
        List<AbstractBinaryTree<E>> list = new ArrayList<>();
        if (left != null) list.addAll(left.postOrder());
        if (right != null) list.addAll(right.postOrder());
        list.add(this);
        return list;
    }

    @Override
    public void forEachInOrder(Consumer<E> consumer) {
        if (left != null) left.forEachInOrder(consumer);
        consumer.accept(key);
        if (right != null) right.forEachInOrder(consumer);
    }

    public void printTreeVisual() {
        if (this == null || this.key == null) {
            System.out.println("Дерево пустое");
            return;
        }

        List<List<String>> lines = new ArrayList<>();
        List<BinaryTree<E>> level = new ArrayList<>();
        List<BinaryTree<E>> next = new ArrayList<>();

        level.add(this);
        int nn = 1;
        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<>();
            nn = 0;
            for (BinaryTree<E> n : level) {
                if (n == null) {
                    line.add(null);
                    next.add(null);
                    next.add(null);
                } else {
                    String aa = n.key.toString();
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.left);
                    next.add(n.right);

                    if (n.left != null) nn++;
                    if (n.right != null) nn++;
                }
            }

            if (widest % 2 == 1) widest++;
            lines.add(line);
            List<BinaryTree<E>> tmp = level;
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
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    } else {
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
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


}


public class Task3 {
    public static void main(String[] args) {

        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        bst.insert(4);
        bst.insert(2);
        bst.insert(6);
        bst.insert(1);
        bst.insert(3);
        bst.insert(5);
        bst.insert(7);



        System.out.println("=== Простое отображение дерева ===");
        bst.printTree();

        System.out.println("\n=== Визуальное отображение дерева ===");
        bst.printTreeVisual();

        System.out.println("\nInOrder обход: " + bst.inOrder());

        System.out.println("\nPreOrder обход: " + bst.preOrder());

        System.out.println("\nPostOrder обход: " + bst.postOrder());

        System.out.println("\nDFS обход: " + bst.dfs());

        System.out.println("\nBFS обход: " + bst.bfs());


        System.out.println("\n======");

        BinaryTree<Integer> tree2 =
                new BinaryTree<>(8,
                        new BinaryTree<>(2,
                                new BinaryTree<>(1),
                                new BinaryTree<>(3)),
                        new BinaryTree<>(6,
                                new BinaryTree<>(5),
                                new BinaryTree<>(7)
                        )
                );
        tree2.printTreeVisual();
        tree2.bfs();
        tree2.dfs();
        String pre = tree2.preOrder().stream()
                .map(node -> node.getKey().toString())
                .reduce((a, b) -> a + " " + b)
                .orElse("");
        System.out.println("Pre-order: " + pre);
        String in = tree2.inOrder().stream()
                .map(node -> node.getKey().toString())
                .reduce((a, b) -> a + " " + b)
                .orElse("");
        System.out.println("In-order: " + in);
        String post = tree2.postOrder().stream()
                .map(node -> node.getKey().toString())
                .reduce((a, b) -> a + " " + b)
                .orElse("");
        System.out.println("Post-order: " + post);
        System.out.println("\n======");


        System.out.println("\n=== Другой пример ===");
        BinarySearchTree<Integer> bst2 = new BinarySearchTree<>();
        bst2.insert(13);
        bst2.insert(8);
        bst2.insert(3);
        bst2.insert(1);
        bst2.insert(6);
        bst2.insert(14);
        bst2.insert(4);
        bst2.insert(7);




        bst2.checkAndInsert(11);

        bst2.printTreeVisual();

        System.out.println("\nInOrder обход: " + bst2.inOrder());
        System.out.println("post");
        System.out.println(bst2.postOrder());
        System.out.println("pre");
        System.out.println(bst2.preOrder());
        System.out.println("bfs");
        System.out.println(bst2.bfs());
        System.out.println("dfs");
        System.out.println(bst2.dfs());
        System.out.println("Содержит 3: " + bst2.contains(3));
        System.out.println("Корень: " + bst2.getValue());
    }
}