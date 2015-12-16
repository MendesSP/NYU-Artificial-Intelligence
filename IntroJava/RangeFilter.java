public class RangeFilter
{
	public static void main(String[] args)
	{
		// read in two command-line arguments
        int lo = Integer.parseInt(args[0]);
        int hi = Integer.parseInt(args[1]);

        // repeat as long as there's more input to read in
        while (!StdIn.isEmpty()) {

            // read in the next integer
            int t = StdIn.readInt();

            // print out the given integer if it's between lo and hi
            if (t >= lo && t <= hi) {
                StdOut.print(t + " ");
            }
        }
        StdOut.println();
	}
}