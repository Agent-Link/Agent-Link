use agentlink_db;

# TRUNCATE agentlink_db.applications;
# TRUNCATE agentlink_db.houses;
# TRUNCATE agentlink_db.open_house_events;
# TRUNCATE agentlink_db.reviews;
# TRUNCATE agentlink_db.users;


INSERT INTO agentlink_db.users (email, first_name, last_name, is_listing_agent, password, team, username, phone)
VALUES('whittaker456@gmail.com','Arianna','Whittaker',TRUE,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Stephens Legacy Group','Arianna33',2105568941),
       ('YYoung489@yahoo.com','Yvone','Young',TRUE,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Holden & Gateway Realty','Yyoung567','5124562491'),
       ('reemaPhill@yahoo.com','Reema','Philips',TRUE,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Garza Homes','reemaGold',4095568991),
       ('parsonsHarvey@gmail.com','Harvey','Parsons',TRUE,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Draco Home Group','parsons77',8175546941),
       ('NayMac@yahoo.com','Nayla','Macleod',TRUE,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Texas Cloud Home Teams','Macleod45',4305627899),
       ('orFox777@yahoo.com','Oriana','Fox',FALSE,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Marson Homes','foxx456',2104455857),
       ('KnorRami@gmail.com','Rami','Knor',FALSE,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Four Andrews Group','Rami89',4305516998),
       ('Ahmed.Zac222@yahoo.com','Zac','Ahmed',FALSE,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Spring Team','JustZac789',9156694851),
       ('RyanRy@gmail.com','Ryan','Dickinson',FALSE,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Fernandez Group','AlwaysRyan567',8062492345),
       ('Dekor.Gregor556@gmail.com','Gregor','Deckor',FALSE,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Rowen Team','Deckor41',4305598991);


INSERT INTO agentlink_db.houses(address, city, description, image_url, listing_active, state, zipcode, listing_agent_id)
VALUES('995 Princeton Rd','Corpus Christi','4 bedroom 3 bathroom','https://cdn.filestackcontent.com/uiZ9HRk6Q6urwjD4e6qG',true,'texas',78415,1),
        ('7369C W. Beaver Ridge Street','McAllen','Big backyard, 2 bedroom, 3 bathroom','https://cdn.filestackcontent.com/uiZ9HRk6Q6urwjD4e6qG',true,'texas',78501,2),
        ('548 Johnson Dr.','Friendswood','smaller town house, 2 bedroom, 1 bathroom','https://cdn.filestackcontent.com/uiZ9HRk6Q6urwjD4e6qG',true,'texas',77546,3),
        ('754 Trenton St.','Desoto','countryside suburb, 4 bedroom, 3 bathroom','https://cdn.filestackcontent.com/uiZ9HRk6Q6urwjD4e6qG',true,'texas',75115,4),
        ('196 Military St.','Pharr','5 bedroom, 4 bathroom','https://cdn.filestackcontent.com/uiZ9HRk6Q6urwjD4e6qG',true,'texas',78577,5),
        ('60 Glenholme Avenue','Corpus Christi','6 bedrooms,3 bathrooms','https://cdn.filestackcontent.com/uiZ9HRk6Q6urwjD4e6qG',true,'texas',78412,6);



INSERT INTO agentlink_db.open_house_events(date_end, date_start, feedback, house_id, review_id, host_agent_id)
VALUES('2018-06-18 12:00:00','2018-06-18 10:00:00','great just testing',1,null,1),
       ('2019-05-20 12:30:00','2019-05-18 15:00:00','another test',2,null,2),
       ('2020-05-18 12:00:00','2020-05-18 10:00:00','another great test',3,null,3),
       ('2020-02-20 12:00:00','2020-02-20 10:00:00','seeder test',4,null,4);

# this will be null we have to manual put in reviews
# INSERT INTO agentlink_db.reviews(date, description, rating, title, buying_agent_id, listing_agent_id)
# VALUES('2018-06-18 10:00:00','another test for reviews',5,'testingReviews',null,null),
#        ('2019-05-18 15:00:00','another test for reviews',4,'testingReviews',null,null),
#        ('2020-05-18 10:00:00','another test for reviews',4,'testingReviews',null,null),
#        ('2020-02-20 10:00:00','another test for reviews',5,'testingReviews',null,null);

INSERT INTO agentlink_db.applications(date, inquiry, open_house_event_id, buying_agent_id)
VALUES('2018-06-18 10:00:00','Id like to host your event',1,6),
       ('2019-05-18 15:00:00','Im available',2,7),
       ('2020-05-18 10:00:00','Gonna need some help',3,8),
       ('2020-02-20 10:00:00','where to begin',4,9);




