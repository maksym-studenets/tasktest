-- TASK 1

-- a
-- Makes with total sales greater than 1000
SELECT make.name   AS vehicle_make,
       COUNT(a.id) AS total_sales
FROM `loan-schema`.m_loan l
         INNER JOIN `asset-schema`.asset a ON l.id = a.m_loan_id
         INNER JOIN `asset-schema`.vehicle_model model ON a.model_id = model.id
         INNER JOIN `asset-schema`.vehicle_make make ON model.vehicle_make_id = make.id
WHERE l.disbursedon_date BETWEEN '2020-01-01' AND '2020-02-29'
GROUP BY make.id, make.name
HAVING total_sales > 1000;

-- b
-- All makes sales data (including 0 units sold)
SELECT make.name   AS vehicle_make,
       COUNT(l.id) AS total_sales
FROM `asset-schema`.vehicle_make make
         LEFT JOIN `asset-schema`.vehicle_model model ON make.id = model.vehicle_make_id
         LEFT JOIN `asset-schema`.asset a ON model.id = a.model_id
         LEFT JOIN `loan-schema`.m_loan l
                   ON (a.m_loan_id = l.id AND l.disbursedon_date BETWEEN '2020-01-01' AND '2020-02-29')
GROUP BY make.id, make.name;


-- TASK 2
SELECT rs.loan_id,
       rs.duedate                                                           AS first_updaid_due_date,
       (COALESCE(rs.principal_amount, 0) + COALESCE(rs.interest_amount, 0)) AS weekly_payment_amount
FROM `loan-schema`.m_loan_repayment_schedule rs
         INNER JOIN (SELECT s.loan_id,
                            MIN(s.duedate) AS first_unpaid_week_due_date
                     FROM `loan-schema`.m_loan_repayment_schedule s
                     WHERE s.completed_derived = 0
                     GROUP BY s.loan_id) unpaid
                    ON (rs.loan_id = unpaid.loan_id AND rs.duedate = unpaid.first_unpaid_week_due_date);


-- TASK 3
-- assuming is_reversed = 0 means transaction succeeded and was NOT refunded
WITH scheduled_amounts AS (SELECT lrs.loan_id AS loan_id,
                                  SUM(
                                          COALESCE(lrs.principal_amount, 0)
                                              + COALESCE(lrs.interest_amount, 0)
                                              + COALESCE(lrs.fee_charges_amount, 0)
                                              + COALESCE(lrs.penalty_charges_amount, 0)
                                  )           AS total_amount_to_pay
                           FROM `loan-schema`.m_loan_repayment_schedule lrs
                           WHERE lrs.duedate <= CURRENT_DATE
                           GROUP BY lrs.loan_id),
     paid_amounts AS (SELECT t.loan_id     AS loan_id,
                             SUM(t.amount) AS total_amount_paid
                      FROM `loan-schema`.m_loan_transaction t
                      WHERE t.is_reversed = 0
                      GROUP BY t.loan_id)
SELECT l.id                                                                       AS loan_id,
       COALESCE(sch.total_amount_to_pay, 0) - COALESCE(paid.total_amount_paid, 0) AS balance
FROM `loan-schema`.m_loan l
         LEFT JOIN scheduled_amounts sch ON l.id = sch.loan_id
         LEFT JOIN paid_amounts paid ON l.id = paid.loan_id
ORDER BY balance DESC;
