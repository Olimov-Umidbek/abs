INSERT INTO public.users (id,"name",surname,status,balance,"version",created_at,updated_at) VALUES
	 ('69f03a72-ddbd-419c-a97c-db1e8b3a216b'::uuid,'Dima','Vadimov','ACTIVE',1700,1,'2025-03-20 11:18:19.594426','2025-03-20 11:18:19.594427'),
	 ('01bc38e2-539c-4a9e-b180-d26f78e0222c'::uuid,'Test','Surname','ACTIVE',10200,2,'2025-03-20 11:19:27.66137','2025-03-20 11:19:27.661372'),
	 ('8a42859f-0861-4b1c-9ad3-8e99d25da6de'::uuid,'Sergei','Kochkarin','ACTIVE',600,4,'2025-03-20 11:19:55.788271','2025-03-20 11:19:55.788275'),
	 ('701b6844-e125-40f8-9040-4ffc786ec0e6'::uuid,'Anton','Davidov','ACTIVE',9300,5,'2025-03-20 11:19:43.497567','2025-03-20 11:19:43.497572');

CREATE TABLE public.transactions_2025_03_17_to_2025_03_23 PARTITION OF public.transactions  FOR VALUES FROM ('2025-03-17 00:00:00') TO ('2025-03-24 00:00:00');
CREATE TABLE public.transactions_2025_03_24_to_2025_03_30 PARTITION OF public.transactions  FOR VALUES FROM ('2025-03-24 00:00:00') TO ('2025-03-31 00:00:00');

CREATE TABLE public.transaction_histories_2025_03_17_to_2025_03_23 PARTITION OF public.transaction_histories  FOR VALUES FROM ('2025-03-17 00:00:00') TO ('2025-03-24 00:00:00');
CREATE TABLE public.transaction_histories_2025_03_24_to_2025_03_30 PARTITION OF public.transaction_histories  FOR VALUES FROM ('2025-03-24 00:00:00') TO ('2025-03-31 00:00:00');


INSERT INTO public.transactions (id,amount,status,sender_id,receiver_id,created_at,updated_at) VALUES
	 ('b260f9b8-30dc-4b9a-938f-9054f58ddbe6'::uuid,200,'COMPLETED','701b6844-e125-40f8-9040-4ffc786ec0e6'::uuid,'8a42859f-0861-4b1c-9ad3-8e99d25da6de'::uuid,'2025-03-20 11:20:51.909448','2025-03-20 11:20:52.203438'),
	 ('5920ccfa-3f21-4b3c-9e45-57f050c275f3'::uuid,500,'COMPLETED','701b6844-e125-40f8-9040-4ffc786ec0e6'::uuid,'69f03a72-ddbd-419c-a97c-db1e8b3a216b'::uuid,'2025-03-20 11:21:18.281127','2025-03-20 11:21:18.36075'),
	 ('c5c401f7-7597-440d-ac6d-e4738c3a54eb'::uuid,300,'COMPLETED','01bc38e2-539c-4a9e-b180-d26f78e0222c'::uuid,'8a42859f-0861-4b1c-9ad3-8e99d25da6de'::uuid,'2025-03-20 11:21:54.17958','2025-03-20 11:21:54.251609'),
	 ('a2a913eb-77b6-43e0-9520-5eff23bdf823'::uuid,300,'ERROR','01bc38e2-539c-4a9e-b180-d26f78e0222c'::uuid,'8a42859f-0861-4b1c-9ad3-8e99d25da6de'::uuid,'2025-03-20 11:21:59.88097','2025-03-20 11:22:00.26042'),
	 ('d005375c-da98-404d-a560-2cd9b0f64e7b'::uuid,10000,'COMPLETED','701b6844-e125-40f8-9040-4ffc786ec0e6'::uuid,'01bc38e2-539c-4a9e-b180-d26f78e0222c'::uuid,'2025-03-20 11:22:27.212854','2025-03-20 11:22:27.31408'),
	 ('f588caa5-51c2-4517-9550-6a321e1d8f5e'::uuid,300,'COMPLETED','701b6844-e125-40f8-9040-4ffc786ec0e6'::uuid,'8a42859f-0861-4b1c-9ad3-8e99d25da6de'::uuid,'2025-03-20 11:26:31.789401','2025-03-20 11:26:31.900536'),
	 ('a53a0490-6d40-44e3-825d-edc2f6f70636'::uuid,1000,'ERROR','8a42859f-0861-4b1c-9ad3-8e99d25da6de'::uuid,'701b6844-e125-40f8-9040-4ffc786ec0e6'::uuid,'2025-03-20 11:26:58.125792','2025-03-20 11:26:58.321999'),
	 ('55c893b1-12c3-40fe-9b3e-eb5db9be63dc'::uuid,300,'COMPLETED','8a42859f-0861-4b1c-9ad3-8e99d25da6de'::uuid,'701b6844-e125-40f8-9040-4ffc786ec0e6'::uuid,'2025-03-20 11:27:29.885939','2025-03-20 11:27:29.943203');

INSERT INTO public.transaction_histories (id,status,transaction_id,created_at,updated_at) VALUES
	 (1,'IN_PROGRESS','b260f9b8-30dc-4b9a-938f-9054f58ddbe6'::uuid,'2025-03-20 11:20:51.944144','2025-03-20 11:20:51.944146'),
	 (2,'HOLD','b260f9b8-30dc-4b9a-938f-9054f58ddbe6'::uuid,'2025-03-20 11:20:52.161689','2025-03-20 11:20:52.16169'),
	 (3,'COMPLETED','b260f9b8-30dc-4b9a-938f-9054f58ddbe6'::uuid,'2025-03-20 11:20:52.204447','2025-03-20 11:20:52.204447'),
	 (4,'IN_PROGRESS','5920ccfa-3f21-4b3c-9e45-57f050c275f3'::uuid,'2025-03-20 11:21:18.285127','2025-03-20 11:21:18.285128'),
	 (5,'HOLD','5920ccfa-3f21-4b3c-9e45-57f050c275f3'::uuid,'2025-03-20 11:21:18.326897','2025-03-20 11:21:18.326898'),
	 (6,'COMPLETED','5920ccfa-3f21-4b3c-9e45-57f050c275f3'::uuid,'2025-03-20 11:21:18.362529','2025-03-20 11:21:18.362529'),
	 (7,'IN_PROGRESS','c5c401f7-7597-440d-ac6d-e4738c3a54eb'::uuid,'2025-03-20 11:21:54.187802','2025-03-20 11:21:54.187804'),
	 (8,'HOLD','c5c401f7-7597-440d-ac6d-e4738c3a54eb'::uuid,'2025-03-20 11:21:54.228551','2025-03-20 11:21:54.228551'),
	 (9,'COMPLETED','c5c401f7-7597-440d-ac6d-e4738c3a54eb'::uuid,'2025-03-20 11:21:54.254163','2025-03-20 11:21:54.254164'),
	 (10,'IN_PROGRESS','a2a913eb-77b6-43e0-9520-5eff23bdf823'::uuid,'2025-03-20 11:21:59.885194','2025-03-20 11:21:59.885194');
INSERT INTO public.transaction_histories (id,status,transaction_id,created_at,updated_at) VALUES
	 (12,'RETRY','a2a913eb-77b6-43e0-9520-5eff23bdf823'::uuid,'2025-03-20 11:22:00.146598','2025-03-20 11:22:00.146599'),
	 (13,'RETRY','a2a913eb-77b6-43e0-9520-5eff23bdf823'::uuid,'2025-03-20 11:22:00.188429','2025-03-20 11:22:00.188429'),
	 (15,'RETRY','a2a913eb-77b6-43e0-9520-5eff23bdf823'::uuid,'2025-03-20 11:22:00.2177','2025-03-20 11:22:00.2177'),
	 (16,'RETRY','a2a913eb-77b6-43e0-9520-5eff23bdf823'::uuid,'2025-03-20 11:22:00.232625','2025-03-20 11:22:00.232626'),
	 (18,'ERROR','a2a913eb-77b6-43e0-9520-5eff23bdf823'::uuid,'2025-03-20 11:22:00.267586','2025-03-20 11:22:00.267586'),
	 (19,'IN_PROGRESS','d005375c-da98-404d-a560-2cd9b0f64e7b'::uuid,'2025-03-20 11:22:27.225036','2025-03-20 11:22:27.225038'),
	 (20,'HOLD','d005375c-da98-404d-a560-2cd9b0f64e7b'::uuid,'2025-03-20 11:22:27.287439','2025-03-20 11:22:27.287439'),
	 (21,'COMPLETED','d005375c-da98-404d-a560-2cd9b0f64e7b'::uuid,'2025-03-20 11:22:27.315364','2025-03-20 11:22:27.315365'),
	 (22,'IN_PROGRESS','f588caa5-51c2-4517-9550-6a321e1d8f5e'::uuid,'2025-03-20 11:26:31.794972','2025-03-20 11:26:31.794973'),
	 (23,'HOLD','f588caa5-51c2-4517-9550-6a321e1d8f5e'::uuid,'2025-03-20 11:26:31.870732','2025-03-20 11:26:31.870732');
INSERT INTO public.transaction_histories (id,status,transaction_id,created_at,updated_at) VALUES
	 (24,'COMPLETED','f588caa5-51c2-4517-9550-6a321e1d8f5e'::uuid,'2025-03-20 11:26:31.900939','2025-03-20 11:26:31.900939'),
	 (25,'IN_PROGRESS','a53a0490-6d40-44e3-825d-edc2f6f70636'::uuid,'2025-03-20 11:26:58.131134','2025-03-20 11:26:58.131138'),
	 (27,'RETRY','a53a0490-6d40-44e3-825d-edc2f6f70636'::uuid,'2025-03-20 11:26:58.206342','2025-03-20 11:26:58.206343'),
	 (28,'RETRY','a53a0490-6d40-44e3-825d-edc2f6f70636'::uuid,'2025-03-20 11:26:58.233198','2025-03-20 11:26:58.233199'),
	 (30,'RETRY','a53a0490-6d40-44e3-825d-edc2f6f70636'::uuid,'2025-03-20 11:26:58.279956','2025-03-20 11:26:58.279957'),
	 (31,'RETRY','a53a0490-6d40-44e3-825d-edc2f6f70636'::uuid,'2025-03-20 11:26:58.295208','2025-03-20 11:26:58.295208'),
	 (33,'ERROR','a53a0490-6d40-44e3-825d-edc2f6f70636'::uuid,'2025-03-20 11:26:58.334242','2025-03-20 11:26:58.334243'),
	 (34,'IN_PROGRESS','55c893b1-12c3-40fe-9b3e-eb5db9be63dc'::uuid,'2025-03-20 11:27:29.892099','2025-03-20 11:27:29.892101'),
	 (35,'HOLD','55c893b1-12c3-40fe-9b3e-eb5db9be63dc'::uuid,'2025-03-20 11:27:29.920274','2025-03-20 11:27:29.920274'),
	 (36,'COMPLETED','55c893b1-12c3-40fe-9b3e-eb5db9be63dc'::uuid,'2025-03-20 11:27:29.946139','2025-03-20 11:27:29.946139');
