# merij

Resolve query string to spring-data-mongodb criteria and sort query.

## Examples

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
