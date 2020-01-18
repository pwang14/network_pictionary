# Networked Pictionary

This program is a networked, multiplayer version of the game Pictionary. 

![Drawing turn](/images/pictionary1.gif)
*It's the user's turn to draw*

![Guessing turn](/images/pictionary2.gif)
*It's the user's turn to guess*

The drawing and network aspects of the program were developed seperately (Paint and Network Test), and then merged together (Paint2).

### Drawing application

The drawing program was developed with the javax.swing (JFrame) and java.awt.event packages.
When a user clicks and drags the mouse (MouseListener), a new line object is added to an ArrayList connecting the initial and final positions of the mouse.
The program contains additional features (different brush sizes and colours, undo with crtl-z, clear canvas button).

### Network

A basic client-server network was implemented using the java.net package.
ArrayLists are used to keep track of client IP Addresses, Ports, usernames, and game scores.

![Network connection](/images/server1.png)

### Merging

The drawing program was modified to allow for networking (java.net package imported in paintApp class).
Whenever the user modifies the canvas, a custom string signal is sent to the server. The server then sends the signal to all its clients.
When the drawing program receives a signal, it uses string manipulation to parse the signal and figure out what type of command was originall sent.
The program then updates its own Canvas in accordance with the signal.
The game of Pictionary only allows for 1 person to draw at a time, thus there is no need to account for signal synchronization and management (since only 1 client is sending signals at a time).

![Commands](/images/server2.png)

If you wish to establish a network over multiple networks (instead of 1 local network), you must use port forwarding.
