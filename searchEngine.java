import java.util.*;
import java.io.*;

// This class implements a google-like search engine
public class searchEngine {

    public HashMap<String, LinkedList<String> > wordIndex;                  // this will contain a set of pairs (String, LinkedList of Strings)	
    public directedGraph internet;             // this is our internet graph
    
    
    
    // Constructor initializes everything to empty data structures
    // It also sets the location of the internet files
    searchEngine() {
	// Below is the directory that contains all the internet files
	htmlParsing.internetFilesLocation = "internetFiles";
	wordIndex = new HashMap<String, LinkedList<String> > ();		
	internet = new directedGraph();				
    } // end of constructor2014
    
    
    // Returns a String description of a searchEngine
    public String toString () {
	return "wordIndex:\n" + wordIndex + "\ninternet:\n" + internet;
    }
    
    
    // This does a graph traversal of the internet, starting at the given url.
    // For each new vertex seen, it updates the wordIndex, the internet graph,
    // and the set of visited vertices.
    
    void traverseInternet(String url) throws Exception {
	/* WRITE SOME CODE HERE */
	if (htmlParsing.queriedURL_links.containsKey(url)){
	 		return;
	}
	LinkedList<String>  site = htmlParsing.getLinks(url);
	Iterator<String> i = site.iterator();
	LinkedList<String>  siteContrent = htmlParsing.getContent(url);

	this.wordIndex.put(url, siteContrent);
	while(i.hasNext()){
		
		String s = i.next();
		internet.addVertex(s);
		internet.addEdge(url, s);
		internet.setPageRank(s ,1);
		internet.visited.put(url, true);
		traverseInternet(s);

	}

	// for (int x=0; x < site.size() ; x++ ) {
	// 	if (htmlParsing.queriedURL_links.containsKey(url)){
	// 		continue;
	// 	}
	// 	System.out.println("url:"+url+"\n"+"next :"+ htmlParsing.getLinks(url));
	// 	internet.addEdge(url, htmlParsing.getLinks(url));
	// 	traverseInternet(site.get(x));
		
	// }
	//directedGraph();
	// while((htmlParsing.getLinks(url)){

	// }
	/* Hints

	   0) This should take about 50-70 lines of code (or less)
	   1) To parse the content of the url, call
	   htmlParsing.getContent(url), which returns a LinkedList of Strings 
	   containing all the words at the given url. Also call htmlParsing.getLinks(url).
	   and assign their results to a LinkedList of Strings.
	   2) To iterate over all elements of a LinkedList, use an Iterator,
	   as described in the text of the assignment
	   3) Refer to the description of the LinkedList methods at
	   http://docs.oracle.com/javase/6/docs/api/ .
	   You will most likely need to use the methods contains(String s), 
	   addLast(String s), iterator()
	   4) Refer to the description of the HashMap methods at
	   http://docs.oracle.com/javase/6/docs/api/ .
	   You will most likely need to use the methods containsKey(String s), 
	   get(String s), put(String s, LinkedList l).  
	*/
	
	
	
    } // end of traverseInternet
    
    
    /* This computes the pageRanks for every vertex in the internet graph.
       It will only be called after the internet graph has been constructed using 
       traverseInternet.
       Use the iterative procedure described in the text of the assignment to
       compute the pageRanks for every vertices in the graph. 
       
       This method will probably fit in about 30 lines.
    */
    void computePageRanks() {
	/* WRITE YOUR CODE HERE */
	for (int x =0 ; x<100 ;x++ ) {
		LinkedList<String> vert = new LinkedList<String>();
		vert = this.internet.getVertices();
		Iterator<String> i = vert.iterator();
		
			
		
		while(i.hasNext()){

		String s = i.next();
		//System.out.println(s);
		double pageRank = this.internet.getPageRank(s);
		double sumation =0.0;
		LinkedList<String> neighbours = internet.getEdgesInto(s);
		Iterator<String> n = neighbours.iterator(); 

			while(n.hasNext()){
				String next = n.next();
				double  PRw = internet.getPageRank(next);
				double  Cw = internet.getOutDegree(next);
				if (Cw ==0) {
					Cw =1;
				}
				sumation = sumation + (PRw/Cw);

				// System.out.println(sumation);
				//System.out.println((PRw/Cw) + "(PRw/Cw)");
				

			}
		// System.out.println("RANK: " +(pageRank+(0.5*sumation)));
		this.internet.setPageRank(s,pageRank+(0.5*sumation));
			
		}
	}
    } // end of computePageRanks
    
	
    /* Returns the URL of the page with the high page-rank containing the query word
       Returns the String "" if no web site contains the query.
       This method can only be called after the computePageRanks method has been executed.
       Start by obtaining the list of URLs containing the query word. Then return the URL 
       with the highest pageRank.
       This method should take about 25 lines of code.
    */
    String getBestURL(String query) {
    	LinkedList<String> hasword = new LinkedList<String>() ;
	Map<String, LinkedList<String> > map = this.wordIndex;
	Iterator<Map.Entry<String, LinkedList<String>> > entries = map.entrySet().iterator();
	for (Map.Entry<String, LinkedList<String> > entry : map.entrySet()) {
		if (entry.getValue().indexOf(query)>=0) {
			hasword.add(entry.getKey());
		}
	}
	Iterator<String> n = hasword.iterator(); 
	String top ="";
	double toprank=0;
	while (n.hasNext()) {
		String current = n.next();
		if (internet.getPageRank(current) >toprank) {
			toprank =internet.getPageRank(current);
			top =current;
		}
	}


	return top; // remove this
    } // end of getBestURL
    
    
	
    public static void main(String args[]) throws Exception{		
	searchEngine mySearchEngine = new searchEngine();
	// to debug your program, start with.
	//	mySearchEngine.traverseInternet("http://www.cs.mcgill.ca/~blanchem/250/a.html");

	// When your program is working on the small example, move on to
	mySearchEngine.traverseInternet("http://www.cs.mcgill.ca/~blanchem/250/a.html");
	mySearchEngine.computePageRanks();

	// this is just for debugging purposes. REMOVE THIS BEFORE SUBMITTING

	//System.out.println(mySearchEngine);
	
	
	BufferedReader stndin = new BufferedReader(new InputStreamReader(System.in));
	String query;
	do {
	    System.out.print("Enter query: ");
	    query = stndin.readLine();
	    if ( query != null && query.length() > 0 ) {
		System.out.println("Best site = " + mySearchEngine.getBestURL(query));
	    }
	} while (query!=null && query.length()>0);				
    } // end of main
}
