## Fuel consumption simulation

A simple command line fuel consumption calculator based on reading data from a CSV file and calculating fuel consumption between two altitudes by interpolating between two values or extrapolating from 2 edge values.

Table.java contains two methods for the calculation:
1. Using a simple loop through entries in the table - assumes entries are ordered from highest to lowest altitude. This approach is faster because it doesn't sort the table (command line implementation uses this method).
2. Using a custom comparator. Slower as it sorts the table from closest to given altitude to furthest, so has O(n log n) complexity.

Created as a pre-assignment for an interview.
