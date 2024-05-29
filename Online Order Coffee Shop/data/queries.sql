-- Queries from CartService

--localhost:8081/
--Retrieves all items in the user's cart and their amounts
select * from cart where username = ?;

--localhost:8081/
--Adds a new item to the cart for the logged-in user.
INSERT INTO cart (username, item_id, quantity) VALUES (?, ?, ?);

--localhost:8081/
--Removes an existing item from the cart of the logged-in user.
delete from cart where (username, item_id) = (?, ?);

--localhost:8081/
--clears the cart of the logged-in user.
select * from cart where username = ?;

--localhost:8081/
--Retrieves all items in the user's cart and their amounts
select * cart where username = ?;

--localhost:8081/
--Retrieves the price of all items in the user's cart
select (c.quantity * i.price) as total from cart c, item i where c.item_id = i.item_id and c.username = ?;

-- Queries from ItemService

--localhost:8081/
--Retrieves the specified item from an item id.
select * from items where item_id = ?;

--localhost:8081/
-- Retrieves all items that use a specified ingredient.
select * from items it, recipe_ingredients ri, ingredients ing where it.rec_id = ri.rec_id and ri.ing_id = ing.ing_id and it.item_id = ?;

--localhost:8081/
--Retrieves the top 5 ordered items 
select i.item_id, count(io.item_id) as order_count from items_orders io join items i on io.item_id = i.item_id group by i.item_id order by order_count desc limit 5;

--localhost:8081/
--Retrieves all orderable items
select * from items;

--localhost:8081/
--Retrieves all ordered items from a specific category
select * from items where item_cat = ?;

--localhost:8081/
--Retrives recipe information for a specific item
select * from recipe_ingredients ri, items i, ingredients ing where ri.rec_id = i.rec_id and ri.ing_id = ing.ing_id and i.item_id = ?;
        
--localhost:8081/
--Returns a specific item based on id
 SELECT * FROM items WHERE item_id = ?;

--localhost:8081/
--Returns a specific item's reviews made by any user
SELECT * FROM reviews WHERE review_id = ?;

-- Queries from OrderService

--localhost:8081/
--start a new order
INSERT INTO orders (username, created_at) VALUES (?, NOW());

--localhost:8081/
--fetch the most recent order that was just made
select order_id from orders where username = ? order by created_at desc limit 1;

--localhost:8081/
--Adds a new item to the items_orders table
INSERT INTO items_orders (order_id, item_id, quantity) VALUES (?, ?, ?);

--localhost:8081/
--returns a table with ingredient id, cart item quantity, recipe_ingredient's ingredient quantity, and inventory ingredient quantity
select inv.ing_id as ing_id, c.quantity as cq, ri.quantity as rq, inv.quantity as iq from cart c, items i, recipe_ingredients ri, inventory inv where c.username = ? and c.item_id = i.item_id and i.rec_id = ri.rec_id and inv.ing_id = ri.ing_id;

--localhost:8081/
--Updates the quantity in inventory of a single specified ingredient
UPDATE inventory SET quantity = ? WHERE ing_id = ?;

--localhost:8081/
--Updates the entire inventory based on all items in a user's cart 
UPDATE inventory SET quantity = ? WHERE ing_id = ?;

-- Queries from ReviewService

--localhost:8081/
--Creates a new review with the given content for the logged-in user.
INSERT INTO review (username, item_id, review_date, review_text) VALUES (?, ?, NOW(), ?);

--localhost:8081/
--Returns all reviews made by a specific user
select * from reviews where username = ? order by review_date desc;

-- Queries from UserService

--localhost:8081/login
-- User Authentication
select * from customer where username = ?;

--localhost:8081/login
-- Get Specific User
select * from customer where username = ?;

--localhost:8081/login
-- Get Specific User 
 insert into customer (username, password) values (?, ?);