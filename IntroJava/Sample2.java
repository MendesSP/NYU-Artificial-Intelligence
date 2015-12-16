public class Sample 2
{
	public static void main(String[] args) {
        int M = Integer.parseInt(args[0]);    // choose this many elements
        int N = Integer.parseInt(args[1]);    // from 0, 1, ..., N-1

        // create permutation 0, 1, ..., N-1
        int[] perm = new int[N];
        for (int i = 0; i < N; i++)
            perm[i] = i;

        // create random sample in perm[0], perm[1], ..., perm[M-1]
        for (int i = 0; i < M; i++)  {

            // random integer between i and N-1
            int r = i + (int) (Math.random() * (N-i));

            // swap elements at indices i and r
            int t = perm[r];
            perm[r] = perm[i];
            perm[i] = t;
        }

        // print results
        for (int i = 0; i < M; i++)
            System.out.print(perm[i] + " ");
        System.out.println();
    }
}