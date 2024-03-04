This software program consists of a user-friendly graphical interface that allows users to run instances of Belief Flow Networks (BFNs), a framework for representing how logical beliefs spread among interacting agents within a network. In a BFN, agents communicate asynchronously. The agents' beliefs are represented using epistemic states, which encompass their current beliefs and conditional beliefs guiding future changes. When communication occurs between two connected agents, the receiving agent changes its epistemic state using an improvement operator, a well-known type of rational iterated belief change operator that generalizes belief revision operators. The software program can be used to generate various types of network structures (random, line, loop, Barabasi-Albert preferential incluence graphs, Kleinberg small world graphs) and simulate the propagation of beliefs within these networks.

Reference of the related paper:
Nicolas Schwind, Katsumi Inoue, Sébastien Konieczny, Pierre Marquis: BeliefFlow: A Framework for Logic-Based Belief Diffusion via Iterated Belief Change. In Proceedings of AAAI'24.

You must have a Java Runtime Environment installed to run the software program. To launch:
> java -jar bfn-gui.jar

QUICK START:
The software program start by launching a 'default' BFN.
To load the running example from the paper: 'Edition -> Load -> Load from library -> BFN from running example'
To generate a new graph: 'Edition -> New graph'
To display the epistemic states of all agents: 'Info -> Display all ep. states'
To simulate a run: 'Run -> Launch a run' (or press the space key when focus is given on the graph frame), then right/left arrow keys to navigate through a run
To stop a run and go back to the edition menu: 'Run -> Launch a run'

Note: the jar file repacks the following referenced librairies:
- JGrapht (1.5.2) https://github.com/jgrapht/jgrapht/tree/master
- JUNG (2.0.1) https://jung.sourceforge.net/

The source code is available in the 'src' directory. The software program is launched through the Main class in the 'main' package.
