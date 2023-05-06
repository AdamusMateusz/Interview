# CDQ Interview Task


## Decision made
1. Job api always returns response when task exists. When `pattern` is not matching `input` the response clearly states that no match was found.
2. When job with given id does not exist, `404 Not Found` is returned
3. If `input text` or `pattern` is missing - request is rejected


## Commands

`docker-compose up --build`