# spring-mongo-query-resolver

This library's name has changed

- release 1.x merij
- release 2.x spring-data-mongodb
- release >= 3.x spring-mongo-query-resolver

# Breaking changes

- release `<= 4.x`: java 8
- release `>= 4.x`: java 17

# What is this repo

Resolve http query string to mongo criteria and mongo sort.

Query operation support

- [x] $eq
- [x] $lt
- [x] $gt
- [x] $lte
- [x] $gte
- [x] $in
- [x] $ne
- [x] $regex

Sort operation support

- [x] asc
- [x] desc

# How to use

Add dependency into your `pom.xml`

`pom.xml`

```xml
<dependencies>
    <dependency>
        <groupId>com.tuana9a</groupId>
        <artifactId>spring-mongo-query-resolver</artifactId>
        <version>4.0.0</version>
    </dependency>
<dependencies>
```

## Resolve criteria query from string

```java
// import com.tuana9a.spring.mongo.qrisolver.resolvers.CriteriaResolver;

String q = "age>5,age<10,age!=8,name==tuana9a,graduate@=prim;high;uni,year>=1991,year<=2003,deleted==false";
Criteria criteria = CriteriaResolver.resolve(q);
Criteria desiredCriteria = Criteria.where("age").gt(5).lt(10).ne(8)
        .and("name").is("tuana9a")
        .and("graduate").in("prim", "high", "uni")
        .and("year").gte(1991).lte(2003)
        .and("deleted").is(false);
Assertions.assertEquals(criteria.getCriteriaObject(), desiredCriteria.getCriteriaObject());
```

after you get your Criteria you can do what ever with it, like: add more criteria to it then add it to the query

```java
criteria = criteria.and("location").near(point).maxDistance(maxDistance);
mongoTemplate.find(new Query(criteria), ParkingStation.class);
```

## Resolve sort from string

```java
// import com.tuana9a.spring.mongo.qrisolver.resolvers.SortResolver;

Sort sort = SortResolver.resolve("age=-1,address=1");
Sort desiredSort = Sort.by(Sort.Direction.DESC, "age").and(Sort.by(Sort.Direction.ASC, "address"));
Assertions.assertEquals(sort, desiredSort);
```

## Global config

```java
import com.tuana9a.spring.mongo.qrisolver.configs.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SetQueryResolverConfig implements CommandLineRunner {
    @Override
    public void run(String... args) {
        Config.REGEX_OPTIONS = "i";
    }
}
```

# Improvement

Create an issue for it.

# Report bug

Create an issue for it.

# Contribution

Just open the PR and I will review it.
