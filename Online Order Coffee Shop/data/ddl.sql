-- Create the database.
create database if not exists cs4370_final;

-- Use the created database.
use cs4370_final;

-- Create the customer table.
create table if not exists customer (
    username varchar(20) not null,
    password varchar(255) not null,
    primary key (username),
    unique (username),
    constraint username_min_length check (char_length(trim(username)) >= 2)
);

create table if not exists orders (
    order_id int auto_increment,        
    created_at datetime,
    username varchar(20),   
    primary key (order_id),     
    foreign key (username) references customer(username)
);

create table if not exists recipe (
    rec_id varchar(10) not null,  
    primary key (rec_id)
);

create table if not exists ingredients (     
    ing_id char(6),     
    ing_name varchar(30) not null,  
    ing_weight int not null,
    ing_meas varchar(10) not null,
    ing_price decimal (4,2) not null, 
    primary key (ing_id)
);

create table if not exists recipe_ingredients (
    rec_id varchar(10) not null,  
    ing_id char(6),  
    quantity int not null,
    primary key (rec_id, ing_id), 
    foreign key (rec_id) references recipe(rec_id)
);

create table if not exists items (
    item_id char(5),     
    rec_id varchar(10) not null,     
    item_name varchar(30) not null,     
    item_cat varchar(15) not null, 
    item_size varchar(10),     
    item_price decimal(4,2) not null,
    primary key (item_id), 
    foreign key (rec_id) references recipe(rec_id)
);

create table if not exists items_orders (
    order_id int,    
    item_id char(5),
    quantity int not null,
    primary key (order_id, item_id, quantity),     
    foreign key (order_id) references orders(order_id), foreign key(item_id) references items(item_id)
);

create table if not exists cart (
    username varchar(20),    
    item_id char(5),
    quantity int not null,
    primary key (username, item_id),     
    foreign key (username) references customer(username), foreign key(item_id) references items(item_id)
);

create table if not exists inventory (     
    inv_id char(6), 
    ing_id char(6) not null, 
    quantity int not null,
    primary key (inv_id),
    foreign key (ing_id) references ingredients(ing_id)
);

create table if not exists review (     
    review_id int auto_increment, 
    username varchar(20) not null,
    item_id char(5),   
    review_date datetime,
    review_text varchar(255) not null, 
    primary key (review_id),
    foreign key (username) references customer(username),
    foreign key (item_id) references items(item_id)
);
