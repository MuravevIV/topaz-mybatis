# Cookbook example of proper mybatis + spring data layer module

Features:

 * Annotations wherever possible
 * Mybatis mappers (instead of repositories)
 * Mybatis type handlers
 * Service layer around mybatis mappers
 * Spring Transactions
 * Spring Profiles (dev/production)
 * Acceptance tests (HSQLDB)
 * Mutation tests (Pitest)

How to:

Just ```mvn clean test```

Also see *pitest* profile in pom.xml
