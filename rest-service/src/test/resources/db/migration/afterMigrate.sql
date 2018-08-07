INSERT INTO position (latitude, longitude, altitude) VALUES (10.0, 20.0, 30.0)
ON CONFLICT DO NOTHING;
INSERT INTO local_data (parameter, year, position_id, values) VALUES ('WIND_2M', 2016, 1, '{23.0,24.0, 16.0}')
ON CONFLICT DO NOTHING;
INSERT INTO local_data (parameter, year, position_id, values) VALUES ('SOLAR_DIRECT', 2016, 1, '{100.0,130.0, 200.0}')
ON CONFLICT DO NOTHING;
