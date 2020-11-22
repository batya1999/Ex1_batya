package ex1.src;

import java.util.*;

public class WGraph_DS implements weighted_graph, java.io.Serializable {
    private HashMap<Integer, NodeInfo> myG2;//declaring a graph
    private int mc, Ec;//counter for changed

    /**
     * undirected weighted graph properties
     */
    //empty constructor
    public WGraph_DS() {
        myG2 = new HashMap<Integer, NodeInfo>();//creating the new graph
        this.mc = 0;
        this.Ec = 0;
    }

    /**
     * compare between 2 graphs objects
     * @param o the graph to be compared
     * @return true if equals , else returns false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return Ec == wGraph_ds.Ec &&
                Objects.equals(myG2, wGraph_ds.myG2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myG2, mc, Ec);
    }

    /**
     * returns a specific node by it's unique key
     * @param key - the node_id
     * @return the required node- if not exists- null
     */
    @Override
    public node_info getNode(int key) {
        if (myG2.containsKey(key))
            return myG2.get(key);//returns the specific node from the map
        return null;
    }

    /**
     * returns HashMap to access the nodes easily
     * @return myG2- the required hashmap
     */
    public HashMap<Integer, NodeInfo> getMAP() {
        return myG2;
    }

    /**
     * help function to get NodeInfo
     * @param key the key associated with the node to be returned
     * @return NodeInfo
     */
    public NodeInfo getNodeBig(int key) {
        if (myG2.containsKey(key))
            return myG2.get(key);//returns the specific node from the map
        return null;
    }

    /**
     * returns a list of all of the nodes in the graph
     * @return list of the nodes in the graph
     */
    @Override
    public Collection<node_info> getV() {
        return new LinkedList<node_info>(myG2.values());
    }

    /**
     * checks if two nodes share an edge between them
     * @param node1 the first vertex to be checked
     * @param node2 the second vertex to be checked
     * @return true if node1-node2 share an edge- else returns false
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if (node1 != node2 && !myG2.isEmpty() && myG2.containsKey(node1) && myG2.containsKey(node2)) {
            if (myG2.get(node1).getMap().get(myG2.get(node2)) != null && myG2.get(node2).getMap().get(myG2.get(node1)) != null)
                return true;

        }
        return false;
    }

    /**
     * returns the weight of an edge
     * @param node1 one side of the edge
     * @param node2 second side of the edge
     * @return the weight node1-node2 share
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {
            return myG2.get(node1).getMap().get(myG2.get(node2));
        }
        return -1;
    }

    /**
     * adds node to the graph
     * @param key the key(id) associated with the node to be added
     */
    @Override
    public void addNode(int key) {
        if (!myG2.containsKey(key)) {
            NodeInfo n = new NodeInfo(key);
            //node_info n2 = new NodeInfo(key);
            myG2.put(key, n);
            mc++;
        }
    }

    /**
     * connects between two given nodes
     * @param node1 first node to connect
     * @param node2 second node to connect
     * @param w the weight that the new edge gets
     */
    @Override

    public void connect(int node1, int node2, double w) {
        if (hasEdge(node1, node2) && w != getEdge(node1, node2) && w >= 0) {
            myG2.get(node1).getMap().put(myG2.get(node2), w);
            myG2.get(node2).getMap().put(myG2.get(node1), w);
            mc++;
        } else if (node1 != node2 && !hasEdge(node1, node2) && myG2.containsKey(node1) && myG2.containsKey(node2) && w >= 0) {
            myG2.get(node1).getMap().put(myG2.get(node2), w);
            myG2.get(node2).getMap().put(myG2.get(node1), w);
            mc++;
            Ec++;
        }
    }

    /**
     * returns a list of the neighbors of a specific node (all of the connected nodes)
     * @param node_id the key of the node we want to get it's neighbors list of
     * @return neighbors list
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        Collection<node_info> n = new ArrayList<>();
        for (NodeInfo i : myG2.get(node_id).getNi()) {
            n.add((node_info) i);
        }
        return n;
    }

    /**
     * removes given node from a graph
     * @param key the node's key(id) to be removed
     * @return the removed node if was exist, else returns null
     */
    @Override
    public node_info removeNode(int key) {
        if (myG2.containsKey(key)) {
            for (NodeInfo i : myG2.get(key).getNi()) {
                //removeEdge(key,i.getKey());
                if (hasEdge(key, i.getKey())) {
                    myG2.get(key).getNi().remove(i.getKey());
                    myG2.get(i.getKey()).getNi().remove(key);
                    Ec--;//counter for the edges

                }
            }
            node_info n = new NodeInfo(key);
            myG2.remove(key);
            //myG.remove(key);
            mc++;
            return n;
        }
        return null;
    }
     public WGraph_DS (WGraph_DS other){
        myG2 = new HashMap<>();
         for (node_info i:other.getV()) {
             NodeInfo n= new NodeInfo(i);
             this.myG2.put(n.getKey(),n);
         }
         for(node_info j: other.getV()){
             int jkey = j.getKey();
             for (node_info i: other.getV(jkey)) {
                 this.connect(i.getKey(),jkey,other.getEdge(i.getKey(),jkey));
             }
         }
     }
    /**
     * removes edge between two nodes
     * @param node1 one side of the edge
     * @param node2 second side of the edge
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (!myG2.isEmpty() && node1 != node2 && hasEdge(node1, node2)) {
            myG2.get(node1).removeNode(myG2.get(node2));
            myG2.get(node2).removeNode(myG2.get(node1));
            Ec--;//counter for the edges
            mc++;
        }
    }

    /**
     * returns the amount of the nodes that the graph contains
     * @return the size of the nodes
     */
    @Override
    public int nodeSize() {
        if (myG2 != null && !myG2.isEmpty()) {
            return myG2.size();
        }
        return 0;
    }

    /**
     * returns the amount of the edges that the graph contains
     * @return size of the edges
     */
    @Override
    public int edgeSize() {
        return Ec;
    }

    /**
     * returns the amount of the changes that happened in the specific graph
     * @return mc the amount of changes
     */
    @Override
    public int getMC() {
        return mc;
    }

    /**
     * this class represents a node in the graph using hashmap to keep the nodes and their distances
     * from a specific node
     */
    public class NodeInfo implements node_info,java.io.Serializable {
        public HashMap<NodeInfo, Double> map;
        private int key;
        private double tag;
        private double dis;
        private String info;

        /**
         * empty constructor
         */
        public NodeInfo() {
            //empty constructor
            map = new HashMap<NodeInfo, Double>();//neighbors list
            this.key = key++;
            this.tag = 0;
            this.info = " ";
            this.dis = Double.MAX_VALUE;
        }

        /**
         * copy constructor for a node
         * @param n the node to be copied
         */
        public NodeInfo(node_info n) {
            this.map=new HashMap<>();
            this.key=n.getKey();
            this.tag=n.getTag();
            this.info=n.getInfo();

        }

        /**
         * constructor for a node by it's key
         * @param key the node's key to be created
         */
        public NodeInfo(int key) {
            map = new HashMap<NodeInfo, Double>();//neighbors list
            this.key = key;
            this.tag = 0;
            this.info = " ";
            this.dis = Double.MAX_VALUE;
        }

        /**
         * returns the distance from a specific node
         * @return the shortest distance from a specific node
         */
        public double getDis() {
            return this.dis;
        }

        /**
         * sets the distance of a node from the specific node
         * @param d the weight to update
         */
        public void setDis(double d) {
            this.dis = d;
        }

        /**
         * returns the key
         * @return key
         */
        @Override
        public int getKey() {
            return this.key;
        }

        /**
         *
         * returns node's information
         * @return info
         */
        @Override
        public String getInfo() {
            return this.info;
        }

        /**
         * sets node's info
         * @param s the info to be updated
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         * returns the tag of a node
         * @return the tag
         */
        @Override
        public double getTag() {
            return this.tag;
        }

        /**
         * sets the tag of a node
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        /**
         * returns the map associated with that node
         * @return map (of the nodes)
         */
        public HashMap<NodeInfo, Double> getMap() {
            return this.map;
        }

        /**
         * adds a node to the neighbors list of a specific node
         * @param t the node to be added
         * @param we the wight the node share with the current node
         */
        public void addNi(NodeInfo t, double we) {
            if (t != null || this != t || !myG2.containsKey(t.getKey()))
                map.put(t, we);
        }

        /**
         * returns neighbors list of the node
         * @return neighbors list of the node
         */
        public Collection<NodeInfo> getNi() {
            return map.keySet();
        }

        /**
         * removes node from the node's list
         * @param node to be removed
         */
        public void removeNode(NodeInfo node) {
            if (null == node)
                return;
            map.remove(node);
            mc++;
        }

        /**
         * help function to check if two nodes equals
         * @param o the node to be compared
         * @return true if equals , else returns false
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeInfo nodeInfo = (NodeInfo) o;
            return key == nodeInfo.key;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }

}