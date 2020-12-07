import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author 周天斌 10252160
 * @Date 2020/12/7 15:32
 * @Version V1.0
 */
public class TopN {
    public <T> List<T> getTopN(Node<T> header, int n) {
        Map<T, Integer> map = new HashMap<>();
        while (header != null) {
            map.put(header.data, (map.getOrDefault(header.data, 0) + 1));
            header = header.next;
        }
        int size = map.size();
        if (n < 0 || size < n) {
            return null;
        }

        MyMinPQ<Item<T>> pq = new MyMinPQ<>();
        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            Item<T> item = new Item<>(entry.getKey(), entry.getValue());
            pq.insert(item);
            if (pq.size() > n) {
                pq.delMin();
            }
        }

        List<T> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            res.add(pq.delMin().data);
        }
        return res;
    }

    class MyMinPQ<Key extends Comparable<Key>> {
        private Key[] pq;
        private int n;

        public MyMinPQ() {
            this(1);
        }

        public MyMinPQ(int capacity) {
            pq = (Key[]) new Comparable[capacity];
            n = 0;
        }

        public int size() {
            return n;
        }

        public boolean isEmpty() {
            return n == 0;
        }

        public void insert(Key x) {
            if (n == pq.length - 1) {
                resize(2 * pq.length);
            }
            pq[++n] = x;
            swim(n);
        }

        public Key delMin() {
            Key min = pq[1];
            exch(1, n--);
            sink(1);
            pq[n + 1] = null;
            return min;
        }

        private void swim(int k) {
            while (k > 1 && more(k / 2, k)) {
                exch(k, k / 2);
                k = k / 2;
            }
        }

        private void sink(int k) {
            while (true) {
                int maxPos = k;
                if (2 * k <= n && more(maxPos, 2 * k)) {
                    maxPos = 2 * k;
                }
                if (2 * k + 1 <= n && more(maxPos, 2 * k + 1)) {
                    maxPos = 2 * k + 1;
                }
                if (maxPos == k) {
                    break;
                }
                exch(k, maxPos);
                k = maxPos;
            }
        }

        private void exch(int i, int j) {
            Key swap = pq[i];
            pq[i] = pq[j];
            pq[j] = swap;
        }

        private boolean less(int i, int j) {
            return pq[i].compareTo(pq[j]) < 0;
        }

        private boolean more(int i, int j) {
            return pq[i].compareTo(pq[j]) > 0;
        }

        private void resize(int capacity) {
            assert capacity > n;
            Key[] temp = (Key[]) new Comparable[capacity];
            for (int i = 1; i <= n; i++) {
                temp[i] = pq[i];
            }
            pq = temp;
        }
    }

    class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    class Item<T> implements Comparable<Item<T>>{
        T data;
        int count;

        public Item(T data, int count) {
            this.data = data;
            this.count = count;
        }

        @Override
        public int compareTo(Item<T> o) {
            return this.count - o.count;
        }
    }
}
