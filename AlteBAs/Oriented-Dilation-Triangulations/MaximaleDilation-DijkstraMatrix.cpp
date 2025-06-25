#include <iostream>
#include <cmath>
#include <algorithm>
#include <limits>
#include <cfloat>
#include "header.h"

void punkt_ausgeben(Punkt punkt)
{
    std::cout << "(" << punkt.x << "," << punkt.y << ")";
}

double distanz_punkte(Punkt p1, Punkt p2)
{
    double dx = static_cast<double>(p2.x - p1.x);
    double dy = static_cast<double>(p2.y - p1.y);
    double s = sqrt(dx*dx + dy*dy);
    return s;
}

// A utility function to find the vertex with minimum distance value, from
// the set of vertices not yet included in shortest path tree
int minDistance(std::vector<double> dist, std::vector<bool> sptSet)
{
	// Initialize min value
	double min = DBL_MAX; int min_index;

	for (int v = 0; v < dist.size(); v++)
		if (sptSet[v] == false && dist[v] <= min)
			min = dist[v], min_index = v;

	return min_index;
}

// A utility function to print the constructed distance array
void printSolution(std::vector<double> dist, int n)
{
	printf("Vertex Distance from Source\n");
	for (int i = 0; i < dist.size(); i++)
		std::cout << i << "    " << dist[i] << '\n';
}

// Function that implements Dijkstra's single source shortest path algorithm
// for a graph represented using adjacency matrix representation
std::vector<double> dijkstra(std::vector<std::vector<double>> graph, int src)
{
    std::vector<double> dist(graph.size());
    
	// int dist[V]; // The output array. dist[i] will hold the shortest
	// distance from src to i

	std::vector<bool> sptSet(graph.size());

	//bool sptSet[V]; // sptSet[i] will be true if vertex i is included in shortest
	// path tree or shortest distance from src to i is finalized

	// Initialize all distances as INFINITE and stpSet[] as false
	for (int i = 0; i < graph.size(); i++)
		dist[i] = DBL_MAX, sptSet[i] = false;

	// Distance of source vertex from itself is always 0
	dist[src] = 0;

	// Find shortest path for all vertices
	for (int count = 0; count < graph.size() - 1; count++) {
		// Pick the minimum distance vertex from the set of vertices not
		// yet processed. u is always equal to src in the first iteration.
		int u = minDistance(dist, sptSet);

		// Mark the picked vertex as processed
		sptSet[u] = true;

		// Update dist value of the adjacent vertices of the picked vertex.
		for (int v = 0; v < graph.size(); v++)

			// Update dist[v] only if is not in sptSet, there is an edge from
			// u to v, and total weight of path from src to v through u is
			// smaller than current value of dist[v]
			if (!sptSet[v] && graph[u][v] && dist[u] != DBL_MAX
				&& dist[u] + graph[u][v] < dist[v])
				dist[v] = dist[u] + graph[u][v];
	}

	// print the constructed distance array
	// printSolution(dist, graph.size());

	return dist;
}

double optimales_dreieck(std::vector<Punkt> punkte, Punkt punkt1, Punkt punkt2)
{
    double optimal = -1;
    for (Punkt punkt : punkte)
    {
        if (!punkte_gleich(punkt, punkt1) && !punkte_gleich(punkt, punkt2)) 
        {
            double dreieckslaenge = distanz(punkt1, punkt2) + distanz(punkt1, punkt) + distanz(punkt2, punkt);
            if (optimal == -1 || optimal > dreieckslaenge)
            {
                optimal = dreieckslaenge;
            }
        }
    }
    return optimal;
}


double berechne_t(std::vector<Kante_gerichtet> kanten, std::vector<Punkt> punkte)
{
    double t = 1;

    std::vector<std::vector<double>> distances(punkte.size(), std::vector<double> (punkte.size()));

	for (Kante_gerichtet k : kanten)
	{
		Punkt p1 = k.start;
		Punkt p2 = k.ende;

		int p1_i = p1.index;
		int p2_i = p2.index;

		distances[p1_i][p2_i] = distanz_punkte(punkte[p1_i], punkte[p2_i]); 
	}

    std::vector<std::vector<double>> distanzen;
    distanzen.resize(punkte.size(), std::vector<double>(punkte.size()-1));

    for (int i=0; i<punkte.size(); i++) 
    {
        distanzen[i] = dijkstra(distances, i);
    }

    for (int i=0; i<punkte.size()-1; i++)
    {
        for (int j=i+1; j<punkte.size(); j++)
        {
            double optimaler_zyklus  = optimales_dreieck(punkte, punkte[i], punkte[j]);
            double kuerzester_zyklus = distanzen[i][j] + distanzen[j][i];
            double odil = kuerzester_zyklus / optimaler_zyklus;

            if (t < odil)
            {
                t = odil;
            } 
        }
    }

    return t;
}