ALTER USER postgres WITH PASSWORD 'postgres';

\c mpp;

CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    account_status VARCHAR(255) NOT NULL
);

CREATE TABLE dealership (
    dealership_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    website VARCHAR(255),
    fk_author_id BIGINT,
    CONSTRAINT fk_dealership_author FOREIGN KEY (fk_author_id) REFERENCES app_user (id)
);

CREATE TABLE car (
    car_id SERIAL PRIMARY KEY,
    brand VARCHAR(50),
    model VARCHAR(100),
    year INTEGER,
    color VARCHAR(50),
    price INTEGER,
    description VARCHAR(1000),
    fk_dealership_id BIGINT,
    fk_author_id BIGINT,
    CONSTRAINT fk_car_dealership FOREIGN KEY (fk_dealership_id) REFERENCES dealership (dealership_id),
    CONSTRAINT fk_car_author FOREIGN KEY (fk_author_id) REFERENCES app_user (id)
);

CREATE TABLE employee (
    employee_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(255) NOT NULL,
    salary INTEGER NOT NULL,
    fk_dealership_id BIGINT,
    fk_author_id BIGINT,
    CONSTRAINT fk_employee_dealership FOREIGN KEY (fk_dealership_id) REFERENCES dealership (dealership_id),
    CONSTRAINT fk_employee_author FOREIGN KEY (fk_author_id) REFERENCES app_user (id)
);

CREATE TABLE supplier (
    supplier_id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    fk_author_id BIGINT,
    CONSTRAINT fk_supplier_author FOREIGN KEY (fk_author_id) REFERENCES app_user (id)
);

CREATE TABLE shipping_contract (
    contract_id SERIAL PRIMARY KEY,
    contract_date DATE,
    contract_years_duration INTEGER,
    fk_dealership_id BIGINT,
    fk_supplier_id BIGINT,
    fk_author_id BIGINT,
    CONSTRAINT fk_contract_dealership FOREIGN KEY (fk_dealership_id) REFERENCES dealership (dealership_id),
    CONSTRAINT fk_contract_supplier FOREIGN KEY (fk_supplier_id) REFERENCES supplier (supplier_id),
    CONSTRAINT fk_contract_author FOREIGN KEY (fk_author_id) REFERENCES app_user (id)
);

DO $$
    DECLARE
        i INT;
        first_name VARCHAR(255);
        last_name VARCHAR(255);
        password VARCHAR(255);
        email VARCHAR(255);
        username VARCHAR(255);
        role VARCHAR(255);
        location VARCHAR(255);
        account_status VARCHAR(255) := 'ACTIVE';

        cnt_admin INT := 0;
        cnt_moderator INT := 0;
        cnt_regular INT := 0;

        first_names TEXT[] := ARRAY['Emma', 'Olivia', 'Ava', 'Isabella', 'Sophia', 'Mia', 'Charlotte', 'Amelia', 'Harper', 'Evelyn', 'Abigail', 'Emily', 'Elizabeth', 'Mila', 'Ella', 'Avery', 'Sofia', 'Camila', 'Aria', 'Scarlett'];
        last_names TEXT[] := ARRAY['Smith', 'Johnson', 'Williams', 'Jones', 'Brown', 'Garcia', 'Miller', 'Davis', 'Martinez', 'Anderson', 'Taylor', 'Thomas', 'Hernandez', 'Moore', 'Jackson', 'Martin', 'Lee', 'Perez', 'Thompson', 'White'];
        locations TEXT[] := ARRAY['Suceava', 'Patrauti', 'Mirauti', 'Buzauwu', 'Viseu de sus'];

    BEGIN
        INSERT INTO app_user (id, first_name, last_name, password, email, username, role, location, account_status)
        VALUES (1, 'Andrei', 'Cibo', '$2a$10$UXKEBjIbMhy.CFW4OY6ePOBLLls.yApnOPYkYQ4rc9qbFZDLOUV8m',
                'e@mail.com', 'tchibo', 'ROLE_ADMIN', 'Suceava', 'ACTIVE');

        FOR i IN 2..10000 LOOP
                IF i % 10 = 0 THEN
                    role := 'ROLE_MODERATOR';
                    cnt_moderator := cnt_moderator + 1;
                    username := 'user_' || 'moderator' || '_' || cnt_moderator;
                ELSE
                    role := 'ROLE_REGULAR';
                    cnt_regular := cnt_regular + 1;
                    username := 'user_' || 'regular' || '_' || cnt_regular;
                END IF;

                first_name := first_names[1 + floor(random() * array_length(first_names, 1))];
                last_name := last_names[1 + floor(random() * array_length(last_names, 1))];
                password := '$2a$10$UXKEBjIbMhy.CFW4OY6ePOBLLls.yApnOPYkYQ4rc9qbFZDLOUV8m'; -- "password"
                email := 'user' || i || '@example.com';
                location := locations[1 + floor(random() * array_length(locations, 1))];

                INSERT INTO app_user (id, first_name, last_name, password, email, username, role, location, account_status)
                VALUES (i, first_name, last_name, password, email, username, role, location, account_status);
            END LOOP;
    END $$;

DO $$
    DECLARE
        batchSize INT := 100; -- Set the desired batch size
        totalRows INT := 1000000; -- Total number of rows to insert
        currentRow INT := 1;
    BEGIN
        WHILE currentRow <= totalRows LOOP
                INSERT INTO dealership (dealership_id, address, email, name, phone, website, fk_author_id)
                SELECT
                    generate_series(currentRow, currentRow + batchSize - 1) AS dealership_id,
                    'Address ' || generate_series(currentRow, currentRow + batchSize - 1) AS address,
                    'email' || generate_series(currentRow, currentRow + batchSize - 1) || '@dealership.com' AS email,
                    'Dealership Name ' || generate_series(currentRow, currentRow + batchSize - 1) AS name,
                    'Phone ' || generate_series(currentRow, currentRow + batchSize - 1) AS phone,
                    'www.dealer' || generate_series(currentRow, currentRow + batchSize - 1) || '.com' AS website,
                    (random() * 9999)::integer + 1 AS fk_author_id;

                currentRow := currentRow + batchSize;
                COMMIT; -- Commit the batch
            END LOOP;
    END $$;

DO $$
    DECLARE
        batchSize INT := 100; -- Set the desired batch size
        totalRows INT := 1000000; -- Total number of rows to insert
        currentRow INT := 1;
    BEGIN
        WHILE currentRow <= totalRows LOOP
                INSERT INTO car (car_id, brand, color, model, price, year, fk_dealership_id, description, fk_author_id)
                SELECT
                    generate_series(currentRow, currentRow + batchSize - 1) AS car_id,
                    'Brand ' || ((generate_series(currentRow, currentRow + batchSize - 1) + currentRow - 1) % 10) AS brand,
                    'Color ' || ((generate_series(currentRow, currentRow + batchSize - 1) + currentRow - 1) % 5) AS color,
                    'Model ' || ((generate_series(currentRow, currentRow + batchSize - 1) + currentRow - 1) % 20) AS model,
                    ((generate_series(currentRow, currentRow + batchSize - 1) + currentRow - 1) % 100000) AS price,
                    ((generate_series(currentRow, currentRow + batchSize - 1) + currentRow - 1) % 20 + 2000) AS year,
                    (random() * 999999)::integer + 1 AS fk_dealership_id,
                    (CONCAT('This car is a ',
                            CASE WHEN random() > 0.5 THEN 'powerful' ELSE 'sleek' END, ' and ',
                            CASE WHEN random() > 0.5 THEN 'stylish' ELSE 'sporty' END, ' ',
                            CASE WHEN random() > 0.5 THEN 'sports car' ELSE 'coupe' END,
                            ' that offers an ',
                            CASE WHEN random() > 0.5 THEN 'exhilarating' ELSE 'exciting' END,
                            ' driving experience. It comes equipped with a ',
                            CASE WHEN random() > 0.5 THEN '5.0-liter V8 engine' ELSE 'turbocharged four-cylinder engine' END,
                            ' that produces ',
                            CAST (random() * 400 + 200 AS VARCHAR (10)), ' horsepower and ',
                            CAST (random() * 200 + 200 AS VARCHAR (10)), ' lb-ft of torque, making it one of the most powerful vehicles in its class. The car has a ',
                            CASE WHEN random() > 0.5 THEN 'sleek and aerodynamic design' ELSE 'bold and muscular look' END,
                            ', with a low profile and a wide stance that exudes confidence and performance. The interior is equally impressive, with premium materials and advanced technology features that provide comfort, convenience, and entertainment. ' ||
                            'This car also boasts impressive safety features, including a suite of driver assistance technologies that help prevent accidents and mitigate the effects of collisions. Overall, the ',
                            'car is an excellent choice for drivers who demand power, performance, and style in their vehicles.')
                        ),
                    (random() * 9999)::integer + 1 AS fk_author_id;

                currentRow := currentRow + batchSize;
                COMMIT; -- Commit the batch
            END LOOP;
    END $$;

DO $$
    DECLARE
        batchSize INT := 100; -- Set the desired batch size
        totalRows INT := 1000000; -- Total number of rows to insert
        currentRow INT := 1;
    BEGIN
        WHILE currentRow <= totalRows LOOP
                INSERT INTO employee (employee_id, email, name, phone, role, salary, fk_dealership_id, fk_author_id)
                SELECT
                    generate_series(currentRow, currentRow + batchSize - 1) AS employee_id,
                    'email' || (generate_series(currentRow, currentRow + batchSize - 1) + currentRow - 1) || '@employee.com' AS email,
                    'Employee Name ' || (generate_series(currentRow, currentRow + batchSize - 1) + currentRow - 1) AS name,
                    'Employee Phone ' || (generate_series(currentRow, currentRow + batchSize - 1) + currentRow - 1) AS phone,
                    (CASE WHEN random() < 0.5 THEN 'Manager' ELSE 'Employee' END) AS role,
                    (random() * 50000 + 300)::integer AS salary,
                    (random() * 999999)::integer + 1 AS fk_dealership_id,
                    (random() * 9999)::integer + 1 AS fk_author_id;

                currentRow := currentRow + batchSize;
                COMMIT; -- Commit the batch
            END LOOP;
    END $$;

DO $$
    DECLARE
        batchSize INT := 100; -- Set the desired batch size
        totalRows INT := 1000000; -- Total number of rows to insert
        currentRow INT := 1;
    BEGIN
        WHILE currentRow <= totalRows LOOP
                INSERT INTO supplier (supplier_id, email, name, phone, fk_author_id)
                SELECT
                    generate_series(currentRow, currentRow + batchSize - 1) AS supplier_id,
                    'email' || ((generate_series(currentRow, currentRow + batchSize - 1) + currentRow - 1) % 10 + 1) || '@gmail.com' AS email,
                    'Name ' || ((generate_series(currentRow, currentRow + batchSize - 1) + currentRow - 1) % 435) AS name,
                    'Phone ' || ((generate_series(currentRow, currentRow + batchSize - 1) + currentRow - 1) % 20) AS phone,
                    (random() * 9999)::integer + 1 AS fk_author_id;

                currentRow := currentRow + batchSize;
                COMMIT; -- Commit the batch
            END LOOP;
    END $$;

DO $$
    BEGIN
        FOR i IN 1..1000 LOOP
                INSERT INTO shipping_contract (contract_id, contract_date, contract_years_duration, fk_supplier_id, fk_dealership_id, fk_author_id)
                SELECT
                    generate_series((i-1)*10000 + 1, i*10000) as contract_id,
                    timestamp '2022-01-01 00:00:00' + (random() * (timestamp '2022-12-31 23:59:59' - timestamp '2022-01-01 00:00:00')) as contract_date,
                    (random() * 40)::integer + 1 as contract_years_duration,
                    (random() * 99999)::integer + 1 as fk_supplier_id,
                    (random() * 99999)::integer + 1 as fk_dealership_id,
                    (random()*9999)::integer+1 as fk_author_id;

                IF i % 50 = 0 THEN
                    RAISE NOTICE 'Inserted batch %', i;
                END IF;
            END LOOP;
    END $$;

create sequence app_user_seq start with 10001 increment by 50;
create sequence car_seq start with 1000001 increment by 50;
create sequence dealership_seq start with 1000001 increment by 50;
create sequence employee_seq start with 1000001 increment by 50;
create sequence shipping_contract_seq start with 1000001 increment by 50;
create sequence supplier_seq start with 1000001 increment by 50;