# spring-data-mongodb

# The project's name was changed

The project's name was changed from merij to spring-data-mongodb

# What is this repo

This is a wrapper around spring data mongodb with some utils function that I added into that.
For example:

- [x] Resolve query string to spring-data-mongodb criteria query.
  - [x] eq
  - [x] lt
  - [x] gt
  - [x] lte
  - [x] gte
  - [x] in
  - [x] ne
  - [x] regex
- [x] Resolve query string to spring-data-mongodb sort query.

# How to use

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

criteria query

```java
public void testCriteria() {
    String q = "age>5,age<10,age!=8,name==tuana9a,graduate@=prim;high;uni,year>=1991,year<=2003,error-should-be-ignored";
    Opts opts = new Opts();
    Collection<CriteriaPart> parts = Arrays.stream(q.split(",")).map(x -> ToolBox.buildCriteriaPart(x, opts)).filter(x -> !x.isError).collect(Collectors.toList());
    Criteria criteria = ToolBox.buildCriteria(parts, opts);
    Criteria desiredCriteria = Criteria.where("age").gt(5).lt(10).ne(8)
            .and("name").is("tuana9a")
            .and("graduate").in("prim", "high", "uni")
            .and("year").gte(1991).lte(2003);
    Assertions.assertEquals(criteria.getCriteriaObject(), desiredCriteria.getCriteriaObject());
}
```

sort

```java
public void testSort() {
    String s = "age=-1,address=1";
    Collection<SortPart> parts = Arrays.stream(s.split(",")).map(x -> ToolBox.buildSortPart(x)).collect(Collectors.toList());
    Sort sort = ToolBox.buildSort(parts);
    // Sort desiredSort = Sort.by(Sort.Direction.ASC, "address").and(Sort.by(Sort.Direction.DESC, "address")); // this will not work, below will work
    Sort desiredSort = Sort.by(Sort.Direction.DESC, "age").and(Sort.by(Sort.Direction.ASC, "address"));
    Assertions.assertEquals(sort, desiredSort);
}
```