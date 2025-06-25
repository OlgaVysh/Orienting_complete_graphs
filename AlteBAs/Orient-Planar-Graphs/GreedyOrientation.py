import itertools
import numpy as np

from DrawGraph import draw_oriented_graph
from GraphFunc import dijkstra, create_distance_matrix


def can_form_cycle_dfs(polygon, edge_vars, edge_index, edges):
    """
    Perform a DFS to check if a cycle can be formed for a polygon by traversing
    its edges in two possible directions (clockwise and counterclockwise).
    """

    # No sorting of vertices; use the polygon as it is
    num_vertices = len(polygon)

    # Track newly oriented edges during this DFS call
    newly_oriented_edges = []

    def try_path(path):
        """Try to traverse a specific path and see if it forms a cycle."""
        for (current, next_node, edge) in path:
            # Respect the existing edge orientation
            if edge_vars[edge] == [1, 0] and (current, next_node) == (edges[edge][1], edges[edge][0]):
                return False  # If orientation doesn't allow traversal
            if edge_vars[edge] == [0, 1] and (current, next_node) == (edges[edge][0], edges[edge][1]):
                return False  # If orientation doesn't allow traversal

            # If the edge is unoriented, orient it to allow traversal
            if edge_vars[edge] == [0, 0]:
                if (current, next_node) == (edges[edge][0], edges[edge][1]):
                    edge_vars[edge] = [1, 0]
                else:
                    edge_vars[edge] = [0, 1]
                newly_oriented_edges.append(edge)  # Track that this edge was oriented

        return True

    # Construct paths for the polygon: clockwise and counterclockwise
    clockwise_path = []
    counterclockwise_path = []
    # Create the paths by connecting consecutive vertices and their edges
    for i in range(num_vertices):
        v1 = polygon[i]
        v2 = polygon[(i + 1) % num_vertices]  # Wrap around to form a cycle
        e = edge_index[tuple(sorted((v1, v2)))]  # Get the edge index
        clockwise_path.append((v1, v2, e))
        counterclockwise_path.append((v2, v1, e))  # Reverse direction for counterclockwise path

    # Try both paths to see if any form a cycle
    if not try_path(clockwise_path):
        # Reset oriented edges if no cycle is found
        for e in newly_oriented_edges:
            edge_vars[e] = [0, 0]
        if not try_path(counterclockwise_path):
            # Reset oriented edges if no cycle is found
            for e in newly_oriented_edges:
                edge_vars[e] = [0, 0]
            return False

    # If a cycle was successfully formed, return True
    return True


def greedy_orient_polygons(polygons, edge_index, edges, pointset):
    # Sort polygons by their perimeter (or area), smallest first
    polygon_sizes = []
    for idx, polygon in enumerate(polygons):
        # Calculate the perimeter for the polygon
        perimeter = 0
        num_vertices = len(polygon)
        for i in range(num_vertices):
            v1 = polygon[i]
            v2 = polygon[(i + 1) % num_vertices]  # Wrap around to the first vertex
            perimeter += np.linalg.norm(pointset[v1] - pointset[v2])
        polygon_sizes.append((idx, perimeter))

    # Sort polygons by perimeter, smallest first
    polygon_sizes.sort(key=lambda x: x[1])

    # Initialize variables
    edge_vars = {idx: [0, 0] for idx in range(len(edges))}  # [0, 0] means unoriented
    oriented_polygon_vars = [0] * len(polygons)  # Initially, no polygon is oriented

    # Greedy orientation process using the simplified DFS
    for idx, _ in polygon_sizes:
        polygon = polygons[idx]
        num_vertices = len(polygon)

        # Process the entire polygon rather than only 3 vertices
        # **Removed specific v1, v2, v3 logic for generalization to more vertices**
        if can_form_cycle_dfs(polygon, edge_vars, edge_index, edges):
            oriented_polygon_vars[idx] = 1  # Mark this polygon as oriented

    # Step: Check if each edge is part of any oriented polygon
    unoriented_edges = []
    for e, (v1, v2) in enumerate(edges):
        edge_is_in_oriented_polygon = False  # Flag to check if the edge is part of any oriented polygon

        # Loop through each polygon
        for p, polygon in enumerate(polygons):
            if oriented_polygon_vars[p] > 0.5:  # If the polygon is oriented
                if (v1 in polygon and v2 in polygon):
                    edge_is_in_oriented_polygon = True
                    break  # Greedy.Edge is part of an oriented polygon, no need to check further

        # If the edge is not part of any oriented polygon, set it as unoriented
        if not edge_is_in_oriented_polygon:
            edge_vars[e][0] = 0  # Reset v1 -> v2
            edge_vars[e][1] = 0  # Reset v2 -> v1
            unoriented_edges.append((e, v1, v2))  # Add the edge to the unoriented list for brute force

    # --- Brute Force the Orientations for Unoriented Edges ---
    if unoriented_edges:  # Only run brute force if there are unoriented edges
        edge_combinations = list(itertools.product([0, 1], repeat=len(unoriented_edges)))
        for combination in edge_combinations:
            # Reset all unoriented edges before testing a new combination
            for e, v1, v2 in unoriented_edges:
                edge_vars[e][0] = 0  # Reset v1 -> v2
                edge_vars[e][1] = 0  # Reset v2 -> v1

            # Set the current combination of orientations
            for i, (e, v1, v2) in enumerate(unoriented_edges):
                if combination[i] == 0:
                    edge_vars[e][0] = 1  # v1 -> v2
                else:
                    edge_vars[e][1] = 1  # v2 -> v1

            # Generate the distance matrix and check for infinite distances
            dist_matrix = create_distance_matrix(len(pointset), edge_index, edge_vars, pointset)
            infinite_paths = False
            shortest_paths = {}

            # Compute shortest paths using Dijkstra's algorithm for each point
            for k in range(len(pointset)):
                distances_from_k = dijkstra(dist_matrix, k)
                for l in range(len(pointset)):
                    if k != l:
                        path_kl = distances_from_k[l]
                        if np.isinf(path_kl):
                            infinite_paths = True
                            break
                        shortest_paths[(k, l)] = path_kl
                if infinite_paths:
                    break

            # If no infinite paths are found, stop brute forcing and return the solution
            if not infinite_paths:
                return edge_vars, oriented_polygon_vars

        # If no valid orientation is found, draw the oriented graph and return None
        draw_oriented_graph(pointset, polygons, edge_index, edge_vars, oriented_polygon_vars)
        return None

    return edge_vars, oriented_polygon_vars
