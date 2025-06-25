from itertools import product
import numpy as np
from scipy.sparse.csgraph import shortest_path
from GraphFunc import create_distance_matrix, calculate_odil_brute


def brute_force_odil(edge_index, edges, pointset):

    # Step 1: Generate all possible orientations for the edges
    num_edges = len(edges)
    all_orientations = list(product([0, 1], repeat=num_edges))

    min_odil = float('inf')

    for orientation in all_orientations:
        # Step 2: Create edge_vars according to the current orientation
        edge_vars = {}
        for idx, direction in enumerate(orientation):
            edge_vars[idx] = [0, 0]
            if direction == 0:
                edge_vars[idx][0] = 1  # edge directed from node 0 to node 1
            else:
                edge_vars[idx][1] = 1  # edge directed from node 1 to node 0

        # Step 3: Create adjacency matrix based on this orientation
        dist_matrix = create_distance_matrix(len(pointset), edge_index, edge_vars, pointset)

        # Use SciPy's shortest_path to calculate shortest paths and distances between all node pairs
        shortest_paths_matrix = shortest_path(csgraph=dist_matrix, directed=True, return_predecessors=False,
                                              unweighted=False)

        # Check for strong connectivity by verifying if any shortest path distance remains infinite
        if np.any(shortest_paths_matrix == np.inf):
            continue  # If any distance is infinite, the graph is not strongly connected

        # Step 4: Extract shortest paths into a dictionary
        shortest_paths = {}
        for i in range(len(pointset)):
            for j in range(i + 1, len(pointset)):  # Only look at the upper triangle (i < j)
                distance = shortest_paths_matrix[i,j]
                shortest_paths[(i, j)] = distance  # Path from i to j
                shortest_paths[(j, i)] = distance  # Path from j to i (symmetric rule)


        # Step 5: Calculate ODIL for this orientation
        odil_values = calculate_odil_brute(pointset, shortest_paths)
        max_odil = max(odil_values.values()) if odil_values else float('inf')
        # Step 6: Update minimum ODIL if the current one is smaller
        if max_odil < min_odil:
            min_odil = max_odil


    return min_odil




