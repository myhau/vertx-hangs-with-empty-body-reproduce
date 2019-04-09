# Reproduce

1. run application - it will spin up web server at `localhost:8080`
2. execute `curl -X POST -d 'some data' localhost:8080` - it works
3. execute `curl -X POST localhost:8080` - it hangs 