create table if not exists public_ip
(
    ip varchar(15) not null
);

insert into public_ip values ('0.0.0.0');