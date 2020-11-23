written by Batya Ashkenazi
EMAIL ADDRESS: batya.ashkenazi@gmail.com

my graph implemention uses 3 classes :
-NodeInfo class- to define the properties of a vertex in an undirected weighted graph

-WGraph_DS class- to define the properties and structure of a graph- using HashMap: to store the whole data easily

-WGraph_Algo class- to define the algorithms and operations we can do on a graph.

my project allows to have a huge graph- of 10^6 vertices.

#-- NodeInfo class :This class represents a node with all its properties as: information, key (id), tag situation, distance between all af the nodes to the specific node, and neighbors list (a list of all the connected vertices to a specific node).I used hashmap in order to store and access the information easily.

#-- WGraph_DS class :This class represents an undirectional weighted graph. we can do operation as: check if two nodes share an edge between them,get the weight of the required edge,change the weight of a given edge, clone a graph using deep copy, add node to the graph, remove as well, connect between two nodes, get a list of node's neighbors and their relationship(weight) with a specific node, and more.

#-- WGraph_Algo class :This class represents the operations and algorithms we can do on an undirected weighted graph-
 as checking how many vertices it contains, how many edges, which part of the graph we want to focus on each time, 
 how many changes have we done in the graph so far, (as changing edge's weight, removing node, removing edge and more updates ),
  check if the graph is connected or not (using the BFS algorithm's idea), 
  check what is the shortest distance between two nodes and even save the actuall shortest path between them in a list of nodes
 ( using DIJKASTRA algorithm's idea;
first of all we define all of the distances from a specific node with infinity,
 and the current node gets '0' distance- from itself.
  each time we visit  a node -we check which neighbor has the lowest weight with the 'father',
  node- to continue with, each iteration we update the lower distance value from the source to the node we visit 
  by a shorter path+the edge's weight  to reach it - if we find one. 
in that way each node keeps the shortest distance from all vertices to it. 
in addition each node has it's own shortest path from the src node in a detailed and updated list (by the fact that each node keeps it's father).
