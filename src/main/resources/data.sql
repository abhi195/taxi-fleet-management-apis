-- H2 DB init

-- Create 3 OFFLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (1, now(), false, 'OFFLINE',
'pwd01', 'driver01');

insert into driver (id, date_created, deleted, online_status, password, username) values (2, now(), false, 'OFFLINE',
'pwd02', 'driver02');

insert into driver (id, date_created, deleted, online_status, password, username) values (3, now(), false, 'OFFLINE',
'pwd03', 'driver03');

-- Create 3 ONLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (4, now(), false, 'ONLINE',
'pwd04', 'driver04');

insert into driver (id, date_created, deleted, online_status, password, username) values (5, now(), false, 'ONLINE',
'pwd05', 'driver05');

insert into driver (id, date_created, deleted, online_status, password, username) values (6, now(), false, 'ONLINE',
'pwd06', 'driver06');

-- Create 1 OFFLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (7,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'OFFLINE',
'pwd07', 'driver07');

-- Create 1 ONLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (8,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'ONLINE',
'pwd08', 'driver08');

-- Create 3 manufacturers

insert into manufacturer (id, date_created, manufacturer_name, date_manufacturer_updated, deleted) values (1, now(),
'Mercedes-Benz', now(), false);

insert into manufacturer (id, date_created, manufacturer_name, date_manufacturer_updated, deleted) values (2, now(),
'Volkswagen', now(), false);

insert into manufacturer (id, date_created, manufacturer_name, date_manufacturer_updated, deleted) values (3, now(),
'BMW', now(), false);


-- Create 5 cars

insert into car (id, date_created, license_plate, seat_count, convertible, rating, engine_type, manufacturer_id,
date_car_updated, deleted)
values (1, now(), '1A-2B3C', 5, false, 2.2, 'GAS', 1, now(), false);

insert into car (id, date_created, license_plate, seat_count, convertible, rating, engine_type, manufacturer_id,
date_car_updated, deleted)
values (2, now(), '1X-2Y3Z', 7, false, 3.5, 'ELECTRIC', 2, now(), false);

insert into car (id, date_created, license_plate, seat_count, convertible, rating, engine_type, manufacturer_id,
date_car_updated, deleted)
values (3, now(), '1L-2M3N', 2, true, 4.8, 'ELECTRIC', 3, now(), false);

insert into car (id, date_created, license_plate, seat_count, convertible, rating, engine_type, manufacturer_id,
date_car_updated, deleted)
values (4, now(), '1D-2E3F', 5, false, 1.8, 'GAS', 2, now(), true);

insert into car (id, date_created, license_plate, seat_count, convertible, rating, engine_type, manufacturer_id,
date_car_updated, deleted)
values (5, now(), '1I-2J3K', 4, true, 4.8, 'ELECTRIC', 1, now(), false);

-- Create 2 ONLINE drivers with car assigned

insert into driver (id, date_created, deleted, online_status, password, username, car_id)
values (9, now(), false, 'ONLINE', 'pwd09', 'driver09', 1);

insert into driver (id, date_created, deleted, online_status, password, username, car_id)
values (10, now(), false, 'ONLINE', 'pwd10', 'driver10', 3);
