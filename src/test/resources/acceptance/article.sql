insert into "user" ("username", "password", "role", "firstname", "lastname") values ('johnDoe', '$2a$10$Xsi0c977I0vxI3Et88J3HOIeXYHhBKEOey49U8rx3MJnqrPHx4wEq', 'USER', 'John', 'Doe');

insert into "article" ("title", "headline", "content", "author_id", "slug", "added_at") values ('Spring Boot Guide', 'Lorem', 'dolor sit amet', (select "id" from "user" where "username" = 'johnDoe'), 'spring-boot-guide', NOW());
insert into "article" ("title", "headline", "content", "author_id", "slug", "added_at") values ('Kotlin Tips', 'Ipsum', 'dolor sit amet', (select "id" from "user" where "username" = 'johnDoe'), 'kotlin-tips', NOW());
