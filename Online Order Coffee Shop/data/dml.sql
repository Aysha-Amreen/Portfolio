--localhost:8081/login
-- User Authentication
SELECT * FROM user WHERE username = ?;

--localhost:8081/login
-- Get Specific User
SELECT * FROM user WHERE userId = ?;
--localhost:8081/post/{userId}
        
--http://localhost:8081/register
-- Register User
INSERT INTO user (username, password, firstName, lastName) VALUES (?, ?, ?, ?);

--localhost:8081/ (homepage)
-- Create Post
INSERT INTO post (userId, postDate, postText) VALUES (?, NOW(), ?);

--http://localhost:8081/people
-- Last Activity -- this is for formatting the data to month date year
SELECT * FROM post WHERE userId = ? ORDER BY postDate DESC;

--http://localhost:8081/post/{postId}
-- Get Posts by user's postId 
SELECT * FROM post WHERE postId = ?

--http://localhost:8081/people
-- Get Followable Users
SELECT * FROM user WHERE NOT userId = ?;

--http://localhost:8081/people
-- Follow a User
INSERT INTO follow (followerUserId, followeeUserId) VALUES(?, ?);

--http://localhost:8081/people
-- Unfollow a User
DELETE FROM follow WHERE (followerUserId, followeeUserId) = (?, ?);

--http://localhost:8081/people
-- Get Followed Users
SELECT userId, firstName, lastName, followerUserId FROM user, follow WHERE userId = followeeUserId AND followerUserId = ?;

--http://localhost:8081/post/{postId}
-- Check if a Post is Hearted by User
SELECT * FROM heart WHERE userId = ?;

--http://localhost:8081/post/{postId}
-- Add a Heart to a Post
INSERT INTO heart (postId, userId) VALUES(?, ?);

--http://localhost:8081/post/{postId}
-- Remove a Heart from a Post
DELETE FROM heart WHERE (postId, userId) = (?, ?);

--http://localhost:8081/post/{postId}
-- Count Hearts for a Post
SELECT * FROM heart WHERE postId = ?;

--http://localhost:8081/post/{postId}
-- Bookmark a Post
INSERT INTO bookmark (postID, userId) VALUES (?, ?);

--http://localhost:8081/post/{postId}
-- Remove a Bookmark from a Post
DELETE FROM bookmark WHERE (postID, userId) = (?, ?);

--http://localhost:8081/post/{postId}
-- Create a Comment
INSERT INTO comment (postId, userId, commentDate, commentText) VALUES (?, ?, NOW(), ?);

--http://localhost:8081/post/{postId}
-- Find Comments by Post ID
SELECT * FROM comment c, user u WHERE c.userId = u.userId AND c.postId = ? ORDER BY c.commentDate DESC;

-- http://localhost:8081/bookmarks
-- find posts bookmarked by the current user
SELECT * FROM bookmark WHERE userId = ?;

-- http://localhost:8081/post/{postId}
-- create new bookmark relation.
INSERT INTO bookmark (postID, userId) VALUES (?, ?);

-- http://localhost:8081/post/{postId}
-- remove a bookmark relation.
DELETE FROM bookmark WHERE postID = ? AND userId = ?;

-- http://localhost:8081/
-- find most recent post by user
SELECT * FROM post WHERE userId = ? ORDER BY CAST(postId AS UNSIGNED) DESC LIMIT 1;

-- http://localhost:8081/post/{postId}
-- create new hashtag relation.
INSERT INTO hashtag (hashTag, postID) VALUES (?, ?);

-- http://localhost:8081/hashtagsearch?hashtags={hashTag}
-- selecting posts that have all specified hashtags
SELECT p.postId FROM post p WHERE EXISTS (SELECT 1 FROM hashtag h? WHERE h?.postId = p.postId AND h?.hashTag = ?) ORDER BY p.postDate DESC;
        
        
