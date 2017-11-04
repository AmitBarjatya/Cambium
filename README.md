# Cambium
Example app using Single Activity Framework and Unidirectional Dataflow

App fetches kickstarter projects from remote api and then displays them in a recyclerview.
Uses Realm for caching data while working offline. Implements MVP design pattern.
Other features includes filtering the list with number of backers in certain range and 
sorting the list based on projects end date(increasing/decreasing order) and title (a-z/z-a).

The response to "Where to place sorting or filtering logic?" can be debated. I personally feel that it largely depends on the answer to "what will be the fastest way to show data on screen?" question. Since in this example I'm using Realm for persistence, which gives us benefits like fast queries and lazy initialization for objects, I feel sorting/filtering is best placed in the "model" layer. In some other case, with RxJava perhaps, it could be a part of presentation layer.
