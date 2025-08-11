// Diagrama de banco no formato DBML para uso no dbdiagram.io
// Gere este diagrama em https://dbdiagram.io

Project estoque {
  database_type: "PostgreSQL"
  note: "Esquema gerado a partir das entidades JPA"
}

Table categories {
  id bigint [pk, increment]
  name varchar [not null, unique]
}

Table locations {
  id bigint [pk, increment]
  name varchar [not null, unique]
}

Table products {
  id bigint [pk, increment]
  name varchar [not null]
  barcode varchar [not null, unique]
  category_id bigint [not null]
}

Table users {
  id bigint [pk, increment]
  name varchar [not null]
  email varchar [not null, unique]
  password varchar [not null]
}

// Relacionamentos
Ref: products.category_id > categories.id

Table stock {
  id bigint [pk, increment]
  product_id bigint [not null]
  location_id bigint [not null]
  entry_date date [not null]
  expiry_date date
  price decimal(19,2) [not null]
  quantity decimal(19,3) [not null]
  movement_type varchar [not null, note: 'IN/OUT']
}

Ref: stock.product_id > products.id
Ref: stock.location_id > locations.id
