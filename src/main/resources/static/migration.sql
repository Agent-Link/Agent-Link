CREATE USER agentlink_user@localhost IDENTIFIED BY 'p@$$w0rd';
GRANT ALL ON agentlink_db.* TO agentlink_user@localhost;


DROP DATABASE agentlink_db;


CREATE DATABASE IF NOT EXISTS agentlink_db;

# USE agentlink_db;
#
#
# CREATE TABLE users (
#                        id INT UNSIGNED NOT NULL AUTO_INCREMENT,
#                        username VARCHAR(150) NOT NULL,
#                        first_name VARCHAR(50) NOT NULL,
#                        last_name  VARCHAR(100) NOT NULL,
#                        user_email VARCHAR(150) NOT NULL UNIQUE,
#                        user_pass VARCHAR(150) NOT NULL,
#                        is_listing_agent BOOLEAN NOT NULL,
#                        phone VARCHAR(50) NOT NULL,
#                        team VARCHAR(150) NOT NULL,
#                        PRIMARY KEY (id)
# );
#
# # Testing insert
# INSERT INTO users (username, first_name, last_name, user_email, user_pass, is_listing_agent, phone, team)
# VALUES ('jimbo', 'Jim', 'Bob', 'jimbob@jimbob.com', '12345', false, '210-555-5555', 'Home Sellerz');
#
# CREATE TABLE houses (
#                         id INT UNSIGNED NOT NULL AUTO_INCREMENT,
#                         listing_agent_id INT UNSIGNED NOT NULL,
#                         address VARCHAR(150) NOT NULL,
#                         city VARCHAR(150) NOT NULL,
#                         state VARCHAR(50) NOT NULL,
#                         zipcode INT UNSIGNED NOT NULL,
#                         description VARCHAR(500) NOT NULL,
#                         FOREIGN KEY (listing_agent_id) REFERENCES users (id),
#                         PRIMARY KEY (id)
# );
#
# # Testing insert
# INSERT INTO houses (listing_agent_id, address, city, state, zipcode, description)
# VALUES (1, '555 randy st', 'san antonio', 'tx', '78218', 'woo wow wee wow');
#
# CREATE TABLE open_house_events (
#                         id INT UNSIGNED NOT NULL AUTO_INCREMENT,
# #                       default host is listing agent
#                         host_agent_id INT UNSIGNED NOT NULL,
#                         house_id INT UNSIGNED NOT NULL,
#                         date_of_event DATE NOT NULL,
#                         feedback TEXT,
#                         FOREIGN KEY (host_agent_id) REFERENCES users (id),
#                         FOREIGN KEY (house_id) REFERENCES houses (id),
#                         PRIMARY KEY (id)
# );
#
# CREATE TABLE applications (
#                         id INT UNSIGNED NOT NULL AUTO_INCREMENT,
#                         buying_agent_id INT UNSIGNED NOT NULL,
#                         open_house_event_id INT UNSIGNED NOT NULL,
#                         date_of_application DATE NOT NULL,
#                         inquiry TEXT,
#                         FOREIGN KEY (buying_agent_id) REFERENCES users (id),
#                         FOREIGN KEY (open_house_event_id) REFERENCES open_house_events (id),
#                         PRIMARY KEY (id)
# );
#
# CREATE TABLE reviews (
#                          id INT UNSIGNED NOT NULL AUTO_INCREMENT,
#                          listing_agent_id INT UNSIGNED NOT NULL,
#                          buying_agent_id INT UNSIGNED NOT NULL,
#                          date DATE NOT NULL,
#                          title VARCHAR(150) NOT NULL,
#                          description VARCHAR(500) NOT NULL,
#                          rating INT UNSIGNED NOT NULL,
#                          FOREIGN KEY (listing_agent_id) REFERENCES users (id),
#                          FOREIGN KEY (buying_agent_id) REFERENCES users (id),
#                          PRIMARY KEY (id)
# );
