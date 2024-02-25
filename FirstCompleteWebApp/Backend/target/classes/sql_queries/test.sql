-- DROP INDEX idx_car_dealership_price;
DROP INDEX idx_car_dealership;
DROP INDEX idx_dealership_id_name;
DROP INDEX idx_dealership_name;
DROP INDEX idx_employee_dealership;
DROP INDEX idx_contract_supplier;
DROP INDEX idx_contract_dealership;
DROP INDEX idx_supplier_name;
DROP INDEX idx_car_author;
DROP INDEX idx_dealership_author;
DROP INDEX idx_contract_author;
DROP INDEX idx_supplier_author;
DROP INDEX idx_employee_author;

ALTER TABLE shipping_contract DISABLE TRIGGER ALL;
ALTER TABLE supplier DISABLE TRIGGER ALL;
ALTER TABLE car DISABLE TRIGGER ALL;
ALTER TABLE employee DISABLE TRIGGER ALL;
ALTER TABLE dealership DISABLE TRIGGER ALL;

delete from shipping_contract;
delete from supplier;
delete from car;
delete from employee;
delete from dealership;

ALTER TABLE shipping_contract ENABLE TRIGGER ALL;
ALTER TABLE supplier ENABLE TRIGGER ALL;
ALTER TABLE car ENABLE TRIGGER ALL;
ALTER TABLE employee ENABLE TRIGGER ALL;
ALTER TABLE dealership ENABLE TRIGGER ALL;

DO $$
DECLARE
    batchSize INT := 1000; -- Set the desired batch size
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
    batchSize INT := 1000; -- Set the desired batch size
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
    batchSize INT := 1000; -- Set the desired batch size
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
    batchSize INT := 1000; -- Set the desired batch size
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
                    (random() * 999999)::integer + 1 as fk_supplier_id,
                    (random() * 999999)::integer + 1 as fk_dealership_id,
                    (random()*9999)::integer+1 as fk_author_id;

                IF i % 100 = 0 THEN
                    RAISE NOTICE 'Inserted batch %', i;
                END IF;
            END LOOP;
    END $$;


SELECT setval('car_seq', (SELECT MAX(car_id) FROM car));
SELECT setval('dealership_seq', (SELECT MAX(dealership_id) FROM dealership));
SELECT setval('employee_seq', (SELECT MAX(employee_id) FROM employee));
SELECT setval('shipping_contract_seq', (SELECT MAX(contract_id) FROM shipping_contract));
SELECT setval('supplier_seq', (SELECT MAX(supplier_id) FROM supplier));


-- CREATE INDEX idx_car_dealership_price ON car (fk_dealership_id, price);
CREATE INDEX idx_car_dealership ON car (fk_dealership_id);
CREATE INDEX idx_dealership_id_name ON dealership (dealership_id, name);
CREATE INDEX idx_dealership_name ON dealership (name);
CREATE INDEX idx_employee_dealership ON employee (fk_dealership_id);
CREATE INDEX idx_contract_supplier ON shipping_contract (fk_supplier_id);
CREATE INDEX idx_contract_dealership ON shipping_contract (fk_dealership_id);
CREATE INDEX idx_supplier_name ON supplier (name);
CREATE INDEX idx_car_author ON car (fk_author_id);
CREATE INDEX idx_dealership_author ON dealership (fk_author_id);
CREATE INDEX idx_contract_author ON shipping_contract (fk_author_id);
CREATE INDEX idx_supplier_author ON supplier (fk_author_id);
CREATE INDEX idx_employee_author ON employee (fk_author_id);