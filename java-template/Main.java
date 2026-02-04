import java.util.*;
import java.io.*;

/**
 * source: https://github.com/ameys2907/cp-templates/blob/main/java-template/Main.java
 * @author _ameysawant_
 */
public class Main 
{
    private static FastIO f;
    private static PrintWriter out;
    
    //debug methods
    static void p(Object... o){out.println(Arrays.deepToString(o));}
    //output print methods
    static void pln(Object o) {out.println(o);}
    static void p(Object o) {out.print(o + " ");}
    
    //fast input output class
    static class FastIO {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer("");
        String next(){
            while(!st.hasMoreTokens()){
                try{st = new StringTokenizer(br.readLine());}
                catch(IOException e){}}
            return st.nextToken();
        }
        int ni() {return Integer.parseInt(next());}
        long nl() {return Long.parseLong(next());}
        int[] nai(int n) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = ni();
            return a;
        }
        
        long[] nal(int n) {
            long[] a = new long[n];
            for (int i = 0; i < n; i++) a[i] = nl();
            return a;
        }
    }
    
    public static void main(String[] args) 
    {
        f = new FastIO();
        out = new PrintWriter(System.out);
        int t = 1;
        while(t > 0)
        {
            t--;
            solve();
        }
        out.flush();
    }
    
    //write code here
    private static void solve()
    {
        
    }
}

//template 1: DSU
class DSU {
    int[] parent;
    int[] size;

    public DSU(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int parent(int x) {
        if (parent[x] == x) return x;
        return parent[x] = parent(parent[x]);
    }

    public void union(int x, int y) {
        int parX = parent(x);
        int parY = parent(y);

        if (parX != parY) {
            if (size[parX] < size[parY]) {
                parent[parX] = parY;
                size[parY] += size[parX];
            } else {
                parent[parY] = parX;
                size[parX] += size[parY];
            }
        }
    }

    public boolean conn(int x, int y) {
        return parent(x) == parent(y);
    }
}

//template 2: Number Theory
class MathUtils {
    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) return 0;
        return (a / gcd(a, b)) * b;
    }

    public static long pow(long base, long exp) {
        long res = 1;
        while (exp > 0) {
            if ((exp & 1) == 1) res *= base;
            base *= base;
            exp >>= 1;
        }
        return res;
    }

    // Fast Modular Exponentiation: (base^exp) % mod
    public static long pow(long base, long exp, int MOD) {
        long res = 1;
        base %= MOD;
        while (exp > 0) {
            if ((exp & 1) == 1) res = (res * base) % MOD;
            base = (base * base) % MOD;
            exp >>= 1;
        }
        return res;
    }

    // Modular Inverse using Fermat's Little Theorem (only if MOD is prime)
    public static long modInverse(long n, int MOD) {
        return pow(n, MOD - 2, MOD);
    }
}

//template 3: dijkstra
class Dijkstra {
    // Returns the shortest distance from src to dest, or -1 if unreachable
    // adj structure: adj.get(u) contains {v, weight}
    public static long dijkstra(int n, List<List<int[]>> adj, int src, int dest) {
        long[] dist = new long[n + 1]; // Use n if 0-indexed, n+1 if 1-indexed
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[src] = 0;

        PriorityQueue<long[]> pq = new PriorityQueue<>(
            (a, b) -> Long.compare(a[1], b[1])
        ); //{node, current_dist}
        
        pq.add(new long[]{src, 0});

        while (!pq.isEmpty()) {
            long[] curr = pq.poll();
            int u = (int) curr[0];
            long d = curr[1];

            if (d > dist[u]) continue; //skip unnecessary entries
            if (u == dest) return d; //early exit

            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                int weight = edge[1];

                if (dist[u] + weight < dist[v]) { //relaxation
                    dist[v] = dist[u] + weight;
                    pq.add(new long[]{v, dist[v]});
                }
            }
        }
        return dist[dest] == Long.MAX_VALUE ? -1 : dist[dest];
    }
}

//template 4: Strings
class KMP {
    // Returns a list of all starting indices of pattern in text
    public static List<Integer> contains(String pattern, String text) {
        List<Integer> matches = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m == 0) return matches; // Edge case
        
        int[] lps = lps(pattern);
        
        int i = 0; // index for text
        int j = 0; // index for pattern
        
        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                j++;
                i++;
            }
            
            if (j == m) {
                matches.add(i - j); // Match found at index (i - j)
                j = lps[j - 1];     // Fall back to previous prefix
            } 
            else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                // Mismatch after j matches
                if (j != 0)
                    j = lps[j - 1]; // Fall back to previous prefix
                else
                    i++;
            }
        }
        return matches;
    }

    // Precomputes the Longest Prefix Suffix (LPS) array
    private static int[] lps(String s) {
        int n = s.length(), lps[] = new int[n];
        for(int i=1; i<n; ++i)
        {
            int prev_idx = lps[i - 1];
            while(prev_idx > 0 && s.charAt(i) != s.charAt(prev_idx))
                prev_idx = lps[prev_idx - 1];
            lps[i] = prev_idx + (s.charAt(i) == s.charAt(prev_idx) ? 1 : 0);
        }
        return lps;
    }
}