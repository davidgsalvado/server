# Server

## Important considerations
 - This project does not create the databases and tables from your query
  automatically, so you have to do so before executing the corresponding task.

## How to execute 

Make sure you are on the ```main``` branch of the project. If you are not,
use this command: ```git checkout main```

Once you are on main branch, perform the following steps:

```
mvn clean install (first time to download dependencies)
mvn sql:execute (execute this if you want to reconstruct
the project local database)
mvn spring-boot:run
```

## Query example

Here you have an example of a correctly written query:

```
CONNECT TO DBASE=osmparser OF TYPEDB=PostgreSQL FROM PORTDB=5432 OF 
HOSTDB=localhost WITH USERDB=david AND PASSWORDDB=password

SELECT node, way {
    name:es, name => name,
    addr:city, "Santiago de Compostela" => city,
    addr:street => street,
    ST_CENTROID(geom) => location
}
FROM (amenity=hospital OR amenity=clinic) TO Hospital
WHERE BBOX=(42.84866, -8.59242, 42.88672, -8.50325)

SELECT way {
    name, "Nombre" => name,
    website => web,
    addr:street => street,
    addr:city => city,
    toBoolean(smoking) => smoke,
    ST_CENTROID(geom) => location
}
FROM (amenity="bar") TO Cafeteria
WHERE BBOX=(42.84866, -8.59242, 42.88672, -8.50325)

SELECT node {
    name:es, name, "Nombre" => name,
    rooms => numberRooms,
    stars => numberStars,
    website => web,
    addr:city => city,
    addr:street => street,
    phone => telephone,
    toBoolean(pets_allowed) => petsAllowed,
    ST_CENTROID(geom) => location
}
FROM (tourism="hotel") TO Hotel
WHERE BBOX=(42.84866, -8.59242, 42.88672, -8.50325)
```


