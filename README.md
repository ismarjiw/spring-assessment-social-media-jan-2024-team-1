# 🐦 Twitter API

## A Scalable and Robust Backend for a Twitter-like Application

<img src="https://i.imgur.com/BWV9P1B.png" alt="Twitter API Screenshot" width="600">

The Twitter API project is a RESTful Spring Boot application designed to emulate the backend infrastructure and functionality of a Twitter-like platform. With a focus on modularity, maintainability, and scalability, this project provides a solid foundation for building powerful social networking applications.

### Key Features

- **🗣️ User Management:** Create, update, and manage user accounts, ensuring secure authentication and authorization.
- **📢 Tweet Functionality:** Post, retrieve, and interact with tweets, including support for hashtags and mentions.
- **👥 Following and Followers:** Enable users to follow and unfollow other users, facilitating social networking and content discovery.
- **🔍 Search and Filtering:** Implement advanced search and filtering capabilities for tweets, hashtags, and users.
- **📊 Analytics and Metrics:** Collect and analyze data to gain insights into user engagement, content performance, and platform usage.

### Under the Hood

- **🛡️ Spring Boot:** Leveraged Spring Boot for building a robust and scalable RESTful backend.
- **📐 Clean Architecture:** Implemented a modular design with separate layers for controllers, services, data transfer objects (DTOs), and mappers, ensuring maintainability and extensibility.
- **📂 Spring Data JPA:** Utilized Spring Data JPA repositories for streamlined PostgreSQL database interactions and clean CRUD operations on User, Tweet, and Hashtag entities.
- **⚠️ Custom Exception Handling:** Implemented customized exception classes for scenarios such as resource not found, bad requests, and unauthorized access, providing informative error responses.
- **🧪 Extensive Testing:** Thoroughly tested the codebase with JUnit tests, ensuring comprehensive coverage of critical scenarios.
- **🚀 API Validation:** Validated API endpoints using Postman and automated testing with Newman for end-to-end API testing.

### Getting Started

To run the application locally, follow these steps:

1. Clone the repository: `git clone https://github.com/ismarjiw/spring-assessment-social-media-jan-2024-team-1.git`
2. Set up the PostgreSQL database and configure the connection settings.
3. Build and run the Spring Boot application.
4. Use Postman or a similar tool to interact with the API endpoints.

## Entity Relationship Diagram
![Spring Assessment ERD](https://user-images.githubusercontent.com/12191780/187276918-ccb2d373-be3b-42ff-a74d-5560ba806a10.png)

## API Endpoints

### `GET   validate/tag/exists/{label}`
Checks whether or not a given hashtag exists.

#### Response
```javascript
'boolean'
```

### `GET   validate/username/exists/@{username}`
Checks whether or not a given username exists.

#### Response
```javascript
'boolean'
```

### `GET   validate/username/available/@{username}`
Checks whether or not a given username is available.

#### Response
```javascript
'boolean'
```

### `GET     users`
Retrieves all active (non-deleted) users as an array.

#### Response
```javascript
['User']
```

### `POST    users`
Creates a new user. If any required fields are missing or the `username` provided is already taken, an error should be sent in lieu of a response.

If the given credentials match a previously-deleted user, re-activate the deleted user instead of creating a new one.

#### Request
```javascript
{
  credentials: 'Credentials',
  profile: 'Profile'
}
```

#### Response
```javascript
'User'
```

### `GET     users/@{username}`
Retrieves a user with the given username. If no such user exists or is deleted, an error should be sent in lieu of a response.

#### Response
```javascript
'User'
```


### `PATCH   users/@{username}`
Updates the profile of a user with the given username. If no such user exists, the user is deleted, or the provided credentials do not match the user, an error should be sent in lieu of a response. In the case of a successful update, the returned user should contain the updated data.

#### Request
```javascript
{
  credentials: 'Credentials',
  profile: 'Profile'
}
```

#### Response
```javascript
'User'
```

### `DELETE  users/@{username}`
"Deletes" a user with the given username. If no such user exists or the provided credentials do not match the user, an error should be sent in lieu of a response. If a user is successfully "deleted", the response should contain the user data prior to deletion.

**IMPORTANT:** This action does not actually drop any records from the database. 

#### Request
```javascript
'Credentials'
```

#### Response
```javascript
'User'
```

### `POST    users/@{username}/follow`
Subscribes the user whose credentials are provided by the request body to the user whose username is given in the url. If there is already a following relationship between the two users, no such followable user exists (deleted or never created), or the credentials provided do not match an active user in the database, an error should be sent as a response. If successful, no data is sent.

#### Request
```javascript
'Credentials'
```

### `POST    users/@{username}/unfollow`
Unsubscribes the user whose credentials are provided by the request body from the user whose username is given in the url. If there is no preexisting following relationship between the two users, no such followable user exists (deleted or never created), or the credentials provided do not match an active user in the database, an error should be sent as a response. If successful, no data is sent.

#### Request
```javascript
'Credentials'
```

### `GET     users/@{username}/feed`
Retrieves all (non-deleted) tweets authored by the user with the given username, as well as all (non-deleted) tweets authored by users the given user is following. This includes simple tweets, reposts, and replies. The tweets should appear in reverse-chronological order. If no active user with that username exists (deleted or never created), an error should be sent in lieu of a response.

#### Response
```javascript
['Tweet']
```

### `GET     users/@{username}/tweets`
Retrieves all (non-deleted) tweets authored by the user with the given username. This includes simple tweets, reposts, and replies. The tweets should appear in reverse-chronological order. If no active user with that username exists (deleted or never created), an error should be sent in lieu of a response.

#### Response
```javascript
['Tweet']
```

### `GET     users/@{username}/mentions`
Retrieves all (non-deleted) tweets in which the user with the given username is mentioned. The tweets should appear in reverse-chronological order. If no active user with that username exists, an error should be sent in lieu of a response.

A user is considered "mentioned" by a tweet if the tweet has `content` and the user's username appears in that content following a `@`.

#### Response
```javascript
['Tweet']
```

### `GET     users/@{username}/followers`
Retrieves the followers of the user with the given username. Only active users should be included in the response. If no active user with the given username exists, an error should be sent in lieu of a response.

#### Response
```javascript
['User']
```

### `GET     users/@{username}/following`
Retrieves the users followed by the user with the given username. Only active users should be included in the response. If no active user with the given username exists, an error should be sent in lieu of a response.

#### Response
```javascript
['User']
```

### `GET     tags`
Retrieves all hashtags tracked by the database.

#### Response
```javascript
['Hashtag']
```

### `GET     tags/{label}`
Retrieves all (non-deleted) tweets tagged with the given hashtag label. The tweets should appear in reverse-chronological order. If no hashtag with the given label exists, an error should be sent in lieu of a response.

A tweet is considered "tagged" by a hashtag if the tweet has `content` and the hashtag's label appears in that content following a `#`

#### Response
```javascript
['Tweet']
```

### `GET     tweets`
Retrieves all (non-deleted) tweets. The tweets should appear in reverse-chronological order.

#### Response
```javascript
['Tweet']
```

### `POST    tweets`
Creates a new simple tweet, with the author set to the user identified by the credentials in the request body. If the given credentials do not match an active user in the database, an error should be sent in lieu of a response.

The response should contain the newly-created tweet.

Because this always creates a simple tweet, it must have a `content` property and may not have `inReplyTo` or `repostOf` properties.

**IMPORTANT:** when a tweet with `content` is created, the server must process the tweet's content for `@{username}` mentions and `#{hashtag}` tags. 

#### Request
```javascript
{
  content: 'string',
  credentials: 'Credentials'
}
```

#### Response
```javascript
'Tweet'
```

### `GET     tweets/{id}`
Retrieves a tweet with a given id. If no such tweet exists, or the given tweet is deleted, an error should be sent in lieu of a response.

#### Response
```javascript
'Tweet'
```

### `DELETE  tweets/{id}`
"Deletes" the tweet with the given id. If no such tweet exists or the provided credentials do not match author of the tweet, an error should be sent in lieu of a response. If a tweet is successfully "deleted", the response should contain the tweet data prior to deletion.

**IMPORTANT:** This action does not actually drop any records from the database.

#### Request
```javascript
'Credentials'
```

#### Response
```javascript
'Tweet'
```

### `POST    tweets/{id}/like`
Creates a "like" relationship between the tweet with the given id and the user whose credentials are provided by the request body. If the tweet is deleted or otherwise doesn't exist, or if the given credentials do not match an active user in the database, an error should be sent. Following successful completion of the operation, no response body is sent.

#### Request
```javascript
'Credentials'
```

### `POST    tweets/{id}/reply`
Creates a reply tweet to the tweet with the given id. The author of the newly-created tweet should match the credentials provided by the request body. If the given tweet is deleted or otherwise doesn't exist, or if the given credentials do not match an active user in the database, an error should be sent in lieu of a response.

Because this creates a reply tweet, content is not optional. Additionally, notice that the `inReplyTo` property is not provided by the request. The server must create that relationship.

The response should contain the newly-created tweet.

**IMPORTANT:** when a tweet with `content` is created, the server must process the tweet's content for `@{username}` mentions and `#{hashtag}` tags.

#### Request
```javascript
{
  content: 'string',
  credentials: 'Credentials'
}
```

#### Response
```javascript
'Tweet'
```

### `POST    tweets/{id}/repost`
Creates a repost of the tweet with the given id. The author of the repost should match the credentials provided in the request body. If the given tweet is deleted or otherwise doesn't exist, or the given credentials do not match an active user in the database, an error should be sent in lieu of a response.

Because this creates a repost tweet, content is not allowed. Additionally, notice that the `repostOf` property is not provided by the request. The server must create that relationship.

The response should contain the newly-created tweet.

#### Request
```javascript
'Credentials'
```

#### Response
```javascript
'Tweet'
```

### `GET     tweets/{id}/tags`
Retrieves the tags associated with the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

#### Response
```javascript
['Hashtag']
```

### `GET     tweets/{id}/likes`
Retrieves the active users who have liked the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

Deleted users should be excluded from the response.

#### Response
```javascript
['User']
```

### `GET     tweets/{id}/context`
Retrieves the context of the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

**IMPORTANT:** While deleted tweets should not be included in the `before` and `after` properties of the result, transitive replies should. What that means is that if a reply to the target of the context is deleted, but there's another reply to the deleted reply, the deleted reply should be excluded but the other reply should remain.

#### Response
```javascript
'Context'
```

### `GET     tweets/{id}/replies`
Retrieves the direct replies to the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

Deleted replies to the tweet should be excluded from the response.

#### Response
```javascript
['Tweet']
```

### `GET     tweets/{id}/reposts`
Retrieves the direct reposts of the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

Deleted reposts of the tweet should be excluded from the response.

#### Response
```javascript
['Tweet']
```

### `GET     tweets/{id}/mentions`
Retrieves the users mentioned in the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

Deleted users should be excluded from the response.

#### Response
```javascript
['User']
```
