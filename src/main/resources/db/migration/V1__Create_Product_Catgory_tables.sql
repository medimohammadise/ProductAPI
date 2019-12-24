CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA "public";
-- Drop table

-- DROP TABLE public.product;

CREATE TABLE product (
	id UUID NOT NULL,
	code varchar(255) NOT NULL,
	description varchar(255) NOT NULL,
	price numeric(21,2) NOT NULL,
	currency varchar(255) NOT NULL,
	created_at timestamp NOT NULL,
	product_category_id UUID NULL,
	CONSTRAINT product_pkey PRIMARY KEY (id)
);

-- Drop table

-- DROP TABLE public.product_category;

CREATE TABLE product_category (
	id UUID NOT NULL,
	code varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	created_at timestamp NOT NULL,
	product_category_id UUID NULL,
	CONSTRAINT product_category_pkey PRIMARY KEY (id),
	CONSTRAINT fk_product_category_product_category_id FOREIGN KEY (product_category_id) REFERENCES product_category(id)
);

ALTER TABLE "product" ADD CONSTRAINT "fk_product_product_category_id" FOREIGN KEY (product_category_id) REFERENCES product_category(id);
