# Derivative-Free Optimization Methods 

A flexible and expandable library for derivative-free optimization algorithms. 

Carlos Cotta, 2024

## Features

Currently included methods are:

* Hooke-Jeeves algorithm
* Nelder-Mead algorithm

## Requirements

Requires JDK 17 or higher. The project relies on the [json-simple](https://cliftonlabs.github.io/json-simple/) library for parsing configuration files and dumping statistics. A Maven dependency is included in the `pom.xml` file for this purpose. Alternatively, non-Maven users may download [`json-simple-4.0.1.jar`](https://cliftonlabs.github.io/json-simple/target/json-simple-4.0.1.jar) and add it to the project build path.

## Usage 

See `dfopt/test/RunDerivativeFree` class for an example of use. 

The configuration of the algorithm is done via a JSON file (see `run/bitstring.json` for an example).

If you are using Maven, the following dependency can be added to your project:

~~~
    <dependency>
    	<groupId>es.uma.lcc.caesium</groupId>
    	<artifactId>derivative-free-optimization</artifactId>
  	<version>1.0</version>
    </dependency>
~~~

## Applications

The library has been used in the following projects (non-exhaustive list):

* [pedestrian-evacuation-optimization](https://github.com/Bio4Res/pedestrian-evacuation-optimization): Optimization of crowd evacuation in case of emergency

