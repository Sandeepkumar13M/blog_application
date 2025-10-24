The blog application is fully working
- If you want to change the role ADMIN or AUTHOR  before registering, we have to change the role in "CustomUserDetailsService"
  ```
     user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("AUTHOR"); // Default role for new users, we can set as ADMIN also
        return userRepository.save(user);
    }
  ```
  - If you have a local PostgreSQL connection, you can connect in " application.properties "; if you have an online database connection that is also mentioned in   " application.properties "
  - In this project, I used the Render database.
  
## Project: Blog Application

### Part 1: CRUD

Blog application using Spring Boot, Thymeleaf and Spring Data JPA: CRUD

(This project contains **pagination**, **search**, **filtering** and **sorting**)

In project we make is a basic blog with features of posts and comments. We are skipping authentication for now. However all unauthorized actions or use cases below will be replaced with authentication in next project using spring boot.

This project uses following technologies

- Spring MVC as MVC framework
- Spring Data JPA for Database Connectivity
- Thymeleaf for theming and templates

#### Use Cases:

- Non logged in user should be able to read a list of blog posts that has title, excerpt, author, published DateTime and tags.
- Non logged in user should be able to read full blog post that has title, content, author, published DateTime and tags.
- Non logged in user should be able to filter blog posts using author, published DateTime and tags
- Non logged in user should be able to sort blog posts using the **published DateTime**
- Non logged in user should be able to search blog posts using full text search on title, content, author and tags
- Non logged in user should be able to navigate to different pages on blog list where each page lists maximum 10 blog posts.
- Non logged in user should be able to create blog post that has title, content, author, published DateTime and tags
- Non logged in user should be able to update a blog post that has title, content, author, published DateTime  and tags
- Non logged in user should be able to delete a blog post that has title, content, author, published DateTime  and tags
- Non logged in user should be able to comment on blog posts using comment form that contains name, email and comment
- Non logged in user should be able to read, update and delete comments.

#### Database Schema:

- User
    - id
    - name
    - email
    - password
- Posts
    - id
    - title
    - excerpt
    - content
    - author
    - published_at
    - is_published
    - created_at
    - updated_at
- tags
    - id
    - name
    - created_at
    - updated_at
- post_tags
    - post_id
    - tag_id
    - created_at
    - updated_at
- comments
    - id
    - name
    - email
    - comment
    - post_id
    - created_at
    - updated_at

#### Steps to make:

1. Make the HTML and CSS
2. Create database schema / Make database ready
3. Start replacing HTML & CSS with thymeleaf
4. and connect data with frontend using Spring MVC and Spring Data JPA

#### Steps M2:

1. Finish author and tags
2. Comments
3. Pagination on `/` in this format for for
    1. page 1: `http://abc.com/?start=1&limit=10`
    2. page 2: `http://abc.com/?start=11&limit=10`
    3. page 3: `http://abc.com/?start=21&limit=10`
4. Filters on `/`  in this fomat `http://abc.com/?authorId="1"&tagId="1"&tagId="2"`
5. Sorting on `/` in this format `http://abc.com/?sortField="publishedAt"&order="asc"`
6. Searching on `/` in this format `http://abc.com/?search="mountblue"` search should pick the union of all matches in title, content, tags, author and excerpt.

#### Steps M3:

- Authentication
- Authorization

`<input type="hidden" name="postId" value="1" />`

### Part 2: Authentication & Authorization

Blog application using Spring Boot, Spring Data JPA, Spring Security: Authentication & Authorization

- Thymeleaf
- pagination
- search
- filtering
- sorting
- Authentication and Authorization

In this project we make the same blog that has features like posts and commenting. Additionally we are adding authentication using Spring Security. All unauthorized actions or use cases below will have spring security's authorization feature using various roles.

This project using Spring Boot and uses following technologies of spring boot

- Spring MVC (Spring Boots configuration without XML)
- Spring Data JPA
- Spring Security
- Thymeleaf for theming and templates

#### Use Cases:

- Non logged in user and logged in user should be able to read a list of blog posts that has title, excerpt, author, published date & Time and tags.
- Non logged and logged in user in user should be able to read full blog post that has title, content, author, published date & Time  and tags.
- Non logged and logged in user in user should be able to filter blog posts that using author, published date & Time  and tags
- Non logged and logged in user should be able to sort blog posts using the published date
- Non logged and logged in user should be able to search blog posts using full text search on title, content, author and tags
- Non logged and logged in user should be able to navigate to different pages on blog list where each page lists maximum 10 blog posts.
- Logged in user with a role of **author** should be able to create blog post that has title, content, author (**disabled and contains his own name**), published date & Time  and tags.
- Logged user with a role of **author** should be able to update his own blog posts that has title, content, author  (**disabled and contains his own name**), published date & Time  and tags
- Logged in user with a role of **author** should be able to delete his own blog posts that has title, content, author (**disabled and contains his own name**), published date & Time  and tags
- Non logged and logged in user in user should be able to comment on blog posts using comment form that contains name, email and comment
- Logged in user should be able to update and delete comments made on his own blog posts.
- Logged in user with a role of **admin** should be able to create blog post that has title, content, author (**can be any author**), published date & Time  and tags.
- Logged in user with a role of **admin** should be able to update blog post that has title, content, author (**can be any author**), published date & Time  and tags.
- Logged in user with a role of **admin** should be able to delete blog post that has title, content, author (**can be any author**), published date & Time  and tags.

### Part 3: Deployment on Heroku & AWS

### Part 4: REST APIs for all features

REST APIs:

* All CRUD operations
* Pagination
* Search
* Filtering
* Sorting
* Authentication and Authorization

<img width="2878" height="1763" alt="Screenshot 2025-10-24 175756" src="https://github.com/user-attachments/assets/f862be8f-5669-4439-83e3-f2283a17d405" />
<img width="2880" height="1800" alt="Screenshot 2025-10-24 180432" src="https://github.com/user-attachments/assets/07be0107-784b-4c9c-9b25-9b1758a644db" />
<img width="2847" height="1790" alt="Screenshot 2025-10-24 180404" src="https://github.com/user-attachments/assets/6dd386b1-5402-4c42-abfb-6a2d1a8864fd" />
<img width="2879" height="1742" alt="Screenshot 2025-10-24 180343" src="https://github.com/user-attachments/assets/f3a6700d-b898-4835-afb6-ac8233db0836" />
<img width="2872" height="1782" alt="Screenshot 2025-10-24 180222" src="https://github.com/user-attachments/assets/eeb8d2d0-8763-48f7-a784-b8925efb1bf3" />
<img width="2857" height="1799" alt="Screenshot 2025-10-24 180015" src="https://github.com/user-attachments/assets/19df87bc-0c6b-49ff-addf-fbb2bb3fe282" />
<img width="2879" height="1796" alt="Screenshot 2025-10-24 175826" src="https://github.com/user-attachments/assets/6ee1cc28-59d7-4776-9855-bea34d6ff571" />
<img width="2879" height="1799" alt="Screenshot 2025-10-24 183903" src="https://github.com/user-attachments/assets/b3d1dd09-549f-4c80-98f3-8f0cfcb890fa" />
<img width="2862" height="1624" alt="image" src="https://github.com/user-attachments/assets/16ddb202-534c-45e9-8eaf-b8362c6d8209" />
<img width="2860" height="1446" alt="image" src="https://github.com/user-attachments/assets/447fe695-ac84-4a2d-8c35-c86529833b23" />



