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
    // Array printing methods (Space Separated)
    static void p(int[] a) { for(int i : a) p(i); }
    static void p(long[] a) { for(long i : a) p(i); }
    static void p(String[] a) { for(String s : a) p(s); }
    static void p(double[] a) { for(double d : a) p(d); }
    static void p(char[] a) { for(char c : a) p(c); }
    
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
    
    // ==========================================
    // 1. STATIC UTILITIES (Call anywhere)
    // ==========================================
    
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
        // Divide first to prevent overflow
        return (a / gcd(a, b)) * b; 
    }

    // Fast Exponentiation: base^exp
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

    // Modular Inverse using Fermat's Little Theorem (requires MOD to be prime)
    public static long modInverse(long n, int MOD) {
        return pow(n, MOD - 2, MOD);
    }

    // ==========================================
    // 2. COMBINATORICS (Requires Instantiation)
    // ==========================================
    
    long[] fact;
    long[] invFact;
    int MOD;

    public MathUtils(int maxN, int mod) {
        this.MOD = mod;
        fact = new long[maxN + 1];
        invFact = new long[maxN + 1];

        fact[0] = 1;
        invFact[0] = 1;

        // Precompute factorials
        for (int i = 1; i <= maxN; i++) {
            fact[i] = (fact[i - 1] * i) % MOD;
        }

        // Precompute inverse factorials using the static pow method
        invFact[maxN] = pow(fact[maxN], MOD - 2, MOD);
        for (int i = maxN - 1; i >= 1; i--) {
            invFact[i] = (invFact[i + 1] * (i + 1)) % MOD;
        }
    }

    public long nPr(int n, int r) {
        if (r < 0 || r > n) return 0;
        return (fact[n] * invFact[n - r]) % MOD;
    }

    public long nCr(int n, int r) {
        if (r < 0 || r > n) return 0;
        long numerator = fact[n];
        long denominator = (invFact[r] * invFact[n - r]) % MOD;
        return (numerator * denominator) % MOD;
    }
}

//template 3: Strings
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


//template 4: Dp
class DP {

    // ==========================================
    // 1. LONGEST INCREASING SUBSEQUENCE (LIS)
    // Time: O(N log N) | Space: O(N)
    // ==========================================
    public static int lis(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;
        
        for (int x : nums) {
            int left = 0, right = size;
            
            // Binary Search to find the insertion point (lower bound)
            while (left != right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < x) {  // Change to '<=' if strictly increasing is not required
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            // Overwrite the element to keep the sequence elements as small as possible
            tails[left] = x;
            if (left == size) {
                size++;
            }
        }
        return size;
    }

    // ==========================================
    // 2. LONGEST COMMON SUBSEQUENCE (LCS)
    // Time: O(N * M) | Space: O(min(N, M))
    // ==========================================
    public static int lcs(String text1, String text2) {
        // Force text2 to be the shorter string to minimize our 1D array size
        if (text1.length() < text2.length()) {
            return lcs(text2, text1);
        }
        
        int n = text1.length();
        int m = text2.length();
        int[] dp = new int[m + 1];
        
        for (int i = 1; i <= n; i++) {
            int prev = 0; // Tracks the diagonal dp[i-1][j-1] from the traditional 2D grid
            for (int j = 1; j <= m; j++) {
                int temp = dp[j];
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[j] = prev + 1;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
                prev = temp;
            }
        }
        return dp[m];
    }
}