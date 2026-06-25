# GitHub Explorer

**Student:** Tomi Bogoeski  
**Index:** 236024  
**Mentor:** Msc. Darko Sasanski  

## Project Topic

**Web Application for Exploring Public GitHub Repositories with Personal Watchlist Management**

GitHub Explorer is a Spring Boot web application that allows users to search public GitHub repositories through GitHub’s external REST API, view repository details, and save interesting repositories to a personal local watchlist.

The application is not only a basic CRUD system because it integrates with an external public API, specifically the GitHub REST API.

## Video Links

- **[Application showcase video](https://drive.google.com/file/d/1-DznsQwsxkcBHN7RP2LVkQG8ZXMD2x-r/view?usp=sharing)** 
- **[Architecture video](https://drive.google.com/file/d/1mGU33YDvZLn6ebYguptP_VuLU5hTXzp2/view?usp=sharing)** 


## Main Features

The application supports the following functionality:

- Searching public GitHub repositories
- Filtering search results by:
  - keyword
  - primary programming language
  - topic
  - minimum number of stars
- Paginated repository search results
- Viewing detailed information about a selected repository
- Displaying repository README content
- Rendering README Markdown using GitHub’s Markdown API
- User registration and login
- Personal watchlist for each registered user
- Saving repositories to the watchlist
- Editing a personal note, tag, and category for saved repositories
- Filtering the watchlist by tag
- Comparing current stars with stars at the time of saving
- Deleting repositories from the watchlist

## Technologies Used

- Java 21
- Spring Boot
- Spring MVC
- Thymeleaf
- Spring Data JPA
- H2 file-based database
- Bootstrap
- GitHub REST API

## Application Architecture

The application follows a simple Spring MVC architecture.

### Model Layer

The model layer contains local application entities and DTO classes.

Local entities are used for data stored in the application database, such as:

- `AppUser`
- `SavedRepository`

DTO classes are used for data received from the GitHub API, such as:

- `GitHubRepositoryDto`
- `GitHubSearchResponseDto`
- `GitHubReadmeDto`
- `GitHubOwnerDto`
- `GitHubLicenseDto`

### Repository Layer

The repository layer uses Spring Data JPA for database access.

- `AppUserRepository`
- `SavedRepositoryRepository`

These repositories are used to store and retrieve users and saved repositories from the local H2 database.

### Service Layer

The service layer contains the main application logic.

- `GitHubRepositoryService`
- `SavedRepositoryService`
- `UserService`

The GitHub service communicates with the external GitHub REST API by using Spring’s `RestClient`.

### Controller Layer

The controller layer handles HTTP requests from the browser.

- `HomeController`
- `RepositoryController`
- `WatchlistController`
- `AuthController`

Controllers receive user input, call service methods, and return Thymeleaf views.

### View Layer

The view layer is implemented with Thymeleaf templates.

The application uses a shared master template and several pages for:

- home/search
- repository results
- repository details
- login
- registration
- watchlist
- watchlist edit form

## Database

The application uses a file-based H2 database.

The database configuration is located in `application.properties`:

```properties
spring.datasource.url=jdbc:h2:file:./data/githubexplorer
