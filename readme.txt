Programming Assignment 5: K-d Trees


/* *****************************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 **************************************************************************** */

We used a private Node class that created a node with both left and right
references to their children/ subtrees. We also tracked the a boolean called
"level" for each node (False = even row / vertical line) (True = odd row/
horizontal line). We also tracked the rectangle associated with each node.

/* *****************************************************************************
 *  Describe your method for range search in a k-d tree.
 **************************************************************************** */

Our range search calls a recursive helper method that checks if the query
rectangle is within the current nodes rectangle, if it is contained it recurses
for both sides and returns once the current nodes rectangle no longer contains
the query rectangle.

/* *****************************************************************************
 *  Describe your method for nearest neighbor search in a k-d tree.
 **************************************************************************** */

Our nearest neighbor search calls a recursive helper method that tracks and
updates a champDist and associatedchampPoint. The method determines if the
query point is to the left or the right of a node and recursively searchs the
correct side and then the other side. Through each recursion the current node
rectangle is checked to make sure the distance from its rectangle to the point
is not greater than the current champion distance (this would prune it if true)
after that it checks the point for a new champion distance and continues the
search until a null pointer is found and returns champPoint.

/* *****************************************************************************
 *  How many nearest-neighbor calculations can your PointST implementation
 *  perform per second for input1M.txt (1 million points), where the query
 *  points are random points in the unit square?
 *
 *  Fill in the table below, using one digit after the decimal point
 *  for each entry. Use at least 1 second of CPU time. Do not use -Xint.
 *  (Do not count the time to read the points or to build the 2d-tree.)
 *
 *  Repeat the same question but with your KdTreeST implementation.
 *
 **************************************************************************** */


                 # calls to         /   CPU time     =   # calls to nearest()
                 client nearest()       (seconds)        per second
                ------------------------------------------------------
PointST:            1000                69.4                14.4

KdTreeST:           1000                1.1                 951.5

Note: more calls per second indicates better performance.


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */


/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */

Sunday Lab TA

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

We ran into a problem with our first implementation of nearest. We thought that
since we had the x and y values for every node we could just keep track of those
values and instead check the point perpendicular to the query point but still on
the current nodes "line" and that would effectively be the nearest search
without the rectangle. For the most part our results were correct with some
minor errors, including the amount of nodes explored. Our pruning did cut
portions of the search but not enough in comparison with tiger file. Therefore
we had to re-implement nearest as well as add the creation of a rectangle to our
put() function.

/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */

We followed the protocol as described on each page. We worked together to solve
most minor problems, however with major issues it was usually one of us that had
an "Aha!" moment. For example Hannah was especially good at figuring out the
helper functions and recursive calls and Lauren was good at visualizing the
steps and fundamentals of the assignment. Together we worked through our
problems with our 2nd nearest implementation (mostly our champDist and
champPoint were being reset each recurse) by using print statements and unit
testing to see what values the code was getting for certain points.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on  how helpful the class meeting was and on how much you learned
 * from doing the assignment, and whether you enjoyed doing it.
 **************************************************************************** */

I felt like I understood the concept of the tree and what each method was
supposed to do, the only problem was to translate that to code! -- Lauren

Our initial implementation worked with the visualizers, but broke on TigerFile
because we didn't use recursion in the way that was apparently expected, but
not specified anywhere. It would be helpful to highlight that we must use
the pseudocode from precept and lecture, so that future students don't try
to implement a unique solution that will break as ours did.  -- Hannah
