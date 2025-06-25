# Project Orienting Complete Graphs

Graphs are a fundamental concept in computer science and serve as powerful models for a wide range of real-world structures and problems. Studying graphs remains an important area of research.

This project focuses on **complete undirected graphs**, where every pair of vertices is connected by exactly one undirected edge. The central objective is to **orient** the edges of such a graph—that is, assign a direction to each edge—such that the **oriented dilation** of the resulting directed graph is minimized.  For more information on oriented dilation, see [this](https://drops.dagstuhl.de/entities/document/10.4230/LIPIcs.ESA.2023.26).

## Implemented Algorithms

The project implements and compares three main orientation algorithms:

1. 2-Approximation Algorithm
2. Greedy Edges Orientation
3. SAT Solver-Based Approach using CP-SAT-Solver 

## Features

In addition, the project includes:

- Utilities for input and output handling
- Algorithms for computing the oriented dilation
- A framework for running and evaluating experimental setups

## Technologies

- Language: Java  
- Build Tool: Maven  
- Testing: JUnit

See documentation on how to work with the project : 📄 [Dokumentation zum Projekt](/Dokumentation.md)
