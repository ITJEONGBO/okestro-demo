CREATE TABLE IF NOT EXISTS properties (
    property VARCHAR(50) PRIMARY KEY NOT NULL,
    property_value VARCHAR(200) NOT NULL
);



INSERT INTO properties (property, property_value)
SELECT 'id', '' as test
    WHERE NOT EXISTS (SELECT property, property_value FROM properties WHERE property = 'id');

INSERT INTO properties (property, property_value)
SELECT 'password', '' as test
    WHERE NOT EXISTS (SELECT property, property_value FROM properties WHERE property = 'password');

INSERT INTO properties (property, property_value)
SELECT 'ip', '' as test
    WHERE NOT EXISTS (SELECT property, property_value FROM properties WHERE property = 'ip');

INSERT INTO properties (property, property_value)
SELECT 'login_limit', '5' as test
    WHERE NOT EXISTS (SELECT property, property_value FROM properties WHERE property = 'login_limit');


INSERT INTO properties (property, property_value)
SELECT 'vnc_ip', '' as test
    WHERE NOT EXISTS (SELECT property, property_value FROM properties WHERE property = 'vnc_ip');

INSERT INTO properties (property, property_value)
SELECT 'vnc_port', '' as test
    WHERE NOT EXISTS (SELECT property, property_value FROM properties WHERE property = 'vnc_port');


INSERT INTO properties (property, property_value)
SELECT 'cpu_threshold', '80' as test
    WHERE NOT EXISTS (SELECT property, property_value FROM properties WHERE property = 'cpu_threshold');

INSERT INTO properties (property, property_value)
SELECT 'memory_threshold', '78' as test
    WHERE NOT EXISTS (SELECT property, property_value FROM properties WHERE property = 'memory_threshold');
