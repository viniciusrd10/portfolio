INSERT INTO event (event_type, status, created_date)
VALUES ('appliance.running', 'ENABLED', current_timestamp),
       ('appliance.waiting', 'DISABLED', current_timestamp),
       ('appliance.fail', 'ENABLED', current_timestamp),
       ('appliance.test', 'ENABLED', current_timestamp),
       ('appliance.charging', 'ENABLED', current_timestamp),
       ('appliance.low-batery', 'ENABLED', current_timestamp),
       ('appliance.on', 'ENABLED', current_timestamp),
       ('appliance.off', 'ENABLED', current_timestamp);