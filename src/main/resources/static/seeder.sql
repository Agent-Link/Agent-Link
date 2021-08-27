USE agentlink_db;

# TRUNCATE agentlink_db.applications;
# TRUNCATE agentlink_db.reviews;
# TRUNCATE agentlink_db.open_house_events;
# TRUNCATE agentlink_db.houses;
# TRUNCATE agentlink_db.users;


INSERT INTO agentlink_db.users (email, first_name, last_name, is_listing_agent, password, team, username, phone)
VALUES /* 1 */('cody@codeup.com','Cody','Duck',true,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Code Homes','codytheduck','2105558941'),
    /* 2 */('YYoung489@yahoo.com','Yvone','Young',true,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Holden & Gateway Realty','Yyoung567','5124562491'),
    /* 3 */('reemaPhill@yahoo.com','Reema','Philips',true,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Garza Homes','reemaGold','4095568991'),
    /* 4 */('parsonsHarvey@gmail.com','Harvey','Parsons',true,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Draco Home Group','parsons77','8175546941'),
    /* 5 */('NayMac@yahoo.com','Nayla','Macleod',true,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Texas Cloud Home Teams','Macleod45','4305627899'),
    /* 6 */('orFox777@yahoo.com','Oriana','Fox',false,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Marson Homes','foxx456','2104455857'),
    /* 7 */('KnorRami@gmail.com','Rami','Knor',false,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Four Andrews Group','Rami89','4305516998'),
    /* 8 */('Ahmed.Zac222@yahoo.com','Zac','Ahmed',false,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Spring Team','JustZac789','9156694851'),
    /* 9 */('RyanRy@gmail.com','Ryan','Dickinson',false,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Fernandez Group','AlwaysRyan567','8062492345'),
    /* 10 */('Dekor.Gregor556@gmail.com','Gregor','Deckor',false,'$2a$10$Ka8b4vJbXPGsP8jeu5MKP.cwI1Gj5b5Bi4ipWpmIeVdk37b7uLDcy','Rowen Team','Deckor41','4305598991');


INSERT INTO agentlink_db.houses(address, city, description, image_url, listing_active, state, zipcode, listing_agent_id)
VALUES/* 1 */('11507 Sayanora Ct','San Antonio','4 bedroom 3 bathroom','https://cdn.filestackcontent.com/daufKGaRmG7HBXczjx4s',true,'Texas','78216',1),
    /* 2 */('227 Herweck Dr','Castle Hills','10 bedroom 3 bathroom','https://cdn.filestackcontent.com/DLpHGilcTnexlzj17dvg',true,'Texas','78213',1),
    /* 3 */('2312 Encino Xing','San Antonio','1 bedroom 3 bathroom','https://cdn.filestackcontent.com/ksNyiKniThCXvc7qf0zn',true,'Texas','78259',1),
    /* 4 */('15603 Trail Bluff St','San Antonio','4 bedroom 3 bathroom','https://cdn.filestackcontent.com/C2tjTklMScSXLA2RE9OU',true,'Texas','78247',1),
    /* 5 */('11119 Reverie Ln','San Antonio','Big backyard, 2 bedroom, 3 bathroom','https://cdn.filestackcontent.com/63syfdODR7eQCvfNvrCu',true,'Texas','78216',2),
    /* 6 */('9107 Contessa Dr','San Antonio','4 bedroom, 2 bathroom','https://cdn.filestackcontent.com/VMQxBMeLSjSsF8UGVmvj',true,'Texas','78216',3),
    /* 7 */('8913 Willmon Way','Windcrest','Townhouse, 3 bedroom, 2 bathroom','https://cdn.filestackcontent.com/ga8xJNcQqi4aGYgLpROt',true,'Texas','78239',4),
    /* 8 */('2111 Domal Ln','San Antonio','5 bedroom, 4 bathroom','https://cdn.filestackcontent.com/seKuDIq4QlGnMqdk1Feq',true,'Texas','78230',5),
    /* 9 */('230 Dwyer Ave UNIT 203','San Antonio','Downtown townhouse, 3 bedrooms,3 bathrooms','https://cdn.filestackcontent.com/OFTi6vIlSWiMuBY3ORdv',true,'Texas','78204',5);


INSERT INTO agentlink_db.reviews(date, description, rating, title, listing_agent_id, buying_agent_id)
VALUES/* 1 */('2021-08-19 15:00:00','Oriana was a great host!',5,'Great Job!',1,6),
    /* 2 */('2021-08-20 17:00:00','Would love to have Oriana host again!',4,'Awesome host!',1,6),
    /* 3 */('2021-05-20 15:37:00','Loved working with Oriana!',5,'Friendly!',2,6),
    /* 4 */('2021-02-21 17:00:00','NOT HAPPY WITH ORIANA, THEY DID NOT SHOW UP ON TIME!',1,'LATE!!',4,6),
    /* 5 */('2021-05-20 18:00:00','Rami did alright, he was 5 minutes late though.',3,'Went good',3,7),
    /* 6 */('2021-08-21 17:00:00','Ryan did a great job, would love to have them host again!',5,'Great host!',5,9);


INSERT INTO agentlink_db.open_house_events(date_start, date_end, feedback, house_id, review_id, host_agent_id)
VALUES/* 1 */('2021-08-18 12:00:00','2021-08-18 15:00:00','Event was great lots of interest',1,1,6),
    /* 2 */('2022-09-20 13:00:00','2022-09-20 17:00:00',null,1,null,1),
    /* 3 */('2021-08-23 14:00:00','2021-08-23 18:00:00','Went great! Potential buyers on this one, will be in contact!',2,null,6), -- do review in demo
    /* 4 */('2022-09-18 14:00:00','2022-09-18 18:00:00',null,2,null,1),
    /* 5 */('2021-08-19 10:00:00','2021-08-19 17:00:00','Not many people showed, was raining hard!',3,2,6),
    /* 6 */('2022-09-19 10:00:00','2022-09-19 17:00:00',null,3,null,1),
    /* 7 */('2021-08-09 10:00:00','2021-08-09 17:00:00',null,4,null,6), -- do review in demo
    /* 8 */('2022-09-03 10:00:00','2022-09-03 17:00:00',null,4,null,1),
    /* 9 */('2021-05-20 12:30:00','2021-05-20 15:00:00','Event went well',5,3,6),
    /* 10 */('2022-09-21 12:30:00','2022-09-21 15:00:00',null,5,null,2),
    /* 11 */('2021-05-18 12:00:00','2021-05-18 18:00:00',null,6,5,7),
    /* 12 */('2022-10-01 12:00:00','2022-10-01 18:00:00',null,6,null,3),
    /* 13 */('2021-02-20 12:00:00','2021-02-20 17:00:00',null,7,4,6),
    /* 14 */('2021-08-20 12:00:00','2021-08-20 17:00:00',null,8,6,9),
    /* 15 */('2022-10-20 12:00:00','2022-10-20 17:00:00',null,8,null,5),
    /* 16 */('2022-09-02 12:00:00','2022-09-02 17:00:00',null,9,null,5);



INSERT INTO agentlink_db.applications(date, inquiry, open_house_event_id, buying_agent_id)
VALUES('2021-09-18 15:00:00','Hi my name is Oriana, I would like to host your event.',2,6),
       ('2021-09-18 15:05:00','Hi my name is Oriana, I would like to host your event.',10,6),
       ('2021-09-19 12:05:00','I am Rami, I want to host your open house!.',2,7),
       ('2021-09-17 14:25:00','Hello I am Ryan, I would love to host your event!',2,9),
       ('2021-09-28 10:00:00','Hi my name is Oriana, I would like to host your event.',12,6),
       ('2021-09-19 12:00:00','Hello Cody, I am Rami, I want to host your open house!.',2,7),
       ('2021-08-13 16:00:0','Hi my name is Oriana, I would like to host your event.',1,6),
       ('2021-08-21 14:00:00','Hi my name is Oriana, I would like to host your event..',3,6),
       ('2021-08-14 15:20:00','Hi my name is Oriana, I would like to host your event.',5,6),
       ('2021-08-06 10:00:00','Hi my name is Oriana, I would like to host your event.',7,6),
       ('2021-05-17 12:30:00','Hi my name is Oriana, I would like to host your event.',9,6),
       ('2021-05-14 12:00:00','I am Rami, I want to host your open house!.',11,7),
       ('2021-02-17 12:00:00','Hi my name is Oriana, I would like to host your event.',13,6),
       ('2021-08-17 12:00:00','Hello I am Ryan, I would love to host your event!',14,9);




