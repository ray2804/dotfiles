## Glossary of terms
**In order of relevance, appearance**

### Client 

A client is a computer or computer program that initiates contact with a server in order to make use of a resource. The first web browser - or browser-editor rather - was called `WorldWideWeb` as, after all, when it was written in 1990 it was the only way to see the web. Much later it was renamed Nexus in order to save confusion between the program and the abstract information space (which is now spelled World Wide Web with spaces). It is in this context, of a web browser and local machine (or program being able to work with HTTP Verbs), that we see the client.

### Server

A server is a computer system that selectively shares its resources; in most common use, a server is a physical computer (a computer hardware system but virtual system is also possible) dedicated to run one or more services (as a host), to serve the needs of the users of other computers on a network. Depending on the computing service that it offers it could be a database server, file server, mail server, print server, web server, gaming server, or some other kind of server. In the context of Internet Protocol (IP) networking, a server is a program that operates as a socket listener. Servers often provide essential services across a network, either to private users inside a large organization or to public users via the Internet.

### Client-server model

The client–server model is an approach to computer network programming and it is now prevalent in computer networks. Email, the World Wide Web, and network printing all apply the client–server model. The model assigns one of two roles to the computers in a network: [Client](#client) or [server](#server). This sharing of computer resources is called time-sharing, because it allows multiple people to use a computer (in this case, the server) at the same time. Because a computer does a limited amount of work at any moment, a time-sharing system must quickly prioritize its tasks to accommodate the clients.

### Resources

Data, CPUs, printers, and data storage devices are some examples of resources.

### Request - response pattern

Clients and servers exchange **messages** in a request-response messaging pattern: The client sends a request, and the server returns a response. To communicate, the computers must have a common language, and they must follow rules so that both the client and the server know what to expect. The language and rules of communication are defined in a communications protocol. All client-server protocols operate in the application layer.

### Machine role

Whether a computer is a client, a server, or both, it can serve multiple functions. For example, a single computer can run web server and file server software at the same time to serve different data to clients making different kinds of requests. Client software can also communicate with server software on the same computer. Communication between servers, such as to synchronize data, is sometimes called inter-server or server-to-server communication.

![Simplified abstraction of original or normal web page client-server operations](http://hackingsys.files.wordpress.com/2012/02/11.gif?w=683)

### 

The Comet approach differs from the original model of the web, in which a browser requests a complete web page at a time. A web server can send *anything* to the web browser *if the web browser specifically requests it*. HTTP servers cannot send data to their clients whenever they want, **there is no way to propagate an update to a browser**. 

This limits prospect of web-based collaborative applications. Then came the not-so-great solution and that was long lived HTTP connection.

### Long lived HTTP connection

The concept of [long lived HTTP connection](http://en.wikipedia.org/wiki/HTTP_persistent_connection) is nothing complicated. Once the browser (the client) establishes a connection with the web server, instead of closing the connection, the server keeps it open as long as it wants. If the connection is kept open for one hour, the web server can send data to the client whenever it wants in that time period. This opened the almost closing door for web apps that need N-way collaboration.
But it has a drawback, different web browsers behave differently to long lived HTTP connections.

The second problem was, popular web servers like Apache are not the best piece of software for maintaining lots of HTTP connections. So, what we need is a separate web server. This class of web servers are known as Comet servers and they have no problem of scaling up to thousands of long lived HTTP connections.

#### HTTP 1.0

Under HTTP 1.0, there is no official specification for how 'keep-alive' operates. It was, in essence, added to an existing protocol. If the client supports keep-alive, it adds an additional header to the request:

```
Connection: Keep-Alive
```

Then, when the server receives this request and generates a response, it also adds a header to the response:

```
Connection: Keep-Alive
```

Following this, the connection is not dropped, but is instead kept open. When the client sends another request, it uses the same connection. This will continue until either the client or the server decides that the conversation is over, and one of them drops 
the connection.

#### HTTP 1.1

In HTTP 1.1, all connections are considered persistent unless declared otherwise.[^1]

[^1]: http://tools.ietf.org/html/rfc2616#section-8.1

![Multiple connections versus persistent connection](http://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/HTTP_persistent_connection.svg/500px-HTTP_persistent_connection.svg.png)

### Server push

Push, or server push, describes a style of Internet-based communication where the request for a given transaction is initiated by the publisher or central server. It is contrasted with pull, where the request for the transmission of information is initiated by the receiver or client.

Use-case:

> A company deals in auctions and facilitates this over the internet. On their own web site they collect offers from different external sellers. These offers need to be sent to potential bidders who connect to the web site using their browser.

### HTTP streaming

HTTP server push (also known as HTTP streaming) is a mechanism for sending data from a web server to a web browser. HTTP server push can be achieved through several mechanisms.

Use-case

>  People can bid on these auctions within a given time until it ran out. As soon as someone bids, the bid is transmitted to all the other connected clients so they may be notified that the price has just increased, and may choose to do a counter bid.

### Polling

Polling, or polled operation, refers to actively sampling the status of an external device by a client program as a synchronous activity. Polling is most often used in terms of input/output (I/O), and is also referred to as polled I/O or software-driven I/O.

> This approach is somewhat problematic: if the server is busy checking the status, it can't be freed to have resources allocated elsewhere: it's very inefficient.

Polling is sometimes used synonymously with busy-wait polling (busy waiting). In this situation, when an I/O operation is required, the computer does nothing other than check the status of the I/O device until it is ready, at which point the device is accessed. In other words, the computer waits until the device is ready. Polling also refers to the situation where a device is repeatedly checked for readiness, and if it is not, the computer returns to a different task.

### Long polling

Long polling is a variation of the traditional polling technique and allows *emulation of an information push* from a server to a client. With long polling, the client requests information from the server in a similar way to a normal poll. However, if the server does not have any information available for the client, *instead of sending an empty response*, the server **holds the request and waits for some information to be available**. 

![Long polling publication/subscription operation and pattern](http://pic.dhe.ibm.com/infocenter/wasinfo/v6r1/topic/com.ibm.websphere.ajax.devguide.help/images/PubSub_longpoll.jpg)

Once the information becomes **available** (or after a suitable timeout), a **complete** response is sent to the client. The client will normally then **immediately re-request** information from the server, so that the server will almost always have an available waiting request that it can use to deliver data in response to an event. 

### Reverse AJAX

In a web/AJAX context, long polling is also known as Comet programming. The use of Comet techniques in web development predates the use of the word Comet as a neologism for the collective techniques. Comet is known by several other names, including Ajax Push, Reverse Ajax, Two-way-web, HTTP Streaming, and HTTP server push among others.

## Comet programming

Comet is a web application model in which a long-held HTTP request allows a web server to push data to a browser, without the browser explicitly requesting it. Comet is an umbrella term, encompassing multiple techniques for achieving this interaction. All these methods rely on features included by default in browsers, such as JavaScript, rather than on non-default plugins. 

![Diagram of the Comet web application model](http://vinaytech.files.wordpress.com/2008/11/image5.png)

Earlier we saw the difference between multiple connections, open and close each request, and those that were persistent in nature such as Comet. And above here you can see if more detail how this flow runs through time. One thing that is less clear from this picture, is the network layout (roughly the horizontal swim-lanes of that diagram) so for completeness, I'll post this additional illustration below:

![System landscape or network diagram of client(s) and server(s) at interaction with each other](http://www.botskool.com/sites/default/files/users/DeAviator/comet_works.gif)

Comet applications use long-lived HTTP or HTTPS connections between the client and server, through which the server can push new data to the client as it becomes available. Unlike traditional Web servers, which deliver a single payload and then immediately close the connection to the client, Comet servers must maintain a continuous connection to each client for the duration of the session. A true Comet implementation requires a very different kind of server architecture to be efficient and scalable.

## Comparison data transfer methods

![How does Comet differ from traditional data transfer and Ajax?](http://www.botskool.com/sites/default/files/users/DeAviator/ajax_vs_comet.png)

In the original request-response model of the Web, a browser receives a complete Web page in response to each request.

Ajax overcomes this by doing away with the need to receive a new page and instead simply request a data response from the server. It refreshes a piece of data instead of complete web page. However, Ajax is still a request-response model, and since Ajax requires that the client initiates each interaction, data cannot be sent at will from the server.

Comet is a push approach, decreasing latency and server load. Once the client has made the initial connection, the server sends updates as they occur, without the client needing to issue further requests. .

Comet can easily provide more simultaneous connections than a traditional web server, and faster data transit between the client and the server.

With Ajax, the action is taken by the user and with Comet, it's an action from the server. Currently Comet is a popular technique for browser-based chat applications since it allows the server to receive a message from one user and display that message to another user. Some web applications that currently use Comet are Google's GTalk, Meebo's chat, and Facebook chat.

Ajax and Comet differ in the expected length of the request. Ajax uses a quick request-response to update or get new information from a server, while Comet typically opens a longer connection to get information as it is available from the server.

### To summarize:

The disadvantages in AJAX approach are:

Unnecessary calls are made to the server, even though there may be no update.
With number of clients increasing, server may get overloaded. This stalls the server with few hundred clients.

![AJAX time-polling based update](http://www.botskool.com/sites/default/files/users/DeAviator/NonCometPicture0.jpg)

Whereas comet technique pushes the data to the clients when the server side data changes,
avoiding unnecessary round-trips to clients. This results in increase in performance of network.

![COMET based update](http://www.botskool.com/sites/default/files/users/DeAviator/CometPicture0.jpg)