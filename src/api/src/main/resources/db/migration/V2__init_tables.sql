CREATE TABLE mock.endoint (
	id uuid NOT NULL,
	description varchar(255) NULL,
	"method" varchar(255) NOT NULL,
	proceed_logic varchar(255) NULL,
	title varchar(255) NOT NULL,
	CONSTRAINT endoint_pkey PRIMARY KEY (id)
);

CREATE TABLE mock.endpoint_log (
	id uuid NOT NULL,
	creation_date timestamp NULL,
	endpoint_id uuid NOT NULL,
	CONSTRAINT endpoint_log_pkey PRIMARY KEY (id)
);

CREATE TABLE mock.endpoint_log_input_mapping (
	log_id uuid NOT NULL,
	input_field_value varchar(255) NULL,
	input_field_name varchar(255) NOT NULL,
	CONSTRAINT endpoint_log_input_mapping_pkey PRIMARY KEY (log_id, input_field_name),
	CONSTRAINT fkphiacraeodvalj5pojy97je4y FOREIGN KEY (log_id) REFERENCES mock.endpoint_log(id)
);

CREATE TABLE mock.endpoint_log_output_mapping (
	log_id uuid NOT NULL,
	output_field_value varchar(255) NULL,
	output_field_name varchar(255) NOT NULL,
	CONSTRAINT endpoint_log_output_mapping_pkey PRIMARY KEY (log_id, output_field_name),
	CONSTRAINT fkh978d5cdysecvegpi3wo7fo2a FOREIGN KEY (log_id) REFERENCES mock.endpoint_log(id)
);

CREATE TABLE mock.param (
	id uuid NOT NULL,
	"key" varchar(255) NULL,
	"type" varchar(255) NULL,
	value varchar(255) NULL,
	body_endpoint_id uuid NULL,
	response_endpoint_id uuid NULL,
	CONSTRAINT param_pkey PRIMARY KEY (id),
	CONSTRAINT fkgagu36f65whp1w2dc422ybfm2 FOREIGN KEY (body_endpoint_id) REFERENCES mock.endoint(id),
	CONSTRAINT fkiqv3wdj9eb7kb2jso0fy5im46 FOREIGN KEY (response_endpoint_id) REFERENCES mock.endoint(id)
);