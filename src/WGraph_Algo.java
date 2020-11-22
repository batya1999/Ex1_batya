package ex1.src;

import java.io.*;
import java.util.*;

/**
 * properties of graph
 */
public class WGraph_Algo implements weighted_graph_algorithms, java.io.Serializable {
    private WGraph_DS graph;//declaring a new graph
    private Queue<node_info> myQ;

    /**
     * empty constructor
     */
    public WGraph_Algo() {//empty constructor
        graph = new WGraph_DS();//creating actual new graph
    }

    /**
     * init- tells the algorithm which sub-graph to focus on
     *
     * @param g the graph to be focused on
     */
    @Override
    public void init(weighted_graph g) {
        this.graph = (WGraph_DS) g;
    }

    /**
     * returns graph
     *
     * @return graph
     */
    @Override
    public weighted_graph getGraph() {
        return this.graph;
    }

    /**
     * deep copy constructor for a graph
     *
     * @return clone of the specific graph
     */
    @Override
    public weighted_graph copy() {
        return new WGraph_DS(this.graph);
    }

    /**
     * checks if the graph is connected using BFS algorithm
     *
     * @return true if the graph is connected, else returns false
     */
    @Override
    public boolean isConnected() {
        if ((graph.nodeSize() == 0) || (graph.nodeSize() == 1)) return true;
        myQ = new LinkedList<node_info>();
        for (node_info t : graph.getV())
            t.setTag(0);//reset all the nodes as '0'
        for (node_info h : graph.getV()) {
            myQ.add(h);//pushing the first element
            h.setTag(1.0);
            break;//now i the first node to start with , never mind which one
        }
        node_info keep;
        while (!myQ.isEmpty()) {
            keep = myQ.poll();
            for (node_info r : graph.getV(keep.getKey())) {
                if (r.getTag() == 0) {
                    r.setTag(1.0);
                    myQ.add(r);
                }
            }
            if (myQ.peek() == null) break;
        }
        boolean b = true;
        for (node_info t : graph.getV())
            b &= t.getTag() == 1;
        return b;
    }

    /**
     * returns the shortest distance between two nodes using dijkastra algorithm
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return the distance between the two given nodes
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (graph == null || graph.getNode(src) == null || graph.getNode(dest) == null)
            return -1;
        if(graph.nodeSize() == 1) return  0;
        List<node_info> l;
        l = shortestPath(src, dest);
//        if(l.get(l.size()-1)!=null) {
            int x = l.get(l.size() - 1).getKey();
            if (graph.getNodeBig(x) != null)
                return graph.getNodeBig(x).getDis();
//        }
        return -1;
    }

    /**
     * returns the list of the shortest path between two nodes using dijkastra algorithm
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return list of the path between start and end nodes
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if (graph == null || (graph.nodeSize() == 1 && src!=dest) || graph.getNode(src) == null || graph.getNode(dest) == null)
            return null;
        WGraph_DS.NodeInfo keep;
        HashMap<Integer, List<node_info>> shortWay = new HashMap<Integer, List<node_info>>();
        for (node_info i : graph.getV())
            i.setTag(0);//to know who we already visited in the future
        graph.getNode(src).setTag(1);//the other tracked nodes will be tagged in minDIS function
        graph.getNodeBig(src).setDis(0.0);//the distance from itself is 0
        List<node_info> l = new LinkedList<>();
        l.add(graph.getNode(src));
        if(src==dest)
            return l;
        shortWay.put(src, l);
        Stack<node_info> st = new Stack<node_info>();
        PriorityQueue<WGraph_DS.NodeInfo> pQ = new PriorityQueue<>(new Comparator<WGraph_DS.NodeInfo>() {
            @Override
            public int compare(WGraph_DS.NodeInfo a, WGraph_DS.NodeInfo b) {
                return -Double.compare(b.getDis(), a.getDis());
            }
        });
        pQ.add(graph.getNodeBig(src));
        while (!pQ.isEmpty()) {
            keep = pQ.poll();
            if (graph.getNodeBig(keep.getKey()).getNi() != null) {
                for (WGraph_DS.NodeInfo j : graph.getNodeBig(keep.getKey()).getNi()) {
                    if (graph.getNodeBig(j.getKey()).getTag() == 0) {
                        if (graph.getEdge(j.getKey(), keep.getKey()) + keep.getDis() < j.getDis()) {
                            j.setDis(graph.getEdge(j.getKey(), keep.getKey()) + keep.getDis());//update the smallest distance
                            List<node_info> s = new LinkedList<node_info>();
                            //node_info keep2=keep;
                            Iterator<node_info> it = shortWay.get((keep.getKey())).iterator();
                            while (it.hasNext()) {
                                s.add(it.next());
                            }
                            //deep copy for the list
                            s.add(j);
                            if (pQ.contains(j)) {
                                pQ.remove(j);
                            }
                            pQ.add(j);
                            shortWay.put(j.getKey(), s);
                        }
                        if (keep.getKey() == graph.getNode(dest).getKey()) {
                            pQ.clear();//to end the while loop
                            break;//to end the for loop
                        }
                    }
                }
            }
            keep.setTag(1);

        }
        if (!shortWay.containsKey(dest))
            return null;
        return shortWay.get(dest);
    }

    /**
     * checks if two graphs are equals
     *
     * @param o the given graph to compare with
     * @return true is equals, else returns false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_Algo that = (WGraph_Algo) o;
        return Objects.equals(graph, that.graph) &&
                Objects.equals(myQ, that.myQ);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph, myQ);
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        WGraph_Algo n = this;
        try {
            FileOutputStream fille = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fille);
            out.writeObject(graph);
            out.close();
            fille.close();


        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            FileInputStream fille = new FileInputStream(file);
            ObjectInputStream input = new ObjectInputStream(fille);
            weighted_graph newGr = (weighted_graph) input.readObject();
            fille.close();
            input.close();
        } catch (IOException | ClassNotFoundException e) {
            return false;

        }
        return true;
    }
}
