# Play like routes, django like middelware, plumbing by netty and completely servlet free :)

## Running it

* download google-closures templates from https://github.com/lightbody/closure-templates and "mvn install" it
* edit able-example and run Main

## Using

Get yorself a copy of "Able.class" and start settings up routes and, ultimately, running .server.start()

## Routing

### Routes are simple regex like this:

        // sample route definition
        able.router.route("^/$", Homepage.class);

### Match groups can be anonymous like this:

        // with groups
        able.router.route("^/hello/(.*)/$", Homepage.class);

### Or be named like below:

        // with named groups
        able.router.route("^/hello/group/(?<person>.*)/$", Homepage.class);

## Middleware:

TODO

## Config

TODO