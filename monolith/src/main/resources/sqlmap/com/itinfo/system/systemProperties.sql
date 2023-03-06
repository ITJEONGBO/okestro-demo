CREATE TABLE IF NOT EXISTS system_properties (
	property            VARCHAR(50) PRIMARY KEY NOT NULL
	, property_value    VARCHAR(200)          NOT NULL
);

INSERT INTO system_properties (property, property_value) 
SELECT 'id', '' FROM DUAL
WHERE NOT EXISTS (SELECT property, property_value FROM system_properties WHERE property = 'id');

INSERT INTO system_properties (property, property_value) 
SELECT 'password', '' FROM DUAL
WHERE NOT EXISTS (SELECT property, property_value FROM system_properties WHERE property = 'password');

INSERT INTO system_properties (property, property_value) 
SELECT 'ip', '' FROM DUAL
WHERE NOT EXISTS (SELECT property, property_value FROM system_properties WHERE property = 'ip');

INSERT INTO system_properties (property, property_value) 
SELECT 'vnc_ip', '' FROM DUAL
WHERE NOT EXISTS (SELECT property, property_value FROM system_properties WHERE property = 'vnc_ip');

INSERT INTO system_properties (property, property_value) 
SELECT 'vnc_port', '' FROM DUAL
WHERE NOT EXISTS (SELECT property, property_value FROM system_properties WHERE property = 'vnc_port');

INSERT INTO system_properties (property, property_value) 
SELECT 'cpu_threshold', '80' FROM DUAL
WHERE NOT EXISTS (SELECT property, property_value FROM system_properties WHERE property = 'cpu_threshold');

INSERT INTO system_properties (property, property_value) 
SELECT 'memory_threshold', '78' FROM DUAL
WHERE NOT EXISTS (SELECT property, property_value FROM system_properties WHERE property = 'memory_threshold');

INSERT INTO system_properties (property, property_value) 
SELECT 'grafana_uri', '' FROM DUAL
WHERE NOT EXISTS (SELECT property, property_value FROM system_properties WHERE property = 'grafana_uri');

INSERT INTO system_properties (property, property_value) 
SELECT 'deepLearning_uri', '' FROM DUAL
WHERE NOT EXISTS (SELECT property, property_value FROM system_properties WHERE property = 'deepLearning_uri');

INSERT INTO system_properties (property, property_value) 
SELECT 'symphony_power_controll', false FROM DUAL
WHERE NOT EXISTS (SELECT property, property_value FROM system_properties WHERE property = 'symphony_power_controll');

INSERT INTO system_properties (property, property_value)
SELECT 'login_limit', '5' FROM DUAL
WHERE NOT EXISTS (SELECT property, property_value FROM system_properties WHERE property = 'login_limit');