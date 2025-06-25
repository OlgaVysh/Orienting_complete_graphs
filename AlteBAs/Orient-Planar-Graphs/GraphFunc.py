import numpy as np
import heapq
import pulp


from FullGraph import perimeters_dict


def create_distance_matrix(num_points, edge_index, edge_vars, pointset):
    dist_matrix = np.full((num_points, num_points), np.inf)

    for edge, idx in edge_index.items():
        if pulp.value(edge_vars[idx][0]) > 0.5:
            dist_matrix[edge[0], edge[1]] = np.linalg.norm(pointset[edge[0]] - pointset[edge[1]])
        elif pulp.value(edge_vars[idx][1]) > 0.5:
            dist_matrix[edge[1], edge[0]] = np.linalg.norm(pointset[edge[0]] - pointset[edge[1]])

    return dist_matrix


def dijkstra(adj_matrix, start):
    num_points = adj_matrix.shape[0]
    distances = [np.inf] * num_points
    distances[start] = 0
    priority_queue = [(0, start)]
    visited = set()

    while priority_queue:
        current_distance, current_node = heapq.heappop(priority_queue)

        if current_node in visited:
            continue

        visited.add(current_node)

        for neighbor in range(num_points):
            if adj_matrix[current_node, neighbor] != np.inf:
                distance = current_distance + adj_matrix[current_node, neighbor]
                if distance < distances[neighbor]:
                    distances[neighbor] = distance
                    heapq.heappush(priority_queue, (distance, neighbor))

    return distances


def calculate_odil(pointset, dist_matrix):
    odil = {}
    num_points = len(pointset)
    shortest_paths = {}
    for k in range(len(pointset)):
        distances_from_k = dijkstra(dist_matrix, k)
        for l in range(len(pointset)):
            if k != l:
                path_kl = distances_from_k[l]
                shortest_paths[(k, l)] = path_kl
    for i in range(num_points):
        for j in range(i + 1, num_points):
            if i != j and (i, j) not in odil and (j, i) not in odil:
                shortest_path_ij = shortest_paths.get((i, j))
                shortest_path_ji = shortest_paths.get((j, i))

                perimeter = perimeters_dict.get((i, j)) or perimeters_dict.get((j, i))
                sum_shortest_paths = shortest_path_ij + shortest_path_ji

                odil_of_ij = sum_shortest_paths / perimeter

                odil[(i, j)] = odil_of_ij
                odil[(j, i)] = odil_of_ij

    max_odil = max(odil.values())

    return max_odil

def calculate_odil_brute(points,paths):
    odil = {}
    for i in range(len(points)):
        for j in range(len(points)):
            if i != j and (i, j) not in odil and (j, i) not in odil:
                shortest_path_ij = paths.get((i, j))
                shortest_path_ji = paths.get((j, i))

                perimeter = perimeters_dict.get((i, j)) or perimeters_dict.get((j, i))
                sum_shortest_paths = shortest_path_ij + shortest_path_ji
                max_odil = sum_shortest_paths / perimeter

                odil[(i, j)] = max_odil

    return odil