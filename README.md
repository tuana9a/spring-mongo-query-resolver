# spring-data-mongodb

# The project's name was changed

The project's name was changed from merij to spring-data-mongodb

# What is this repo

This is a wrapper around spring data mongodb with some utils function that I added into that.
For example:

- [x] Resolve query string to spring-data-mongodb criteria and sort query.

## Usage

`pom.xml`

```xml
<dependencies>
  <dependency>
      <groupId>com.tuana9a</groupId>
      <artifactId>spring-data-mongodb</artifactId>
      <version>2.0.0-SNAPSHOT</version>
  </dependency>
<dependencies>
```

`?q=age>5,age<10,name==tuana9a` will be resolve to

```json
{
  "age": {
    "$gt": 5,
    "$lt": 10
  },
  "name": "tuana9a"
}
```

TODO: more examples with real java code
