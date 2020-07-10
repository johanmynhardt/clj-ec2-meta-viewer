# Clojure EC2 Meta Viewer

Just a simple EC2 Metadata viewer/proxy. There are software to do this already, this is just a simple Clojure-based implementation.

# Building

```$ make build-jar```

This results in an executable jar;

```$ sudo java -jar build/clj-ec2-meta-viewer.jar```

`sudo` is required to start on port `80`.

# Dependencies

This makes use of 2 simple Clojure libraries:

- `weavejester/ring-server` - [Ring-Server](https://github.com/weavejester/ring-server) _A library for starting a web server to serve a Ring handler with sensible default options and environment variable overrides._
- `seancorfield/depstar` - [depstar](https://github.com/seancorfield/depstar) _Builds JARs, uberjars, does AOT, manifest generation, etc for deps.edn projects_