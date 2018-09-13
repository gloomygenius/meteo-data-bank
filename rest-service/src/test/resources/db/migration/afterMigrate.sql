INSERT INTO position (position_id, latitude, longitude, altitude) VALUES (nextval('position_id_seq'), 10.0, 20.0, 30.0)
ON CONFLICT DO NOTHING;
INSERT INTO local_data (data_meta_info_id, year, position_id, payload) VALUES (1, 2016, 1, '{100.0,130.0, 200.0}')
ON CONFLICT DO NOTHING;
