# K-D Trees

This project was completed in **Fall 2022** for the **COS226 Algorithms and Data Structures** course. More information can be found here:

https://www.cs.princeton.edu/courses/archive/spring25/cos226/assignments/kdtree/specification.php

This program implements a mutable symbol-table data type named KdTreeST.java, which organizes two-dimensional points using a 2d-tree (a generalization of a binary search tree for 2D keys). The 2d-tree enables efficient operations such as:

* Range search: Find all points contained within a given query rectangle.

* Nearest-neighbor search: Find the closest point to a given query point.

## How to Run the Code

### 1. Compile the Program
Visualize the K-D Tree
```bash
javac-algs4 KdTreeVisualizer.java
java-algs4 KdTreeVisualizer input5.txt
```
Visualize the Range Search
```bash
javac-algs4 RangeSearchVisualizer.java
java-algs4 RangeSearchVisualizer input5.txt
```
Visualize the Nearest Neighbor Search
```bash
javac-algs4 NearestNeighborVisualizer.java
java-algs4 NearestNeighborVisualizer input5.txt
```

