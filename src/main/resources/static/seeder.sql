use agentlink_dbl;

TRUNCATE agentlink_db.applications;
TRUNCATE agentlink_db.house_image;
TRUNCATE agentlink_db.houses;
TRUNCATE agentlink_db.open_house_events;
TRUNCATE agentlink_db.reviews;
TRUNCATE agentlink_db.users;


INSERT INTO agentlink_db.users (email, first_name, is_listing_agent, last_name, password, phone, team, username)
VALUES ('testThisEmail.com','John',1,'Doe','$2a$10$PesHwITP4osYEuj8z4sybu9L6bsfb.21PcTN55s/KKnKKnWk.cXBu','2105556162',
        'neptune','testUser');
INSERT INTO agentlink_db.users(email, first_name, is_listing_agent, last_name, password, phone, team, username)
VALUES ('newSeederEmail@mail.com','jane',1,'Doe','$2a$10$PesHwITP4osYEuj8z4sybu9L6bsfb.21PcTN55s/KKnKKnWk.cXBu',
    '2105556162',
    'neptune',
    'testUser2');

INSERT INTO agentlink_db.houses( address, city, description, image_url, state, zipcode, listing_agent_id, listing_active)
VALUES ('8229 Myrtle Court Suite 16','Austin','a test house for seeder','https://cdn.filestackcontent
.com/nt0WddqARnmVA43FroOy','Texas','78704',2,1);



INSERT INTO agentlink_db.open_house_events(date_end, date_start, feedback, house_id, review_id, host_agent_id)VALUES('2018-06-18 12:00:00','2018-06-18 10:00:00','great just testing',1,null,2);

INSERT INTO agentlink_db.reviews(date, description, rating, title, buying_agent_id, listing_agent_id)VALUES('2018-06-18 12:00:00','another test for reviews',5,'testingReviews',1,2);

INSERT INTO agentlink_db.applications(date, inquiry, open_house_event_id, buying_agent_id)VALUES ('2018-06-18 12:00:00','testing out applications',1,1);




