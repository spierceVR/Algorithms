/**
 * UnionFind/Disjoint Set data structure implementation. This code was inspired by the union find
 * implementation found in 'Algorithms Fourth Edition' by Robert Sedgewick and Kevin Wayne.
 *
 * @author William Fiset, william.alexandre.fiset@gmail.com
 */
package com.williamfiset.algorithms.datastructures.unionfind;

public class UnionFind {

  // The number of elements in this union find
  private int size;

  // Used to track the size of each of the component
  private int[] sz;

  // id[i] points to the parent of i, if id[i] = i then i is a root node
  private int[] id;

  // Tracks the number of components in the union find
  private int numComponents;

  public UnionFind(int size) {

    if (size <= 0) throw new IllegalArgumentException("Size <= 0 is not allowed");

    this.size = numComponents = size; //same because at the beginning all elements are root elements and are their own components
    sz = new int[size];
    id = new int[size];

    // setup the ID array (and component size array)
    for (int i = 0; i < size; i++) {
      id[i] = i; // Link to itself (self root)
      sz[i] = 1; // Each component is originally of size one
    }
  }

  // Find which component/set 'p' belongs to, takes amortized constant time.
  public int find(int p) {

    // Find the root of the component/set
    int root = p;
    while (root != id[root]) root = id[root];
    // after root is found, p still points to the first element in the chain to the root
    
    // Compress the path leading back to the root.
    // Doing this operation is called "path compression"
    // and is what gives us amortized time complexity.
    while (p != root) {
      int next = id[p]; // save p's next pointer
      id[p] = root; // change p's next pointer to the root, skipping the rest of the chain
      p = next; // make p point to the saved next pointer (next element in the chain)
    }

    return root; // return the root node
  }

  // This is an alternative recursive formulation for the find method
  // public int find(int p) {
  //   if (p == id[p]) return p;
  //   return id[p] = find(id[p]);
  // }

  // Return whether or not the elements 'p' and
  // 'q' are in the same components/set.
  public boolean connected(int p, int q) {
    return find(p) == find(q); // roots are same if the elements are in the same component
  }

  // Return the size of the components/set 'p' belongs to
  public int componentSize(int p) {
    return sz[find(p)];
  }

  // Return the number of elements in this UnionFind/Disjoint set
  public int size() {
    return size;
  }

  // Returns the number of remaining components/sets
  public int components() {
    return numComponents;
  }

  // Unify the components/sets containing elements 'p' and 'q'
  public void unify(int p, int q) {

    // These elements are already in the same group!
    if (connected(p, q)) return;

    int root1 = find(p);
    int root2 = find(q);

    // Merge smaller component/set into the larger one. (By convention)
    if (sz[root1] < sz[root2]) {
      sz[root2] += sz[root1]; // update size of unified component 
      id[root1] = root2; // parent of root 1 is now root2
      sz[root1] = 0; // size of root1 is now zero because it's not a root anymore, just an element in the unified component
    } else {
      sz[root1] += sz[root2]; // same as if block but in the case where root1 sz > root2 sz
      id[root2] = root1;
      sz[root2] = 0;
    }

    // Since the roots found are different we know that the
    // number of components/sets has decreased by one
    numComponents--;
  }
}
