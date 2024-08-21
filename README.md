# spring-mongo-query-resolver

This library's formerly name is "merij", "spring-data-mongodb"

- release 1.x merij
- release 2.x spring-data-mongodb
- release > 3.x spring-mongo-query-resolver

# what is this repo

Resolve http query string to mongo criteria query and mongo sort.

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

# how to use

Add dependency into your `pom.xml`

`pom.xml`

```xml
<dependencies>
  <dependency>
      <groupId>com.tuana9a</groupId>
      <artifactId>spring-mongo-query-resolver</artifactId>
      <version>3.0.0</version>
  </dependency>
<dependencies>
```

## criteria query

```java
@Test
public void testCriteria() throws Error {
    String q = "age>5,age<10,age!=8,name==tuana9a,graduate@=prim;high;uni,year>=1991,year<=2003";
    Opts opts = new Opts();
    List<CriteriaPart> parts = new LinkedList<>();
    for (String x : q.split(",")) {
        parts.add(ToolBox.buildCriteriaPart(x, opts));
    }
    Criteria criteria = ToolBox.buildCriteria(parts, opts);
    Criteria desiredCriteria = Criteria.where("age").gt(5).lt(10).ne(8)
            .and("name").is("tuana9a")
            .and("graduate").in("prim", "high", "uni")
            .and("year").gte(1991).lte(2003);
    Assertions.assertEquals(criteria.getCriteriaObject(), desiredCriteria.getCriteriaObject());
}
```

## sort

```java
@Test
public void testSort() throws Error {
    String s = "age=-1,address=1";
    Collection<SortPart> parts = new LinkedList<>();
    for (String x : s.split(",")) {
        parts.add(ToolBox.buildSortPart(x));
    }
    Sort sort = ToolBox.buildSort(parts);
    Sort desiredSort = Sort.by(Sort.Direction.DESC, "age").and(Sort.by(Sort.Direction.ASC, "address"));
    Assertions.assertEquals(sort, desiredSort);
}
```

# Improvement

Create an issue for it.

# Report bug

Create an issue for it.

# Contribution

Any contributions would helps. Just open your PR and I will review it.
